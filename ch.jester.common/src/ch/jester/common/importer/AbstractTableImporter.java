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

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

import ch.jester.common.utility.AdapterBinding;
import ch.jester.common.utility.ServiceConsumer;
import ch.jester.commonservices.api.importer.IImportPropertyMatcher;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IPropertyTranslator;
import ch.jester.commonservices.api.importer.ITestableImportHandler;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.exceptions.ProcessingException;


/**
 * ServiceConsumer Template für TabellenImports undefinierter Art.
 *
 * @param <T>
 * @param <V>
 */
public abstract class AbstractTableImporter<T, V> extends ServiceConsumer implements IImportHandler<InputStream>, ITestableImportHandler<InputStream>, IImportPropertyMatcher{
	int mWorkUnits = 10000000;
	int mSingleUnitOfWork = -1;
	int mTestLines = -1;
	private boolean mIsTestRun;
	private IVirtualTable<T> mProvider;
	protected HashMap<String, String> mInputLinking = new HashMap<String, String>();
	protected PropertyTranslator mPropertyTranslator = new PropertyTranslator();

	public IPropertyTranslator getPropertyTranslator(){
		return mPropertyTranslator;
	}
	
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
			public String[] getRowInput(int pCount) {
				return mProvider.getRowInput(pCount);
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
		binding.add(this, IImportPropertyMatcher.class);
		binding.bind();
		
	}
	
	protected int getSingleUnitOfWork(){
		if(mSingleUnitOfWork==-1){
			mSingleUnitOfWork = getTotalUnitsOfWork() / mProvider.getTotalRows();
		}
		return mSingleUnitOfWork;
	}

	/**Eine Default Einstellung von 10000000
	 * @return
	 */
	protected int getTotalUnitsOfWork() {
		return mWorkUnits;
	}
	/**
	 * Print auf Console
	 * @param string
	 */
	protected void console(String[] string) {
		for(String s:string){
			System.out.print(s);
			System.out.print(Messages.AbstractTableImporter_0);
		}
		System.out.println();
	}
	protected int persistencyEveryEntry(){
		return -1;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}
	@Override
	public Object handleImport(InputStream pInputStream,
			IProgressMonitor pMonitor) throws ProcessingException{
		return handleImport(pInputStream, mTestLines, pMonitor);
	}
	
	protected boolean isTestRun(){
		return mIsTestRun;
	}
	
	@Override
	public Object handleImport(InputStream pInputStream,int pContentLines,
			IProgressMonitor pMonitor) {
		initialize();
		mIsTestRun = mTestLines!=pContentLines;
		//mProvider = null;
		try{
			if(mProvider==null){
				mProvider = initialize(pInputStream);
			}
		}catch(ProcessingException e){
			mProvider = null;
			throw e;
		}
		int rowsToRead = mProvider.getTotalRows();
		if(mIsTestRun){
			rowsToRead = pContentLines;
		}
		
		try {
			pMonitor.beginTask(Messages.AbstractTableImporter_processing_input, getTotalUnitsOfWork());
			String[] header = mProvider.getHeaderEntries();
			//console(header);
			pMonitor.worked(getSingleUnitOfWork());

			List<V> domainObjects = new ArrayList<V>();
			for(int i=1;i<rowsToRead;i++){
				T row = mProvider.getRow(i);
				if(i%1000==0){
					pMonitor.subTask(Messages.AbstractTableImporter_reading_row+i);
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
					doModifications(vnew, domainProperties);
				}
				if(persistencyEveryEntry()!=-1 && i%persistencyEveryEntry()==0){
					List<V> copy= new ArrayList<V>(domainObjects);
					persist(copy, pMonitor);
					domainObjects.clear();
				}
				
				pMonitor.worked(getSingleUnitOfWork());
			}
			if(!mIsTestRun){
				pMonitor.subTask(Messages.AbstractTableImporter_save_db);
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
			throw new ProcessingException(e);
		}finally{
			finished();
		}
		return null;
	}
	
	/**
	 * Aufruf wenn fertig.
	 */
	protected void finished(){};
	/**
	 * Wird für die Initialisierung aufgerufen.
	 */
	protected void initialize(){};
	
	protected void doModifications(V vnew, Properties domainProperties) {
		
	}

	protected boolean doAutoMatching(V pDomainObject, String pDomainProperty, String pInputProperty, Properties p) {
		return true;
	}

	private void _createDomainObject(V domainObject, Properties properties) {
		Iterator<String> matchings = this.mInputLinking.keySet().iterator();
		while(matchings.hasNext()){
			String domain_property = matchings.next();
			String input_property = this.mInputLinking.get(domain_property);
			Object input = properties.get(input_property);
			if(input==null){continue;}
			boolean result = doAutoMatching(domainObject, domain_property, input_property, properties);
			if(result){
				writeValue(domainObject, domain_property, input);
			}
			
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
			throw new ProcessingException(Messages.AbstractTableImporter_cant_set_value1+object+Messages.AbstractTableImporter_cant_set_value2+domain_property+Messages.AbstractTableImporter_pls_check_settings, e);
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
				if(object.toString().trim().equals("")){ //$NON-NLS-1$
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
	protected Properties createProperties(String[] header, String[] details) throws ProcessingException {
		Properties p = new Properties();
		if(header.length!=details.length){
			throw new ProcessingException(Messages.AbstractTableImporter_headerlength_detaillength_dontmatch);
		}
		for(int i=0;i<header.length;i++){
			p.put(header[i], details[i]);
		}
		return p;
	}
	
	@Override
	public void setInputMatching(HashMap<String, String> pMap) {
		mInputLinking.clear();
		mInputLinking.putAll(pMap);
		
	}

	@Override
	public HashMap<String, String> getInputMatching() {
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
	
	/**
	 * Initialisierung der Virtuellen Tabelle für den Stream
	 * @param pInputStream
	 * @return eine Implementation von IVirtualTable
	 * @throws ProcessingException
	 */
	public abstract IVirtualTable<T> initialize(InputStream pInputStream) throws ProcessingException;
}
