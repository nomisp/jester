package ch.jester.ui.round.form;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.model.Pairing;
import ch.jester.model.Result;
import ch.jester.model.Round;
import ch.jester.ui.round.editors.ResultController;

public class ResultForm extends FormPage implements IDirtyManagerProvider{
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
				System.out.println(mViewer+" updating");
				setSelection();
				mViewer.refresh();
			}
			
		}
		public void setSelection(){
			String resultString = null;
			Result result = null;
			Result changedResult = mController.getChangedResults().get(mPairing);
			if(changedResult!=null){
				resultString = changedResult.getShortResult();
			}else{
				resultString = mPairing.getResult();
			}
			
			if(resultString!=null){	
				result = Result.findByShortResult(resultString);
			}
			if(result!=null){
				Result selected = (Result) ((IStructuredSelection)mViewer.getSelection()).getFirstElement();
				if(selected!=result){
					mViewer.setSelection(new StructuredSelection(result));
				}
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
		form.setText("Pairing Results");
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		managedForm.getToolkit().decorateFormHeading(form.getForm());
		buildSections(managedForm);
		mController.getDirtyManager().reset();
	}

	private void buildSections(IManagedForm managedForm) {
		List<Round> rounds = mController.getRounds();
		
		for(Round r:rounds){
			Composite c = createSection(managedForm, "Round: "+r.getNumber());
			for(Pairing p:r.getPairings()){
				createFileds(managedForm, c, r, p);
			}
			
		}
		
	}
	private void createFileds(IManagedForm managedForm, Composite c, Round r, Pairing p) {
		String black = p.getBlack().getLastName()+", "+p.getBlack().getFirstName();
		String white = p.getWhite().getLastName()+", "+p.getWhite().getFirstName();
		Result result = null;
		/*if(p.getResult()!=null){	
			result = Result.findByShortResult(p.getResult());
		}*/
		String text = "Black: "+black+" vs White: "+white;
		
		managedForm.getToolkit().createLabel(c, text, SWT.NONE);
		ComboViewer viewer = new ComboViewer(c, SWT.READ_ONLY);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.addSelectionChangedListener(new PairingResultChanged(p));
		viewer.setInput(Result.values());
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		SelectionSetter setter = new SelectionSetter(viewer, p);
		setter.setSelection();
		mSelectionSetters.add(setter);
		mController.addPropertyChangeListener("changedResults",setter);
		
		/*if(result!=null){
			viewer.setSelection(new StructuredSelection(result));
		}*/
		mController.getSWTDirtyManager().add(viewer.getControl());
		
	}
	public void dispose(){
		super.dispose();
		for(SelectionSetter setter:mSelectionSetters){
			mController.removePropertyChangeListener("changedResults",setter);
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
			Result res = (Result)sts.getFirstElement();
			mController.addChangedResults(pairing, res);
		}
	}
}
