package ch.jester.ui.tournament;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.ui.tournament.internal.Activator;

public class NewTournamentWizard extends Wizard implements INewWizard {
	
	private ILogger mLogger;
	
	public NewTournamentWizard() {
		mLogger = Activator.getDefault().getActivationContext().getLogger();
		mLogger.info("New tournament wizard started");
	}
	
	@Override
	public void addPages() {
		mLogger.info("New tournament wizard started: adding pages");
		super.addPages();
		addPage(new NewTournamentWizPageName());
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New Tournament");
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
