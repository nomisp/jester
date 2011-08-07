package ch.jester.ui.player.editor.view;


import java.util.List;

import messages.Messages;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.ui.databinding.PageController;
import ch.jester.common.ui.handlers.api.IHandlerAdd;
import ch.jester.common.ui.handlers.api.IHandlerDelete;
import ch.jester.common.ui.handlers.api.IHandlerEditor;
import ch.jester.common.ui.listeners.DefaultSelectionCountListener;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.view.AbstractView;
import ch.jester.common.utility.AdapterBinding;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;
import ch.jester.ui.Activator;


/**
 * Die PlayerView.
 *
 */
public class PlayersView extends AbstractView{
	public PlayersView() {
		
	}
	private TableViewer getTable(){
		return tableViewer;
	}

	public static final String ID = "ch.jester.ui.view.players"; //$NON-NLS-1$
	private Table table;
	private TableViewer tableViewer; 
	private DaoController<?> mController;
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
					tblclmnPlayers.setText(Messages.PlayersView_lbl_players_titel);
				
				/*	tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn city = tableViewerColumn.getColumn();
					tcl_tableViewComposite.setColumnData(city, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
					city.setText("City");*/
					
				
				}
				tableViewer.setContentProvider(ArrayContentProvider.getInstance());
			}
		}
		PlatformUI.getWorkbench().getHelpSystem().setHelp(table , "ch.jester.ui.playerview");
		postCreatePartControl();
	
	}

	protected void install(){
		getSite().setSelectionProvider(tableViewer);
		installPopupManager(tableViewer);
		createActions();
	}
	@Override
	public void setFocus() {
		super.setFocus();
		table.setFocus();
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
				mController.handleOpenEditor(selectedObject);
				
			}
			
		});

		
		IDaoService<Player> pdao= Activator.getDefault().getActivationContext().getService(IPlayerDao.class);
		mController = new DaoController<Player>(this, getTable(), pdao){

			@Override
			public String[] observableProperties() {
				return new String[]{"lastName","firstName"}; //$NON-NLS-1$ //$NON-NLS-2$
			}
			public String indexedCallBackLabels(Player pDao, int col) {
				if(col==0){
					return pDao.getLastName()+", "+pDao.getFirstName();
				}
				if(col==1){
					return pDao.getCity();
				}
				return null;
			};

			@Override
			public String callBackLabels(Player pDao) {
				return pDao.getLastName()+", "+pDao.getFirstName(); //$NON-NLS-1$
			}
			
		};
		//Activator.getDefault().getActivationContext().getServiceUtil().registerService(DaoController.class, mController);
		
		//Registrierung von sich selbst, als StructuredViewer und PageController
		AdapterBinding binding = new AdapterBinding(this);
		binding.add(tableViewer, StructuredViewer.class);
		binding.add(mController, DaoController.class, IHandlerDelete.class, IHandlerAdd.class, IHandlerEditor.class);
		binding.add(mController.getPageController(), PageController.class);
		binding.bind();
		Transfer[] transferTypes = new Transfer[]{LocalSelectionTransfer.getTransfer()};
		tableViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE, transferTypes, new DNDListener());
	}

	class DNDListener implements DragSourceListener{
		SelectionUtility su = new SelectionUtility(null);
		@Override
		public void dragStart(DragSourceEvent event) {
			//drag only saved entities
			su.setSelection(tableViewer.getSelection());
			@SuppressWarnings("unchecked")
			List<IEntityObject> entities = su.getAsStructuredSelection().toList();
			for(IEntityObject e:entities){
				if(e.isUnsafed()){
					event.doit=false;
					return;
				}
			}
			
		}

		@Override
		public void dragSetData(DragSourceEvent event) {
			// Here you do the convertion to the type which is expected.
	
			
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
			LocalSelectionTransfer.getTransfer().setSelection(selection);


		}

		@Override
		public void dragFinished(DragSourceEvent event) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
}
