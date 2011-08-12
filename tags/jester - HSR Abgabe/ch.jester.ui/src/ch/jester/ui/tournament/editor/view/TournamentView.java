package ch.jester.ui.tournament.editor.view;

import java.util.Collection;

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
import ch.jester.common.ui.handlers.api.IHandlerAdd;
import ch.jester.common.ui.handlers.api.IHandlerDelete;
import ch.jester.common.ui.handlers.api.IHandlerEditor;
import ch.jester.common.ui.listeners.DefaultSelectionCountListener;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.view.AbstractView;
import ch.jester.common.utility.AdapterBinding;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.dao.ITournamentDao;
import ch.jester.model.Tournament;
import ch.jester.ui.Activator;


/**
 * Die Turnier Admin View
 *
 */
public class TournamentView extends AbstractView{
	private TableViewer getTable(){
		return tableViewer;
	}

	public static final String ID = "ch.jester.ui.view.tournamentview"; //$NON-NLS-1$
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
					tblclmnPlayers.setText("Tournaments");
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
				mController.handleOpenEditor(selectedObject);
				
			}
			
		});

		
		final IDaoService<Tournament> pdao= Activator.getDefault().getActivationContext().getService(ITournamentDao.class);
		mController = new DaoController<Tournament>(this, getTable(), pdao){
			@Override
			public String[] observableProperties() {
				return new String[]{"name"};
			}

			@Override
			public String callBackLabels(Tournament pDao) {
				return pDao.getName()+" "+pDao.getDateFrom()+" - "+pDao.getDateTo();
			}
			public void handleAdd(Tournament pPlayer) {
				super.handleAdd(pPlayer);
				pdao.save(pPlayer);
			};
			public void handleAdd(Collection<Tournament> pPlayerCollection) {
				super.handleAdd(pPlayerCollection);
				pdao.save(pPlayerCollection);
			};
			
		};
		//Activator.getDefault().getActivationContext().getServiceUtil().registerService(DaoController.class, mController);
		
		//Registrierung von sich selbst, als StructuredViewer und PageController
		AdapterBinding binding = new AdapterBinding(this);
		binding.add(tableViewer, StructuredViewer.class);
		binding.add(mController, DaoController.class, IHandlerDelete.class, IHandlerAdd.class, IHandlerEditor.class);
		binding.add(mController.getPageController(), PageController.class);
		binding.bind();
	}

}
