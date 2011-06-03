package ch.jester.common.importer;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

import ch.jester.common.utility.AdapterBinding;
import ch.jester.commonservices.api.importer.IImportAttributeMatcher;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.ITestableImportHandler;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.exceptions.ProcessingException;

public abstract class AbstractTableImporter<T, V> implements IImportHandler<InputStream>, ITestableImportHandler<InputStream>, IImportAttributeMatcher{
	int workUnits = 10000000;
	int singleUnitOfWork = -1;
	int testLines = -1;
	private IVirtualTable<T> mProvider;
	protected HashMap<String, String> mInputLinking = new HashMap<String, String>();
//	private IVirtualTable<T> mProvider;
	
	public AbstractTableImporter(){
		AdapterBinding binding = new AdapterBinding(this);
		binding.add(new IVirtualTable<T>() {

			@Override
			public String[] getHeaderEntries() {
				return mProvider.getHeaderEntries();
			}

			@Override
			public T getRow(int i) {
				return mProvider.getRow(i);
			}

			@Override
			public int getTotalRows() {
				return mProvider.getTotalRows();
			}

			@Override
			public String[] processRow(T pRow, int pLenght) {
				return mProvider.processRow(pRow, pLenght);
			}

			@Override
			public boolean canAddCells() {
				return mProvider.canAddCells();
			}

			@Override
			public String[] getDynamicInput(int pCount) {
				return mProvider.getDynamicInput(pCount);
			}

			@Override
			public void clearCells() {
				mProvider.clearCells();
				
			}

			@Override
			public void addCell(
					ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell cell) {
				mProvider.addCell(cell);
				
			}

			@Override
			public List<ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell> getCells() {
				return mProvider.getCells();
			}
		
		}
		,IVirtualTable.class);
		binding.add(this, ITestableImportHandler.class);
		binding.add(this, IImportAttributeMatcher.class);
		binding.bind();
		
	}
	
	protected int getSingleUnitOfWork(){
		if(singleUnitOfWork==-1){
			singleUnitOfWork = getTotalUnitsOfWork() / mProvider.getTotalRows();
		}
		return singleUnitOfWork;
	}

	/**Eine Default Einstellung von 10000000
	 * @return
	 */
	protected int getTotalUnitsOfWork() {
		return workUnits;
	}
	/**
	 * Print auf Console
	 * @param string
	 */
	protected void console(String[] string) {
		for(String s:string){
			System.out.print(s);
			System.out.print("  -  ");
		}
		System.out.println();
	}
	protected int persistencyEveryEntry(){
		return -1;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}
	@Override
	public Object handleImport(InputStream pInputStream,
			IProgressMonitor pMonitor) throws ProcessingException{
		return handleImport(pInputStream, testLines, pMonitor);
	}
	@Override
	public Object handleImport(InputStream pInputStream,int pContentLines,
			IProgressMonitor pMonitor) {
		boolean test = testLines!=pContentLines;
		if(mProvider==null){
			mProvider = initialize(pInputStream);
		}
		int rowsToRead = mProvider.getTotalRows();
		if(test){
			rowsToRead = pContentLines;
		}
		
		try {
			pMonitor.beginTask("Processing Input", getTotalUnitsOfWork());
			String[] header = mProvider.getHeaderEntries();
			//console(header);
			pMonitor.worked(getSingleUnitOfWork());

			List<V> domainObjects = new ArrayList<V>();
			for(int i=1;i<rowsToRead;i++){
				T row = mProvider.getRow(i);
				if(i%1000==0){
					pMonitor.subTask("Reading Row "+i);
				}
				if(row==null){continue;}
				String[] details;
				details = mProvider.processRow(row, header.length);
				//console(details);
				Properties domainProperties = createProperties(header, details);
				V vnew = createNewDomainObject();
				_createDomainObject(vnew, domainProperties);
				//V v = createDomainObject(domainProperties);
				if(addToCollection(vnew)){
					domainObjects.add(vnew);
				}
				if(persistencyEveryEntry()!=-1 && i%persistencyEveryEntry()==0){
					List<V> copy= new ArrayList<V>(domainObjects);
					persist(copy, pMonitor);
					domainObjects.clear();
				}
				
				pMonitor.worked(getSingleUnitOfWork());
			}
			if(!test){
				pMonitor.subTask("Save to DB");
				persist(domainObjects, pMonitor);
				pMonitor.done();
				done();
				mProvider=null;
			}
		}
		catch (ProcessingException e){
			throw e;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private void _createDomainObject(Object domainObject, Properties properties) {
		Iterator<String> matchings = this.mInputLinking.keySet().iterator();
		while(matchings.hasNext()){
			String domain_property = matchings.next();
			String input_property = this.mInputLinking.get(domain_property);
			Object input = properties.get(input_property);
			if(input==null){continue;}
			writeValue(domainObject, domain_property, input);
			
		}
	}
	
	private void writeValue(Object domainObject, String domain_property, Object object) {
		BeanInfo originalInfo = null;
		try {
			originalInfo = Introspector.getBeanInfo(domainObject.getClass());
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PropertyDescriptor[] descriptor = originalInfo.getPropertyDescriptors();
		
		PropertyDescriptor desc = null;
		for(PropertyDescriptor d: descriptor){
			if(d.getDisplayName().equals(domain_property)){
				desc=d;
				break;
			}
		}
		
		Method writeMethod = desc.getWriteMethod();
		Class<?> param = writeMethod.getParameterTypes()[0];
		try {
			Object result = convertInput(param, object);
			writeMethod.invoke(domainObject, result);
		} catch(NumberFormatException e){
			throw new ProcessingException("Can't set value '"+object+"' to property '"+domain_property+"'!\n\nPlease check your settings.", e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private Object convertInput(Class<?> param, Object object) throws NumberFormatException {
			if(param == java.lang.String.class){
				return object.toString().trim();
			}
			if(param == java.lang.Integer.class){
				if(object.toString().trim().equals("")){
					object = 0;
				}
				return Integer.parseInt(object.toString().trim());
			}
		return object;
	}
	

	
	@Override
	public String[] getInputAttributes() {
		return mProvider.getHeaderEntries();
	}
	
	/**Erzeugt aus einem Header (Row Einträge) und einer Detail Row ein Property Object
	 * @param header
	 * @param details
	 * @return
	 */
	protected Properties createProperties(String[] header, String[] details) {
		Properties p = new Properties();
		if(header.length!=details.length){
			System.out.println("autsch");
		}
		for(int i=0;i<header.length;i++){
			p.put(header[i], details[i]);
		}
		return p;
	}
	
	@Override
	public void setInputLinking(HashMap<String, String> pMap) {
		mInputLinking.clear();
		mInputLinking.putAll(pMap);
		
	}

	@Override
	public HashMap<String, String> getInputLinking() {
		return mInputLinking;
	}
	
	protected void done(){
		
	}
	
	protected boolean addToCollection(V v){
		return true;
	}
	
	/**
	 * Speichert die Liste der Domain Objekte
	 * @param pDomainObjects
	 * @param pMonitor 
	 */
	protected abstract void persist(List<V> pDomainObjects, IProgressMonitor pMonitor);

	protected abstract V createNewDomainObject();
	
	public abstract IVirtualTable<T> initialize(InputStream pInputStream);
}
