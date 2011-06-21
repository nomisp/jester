package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;
import java.io.IOException;
import java.util.List;

import ch.jester.common.importer.AbstractWebAdapter;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;


public class FIDEWebAdapter extends AbstractWebAdapter {
	private String WEB = "http://ratings.fide.com/download.phtml";
	private String FIDE_PATTERN = "(<a href=)(.*[^\\S>])(.*>)(.*)(</a>)";
	public FIDEWebAdapter() {
		super();
		setDownloadAddress(WEB);
		mPrefManager.setId("ch.jester.fide.txt.importer");
		mPrefManager.create("webaddress", "Web Address", WEB);
		mPrefManager.create("pattern", "Pattern", FIDE_PATTERN);
		mPrefManager.create("extensionFilter", "ExtensionFilter",".zip");
		mPrefManager.create("groupName", "Name Group", 4);
		mPrefManager.create("groupURL", "URL Group", 2);
		reader.setFilter(createFilterFromProperties());
		
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
		if(internalKey.equals("webaddress")){
			setDownloadAddress(mValue.toString());
		}else{
			createFilterFromProperties();
			mLinkList=null;
		}
		
	}



}
