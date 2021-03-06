package ch.jester.ui.tournament.cnf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.navigator.CommonViewer;

import ch.jester.common.persistency.EventLoadMatchingFilter;
import ch.jester.common.persistency.PersistencyListener;
import ch.jester.common.ui.handlers.api.IHandlerDelete;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.AdapterBinding;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IDatabaseStateService;
import ch.jester.commonservices.api.persistency.IDatabaseStateService.State;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.ICategoryDao;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Root;
import ch.jester.model.Round;
import ch.jester.model.Tournament;

/**
 * ContentProvider für den NavigationTree des Turnieres
 *
 */
public class TournamentContentProvider implements ITreeContentProvider, IHandlerDelete<IEntityObject> {

	private static final Object[] EMPTY_ARRAY = new Object[0];
	private Tournament[] tournaments;
	private ServiceUtility su = new ServiceUtility();
	private CommonViewer viewer;
	
	public TournamentContentProvider() {
		IPersistencyEventQueue queue = su.getService(IPersistencyEventQueue.class);
		//Installieren eines Listeners mit einem Filter, der nur Tournament Changes von der DB
		//weiterleitet.
		queue.addListener(new PersistencyListener(new EventLoadMatchingFilter(Tournament.class)) {		
			@Override
			public void persistencyEvent(IPersistencyEvent pEvent) {
				initializeTournaments();
				UIUtility.syncExecInUIThread(new Runnable() {
					@Override
					public void run() {
						viewer.refresh();
					}
				});	
			}
		});
		queue.addListener(new PersistencyListener(new EventLoadMatchingFilter(Category.class)) {		
			@Override
			public void persistencyEvent(IPersistencyEvent pEvent) {
				UIUtility.syncExecInUIThread(new Runnable() {
					@Override
					public void run() {
						Object o[] = viewer.getExpandedElements();
						viewer.setExpandedElements(o);
						TreePath[] paths = viewer.getExpandedTreePaths();
						if(paths.length==0){return;}
						paths[0].getLastSegment();

						viewer.refresh();
						viewer.setExpandedTreePaths(paths);
						viewer.getControl().setRedraw(false);
						for(TreePath path:paths){
							TreePath parentPath = path.getParentPath();
							Object leaf = parentPath.getLastSegment();
							viewer.expandToLevel(leaf, parentPath.getSegmentCount());
							
							//Object lastLeaf = path.getLastSegment();
							//viewer.expandToLevel(lastLeaf, path.getSegmentCount());
						}
						viewer.getControl().setRedraw(true);
					}
				});	
			}
		});
	}
	
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
			return getCategoryChildren((Category)parentElement);
		} else if (parentElement instanceof PlayerFolder) {
			return ((PlayerFolder)parentElement).getElements();
		} else if (parentElement instanceof Round) {
			return getPairings((Round)parentElement);
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Pairing) {
			return ((Pairing)element).getRound();
		} else if (element instanceof Round) {
			return ((Round)element).getCategory();
		} else if (element instanceof Player) {
			ICategoryDao categoryPersister = su.getDaoServiceByServiceInterface(ICategoryDao.class);
			return categoryPersister.findByPlayer((Player)element);
		} else if (element instanceof PlayerFolder) {
			return ((PlayerFolder)element).getParent();
		} else if (element instanceof Category) {
			return ((Category)element).getTournament();
		} else if (element instanceof Tournament) {
			return ((Tournament)element).getRootElement();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return (element instanceof Root || element instanceof Tournament 
				|| element instanceof Category || element instanceof PlayerFolder 
				|| element instanceof Round);
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
		this.viewer = (CommonViewer) viewer;
		AdapterBinding binding = new AdapterBinding(((CommonViewer) viewer).getCommonNavigator());
		binding.add(this, IHandlerDelete.class);
		binding.bind();
	}
	
	/**
	 * Laden aller Turniere aus der Datenbank
	 */
	private void initializeTournaments() {
		su.getService(IDatabaseStateService.class).executeOnStateChange(State.RUN, new Runnable(){
			@Override
			public void run() {
				IDaoService<Tournament> tournamentPersister = su.getDaoServiceByEntity(Tournament.class);
				List<Tournament> allTournaments = tournamentPersister.executeNamedQuery("AllActiveTournaments");
//				tournaments = allTournaments.size() > 0 ? (Tournament[]) allTournaments.toArray() : new Tournament[0];
				tournaments = getTournamentArray(allTournaments);
				UIUtility.syncExecInUIThread(new Runnable(){
					@Override
					public void run() {
						if(!viewer.isBusy()){
							if(viewer.getControl().isDisposed()){return;}
							viewer.refresh();
						}
					}
					
				});
			}
			
		});

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
	
	
	/**
	 * Liefert die Child Objekte einer Kategorie (Player und Rounds)
	 * @param category
	 * @return Object[] mit den Player und Round Objekten
	 */
	private Object[] getCategoryChildren(Category category) {
		List<Round> rounds = category.getRounds();
		List<Object> objects = new ArrayList<Object>();
		objects.add(new PlayerFolder(category));
		objects.addAll(rounds);
		return objects.toArray();
	}
	
	private Object[] getPairings(Round round) {
		Object[] pairings = round.getPairings().toArray();
		if (pairings != null) return pairings;
		return EMPTY_ARRAY;
	}

	@Override
	public void handleDelete(List<IEntityObject> pList) throws ProcessingException{
		try{
		for(IEntityObject t:pList){
			if (t instanceof Tournament) {
				IDaoService<Tournament> tournamentPersister = su.getDaoServiceByEntity(Tournament.class);
				tournamentPersister.delete((Tournament)t);
			} else if (t instanceof Category) {	// Falls im CNF eine Category selektiert wurde
				IDaoService<Tournament> tournamentPersister = su.getDaoServiceByEntity(Tournament.class);
				tournamentPersister.delete(((Category)t).getTournament());
			}
		}
		}catch(Exception e){
			throw new ProcessingException(e);
		}
	}
}
