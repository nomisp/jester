package ch.jester.ui.round.form;

import java.awt.DisplayMode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.internal.EditorStack;
import org.eclipse.ui.internal.PartPane;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.util.PlayerColor;
import ch.jester.model.util.Result;
import ch.jester.model.util.Result.ResultCombination;
import ch.jester.model.Round;
import ch.jester.ui.round.editors.ResultController;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;
import ch.jester.ui.tournament.internal.Activator;

@SuppressWarnings("restriction")
public class RoundForm extends FormPage implements IZoomableWorkbenchPart, ISelectionProvider{
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
	private LayoutAlgorithm mUsedLayout;
	private IManagedForm mManagedForm;
	private Listener mResizeListener;
	private PaintListener mPaintListener;
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
		 */
		Rectangle clientArea = getEditorSite().getShell().getClientArea();

		IEditorSite site = getEditorSite();
		PartSite psite = (PartSite) site.getPart().getSite();
		PartPane ppane = psite.getPane();
		EditorStack estack = (EditorStack) ppane.getContainer();
		clientArea = estack.getEditorArea().getBounds();
	
		//clientArea = mManagedForm.getForm().getShell().getClientArea();
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
		int mod = (int) ((1.02f)*pairingCnt);
		settings.height = settings.height + mod;
		//EditorScale (ungefähr)
		settings.editorScale=1.0f;
		settings.width=clientArea.width-clientArea.x;
		return settings;
		
	}

	private CaluculatedCompositeSettings getSizeSettings(){
		//zur Zeit wird nur dieses verwendet... für andere Layouts Settings entsprechend aufbereiten
		if(mUsedLayout instanceof HorizontalTreeLayoutAlgorithm){
			return calculateCompositeSizeForHorizontalTreeLayout();
		}
		return new CaluculatedCompositeSettings();
	}

	private void doViewerSizing(CaluculatedCompositeSettings settings){		
		GridData data = new GridData();
		mViewer.getGraphControl().setLocation(0, 0);
		mViewer.getGraphControl().setPreferredSize((int) (settings.width), settings.height);
		data.grabExcessHorizontalSpace=true;
		data.grabExcessVerticalSpace=true;
		data.verticalAlignment=SWT.FILL;
		data.horizontalAlignment=SWT.FILL;
		data.minimumWidth=mViewer.getGraphControl().getSize().x;
		data.minimumHeight=mViewer.getGraphControl().getSize().y;
		mViewer.getGraphControl().setLayoutData(data);
		mViewer.getGraphControl().redraw();
		mViewer.getGraphControl().getParent().getParent().redraw();
		
		mManagedForm.reflow(true);
		mManagedForm.refresh();
	}
	
	private CaluculatedCompositeSettings initViewer(Composite parent){
		mViewer = new GraphViewer(parent, ZestStyles.NONE|SWT.NONE);
		mViewer.getGraphControl().setLayout(new GridLayout(1, true));

		
		mViewer.setContentProvider(new ZestNodeContentProvider());
		mViewer.setLabelProvider(new ZestLabelProvider());
		
		mViewer.setInput(mModelContentProvider.getAllNodes());
		LayoutAlgorithm layout = setLayout();
		mViewer.setLayoutAlgorithm(layout, true);
		mUsedLayout = layout;
		return getSizeSettings();
		
		
	}

	
	protected void createFormContent(IManagedForm managedForm) {

		mManagedForm = managedForm;
		mManagedForm.getForm().setBusy(true);
		setDecoratedTitle(managedForm);

		ScrolledForm form = managedForm.getForm();
		
		
		Composite body = form.getBody();
		GridLayout layout;
		body.setLayout(layout = new GridLayout(1, true));
		//srollers direkt neben form, ohne gap
		layout.marginHeight=0;
		layout.marginWidth=0;
		CaluculatedCompositeSettings settings = initViewer(body);
		doViewerSizing(settings);
		super.getEditorSite().getShell().addPaintListener(mPaintListener = new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				CaluculatedCompositeSettings settings = getSizeSettings();
				doViewerSizing(settings);
				mViewer.applyLayout();
				mViewer.refresh();
			}
			
		});
		super.getEditorSite().getShell().addListener(SWT.Resize|SWT.Move, mResizeListener = new Listener(){

			@Override
			public void handleEvent(Event event) {
				mViewer.refresh();
			}
			
		});
		
		mViewer.applyLayout();
		mViewer.getGraphControl().addMenuDetectListener(new MenuDetectListener() {
			
			@Override
			public void menuDetected(MenuDetectEvent e) {
				mLogger.debug("MenuDetectEvent: "+e.widget);
				if(e.widget instanceof Graph){
					Graph graph = (Graph) e.widget;
					@SuppressWarnings("unchecked")
					List<GraphNode> glist = graph.getSelection();
					if(glist.isEmpty()||glist.size()>1){return;}
					GraphNode selected = glist.get(0);
					Object data = selected.getData();
					
					if(data instanceof ZestDataNode){
						ZestDataNode node = (ZestDataNode) data;
						Pairing pairing = null;
						Object source = null;
						if(node.getData() instanceof Pairing){
							pairing = (Pairing) node.getData();
							source = pairing;
						}
						if(node.getData() instanceof Player){
							pairing = ((PlayerDataNode)node).getPairing();
							source = node;
						}
						if(pairing!=null){
							if(graph.getMenu()!=null){
								graph.getMenu().dispose();
							}
							graph.setMenu(getMenu(pairing, source));
						}else{
							graph.setMenu(null);
						}
					}
					
					
				}

				
			}
		});
		
		
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
		mManagedForm.getForm().setBusy(false);
	}
	
	private void setDecoratedTitle(IManagedForm managedForm) {
		managedForm.getForm().setText(mTitle);
		managedForm.getToolkit().decorateFormHeading(managedForm.getForm().getForm());
		
	}

	private Menu getMenu(Pairing pairing, Object source){
		PlayerColor playerColor = null;
		ResultCombination[] combination = null;
		boolean isBlackNullPLayer = pairing.getBlack().getPlayer()==null;
		boolean isWhiteNullPLayer = pairing.getWhite().getPlayer()==null;
		boolean isOpponentNullPlayer = isBlackNullPLayer|| isWhiteNullPLayer;
		if(pairing == source){
			combination = Result.toResultCombinationViewForPairing();
		}else{
			if(source instanceof PlayerDataNode){
				combination = Result.toResultCombinationViewForPlayer();
				if( ((PlayerDataNode)source).isBlack()){
					playerColor = PlayerColor.BLACK;
				}
				else{
					playerColor = PlayerColor.WHITE;
				}
			}
			
		}
	
		Menu menu = new Menu(Display.getCurrent().getActiveShell(), SWT.CASCADE);
		Result currentResult = mController.getLastPairingResult(pairing);
		for(ResultCombination c:combination){
			MenuItem item = new MenuItem(menu, SWT.CHECK);
			installMenuSelectionListener(item);
			item.setData(new Object[]{pairing, c, playerColor});
			item.setText(c.toString());
			if(source instanceof PlayerDataNode){
				if(isOpponentNullPlayer){
					if(c.getResult().getShortResult().startsWith("0")||c.getResult().getShortResult().equalsIgnoreCase("x")){
						item.setEnabled(false);
					}
				}
			}else{
				if(isOpponentNullPlayer){
					
					if(isBlackNullPLayer){
						if(c.toString().startsWith("0")||c.toString().equalsIgnoreCase("x")){
							item.setEnabled(false);
						}
					}else{
						if(c.toString().startsWith("1")||c.toString().equalsIgnoreCase("x")){
							item.setEnabled(false);
						}
					}
				}
			}
			
			if(currentResult!=null){
				if(playerColor==null || playerColor == PlayerColor.WHITE){
					if(currentResult.getShortResult().equals(c.getResult().getShortResult())){
						item.setSelection(true);
					}
				
				}else if(playerColor == PlayerColor.BLACK){
					if(currentResult.getOpposite().getShortResult().equals(c.getResult().getShortResult())){
						item.setSelection(true);
					}
				
				}
			}
		}
	/*	Menu menu = new Menu(Display.getCurrent().getActiveShell(), SWT.CASCADE);
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
		}*/
		return menu;
	}

	
	private void installMenuSelectionListener(MenuItem item) {
		item.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object[] data = (Object[]) e.widget.getData();
				Pairing p = (Pairing) data[0];
				ResultCombination comb = (ResultCombination) data[1];
				Result r = comb.getResult();
				PlayerColor color = (PlayerColor) data[2];
				if(color != null){
					mController.addChangedResults(p, r, color);
				}else{
					mController.addChangedResults(p, r, p);
				}
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
		mTitle = mController.getTitlePath();
		
	}
	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return mViewer;
	}
	
	@Override
	public void dispose() {
		mController.removePropertyChangeListener(mUpdater);
		getEditorSite().getShell().removeListener(SWT.Resize|SWT.Move, mResizeListener);
		getEditorSite().getShell().removePaintListener(mPaintListener);
		super.dispose();
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		mViewer.addSelectionChangedListener(listener);
		
	}

	@Override
	public ISelection getSelection() {
		return mViewer.getSelection();
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		mViewer.removeSelectionChangedListener(listener);
		
	}

	@Override
	public void setSelection(ISelection selection) {
		mViewer.setSelection(selection);
		
	}

}
