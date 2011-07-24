package ch.jester.common.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.jester.common.web.PageReader.IPageReaderFilter;
import ch.jester.commonservices.api.importer.ILink;

public class LinkFilter implements IPageReaderFilter{
	private Pattern mLinkPattern;
	private int mURLGroup;
	private int mNameGroup;
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

	public void setURLGroup(int i){
		mURLGroup = i;
	}
	
	public void setNameGroup(int i){
		mNameGroup = i;
	}
	
	public int getURLGroup(){
		return mURLGroup;
	}
	
	public int setNameGroup(){
		return mNameGroup;
	}
	
	public List<ILink> getLinks(){
		return mLinks;
	}
	
}