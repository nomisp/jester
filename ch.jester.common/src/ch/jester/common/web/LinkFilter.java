package ch.jester.common.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.jester.common.web.PageReader.IPageReaderFilter;
import ch.jester.commonservices.api.importer.ILink;

public class LinkFilter implements IPageReaderFilter{
	private Pattern mLinkPattern;
	private int mURLGroup, mNameGroup;
	public static String FIDE_PATTERN = "(<a href=)(.*[^\\S>])(.*>)(.*)(</a>)";
	List<ILink> mLinks = new ArrayList<ILink>();
	@Override
	public void filter(String arg0, PageReader pReader) {
		Matcher matcher = mLinkPattern.matcher(arg0);
		int start = 0;
		while(matcher.find(start)){
			String url = matcher.group(mURLGroup);
			String text = matcher.group(mNameGroup);
			mLinks.add(pReader.createLink(text, url));
			start = matcher.end()+1;
			
		}
		
	}
	public void setPattern(String pPattern, int pGroupName, int pGroupURL){
		mLinkPattern=Pattern.compile(pPattern);
		mURLGroup=pGroupURL;
		mNameGroup=pGroupName;
	}

	
	public static LinkFilter createFIDEFilter(){
		LinkFilter f = new LinkFilter();
		f.setPattern(FIDE_PATTERN, 4, 2);
		return f;
		
	}
	
	public List<ILink> getLinks(){
		return mLinks;
	}
	public static LinkFilter createSSBFilter() {
		String pattern ="(a href=\")([a-zA-Z0-9/\\.]*)";
		LinkFilter f = new LinkFilter();
		f.setPattern(pattern, 2, 2);
		return f;
	}
	

	
}