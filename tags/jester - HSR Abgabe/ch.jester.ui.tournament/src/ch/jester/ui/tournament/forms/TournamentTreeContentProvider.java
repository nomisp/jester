package ch.jester.ui.tournament.forms;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.Tournament;

/**
 * ContentProvider für Turniere
 *
 */
public class TournamentTreeContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Tournament) {
			return getCategories((Tournament)parentElement);
		} else if (parentElement instanceof Category) {
			return getRounds((Category)parentElement);
//		} else if (parentElement instanceof PlayerFolder) {
//			return ((PlayerFolder)parentElement).getElements();
//		} else if (parentElement instanceof Round) {
//			return getPairings((Round)parentElement);
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Round) {
			return ((Round)element).getCategory();
		}
		return EMPTY_ARRAY;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof Tournament || element instanceof Category;
	}
	
	private Object[] getCategories(Tournament tournament) {
		Object[] categories = tournament.getCategories().toArray();
		if (categories != null) return categories;
		return EMPTY_ARRAY;
	}
	
	private Object[] getRounds(Category category) {
		Object[] rounds = category.getRounds().toArray();
		if (rounds != null) return rounds;
		return EMPTY_ARRAY;
	}

}
