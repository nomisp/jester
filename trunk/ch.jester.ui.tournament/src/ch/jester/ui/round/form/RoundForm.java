package ch.jester.ui.round.form;

import java.awt.DisplayMode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
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
	class CaluculatedCompositeSettings{
		int height;
		int width;
		float editorScale;
	}
	private GraphViewer mViewer;
	private RoundNodeModelContentProvider mModelContentProvider ;/*= new RoundNodeModelContentProvider();*/
	private String mTitle;
	private ResultController mController;
	private PropertyChangeListener mUpdater;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	public RoundForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	private void createZoomControl(IManagedForm managedForm){
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		managedForm.getForm().getToolBarManager().add(toolbarZoomContributionViewItem);
		managedForm.getForm().getToolBarManager().update(true);
		
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
	
	
	private CaluculatedCompositeSettings calculateCompositeSizeForHorizontalTreeLayout(){
		/* Hack... damit die Scrollbalken schöne erscheinen und der Graph nicht
		 * selbst Scroller zeichnet.
		 * Wir machen einfach das parent composite so gross, dass der Graphviewer sicher
		 * genügend Platz hat (keine scroller zeichnet) und das managedform die scroller zeichnen kann.
		 * 
		 * 							Player A
		 * 				Pairing
		 *  round 1					Player B
		 * 							player C
		 * 				Pairing
		 * 							Player D
		 * 
		 * 
		 * 
		 * 
		 */
	//	int roundCnt = mController.getRounds().size();
		int pairingCnt = 0;
		for(Round r:mController.getRounds()){
			pairingCnt+=r.getPairings().size();
		}
		int playerCnt = pairingCnt*2;
		
		//ungefähre höhe eines nodes (Sicherstellung gap für die einzelnen Spieler)
		int nodeHeight = 30;
		DisplayMode displayMode = UIUtility.getLargestDisplay();
		CaluculatedCompositeSettings settings = new CaluculatedCompositeSettings();
		settings.height = playerCnt*nodeHeight;
		if(settings.height<displayMode.getHeight()){
			settings.height=displayMode.getHeight();
		}
		//EditorScale (ungefähr)
		settings.width = (int) (displayMode.getWidth());
		settings.editorScale=0.7f;
		return settings;
		
	}
	

	
	private CaluculatedCompositeSettings initViewer(Composite parent){
		mViewer = new GraphViewer(parent, ZestStyles.NONE|SWT.NONE);
		
		mViewer.setContentProvider(new ZestNodeContentProvider());
		mViewer.setLabelProvider(new ZestLabelProvider());
		
		mViewer.setInput(mModelContentProvider.getAllNodes());
		LayoutAlgorithm layout = setLayout();
		mViewer.setLayoutAlgorithm(layout, true);
		
		//zur Zeit wird nur dieses Verwendet... für andere Layouts Settings entsprechend aufbereiten
		if(layout instanceof HorizontalTreeLayoutAlgorithm){
			return calculateCompositeSizeForHorizontalTreeLayout();
		}
		return new CaluculatedCompositeSettings();
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		setDecoratedTitle(managedForm);

		ScrolledForm form = managedForm.getForm();
		
		Composite body = form.getBody();

		CaluculatedCompositeSettings settings = initViewer(body);
		
		mViewer.getGraphControl().setLocation(0, 0);
		mViewer.getGraphControl().setPreferredSize((int) (settings.width*settings.editorScale), settings.height);
		Point p =mViewer.getGraphControl().computeSize((int)(settings.width*settings.editorScale)+200, settings.height);
		mViewer.getGraphControl().setSize(p);
		
		body.pack(true);
		
		managedForm.reflow(true);	
		
		mViewer.applyLayout();
		mViewer.getGraphControl().addMenuDetectListener(new MenuDetectListener() {
			
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
		
		createZoomControl(managedForm);
		
		//Nodes können nicht bewegt werden... sonst sieht man unter umständen den hack... wollen wir nicht.
		//ist auch nicht nötig
		mViewer.getGraphControl().getLightweightSystem().setEventDispatcher(
				new SWTEventDispatcher() {
					public void dispatchMouseMoved(
							org.eclipse.swt.events.MouseEvent me) {
						// Doing nothing
					}
				});
		mController.getDirtyManager().reset();

	}
	
	private void setDecoratedTitle(IManagedForm managedForm) {
		managedForm.getForm().setText(mTitle);
		managedForm.getToolkit().decorateFormHeading(managedForm.getForm().getForm());
		
	}

	private Menu getMenu(Pairing pairing){
		Menu menu = new Menu(Display.getCurrent().getActiveShell(), SWT.CASCADE);
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
		HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);

		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;

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
		mController.removePropertyChangeListener(mUpdater);
		super.dispose();
	}

}
