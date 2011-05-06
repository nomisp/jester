package ch.jester.ui.player.editor.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.menus.DynamicToolBarContributionItem;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.part.ViewPart;

import ch.jester.common.ui.listeners.DefaultSelectionCountListener;
import ch.jester.common.ui.listeners.OpenEditorDoubleClickListener;
import ch.jester.common.ui.utility.MenuManagerUtility;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.common.utility.DefaultAdapterFactory;
import ch.jester.ui.Activator;
import ch.jester.ui.contentprovider.PageController;
import ch.jester.ui.player.editor.ctrl.PlayerListController;

public class PlayersView extends ViewPart{
	private DataBindingContext m_bindingContext;
	
	//private SelectionUtility mSelectionUtil = new SelectionUtility(null);
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			return element.toString();
		}
	}
	private TableViewer getTable(){
		return tableViewer;
	}

	public static final String ID = "ch.jester.ui.view.players"; //$NON-NLS-1$
	private MenuManager mPopupManager;
	private Table table;
	private TableViewer tableViewer; 
	private PlayerListController mController;
	private TableViewerColumn tableViewerColumn;
	IToolBarManager toolbarManager;
	private UndoActionHandler undoActionHandler;
	private RedoActionHandler redoActionHandler;
	public PlayersView() {
		
	}
	@Override
	public void init(IViewSite site) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site);
	/*	IUndoContext undoContext = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		 undoActionHandler = new UndoActionHandler(this.getSite(), undoContext);
		 undoActionHandler.setPruneHistory(false);
		 redoActionHandler = new RedoActionHandler(this.getSite(), undoContext);
		 redoActionHandler.setPruneHistory(false);
		 
		  IActionBars actionBars = getViewSite().getActionBars();

		// Register the global menu actions
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoActionHandler);
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoActionHandler);*/
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		{
			Composite tableViewComposite = new Composite(container, SWT.NONE);
			TableColumnLayout tcl_tableViewComposite = new TableColumnLayout();
			tableViewComposite.setLayout(tcl_tableViewComposite);
			{
				tableViewer = new TableViewer(tableViewComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
				table = tableViewer.getTable();
				table.setLinesVisible(true);
			
				{
					tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnPlayers = tableViewerColumn.getColumn();
					tcl_tableViewComposite.setColumnData(tblclmnPlayers, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					tblclmnPlayers.setText("Players");
				}
				tableViewer.setContentProvider(ArrayContentProvider.getInstance());
				tableViewer.setLabelProvider(new TableLabelProvider());
			}
		}
		
		createActions();
		initializeToolBar();
		initializeMenu();
		m_bindingContext = initDataBindings();
		
	
	}
	 private void activateView() {
		   getSite().getPage().activate(this);
		   
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		
		
		table.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				table.redraw();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				activateView();
			}
		});
		getSite().setSelectionProvider(tableViewer);
		//installiert den PopupManager
		mPopupManager = MenuManagerUtility.installPopUpMenuManager(getSite(), tableViewer);
		//Listener: Fokus setzen, wenn ContextMenu aktiviert wird
		mPopupManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				activateView();
			}
		});
		
		

		
		//wie viele Items sind selektiert
		tableViewer.addSelectionChangedListener(new DefaultSelectionCountListener());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				activateView();
				
			}
		});
		
		//öffne editor
		tableViewer.addDoubleClickListener(new OpenEditorDoubleClickListener());
		mController = new PlayerListController(this, getTable());
		Activator.getDefault().getActivationContext().getServiceUtil().registerService(PlayerListController.class, mController);
		
		//Registrierung von sich selbst, als StructuredViewer und PageController
		DefaultAdapterFactory factory = new DefaultAdapterFactory(this);
		factory.add(StructuredViewer.class, tableViewer);
		factory.add(PageController.class, mController.getPageController());
		factory.registerAtPlatform();
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		
	
	}
	
	public void ggg(){
		IContributionItem item = toolbarManager.find("ch.jester.gotopagefield");
		DynamicToolBarContributionItem tci = (DynamicToolBarContributionItem) item;
		
		//tci.getWorkbenchWindow()
	
		//AdapterUtility.getAdaptedObject(item, Text.class);
		System.out.println(item);
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
		

	}

	@Override
	public void setFocus() {
		//System.out.println("View has focus");
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		
		
		//IObservableList selfList = Properties.selfList(Player.class).observe(mController.getPlayerList());
		//ViewerSupport.bind(tableViewer, selfList, BeanProperties.values(Player.class, new String[]{"firstName", "lastName"}));
		//bindingContext.b
		//
		return bindingContext;
	}
}
