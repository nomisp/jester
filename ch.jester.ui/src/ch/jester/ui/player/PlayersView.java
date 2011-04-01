package ch.jester.ui.player;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import ch.jester.common.ui.labelprovider.DefaultToStringTableCellProvider;
import ch.jester.common.ui.utility.MenuManagerUtility;
import ch.jester.common.utility.DefaultAdapterFactory;

public class PlayersView extends ViewPart{

	public static final String ID = "ch.jester.ui.view.players"; //$NON-NLS-1$
	private MenuManager menuManager;
	private Table table;
	private TableViewer tableViewer;
	public PlayersView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100, -10);
		fd_table.left = new FormAttachment(0);
		fd_table.top = new FormAttachment(0, 32);
		fd_table.right = new FormAttachment(0, 148);
		table.setLayoutData(fd_table);

		createActions();
		initializeToolBar();
		initializeMenu();

		menuManager = MenuManagerUtility.installPopUpMenuManager(getSite(), tableViewer);

		{
			TableViewerColumn tvCol1 = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tableColumn = tvCol1.getColumn();
			tableColumn.setWidth(100);
			tableColumn.setText("Player");
			tvCol1.setLabelProvider(new DefaultToStringTableCellProvider());
		}
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		
		DefaultAdapterFactory factory = new DefaultAdapterFactory(this);
		factory.add(StructuredViewer.class, tableViewer);
		factory.registerAtPlatform();
		
		
	
	}


	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
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
