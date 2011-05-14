package ch.jester.ui.tournament.cnf;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.ICategoryDao;
import ch.jester.dao.ITournamentDao;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Root;
import ch.jester.model.Tournament;

/**
 * ContentProvider für den NavigationTree des Turnieres
 * @author Peter
 *
 */
public class TournamentContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];
	private Tournament[] tournaments;
	private ServiceUtility su = new ServiceUtility();
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Root) {
			if (tournaments == null) {
				initializeTournaments();
			}
			return tournaments;
		} else if (parentElement instanceof Tournament) {
			return getCategories((Tournament)parentElement);
		} else if (parentElement instanceof Category) {
			return getPlayers((Category)parentElement);
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Player) {
			ICategoryDao categoryPersister = su.getExclusiveService(ICategoryDao.class);
			return categoryPersister.findByPlayer((Player)element);
		} else if (element instanceof Category) {
			return ((Category)element).getTournament();
		} else if (element instanceof Tournament) {
			return ((Tournament)element).getRootElement();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return (element instanceof Root || element instanceof Tournament || element instanceof Category);
	}

	@Override
	public void dispose() {
		this.tournaments = null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Laden aller Turniere aus der Datenbank
	 */
	private void initializeTournaments() {
		ITournamentDao tournamentPersister = su.getExclusiveService(ITournamentDao.class);
		List<Tournament> allTournaments = tournamentPersister.getAll("AllTournaments");
//		tournaments = allTournaments.size() > 0 ? (Tournament[]) allTournaments.toArray() : new Tournament[0];
		tournaments = getTournamentArray(allTournaments);
	}
	
	private Tournament[] getTournamentArray(List<Tournament> list) {
		Tournament[] arr = new Tournament[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	private Object[] getCategories(Tournament tournament) {
		Object[] categories = tournament.getCategories().toArray();
		if (categories != null) return categories;
		return EMPTY_ARRAY;
	}
	
	private Object[] getPlayers(Category category) {
		Object[] players = category.getPlayers().toArray();
		if (players != null) return players;
		return EMPTY_ARRAY;
	}
}
