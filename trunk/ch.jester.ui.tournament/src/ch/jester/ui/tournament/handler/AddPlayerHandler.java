package ch.jester.ui.tournament.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.navigator.CommonNavigator;

import ch.jester.common.ui.editor.IEditorDaoInputAccess;
import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.dao.ICategoryDao;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.factories.ModelFactory;
import ch.jester.model.factories.PlayerNamingUtility;
import ch.jester.ui.tournament.cnf.PlayerFolder;
import ch.jester.ui.tournament.cnf.TournamentNavigator;

/**
 * Handler um Spieler zu einer Kategorie zuzuordnen
 * @author Peter
 *
 */
public class AddPlayerHandler extends AbstractCommandHandler implements IHandler {

	private Category selectedCategory;
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		//alternative(event);
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Category cat = getFirstSelectedAs(Category.class);
		if(cat==null){
			PlayerFolder folder = getFirstSelectedAs(PlayerFolder.class);
			if(folder!=null){
				cat = (Category) folder.getParent();
			}
		}
		if(cat!=null){
			selectedCategory = cat;
//			openEditor(cat);
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(window.getShell(), new PlayerLabelProvider());
			dialog.setTitle("Select Players");
			dialog.setMessage("Select Players (Ctrl+Click for multiple selection)");
			dialog.setElements(getPlayers());
			dialog.setMultipleSelection(true);
			dialog.setIgnoreCase(true);
			dialog.setStatusLineAboveButtons(true);
			if (dialog.open() != Window.OK) {
				return false;
			}
			Object[] result = dialog.getResult();
			wirePlayers(result);
			CommonNavigator cn = (CommonNavigator)getView(TournamentNavigator.ID);
			cn.getCommonViewer().refresh();
		}
		return null;
	}
	
	/**
	 * Holen der Spieler aus der DB
	 * @return
	 */
	private Object[] getPlayers() {
		IDaoService<Player> pdao= getServiceUtil().getDaoServiceByServiceInterface(IPlayerDao.class);
		return pdao.getAll().toArray();
	}
	
	/**
	 * Verknüpfen der Spieler mit der Kategorie
	 * @param selection
	 */
	private void wirePlayers(Object[] selection) {
		for (Object object : selection) {
			Player p = (Player)object;
			PlayerCard pc = ModelFactory.getInstance().createPlayerCard(selectedCategory, p, 
										selectedCategory.getTournament().getPrimaryRankingSystem().getShortType());
			selectedCategory.addPlayerCard(pc);
		}
		IDaoService<Category> catDao= getServiceUtil().getDaoServiceByServiceInterface(ICategoryDao.class);
		catDao.save(selectedCategory);
	}
	
	public class CategoryEditorInput implements IEditorDaoInputAccess<Category> {
		
		private final Integer id;
		
		public CategoryEditorInput(Integer id) {
			this.id = id;
		}
	    
		public int getId() {
	        return id;
	    }
	    
		@Override
		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
			return null;
		}

		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return null;
		}

		@Override
		public String getName() {
			return String.valueOf(id);
		}

		@Override
		public IPersistableElement getPersistable() {
			return null;
		}

		@Override
		public String getToolTipText() {
			return "Adds players to a Category";
		}
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + id;
	        return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        CategoryEditorInput other = (CategoryEditorInput) obj;
	        if (id != other.id)
	            return false;
	        return true;
	    }

		@Override
		public Category getInput() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setInput(Category pT) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isAlreadyDirty() {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	/**
	 * Label Provider für die Spieler
	 * @author Peter
	 *
	 */
	public class PlayerLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			Player p = (Player)element;
			StringBuffer sb = new StringBuffer();
			sb.append(PlayerNamingUtility.createName(p));
			sb.append(" ");
			sb.append(p.getCity());
			return  sb.toString();
		}
	}
}
