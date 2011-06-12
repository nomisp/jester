package ch.jester.ui.tournament.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.model.Tournament;
import ch.jester.ui.tournament.ctrl.TournamentDetailsController;
import ch.jester.ui.tournament.forms.CategoryFormPage;
import ch.jester.ui.tournament.forms.TournamentFormPage;

/**
 * Turnier-Editor
 * @author Peter
 *
 */
public class TournamentEditor extends AbstractEditor<Tournament> {
	
	public static final String ID = "ch.jester.ui.tournament.tournamentEditor";
	private TournamentFormPage mTournamentPage;
	private TournamentDetailsController mTournamentController;
	
	public TournamentEditor() {
		super(true);
//		mLogger.debug("Tournament Editor: " + mDaoInput.getInput().getName());
	}
	
//	protected FormToolkit createToolkit(Display display) {
//		return new FormToolkit(ExamplePlugin.getDefault().getFormColors(display));
//	}

	@Override
	protected void addPages() {
		TournamentFormPage tournamentPage = new TournamentFormPage(this, "TournamentFormPage", "TournamentEditor");
		tournamentPage.addPartPropertyListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				init_0(null);
				
			}
		});
		CategoryFormPage categoryPage = new CategoryFormPage(this);
		try {
			addPage(tournamentPage);
			addPage(categoryPage);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}
	
	public void init_0(Object parent) {
		//Composite container = new Composite(parent, SWT.NONE);
		
		//mPlayerDetails = new PlayerDetailsView(container, SWT.NONE);
		//mPlayerDetails.setBounds(0, 0, 365, 300);
		mTournamentPage = (TournamentFormPage) parent;
		mTournamentController = mTournamentPage.getController();			
		mTournamentController.setTournament(mDaoInput.getInput());	
		mTournamentController.getDirtyManager().setDirty(mDaoInput.isAlreadyDirty());
		
		
		setDirtyManager(mTournamentController.getDirtyManager());
		getDirtyManager().addDirtyListener(this);
		mTournamentController.getDirtyManager().addDirtyListener(new IDirtyListener() {
			@Override
			public void propertyIsDirty() {
				setPartName(mTournamentPage.getNameText().getText());
				
			}
		});
		setPartName(mTournamentPage.getNameText().getText()+", "+mTournamentPage.getDescriptionText().getText());
	
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void editorClosed() {
		if (!wasSaved()) {
			mTournamentController.getTournament();
		}
		mTournamentController.updateUI();
	}
}
