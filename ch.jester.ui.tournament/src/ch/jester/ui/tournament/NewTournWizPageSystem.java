package ch.jester.ui.tournament;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.system.api.calculator.IEloCalculatorEntry;
import ch.jester.system.api.calculator.IEloCalculatorManager;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;

public class NewTournWizPageSystem extends WizardPage {

	private ComboViewer comboPairingSystemViewer;
	private ComboViewer comboRankingSystemViewer;
	private ComboViewer comboEloCalculatorViewer;
	private ServiceUtility mService = new ServiceUtility();
	
	private IPairingAlgorithmEntry pairingAlgorithmEntry;
	private IRankingSystemEntry rankingSystemEntry;
	private IEloCalculatorEntry eloCalculatorEntry;

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
		
		comboPairingSystemViewer = new ComboViewer(container, SWT.NONE);
		Combo comboPairingSystem = comboPairingSystemViewer.getCombo();
		comboPairingSystemViewer.setContentProvider(new PairingSystemProvider());
		comboPairingSystemViewer.addSelectionChangedListener(new PairingSystemSelectionListener());
		comboPairingSystemViewer.setInput(1);
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
		
		comboRankingSystemViewer = new ComboViewer(container, SWT.NONE);
		Combo comboRankingSystem = comboRankingSystemViewer.getCombo();
		comboRankingSystemViewer.setContentProvider(new RankingSystemProvider());
		comboRankingSystemViewer.addSelectionChangedListener(new RankingSystemSelectionListener());
		comboRankingSystemViewer.setInput(1);
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
		
		comboEloCalculatorViewer = new ComboViewer(container, SWT.NONE);
		Combo comboEloCalculator = comboEloCalculatorViewer.getCombo();
		comboEloCalculatorViewer.setContentProvider(new EloCalculatorProvider());
		comboEloCalculatorViewer.addSelectionChangedListener(new EloCalculatorSelectionListener());
		comboEloCalculatorViewer.setInput(1);
		FormData fd_comboEloCalculator = new FormData();
		fd_comboEloCalculator.right = new FormAttachment(comboPairingSystem, 0, SWT.RIGHT);
		fd_comboEloCalculator.top = new FormAttachment(sepRankingCalculator, 6);
		fd_comboEloCalculator.left = new FormAttachment(comboPairingSystem, 0, SWT.LEFT);
		comboEloCalculator.setLayoutData(fd_comboEloCalculator);
	}
	
	private boolean validatePage() {
		if (eloCalculatorEntry != null && rankingSystemEntry != null && pairingAlgorithmEntry != null) {
			setPageComplete(true);	// Nachher kommen nur noch die Kategorien und die braucht es nicht zwingend (muss einfach eine Dummy-Kategorie angelegt werden)
			return true;
		}
//		getWizard().getContainer().updateButtons();
		return false;
	}
	
	private class PairingSystemProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	

		}

		@Override
		public Object[] getElements(Object inputElement) {
			IPairingManager manager = mService.getService(IPairingManager.class);
			if(manager==null||inputElement==null){
				return new Object[]{};
			}
			List<IPairingAlgorithmEntry> pairingSystems = null;
			pairingSystems = manager.getRegistredEntries();
			return pairingSystems.toArray();
		}
	}
	

	private class PairingSystemSelectionListener implements ISelectionChangedListener {
		SelectionUtility su = new SelectionUtility(null);

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			pairingAlgorithmEntry = su.getFirstSelectedAs(IPairingAlgorithmEntry.class);
			validatePage();
		}
	}
	
	private class RankingSystemProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {	

		}

		@Override
		public Object[] getElements(Object inputElement) {
			IRankingSystemManager manager = mService.getService(IRankingSystemManager.class);
			if(manager==null||inputElement==null){
				return new Object[]{};
			}
			List<IRankingSystemEntry> rankingSystems = null;
			rankingSystems = manager.getRegistredEntries();
			return rankingSystems.toArray();
		}
	}
	

	private class RankingSystemSelectionListener implements ISelectionChangedListener {
		SelectionUtility su = new SelectionUtility(null);

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			rankingSystemEntry = su.getFirstSelectedAs(IRankingSystemEntry.class);
			validatePage();
		}
	}
	
	private class EloCalculatorProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {	

		}

		@Override
		public Object[] getElements(Object inputElement) {
			IEloCalculatorManager manager = mService.getService(IEloCalculatorManager.class);
			if(manager==null||inputElement==null){
				return new Object[]{};
			}
			List<IEloCalculatorEntry> rankingSystems = null;
			rankingSystems = manager.getRegistredEntries();
			return rankingSystems.toArray();
		}
	}
	

	private class EloCalculatorSelectionListener implements ISelectionChangedListener {
		SelectionUtility su = new SelectionUtility(null);

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			eloCalculatorEntry = su.getFirstSelectedAs(IEloCalculatorEntry.class);
			validatePage();
		}
	}
	
	public IPairingAlgorithmEntry getPairingAlgorithmEntry() {
		return pairingAlgorithmEntry;
	}

	public void setPairingAlgorithmEntry(IPairingAlgorithmEntry pairingAlgorithmEntry) {
		this.pairingAlgorithmEntry = pairingAlgorithmEntry;
	}

	public IRankingSystemEntry getRankingSystemEntry() {
		return rankingSystemEntry;
	}

	public void setRankingSystemEntry(IRankingSystemEntry rankingSystemEntry) {
		this.rankingSystemEntry = rankingSystemEntry;
	}

	public IEloCalculatorEntry getEloCalculatorEntry() {
		return eloCalculatorEntry;
	}

	public void setEloCalculatorEntry(IEloCalculatorEntry eloCalculatorEntry) {
		this.eloCalculatorEntry = eloCalculatorEntry;
	}
}
