package ch.jester.ui.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import ch.jester.model.Player;

public class PersonFilter extends IFilter {

	private String searchString;

	/* (non-Javadoc)
	 * @see ch.jester.ui.filter.IFilter#setSearchText(java.lang.String)
	 */
	@Override
	public void setSearchText(String s) {
		
		// Search must be a substring of the existing value
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Player p = (Player) element;
		if (p.getLastName().matches(searchString)) {
			return true;
		}
		if (p.getLastName().matches(searchString)) {
			return true;
		}
		return false;
	}
}
