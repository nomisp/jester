package ch.jester.ui.filter;

import org.eclipse.jface.viewers.ViewerFilter;

public abstract class IFilter extends ViewerFilter{

	public abstract void setSearchText(String s);

}