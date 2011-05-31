package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;
import java.io.IOException;
import java.util.List;

import ch.jester.common.importer.AbstractWebAdapter;
import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.LinkFilter;
import ch.jester.commonservices.api.importer.ILink;


public class FIDEWebAdapter extends AbstractWebAdapter {
	private static String WEB = "http://ratings.fide.com/download.phtml";
	public FIDEWebAdapter() {
		super();
		reader.setFilter(new ExtensionFilter(".zip", linkfilter=LinkFilter.createFIDEFilter()));

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
