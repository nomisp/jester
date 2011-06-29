package ch.jester.ui.round.form;

import java.util.ArrayList;
import java.util.HashSet;
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
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Result;
import ch.jester.model.Round;

public class ResultForm extends FormPage implements IDirtyManagerProvider{
	private IEntityObject mInput;
	private SWTDirtyManager mDirtyManager = new SWTDirtyManager();
	private List<PairingResult> changedPairings = new ArrayList<PairingResult>();
	public ResultForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		form.setText("Pairing Results");
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		managedForm.getToolkit().decorateFormHeading(form.getForm());
		buildSections(managedForm);
	}

	private void buildSections(IManagedForm managedForm) {
		List<Round> rounds = getRounds();
		
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
		if(p.getResult()!=null){	
			result = Result.findByShortResult(p.getResult());
		}
		String text = "Black: "+black+" vs White: "+white;
		
		managedForm.getToolkit().createLabel(c, text, SWT.NONE);
		ComboViewer viewer = new ComboViewer(c, SWT.READ_ONLY);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.addSelectionChangedListener(new PairingResultChanged(p));
		viewer.setInput(Result.values());
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(result!=null){
			viewer.setSelection(new StructuredSelection(result));
		}
		mDirtyManager.add(viewer.getControl());
		
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
	public void setInput(IEntityObject input) {
		mInput = input;
		
	}
	public List<PairingResult> getChangedPairings(){
		return changedPairings;
	}
	
	private List<Round> getRounds(){
		if(mInput instanceof Category){
			Category cat = (Category) mInput;
			return cat.getRounds();
		}
		if(mInput instanceof Round){
			List<Round> list = new ArrayList<Round>();
			list.add((Round) mInput);
			return list;
		}
		return null;
	}
	@Override
	public DirtyManager getDirtyManager() {
		return mDirtyManager;
	}
	public class PairingResult{
		public Pairing pairing;
		public Result result;
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
			PairingResult result = new PairingResult();
			result.pairing=pairing;
			result.result=res;
			changedPairings.add(result);
		}
	}
}
