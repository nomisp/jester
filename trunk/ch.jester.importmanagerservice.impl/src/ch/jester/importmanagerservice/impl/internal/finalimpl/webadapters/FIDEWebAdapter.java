package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.LinkFilter;
import ch.jester.common.web.PageReader;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.importer.IWebImportAdapter;


public class FIDEWebAdapter implements IWebImportAdapter {
	private static String WEB = "http://ratings.fide.com/download.phtml";
	private List<ILink> mLinkList;
	private LinkFilter linkfilter;
	private PageReader reader = new PageReader();
	private IImportHandler mAdaptedHandler;
	public FIDEWebAdapter() {
		reader.setFilter(new ExtensionFilter(".zip", linkfilter=LinkFilter.createFIDEFilter()));

	}

	@Override
	public Object handleImport(InputStream pInputStream,
			IProgressMonitor pMonitor) {
		return mAdaptedHandler.handleImport(pInputStream, pMonitor);
	}

	@Override
	public List<ILink> getLinks() throws IOException {
		if(mLinkList==null){
			reader.readPage(WEB);
			mLinkList = linkfilter.getLinks();
		}
		return mLinkList;
	}

	@Override
	public void setIImportHandler(IImportHandler pAdaptedHandler) {
		mAdaptedHandler=pAdaptedHandler;
	}

}
