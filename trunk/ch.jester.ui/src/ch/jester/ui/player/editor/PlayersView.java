package ch.jester.ui.player.editor;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import ch.jester.common.ui.listeners.DefaultSelectionCountListener;
import ch.jester.common.ui.listeners.OpenEditorDoubleClickListener;
import ch.jester.common.ui.utility.MenuManagerUtility;
import ch.jester.common.utility.DefaultAdapterFactory;
import ch.jester.ui.Activator;

public class PlayersView extends ViewPart{
	//private SelectionUtility mSelectionUtil = new SelectionUtility(null);
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			return element.toString();
		}
	}
	public TableViewer getTable(){
		return tableViewer;
	}

	public static final String ID = "ch.jester.ui.view.players"; //$NON-NLS-1$
	private MenuManager menuManager;
	private Table table;
	private TableViewer tableViewer; 
	private PlayerListController mController;
	private TableViewerColumn tableViewerColumn;
	public PlayersView() {
		
		
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
				tableViewer = new TableViewer(tableViewComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
				table = tableViewer.getTable();
				table.setHeaderVisible(true);
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
		
	
	}
	 private void activateView() {
		   getSite().getPage().activate(this);
		}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		getSite().setSelectionProvider(tableViewer);
		//installiert den PopupManager
		menuManager = MenuManagerUtility.installPopUpMenuManager(getSite(), tableViewer);
		//Listener: Fokus setzen, wenn ContextMenu aktiviert wird
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				activateView();
			}
		});
		
		//Registrierung von sich selbst, als StructuredViewer
		DefaultAdapterFactory factory = new DefaultAdapterFactory(this);
		factory.add(StructuredViewer.class, tableViewer);
		factory.registerAtPlatform();
		
		//wie viele Items sind selektiert
		tableViewer.addSelectionChangedListener(new DefaultSelectionCountListener());
		
		//öffne editor
		tableViewer.addDoubleClickListener(new OpenEditorDoubleClickListener());
		mController = new PlayerListController(getTable());
		Activator.getDefault().getActivationContext().getServiceUtil().registerService(PlayerListController.class, mController);
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
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
		// Set the focus
	}
}
