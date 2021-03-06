package ch.jester.importer.ssb;

import java.io.IOException;
import java.util.List;

import ch.jester.common.importer.AbstractWebAdapter;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;

/**
 * WebAdapter Impl für {@link SSBExcelImporter2}
 *
 */
public class SSBWebAdapter extends AbstractWebAdapter {
	private String WEB = "http://www.schachbund.ch/schachsport/fldownload.php";
	private static String WEB_DL = "http://www.schachbund.ch/schachsport";
	private String pattern ="(a href=\")([a-zA-Z0-9/\\.]*)";
	public SSBWebAdapter() {
		super();
		setDownloadAddress(WEB);
		reader.setDownloadRoot(WEB_DL);
		mPrefManager.setId("ch.jester.ssb.xls.importer");
		mPrefManager.create("webaddress", "Web Address", WEB);
		mPrefManager.create("webdl", "Absolute Path", WEB_DL);
		mPrefManager.create("pattern", "Pattern", pattern);
		mPrefManager.create("extensionFilter", "ExtensionFilter", ".zip");
		mPrefManager.create("groupName", "Name Group", 2);
		mPrefManager.create("groupURL", "URL Group", 2);
		reader.setFilter(createFilterFromProperties());

	}


	@Override
	public List<ILink> getLinks() throws IOException {
		if(mLinkList==null){
			reader.readPage(getDownloadAddress());
			mLinkList = super.linkfilter.getLinks();
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
