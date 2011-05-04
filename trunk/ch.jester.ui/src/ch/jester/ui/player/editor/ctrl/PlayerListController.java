package ch.jester.ui.player.editor.ctrl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.EditorAccessor.EditorAccess;
import ch.jester.common.ui.utility.IEditorInputAccess;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.contentprovider.PageController;
import ch.jester.ui.player.editor.PlayerEditor;


public class PlayerListController{
	//private List<Player> mPlayers = new ArrayList<Player>();
	private TableViewer mViewer;
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	IPlayerDao persister = mServices.getExclusiveService(IPlayerDao.class);
	ViewPart mPart;
	PageController<Player> pageController;
	DataBindingContext context;
	WritableList obsModel;
	public PlayerListController(){

	}

	public PlayerListController(ViewPart pPart, TableViewer pViewer){
		int cacheSize = 1000;
		mViewer=pViewer;
		context = new DataBindingContext();
		obsModel = new WritableList(new ArrayList<Player>(), Player.class);
		pageController = new PageController<Player>(obsModel,mViewer, mServices.getExclusiveService(IPlayerDao.class), cacheSize);
		mPart=pPart;
		mViewer.setInput(obsModel);
		ObservableListContentProvider contentProvider;
		mViewer.setContentProvider(contentProvider = new ObservableListContentProvider());
		ObservableMapLabelProvider lblprov = new PlayerMapLabelProvider(Properties
				.observeEach(contentProvider.getKnownElements(),
						BeanProperties.values(
								new String[] { "lastName", "firstName" } )));
		mViewer.setLabelProvider(lblprov);
	}
	

	
	public void addPlayer(Player pPlayer) {
		addPlayer(createList(pPlayer));
	}
	public void addPlayer(Collection<Player> pPlayerCollection) {
		obsModel.addAll(pPlayerCollection);
		context.updateTargets();
	}
	public void openEditor(Object pObject){
		final EditorAccess access = mServices.getService(IEditorService.class).openEditor(pObject);	
		IPartService service = (IPartService)mPart.getSite().getService(IPartService.class);
		service.addPartListener(new PartListener2Adapter() {
			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				if(partRef == access.getReference()){
					PlayerEditor closedEditor = (PlayerEditor) access.getReference().getEditor(false);
					if(!closedEditor.wasSaved()){
						IEditorInputAccess<?> input = (IEditorInputAccess<?>)closedEditor.getEditorInput();
						obsModel.remove(input.getInput());
					}
					System.out.println("Was saved: "+closedEditor.wasSaved());
				}
				
			}
		});
	}
	
	public void removePlayer(Player pPlayer) {
		removePlayer(createList(pPlayer));
	}
	public void removePlayer(final List<Player> pPlayerList) {
		
		UIUtility.syncExecInUIThread(new Runnable() {
			//ToDo remove from DB
			@Override
			public void run() {
				persister.delete(pPlayerList);
				obsModel.removeAll(pPlayerList);
				context.updateTargets();
				
			}
		});

	}
	
	/**
	 * Für die Search
	 * @param pPlayerCollection
	 */
	public void setSearched(final Collection<Player> pPlayerCollection){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				mViewer.setInput(new WritableList(pPlayerCollection, Player.class));
				
			}
		});

		
	}
	public void clearSearched(){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				mViewer.setInput(pageController.getPageContent());
				
			}
		});
	
	}
	
	private List<Player> createList(Player o){
		ArrayList<Player> list = new ArrayList<Player>();
		list.add(o);
		return list;
	}

	public PageController<?> getPageController() {
		return pageController;
	}
	class PlayerMapLabelProvider extends ObservableMapLabelProvider{

		public PlayerMapLabelProvider(IObservableMap[] attributeMaps) {
			super(attributeMaps);
			
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return getText(element);
		}
		@Override
		public String getText(Object element) {
			if(element instanceof Player){
				Player p = (Player) element;
				return p.getLastName()+", "+p.getFirstName();
			}
			return super.getText(element);
		}
		
	}

}
