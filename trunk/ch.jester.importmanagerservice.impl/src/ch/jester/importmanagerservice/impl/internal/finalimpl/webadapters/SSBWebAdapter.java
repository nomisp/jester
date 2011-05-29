package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;

import java.io.IOException;
import java.util.List;

import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.LinkFilter;
import ch.jester.commonservices.api.importer.ILink;

public class SSBWebAdapter extends AbstractWebAdapter {
	private static String WEB = "http://www.schachbund.ch/schachsport/fldownload.php";
	private static String WEB_DL = "http://www.schachbund.ch/schachsport";
	public SSBWebAdapter() {
		super();
		reader.setFilter(new ExtensionFilter(".zip", linkfilter=LinkFilter.createSSBFilter()));
		reader.setDownloadRoot(WEB_DL);
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
