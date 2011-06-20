package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;
import java.io.IOException;
import java.util.List;

import ch.jester.common.importer.AbstractWebAdapter;
import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.LinkFilter;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;


public class FIDEWebAdapter extends AbstractWebAdapter {
	private String WEB = "http://ratings.fide.com/download.phtml";
	private String FIDE_PATTERN = "(<a href=)(.*[^\\S>])(.*>)(.*)(</a>)";
	public FIDEWebAdapter() {
		super();
		setDownloadAddress(WEB);
		mPrefManager.setPrefixKey("ch.jester.fide.txt.importer");
		reader.setFilter(new ExtensionFilter(".zip", linkfilter=createDefaultFIDEFilter()));

		//
		mPrefManager.create("webaddress", "Web Address", WEB);
		mPrefManager.create("pattern", "Pattern", FIDE_PATTERN);
		mPrefManager.create("extensionFilter", "ExtensionFilter",".zip");
		mPrefManager.create("groupName", "Name Group", 4);
		mPrefManager.create("groupURL", "URL Group", 2);
		//
		
	}

	private LinkFilter createDefaultFIDEFilter(){
		LinkFilter f = new LinkFilter();
		f.setPattern(FIDE_PATTERN, 4, 2);
		return f;
		
	}

	@Override
	public List<ILink> getLinks() throws IOException {
		if(mLinkList==null){
			reader.readPage(getDownloadAddress());
			mLinkList = linkfilter.getLinks();
		}
		return mLinkList;
	}

	@Override
	public void propertyValueChanged(String internalKey, Object mValue,
			IPreferenceProperty preferenceProperty) {
		System.out.println("internalKey; "+internalKey);
	}






}
