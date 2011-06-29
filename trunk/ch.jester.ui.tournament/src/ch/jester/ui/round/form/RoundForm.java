package ch.jester.ui.round.form;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;

public class RoundForm extends FormPage{
	private int layout = 1;
	private GraphViewer viewer;
	private RoundNodeModelContentProvider modelContentProvider = new RoundNodeModelContentProvider();
	private String title;

	public RoundForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
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
		
		
		
		managedForm.getForm().setText(title);
		managedForm.getToolkit().decorateFormHeading(managedForm.getForm().getForm());
		
		viewer = new GraphViewer(compPersonal, SWT.NONE);
		Control control = viewer.getControl();
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		new Label(compPersonal, SWT.NONE);

		
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider());
		
		viewer.setInput(modelContentProvider.getAllNodes());
		LayoutAlgorithm layout = setLayout();
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();
		
		//fillToolBar();

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
		modelContentProvider = contentProvider;
		Object input = modelContentProvider.getInput();
		if(input instanceof Category){
			title = ((Category)input).getDescription();
		}else if(input instanceof Round){
			title = "Round "+((Round)input).getNumber()+"";
		}
		
	}

}
