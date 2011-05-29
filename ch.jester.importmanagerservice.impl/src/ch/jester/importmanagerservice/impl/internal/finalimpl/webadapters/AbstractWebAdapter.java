package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.web.LinkFilter;
import ch.jester.common.web.PageReader;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.importer.IWebImportAdapter;

public abstract class AbstractWebAdapter implements IWebImportAdapter  {

	protected List<ILink> mLinkList;
	protected LinkFilter linkfilter;
	protected PageReader reader = new PageReader();
	@SuppressWarnings("rawtypes")
	private IImportHandler mAdaptedHandler;

	public AbstractWebAdapter(){
		
	}
	
	@Override
	public Object handleImport(InputStream pInputStream, IProgressMonitor pMonitor) {
		return mAdaptedHandler.handleImport(pInputStream, pMonitor);
	}

	@Override
	public void setIImportHandler(@SuppressWarnings("rawtypes") IImportHandler pAdaptedHandler) {
		mAdaptedHandler=pAdaptedHandler;
	}
	@Override
	public Object getAdapter(Class adapter) {
		return mAdaptedHandler.getAdapter(adapter);
	}
}