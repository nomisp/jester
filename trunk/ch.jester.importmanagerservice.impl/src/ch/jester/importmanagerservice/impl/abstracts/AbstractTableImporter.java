package ch.jester.importmanagerservice.impl.abstracts;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.importer.IImportHandler;

public abstract class AbstractTableImporter<T, V> implements IImportHandler{
	int workUnits = 10000000;
	int singleUnitOfWork = -1;
	private ITableProvider<T> mProvider;
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
	public Object handleImport(InputStream pInputStream,
			IProgressMonitor pMonitor) {
		mProvider = initialize(pInputStream);
		
		try {
			pMonitor.beginTask("Processing Input", getTotalUnitsOfWork());
			String[] header = mProvider.getHeaderEntries();
			//console(header);
			pMonitor.worked(getSingleUnitOfWork());

			List<V> domainObjects = new ArrayList<V>();
			for(int i=1;i<mProvider.getTotalRows();i++){
				T row = mProvider.getRow(i);
				if(i%1000==0){
					pMonitor.subTask("Reading Row "+i);
				}
				if(row==null){continue;}
				String[] details;
				details = mProvider.processRow(row, header.length);
				//console(details);
				Properties domainProperties = createProperties(header, details);
				V v = createDomainObject(domainProperties);
				if(v!=null){
					domainObjects.add(createDomainObject(domainProperties));
				}
				if(persistencyEveryEntry()!=-1 && i%persistencyEveryEntry()==0){
					List<V> copy= new ArrayList<V>(domainObjects);
					persist(copy);
					domainObjects.clear();
				}
				
				pMonitor.worked(getSingleUnitOfWork());
			}
			pMonitor.subTask("Save to DB");
			persist(domainObjects);
			pMonitor.done();
			done();
			mProvider=null;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**Erzeugt aus einem Header (Row Einträge) und einer Detail Row ein Property Object
	 * @param header
	 * @param details
	 * @return
	 */
	protected Properties createProperties(String[] header, String[] details) {
		Properties p = new Properties();
		for(int i=0;i<header.length;i++){
			p.put(header[i], details[i]);
		}
		return p;
	}
	protected void done(){
		
	}
	/**
	 * Speichert die Liste der Domain Objekte
	 * @param pDomainObjects
	 */
	protected abstract void persist(List<V> pDomainObjects);

	/**erzeugt die einzelnen DomainObjekte aus den Properties
	 * @param pProperties
	 * @return
	 */
	protected abstract V createDomainObject(Properties pProperties);
	public abstract ITableProvider<T> initialize(InputStream pInputStream);
}
