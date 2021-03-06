package ch.jester.common.ui.view;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.part.ViewPart;

import ch.jester.common.ui.utility.MenuManagerUtility;

/**
 * Eine um Hilfsmethoden erweiterte Standardview.
 *
 */
public class AbstractView extends ViewPart{
	protected MenuManager mPopupManager;
	protected IToolBarManager mToolbarManager;
	protected IMenuManager mMenuManager;
	protected UndoActionHandler mUndoActionHandler;
	protected RedoActionHandler mRedoActionHandler;
	protected IUndoContext mUndoContext;
	@Override
	public void init(IViewSite site) throws PartInitException {
			super.init(site);
			if(enableUndoRedo()){
				installUndoRedoHandlers();
			}
	}
	
	
	
	@Override
	public void createPartControl(Composite parent) {

	
	}

	/**
	 * Hilfsinitialisierung von Menu/Toolbar
	 */
	protected void postCreatePartControl(){
		initializeMenu();
		initializeToolBar();
		install();
	}
	
	@Override
	public void setFocus() {
		activateView();
		
	}
	/**
	 * Aktiviert die View.
	 */
	protected void activateView() {
		   getSite().getPage().activate(AbstractView.this);
		   
	}
	 
	/**
	 * Installiert einen PopupManager an der übergeben View.
	 * @param pViewer
	 */
	protected void installPopupManager(Viewer pViewer){
		//installiert den PopupManager
		mPopupManager = MenuManagerUtility.installPopUpMenuManager(getSite(), pViewer);
		//Listener: Fokus setzen, wenn ContextMenu aktiviert wird
		mPopupManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				activateView();
			}
		});
	}
	/**
	 * initialisiert die Toolbar
	 */
	private void initializeToolBar() {
		mToolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	
	}
	/**
	 * init vom Menu
	 */
	private void initializeMenu() {
		 mMenuManager = getViewSite().getActionBars()
				.getMenuManager();
	}
	
	/**
	 * installiert UndoRedo handlers.
	 */
	protected void installUndoRedoHandlers(){
		 mUndoContext = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		 mUndoActionHandler = new UndoActionHandler(this.getSite(), mUndoContext);
		 mUndoActionHandler.setPruneHistory(false);
		 mRedoActionHandler = new RedoActionHandler(this.getSite(), mUndoContext);
		 mRedoActionHandler.setPruneHistory(false);
		 
		  IActionBars actionBars = getViewSite().getActionBars();

		// Register the global menu actions
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), mUndoActionHandler);
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), mRedoActionHandler);
	
	}
	

	
	/**
	 * @return true wenn UNDO/REDO aktiv sein soll. Sonst false.
	 */
	protected boolean enableUndoRedo(){
		return true;
	}
	/**
	 * zusätzliche inits.
	 */
	protected void install(){
		
	}
}
