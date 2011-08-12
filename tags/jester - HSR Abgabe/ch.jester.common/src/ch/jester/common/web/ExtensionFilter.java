package ch.jester.common.web;

import ch.jester.common.web.PageReader.IPageReaderFilter;

/**
 * ein ExtensionFilter z.B für Links welche mit zip enden.
 * Der ExtensionFilter ist ein Decorator für andere Filter.
 * Er gibt das Resultat nur weiter, wenn die Extension stimmt.
 *
 */
public class ExtensionFilter implements IPageReaderFilter{
	private IPageReaderFilter mFilter;
	private String mExtension;
	/**
	 * Dekorieren eines anderen Filters
	 * @param pExtension die Extension welche vorhanden sein muss
	 * @param pFilter ein anderer Filter
	 */
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