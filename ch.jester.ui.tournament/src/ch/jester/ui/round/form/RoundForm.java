package ch.jester.ui.round.form;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Result;
import ch.jester.model.Round;
import ch.jester.ui.round.editors.ResultController;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;
import ch.jester.ui.tournament.internal.Activator;

public class RoundForm extends FormPage implements IZoomableWorkbenchPart{
	private GraphViewer mViewer;
	private RoundNodeModelContentProvider mModelContentProvider ;/*= new RoundNodeModelContentProvider();*/
	private String mTitle;
	private ResultController mController;
	private PropertyChangeListener mUpdater;
	private MenuDetectListener mMdl;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	public RoundForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	public void setResultController(ResultController pController){
		mController=pController;
		installListener();
	}
	private void installListener(){
		mController.addPropertyChangeListener(mUpdater = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				UIUtility.syncExecInUIThread(new Runnable() {
					
					@Override
					public void run() {
						mViewer.refresh();
						
					}
				});
			}
		});
	}
	
	
	protected void createFormContent(IManagedForm managedForm) {
		
		managedForm.getForm().getBody().setLayout(new GridLayout());
		
		Composite compPersonal = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		GridData gd_compPersonal = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		//gd_compPersonal.heightHint = 800;
		//gd_compPersonal.widthHint = 800;
		gd_compPersonal.grabExcessHorizontalSpace=true;
		gd_compPersonal.grabExcessVerticalSpace=true;
		compPersonal.setLayoutData(gd_compPersonal);
		managedForm.getToolkit().paintBordersFor(compPersonal);
		compPersonal.setLayout(new GridLayout(2, true));
		
		
		
		managedForm.getForm().setText(mTitle);
		managedForm.getToolkit().decorateFormHeading(managedForm.getForm().getForm());
		
		mViewer = new GraphViewer(compPersonal, SWT.NONE);
		Control control = mViewer.getControl();
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		new Label(compPersonal, SWT.NONE);

		
		mViewer.setContentProvider(new ZestNodeContentProvider());
		mViewer.setLabelProvider(new ZestLabelProvider());
		
		mViewer.setInput(mModelContentProvider.getAllNodes());
		LayoutAlgorithm layout = setLayout();
		mViewer.setLayoutAlgorithm(layout, true);
		mViewer.applyLayout();
		
		
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		managedForm.getForm().getToolBarManager().add(toolbarZoomContributionViewItem);
		managedForm.getForm().getToolBarManager().update(true);
		
		/*MenuManager menuMgr = new MenuManager() ;
		final Menu menu = menuMgr.createContextMenu( viewer.getGraphControl());
		viewer.getGraphControl().setMenu( menu );
		this.getSite().registerContextMenu( menuMgr, viewer );*/
		
		mViewer.getGraphControl().addMenuDetectListener(mMdl = new MenuDetectListener() {
			
			@Override
			public void menuDetected(MenuDetectEvent e) {
				mLogger.debug("MenuDetectEvent: "+e.widget);
				if(e.widget instanceof Graph){
					Graph graph = (Graph) e.widget;
					List<GraphNode> glist = graph.getSelection();
					if(glist.isEmpty()||glist.size()>1){return;}
					GraphNode selected = glist.get(0);
					Object data = selected.getData();
					
					if(data instanceof ZestDataNode){
						ZestDataNode node = (ZestDataNode) data;
						Pairing pairing = null;
						if(node.getData() instanceof Pairing){
							pairing = (Pairing) node.getData();
						}
						if(node.getData() instanceof Player){
							pairing = ((PlayerDataNode)node).getPairing();
						}
						if(pairing!=null){
							if(graph.getMenu()!=null){
								graph.getMenu().dispose();
							}
							graph.setMenu(getMenu(pairing));
						}else{
							graph.setMenu(null);
						}
					}
					
					
				}

				
			}
		});
		
		mController.getDirtyManager().reset();
		//item.setMenu(menu);

	}
	
	private Menu getMenu(Pairing pairing){
		Menu menu = new Menu(Display.getCurrent().getActiveShell(), SWT.CASCADE);
	/*	Result currentResult = mController.getChangedResults().get(pairing);
		if(currentResult == null && pairing.getResult()!=null){
			currentResult = Result.findByShortResult(pairing.getResult());
		}*/
		Result currentResult = mController.getLastPairingResult(pairing);
		for(Result r:Result.values()){
			MenuItem item = new MenuItem(menu, SWT.CHECK);
			installMenuSelectionListener(item);
			item.setData(new Object[]{pairing, r});
			item.setText(r.getShortResult());
			if(currentResult!=null){
				if(currentResult.getShortResult().equals(r.getShortResult())){
					item.setSelection(true);
				}
			}
		}
		return menu;
	}

	
	private void installMenuSelectionListener(MenuItem item) {
		item.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object[] data = (Object[]) e.widget.getData();
				Pairing p = (Pairing) data[0];
				Result r = (Result) data[1];
				mController.addChangedResults(p, r);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
			
		});
		
	}
	private LayoutAlgorithm setLayout() {
		LayoutAlgorithm layout;
		// layout = new
		// SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		//layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		 //layout = new
		 //GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		layout = new
		HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING );

		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;

	}
	/*private void fillToolBar() {
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(
				this);
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);

	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}*/



	public void setLayoutManager() {
	

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
	public void setContentProvider(RoundNodeModelContentProvider contentProvider) {
		mModelContentProvider = contentProvider;
		Object input = mModelContentProvider.getInput();
		if(input instanceof Category){
			mTitle = ((Category)input).getDescription();
		}else if(input instanceof Round){
			mTitle = "Round "+((Round)input).getNumber()+"";
		}
		
	}
	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return mViewer;
	}
	
	@Override
	public void dispose() {

	//	viewer.getGraphControl().removeMenuDetectListener(mdl);
		mController.removePropertyChangeListener(mUpdater);
		super.dispose();
	}

}
