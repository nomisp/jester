package ch.jester.ui.tournament.forms;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.model.Tournament;
import ch.jester.system.api.pairing.ITournamentEditorConstraintsProvider;
import ch.jester.system.api.pairing.ui.TournamentEditorConstraints;
import ch.jester.ui.tournament.editors.TournamentEditor;

/**
 * FormPage für die Kategorien/Runden beinhaltet einen MasterDetail
 *
 */
public class CategoryFormPage extends FormPage implements ITournamentEditorConstraintsProvider {

	private CategoryMasterDetail block;
	private TournamentEditor tournamentEditor;
	public CategoryFormPage(TournamentEditor editor) {
		this(editor, "CategoryPage", Messages.CategoryFormPage_title); //$NON-NLS-1$
		tournamentEditor=editor;
		block = new CategoryMasterDetail(this);
		
	}
	
	public boolean isValid(){
		return block.isValid();
	}

	@Override
	public TournamentEditorConstraints getTournamentEditorConstraints() {
		return tournamentEditor.getTournamentEditorConstraints();
	}
	
	public void setTournament(Tournament t){
		block.setTournament(t);
	}
	
	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public CategoryFormPage(String id, String title) {
		super(id, title);
	}

	/**
	 * Create the form page.
	 * @param editor
	 * @param id
	 * @param title
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter id "Some id"
	 * @wbp.eval.method.parameter title "Some title"
	 */
	public CategoryFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(Messages.CategoryFormPage_title);
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		block.createContent(managedForm);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		block.save();
	}
	
	/**
	 * Liefert den DirtyManager der CategoryDetailsPage
	 * @return
	 */
	public SWTDirtyManager getCategoryDetailsDirtyManager() {
		return block.getCategoryDetailsPage().getDirtyManager();
	}


}
