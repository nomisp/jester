package ch.jester.importmanagerservice.impl.internal.finalimpl.webadapters;

import java.io.IOException;
import java.util.List;

import ch.jester.common.importer.AbstractWebAdapter;
import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.LinkFilter;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.preferences.PreferenceProperty;

public class SSBWebAdapter extends AbstractWebAdapter {
	private String WEB = "http://www.schachbund.ch/schachsport/fldownload.php";
	private static String WEB_DL = "http://www.schachbund.ch/schachsport";
	private String pattern ="(a href=\")([a-zA-Z0-9/\\.]*)";
	public SSBWebAdapter() {
		super();
		setDownloadAddress(WEB);
		reader.setFilter(new ExtensionFilter(".zip", super.linkfilter=createDefaultSSBFilter()));
		reader.setDownloadRoot(WEB_DL);
		getPreferenceManager().setPrefixKey("ch.jester.ssb.xls.importer");
		getPreferenceManager().create("webaddress", "Web Address", WEB);
		getPreferenceManager().create("webdl", "Absolut Path", WEB_DL);
		getPreferenceManager().create("pattern", "Pattern", pattern);
		getPreferenceManager().create("extensionFilter", "ExtensionFilter", ".zip");
		getPreferenceManager().create("groupName", "Name Group", 2);
		getPreferenceManager().create("groupURL", "URL Group", 2);
		
		
		reader.setFilter(createFilterFromProps());
		//reader.setDownloadRoot(WEB_DL);
	}

	private  LinkFilter createDefaultSSBFilter() {
		LinkFilter f = new LinkFilter();
		f.setPattern(pattern, 2, 2);

		return f;
	}
	protected ExtensionFilter createFilterFromProps() {
		LinkFilter f = new LinkFilter();
		String pattern = getPreferenceManager().getPropertyByInternalKey("pattern").getValue().toString();
		String exFilter = getPreferenceManager().getPropertyByInternalKey("extensionFilter").getValue().toString();
		int grpName = (Integer) getPreferenceManager().getPropertyByInternalKey("groupName").getValue();
		int grpURL = (Integer) getPreferenceManager().getPropertyByInternalKey("groupURL").getValue();
		f.setPattern(pattern, grpName, grpURL);
		super.linkfilter = f;
		ExtensionFilter ef = new ExtensionFilter(exFilter, f);
		reader.setFilter(ef);
		System.out.println("New Filter created: "+pattern+" "+exFilter+" "+grpName+" "+grpURL);
		return ef;
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
			PreferenceProperty preferenceProperty) {
		if(internalKey.equals("webaddress")){
			setDownloadAddress(mValue.toString());
		}else{
			createFilterFromProps();
			mLinkList=null;
		}
		
	}



}
