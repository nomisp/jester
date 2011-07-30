package ch.jester.ui.round.form;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import messages.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.model.Pairing;
import ch.jester.model.Round;
import ch.jester.model.factories.PlayerNamingUtility;
import ch.jester.model.util.Result;
import ch.jester.model.util.Result.ResultCombination;
import ch.jester.ui.round.editors.ResultController;
import ch.jester.ui.tournament.internal.Activator;

public class ResultForm extends FormPage implements IDirtyManagerProvider{
	private List<Section> mSectionList = new ArrayList<Section>();
	private StringBuilder mLabelBuilder = new StringBuilder();
	class SelectionSetter implements PropertyChangeListener{
		ComboViewer mViewer;
		Pairing mPairing;
		public SelectionSetter(ComboViewer viewer, Pairing p){
			mViewer = viewer;
			mPairing = p;
		}
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			if(arg0.getSource()==mPairing){
				System.out.println(mViewer+" updating"); //$NON-NLS-1$
				setSelection();
				mViewer.refresh();
			}
			
		}
		public void setSelection(){
			Result result = mController.getLastPairingResult(mPairing);
			if(result!=null){
				ResultCombination selected = (ResultCombination) ((IStructuredSelection)mViewer.getSelection()).getFirstElement();
				if(selected==null || selected.getResult()!=result){
					mViewer.setSelection(new StructuredSelection(result.toResultCombinationForPairing()));
				}
			}else{
				Result rr = Result.findByShortResult(mPairing.getResult());
				if(rr==null){return;}
				mViewer.setSelection(new StructuredSelection(rr.toResultCombinationForPairing()));
			}
		}
		
	}
	private List<SelectionSetter> mSelectionSetters = new ArrayList<SelectionSetter>();
	private ResultController mController;
	public ResultForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	public void setResultController(ResultController pController){
		mController=pController;
	}

	
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		form.setText(Messages.ResultForm_1+mController.getTitlePath());
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		managedForm.getToolkit().decorateFormHeading(form.getForm());
		buildSections(managedForm);
		managedForm.reflow(true);
		mController.getDirtyManager().reset();
		createToolBarActions(managedForm);
		
	}
	
	private final void expandSections(final ScrolledForm form, final boolean b){
		UIUtility.busyIndicatorJob("", new UIUtility.IBusyRunnable() { //$NON-NLS-1$
			
			@Override
			public void stepOne_InUIThread() {
				form.redraw();
				form.setRedraw(false);
				
			}
			
			@Override
			public void stepTwo_InJob() {
				UIUtility.syncExecInUIThread(new Runnable() {
					
					@Override
					public void run() {
						for(Section s:mSectionList){
							s.setExpanded(b);
						}
					}
				});

			}
			

			
			@Override
			public void finalStep_inUIThread() {
				form.setRedraw(true);
				form.redraw();
				
			}
		});
	}

	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		Action expand = new Action("CollapseSections", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$
			public void run() {
				expandSections(form, true);
			}
		};
		expand.setText(Messages.ResultForm_3);
		expand.setImageDescriptor(Activator.imageDescriptor("/icons/expand_all.gif")); //$NON-NLS-1$
		Action collapse = new Action("ExpandSections", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$
			public void run() {
				expandSections(form, false);
			}
		};
		collapse.setText(Messages.ResultForm_5);
		collapse.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL));
		//haction.setChecked(true);
		
		//haction.setToolTipText(Messages.CategoryMasterDetail_tt_horizontal);
	//	haction.setImageDescriptor(UIUtility.getImageDescriptor(Activator.getDefault().getActivationContext().getPluginId(),
	//				"icons/application_tile_horizontal.png")); //$NON-NLS-1$
		managedForm.getForm().getToolBarManager().add(collapse);
		managedForm.getForm().getToolBarManager().add(expand);
		
		
		managedForm.getForm().getToolBarManager().update(true);

	}
	private void buildSections(IManagedForm managedForm) {
		List<Round> rounds = mController.getRounds();
		
		for(Round r:rounds){
			Composite c = createSection(managedForm, Messages.ResultForm_6+r.getNumber());
			for(Pairing p:r.getPairings()){
				createFileds(managedForm, c, r, p);
			}
			
		}
		
	}
	private void createFileds(IManagedForm managedForm, Composite c, Round r, Pairing p) 
	{
		mLabelBuilder.delete(0, mLabelBuilder.length());
		mLabelBuilder.append(PlayerNamingUtility.createPairingName(p, Messages.ResultForm_7));
		boolean disableResultCombo = p.getBlack().getPlayer()==null||p.getWhite().getPlayer()==null;
		managedForm.getToolkit().createLabel(c, mLabelBuilder.toString(), SWT.NONE);
		ComboViewer viewer = new ComboViewer(c, SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.addSelectionChangedListener(new PairingResultChanged(p));
		viewer.setInput(Result.toResultCombinationViewForPairing());
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		viewer.getControl().setEnabled(!disableResultCombo);
		SelectionSetter setter = new SelectionSetter(viewer, p);
		setter.setSelection();
		mSelectionSetters.add(setter);
		mController.addPropertyChangeListener("changedResults",setter); //$NON-NLS-1$
		
		mController.getSWTDirtyManager().add(viewer.getControl());
		//Result rsaved = mController.getChangedResults().get(p);
		
	}
	public void dispose(){
		super.dispose();
		mSectionList.clear();
		for(SelectionSetter setter:mSelectionSetters){
			mController.removePropertyChangeListener("changedResults",setter); //$NON-NLS-1$
		}
	}
	private Composite createSection(IManagedForm managedForm, String title){
		Composite compPersonal = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		compPersonal.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		managedForm.getToolkit().paintBordersFor(compPersonal);
		compPersonal.setLayout(new GridLayout(2, false));
		
		Section sctnPersonal = managedForm.getToolkit().createSection(compPersonal, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnPersonal = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		gd_sctnPersonal.widthHint = 561;
		sctnPersonal.setLayoutData(gd_sctnPersonal);
		managedForm.getToolkit().paintBordersFor(sctnPersonal);
		sctnPersonal.setText(title);
		
		Composite composite_2 = managedForm.getToolkit().createComposite(sctnPersonal, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite_2);
		sctnPersonal.setClient(composite_2);
		composite_2.setLayout(new GridLayout(2, false));
		
		mSectionList.add(sctnPersonal);
		return composite_2;
	}
	
	@Override
	public DirtyManager getDirtyManager() {
		return mController.getDirtyManager();
	}

	
	class PairingResultChanged implements ISelectionChangedListener{

		Pairing pairing;
		public PairingResultChanged(Pairing p){
			pairing = p;
		}
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection sts = (IStructuredSelection) event.getSelection();
			ResultCombination res = (ResultCombination)sts.getFirstElement();
			mController.addChangedResults(pairing, res.getResult(), pairing);
		}
	}
}
