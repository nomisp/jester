package ch.jester.ui.importer.internal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import ch.jester.common.utility.AdapterUtility;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.importer.IImportPropertyMatcher;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.ITestableImportHandler;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.exceptions.ProcessingException;

public class ParseController {
	private ImportData mSelection;
	private Exception lastException;
	ParseController(){
		
	}
	public boolean hasException(){
		return lastException!=null;
	}
	public void setData(ImportData mSeData){
		mSelection = mSeData;
	}
	
	
	public void testRun() throws ProcessingException{
			if(!canTest()){return;}
			try{
			
			String[] headers = getInputAttributes();
			getContent(headers.length);
			}catch(ProcessingException e){
				lastException = e;
				throw e;
			}
	}
	public void importRun(IProgressMonitor mMonitor) throws ProcessingException{
		try {
			final InputStream instream = ZipUtility.getZipEntry(mSelection.getSelectedZipFile(), mSelection.getSelectedZipEntry());
			mSelection.getSelectedHandlerEntry().getService().handleImport(instream, mMonitor);
			//mSelection.getSelectedHandlerEntry().releaseService();
			instream.close();
		} catch (Exception e) {
			lastException = e;
			if(e instanceof ProcessingException){
				throw (ProcessingException)e;
			}
			throw new ProcessingException(e);
		}
}
	
	public boolean canAddColumns(){
		
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		IVirtualTable<?> virtual = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		return virtual.canAddCells();
	}

	@SuppressWarnings("rawtypes")
	public IImportPropertyMatcher getImportAttributeMatcher(){
		if(mSelection.getSelectedHandlerEntry()==null){return null;}
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		IImportPropertyMatcher attributehandler = AdapterUtility.getAdaptedObject(handler, IImportPropertyMatcher.class);
		return attributehandler;
	}
	
	public IVirtualTable<?> getVirtualTableProvider(){
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		IVirtualTable<?> virtual = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		return virtual;
	}
	private boolean canTest(){
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		@SuppressWarnings("unchecked")
		ITestableImportHandler<Object> testableHandler = AdapterUtility.getAdaptedObject(handler, ITestableImportHandler.class);
		return testableHandler!=null;
	}

	public boolean canDoMatching(){
		return getImportAttributeMatcher()!=null;
	}
	
	public String[] getInputAttributes(){
		if(hasException()){
			getImportAttributeMatcher().resetInputMatching();
			lastException = null;
		}
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		@SuppressWarnings("unchecked")
		ITestableImportHandler<Object> testableHandler = AdapterUtility.getAdaptedObject(handler, ITestableImportHandler.class);
		if(testableHandler==null){
			return new String[]{};
		}
		final InputStream instream = ZipUtility.getZipEntry(mSelection.getSelectedZipFile(), mSelection.getSelectedZipEntry());
		try {
			testableHandler.handleImport(instream,20, new NullProgressMonitor());
			instream.close();
		} catch (Exception e) {
			if(e instanceof ProcessingException){
				throw (ProcessingException)e;
			}
			throw new ProcessingException(e);
		}
		IVirtualTable<?> access = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		return access.getHeaderEntries();
	}

	/*public String[] getDomainAttributes(){
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		IImportAttributeMatcher attributehandler = AdapterUtility.getAdaptedObject(handler, IImportAttributeMatcher.class);
		return attributehandler.getDomainObjectAttributes();
	}*/
	
	public IImportPropertyMatcher getAttributeMatcher(){
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		IImportPropertyMatcher attributehandler = AdapterUtility.getAdaptedObject(handler, IImportPropertyMatcher.class);
		return attributehandler;
	}

	public HashMap<String, String> getPredefiniedLinking(){
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		IImportPropertyMatcher attributehandler = AdapterUtility.getAdaptedObject(handler, IImportPropertyMatcher.class);
		return attributehandler.getInputMatching();
		
	}
	@SuppressWarnings("unchecked")
	public List<String[]> getContent(int pLength) throws ProcessingException{
		IImportHandler<?> handler = mSelection.getSelectedHandlerEntry().getService();
		IVirtualTable<Object> access = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		
		List<String[]> inputContent = new ArrayList<String[]>();
		for(int i=1;i<20;i++){
			Object o = access.getRow(i);
			if(o!=null){
				String[] rowcontent = access.processRow(o, pLength);
				inputContent.add(rowcontent);
			}
		}
		
		
		return inputContent;

	}
}
