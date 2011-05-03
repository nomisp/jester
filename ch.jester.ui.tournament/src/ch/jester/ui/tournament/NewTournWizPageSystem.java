package ch.jester.ui.tournament;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Combo;

public class NewTournWizPageSystem extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public NewTournWizPageSystem() {
		super("wizardPage");
		setTitle("New Tournament System");
		setMessage("Defining the tournament system");
		setDescription("Defining the tournament system");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FormLayout());
		
		Label lblPairingSystem = new Label(container, SWT.NONE);
		FormData fd_lblPairingSystem = new FormData();
		fd_lblPairingSystem.top = new FormAttachment(0, 28);
		fd_lblPairingSystem.left = new FormAttachment(0, 20);
		lblPairingSystem.setLayoutData(fd_lblPairingSystem);
		lblPairingSystem.setText("Pairing System");
		
		Combo comboPairingSystem = new Combo(container, SWT.NONE);
		FormData fd_comboPairingSystem = new FormData();
		fd_comboPairingSystem.top = new FormAttachment(lblPairingSystem, -3, SWT.TOP);
		fd_comboPairingSystem.left = new FormAttachment(lblPairingSystem, 139);
		comboPairingSystem.setLayoutData(fd_comboPairingSystem);
		
		Label sepPairingRanking = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_sepPairingRanking = new FormData();
		fd_sepPairingRanking.top = new FormAttachment(comboPairingSystem, 21);
		fd_sepPairingRanking.left = new FormAttachment(0, 10);
		fd_sepPairingRanking.right = new FormAttachment(0, 572);
		sepPairingRanking.setLayoutData(fd_sepPairingRanking);
		
		Label lblRankingsystem = new Label(container, SWT.NONE);
		FormData fd_lblRankingsystem = new FormData();
		fd_lblRankingsystem.top = new FormAttachment(0, 113);
		fd_lblRankingsystem.left = new FormAttachment(lblPairingSystem, 0, SWT.LEFT);
		lblRankingsystem.setLayoutData(fd_lblRankingsystem);
		lblRankingsystem.setText("Ranking-System");
		
		Combo comboRankingSystem = new Combo(container, SWT.NONE);
		fd_sepPairingRanking.bottom = new FormAttachment(100, -174);
		fd_comboPairingSystem.right = new FormAttachment(comboRankingSystem, 0, SWT.RIGHT);
		FormData fd_comboRankingSystem = new FormData();
		fd_comboRankingSystem.left = new FormAttachment(lblRankingsystem, 129);
		fd_comboRankingSystem.right = new FormAttachment(100, -108);
		fd_comboRankingSystem.top = new FormAttachment(0, 110);
		comboRankingSystem.setLayoutData(fd_comboRankingSystem);
		
		Label sepRankingCalculator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_sepRankingCalculator = new FormData();
		fd_sepRankingCalculator.top = new FormAttachment(comboRankingSystem, 20);
		fd_sepRankingCalculator.right = new FormAttachment(sepPairingRanking, 0, SWT.RIGHT);
		fd_sepRankingCalculator.left = new FormAttachment(0, 10);
		sepRankingCalculator.setLayoutData(fd_sepRankingCalculator);
		
		Label lblEloperformanceCalculator = new Label(container, SWT.NONE);
		fd_sepRankingCalculator.bottom = new FormAttachment(lblEloperformanceCalculator, -10);
		FormData fd_lblEloperformanceCalculator = new FormData();
		fd_lblEloperformanceCalculator.top = new FormAttachment(0, 198);
		fd_lblEloperformanceCalculator.left = new FormAttachment(lblPairingSystem, 0, SWT.LEFT);
		lblEloperformanceCalculator.setLayoutData(fd_lblEloperformanceCalculator);
		lblEloperformanceCalculator.setText("Elo-/Performance Calculator");
		
		Combo comboEloCalculator = new Combo(container, SWT.NONE);
		FormData fd_comboEloCalculator = new FormData();
		fd_comboEloCalculator.right = new FormAttachment(comboPairingSystem, 0, SWT.RIGHT);
		fd_comboEloCalculator.top = new FormAttachment(sepRankingCalculator, 6);
		fd_comboEloCalculator.left = new FormAttachment(comboPairingSystem, 0, SWT.LEFT);
		comboEloCalculator.setLayoutData(fd_comboEloCalculator);
	}
}
