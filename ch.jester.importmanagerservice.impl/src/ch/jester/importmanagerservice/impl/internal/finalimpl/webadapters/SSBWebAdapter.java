package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.LinkFilter;
import ch.jester.common.web.PageReader;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.importer.IWebImportAdapter;

public class SSBWebAdapter implements IWebImportAdapter {
	private static String WEB = "http://www.schachbund.ch/schachsport/fldownload.php";
	private static String WEB_DL = "http://www.schachbund.ch/schachsport";
	private List<ILink> mLinkList;
	private LinkFilter linkfilter;
	private PageReader reader = new PageReader();
	public SSBWebAdapter() {
		reader.setFilter(new ExtensionFilter(".zip", linkfilter=LinkFilter.createSSBFilter()));
		reader.setDownloadRoot(WEB_DL);
	}

	@Override
	public Object handleImport(InputStream pInputStream,
			IProgressMonitor pMonitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ILink> getLinks() throws IOException {
		if(mLinkList==null){
			reader.readPage(WEB);
			mLinkList = linkfilter.getLinks();
		}
		return mLinkList;
	}

}
