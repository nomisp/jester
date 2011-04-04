package ch.jester.ui.player;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
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
import ch.jester.model.Player;
import ch.jester.ui.Activator;

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

	public static final String ID = "ch.jester.ui.view.players"; //$NON-NLS-1$
	private MenuManager menuManager;
	private Table table;
	private TableViewer tableViewer; 
	private PlayerListModel mPlayerList;
	private TableViewerColumn tableViewerColumn;
	public PlayersView() {
		mPlayerList = Activator.getDefault().getActivationContext().getService(PlayerListModel.class);

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
		m_bindingContext = initDataBindings();
		
		
	
	}


	/**
	 * Create the actions.
	 */
	private void createActions() {
		getSite().setSelectionProvider(tableViewer);
		menuManager = MenuManagerUtility.installPopUpMenuManager(getSite(), tableViewer);
		
		DefaultAdapterFactory factory = new DefaultAdapterFactory(this);
		factory.add(StructuredViewer.class, tableViewer);
		factory.registerAtPlatform();
		
		tableViewer.addSelectionChangedListener(new DefaultSelectionCountListener());
		
		tableViewer.addDoubleClickListener(new OpenEditorDoubleClickListener());
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
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(listContentProvider);
		//
		IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), Player.class, new String[]{"lastName", "firstName"});
		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		//
		IObservableList mPlayerListPlayersObserveList = BeansObservables.observeList(Realm.getDefault(), mPlayerList, "players");
		tableViewer.setInput(mPlayerListPlayersObserveList);
		
		//
		return bindingContext;
	}
}
