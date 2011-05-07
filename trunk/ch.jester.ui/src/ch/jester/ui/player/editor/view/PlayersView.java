package ch.jester.ui.player.editor.view;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.ui.databinding.PageController;
import ch.jester.common.ui.listeners.DefaultSelectionCountListener;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.view.AbstractView;
import ch.jester.common.utility.DefaultAdapterFactory;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;
import ch.jester.ui.Activator;


public class PlayersView extends AbstractView{
	private TableViewer getTable(){
		return tableViewer;
	}

	public static final String ID = "ch.jester.ui.view.players"; //$NON-NLS-1$
	private Table table;
	private TableViewer tableViewer; 
	private DaoController mController;
	private TableViewerColumn tableViewerColumn;

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
			}
		}
		
		postCreatePartControl();
	
	}

	protected void install(){
		getSite().setSelectionProvider(tableViewer);
		
		installPopupManager(tableViewer);
		createActions();
	}
	
	
	protected void createActions() {
		//wie viele Items sind selektiert
		tableViewer.addSelectionChangedListener(new DefaultSelectionCountListener());
		
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				activateView();
				
			}
		});
		
		//öffne editor
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object selectedObject = new SelectionUtility(event.getSelection()).getFirstSelected();
				mController.openEditor(selectedObject);
				
			}
			
		});

		
		IDaoService<Player> pdao= Activator.getDefault().getActivationContext().getService(IPlayerDao.class);
		mController = new DaoController<Player>(this, getTable(), pdao){

			@Override
			public String[] observableProperties() {
				return new String[]{"lastName","firstName"};
			}

			@Override
			public String callBackLabels(Player pDao) {
				return pDao.getLastName()+", "+pDao.getFirstName();
			}
			
		};
		//Activator.getDefault().getActivationContext().getServiceUtil().registerService(DaoController.class, mController);
		
		//Registrierung von sich selbst, als StructuredViewer und PageController
		DefaultAdapterFactory factory = new DefaultAdapterFactory(this);
		factory.add(StructuredViewer.class, tableViewer);
		factory.add(DaoController.class, mController);
		factory.add(PageController.class, mController.getPageController());
		factory.registerAtPlatform();
	}

}
