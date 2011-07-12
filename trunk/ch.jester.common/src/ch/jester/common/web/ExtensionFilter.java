package ch.jester.common.web;

import ch.jester.common.web.PageReader.IPageReaderFilter;

/**
 * @author  t117221
 */
public class ExtensionFilter implements IPageReaderFilter{
	/**
	 * @uml.property  name="mFilter"
	 * @uml.associationEnd  
	 */
	private IPageReaderFilter mFilter;
	private String mExtension;
	public ExtensionFilter(String pExtension, IPageReaderFilter pFilter){
		mExtension=pExtension;
		mFilter=pFilter;
	}
	
	@Override
	public void filter(String pContentLine, PageReader pReader) {
		if(pContentLine.contains(mExtension)&&mFilter!=null){
			mFilter.filter(pContentLine, pReader);
		}
		
	}
	
}