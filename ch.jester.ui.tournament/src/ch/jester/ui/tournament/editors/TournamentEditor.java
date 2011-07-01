package ch.jester.ui.tournament.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PartInitException;

import ch.jester.common.settings.ISettingObject;
import ch.jester.common.settings.SettingHelper;
import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.SettingItem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.ui.tournament.ctrl.TournamentDetailsController;
import ch.jester.ui.tournament.forms.CategoryFormPage;
import ch.jester.ui.tournament.forms.TournamentFormPage;

/**
 * Turnier-Editor
 *
 */
public class TournamentEditor extends AbstractEditor<Tournament> {
	
	public static final String ID = "ch.jester.ui.tournament.tournamentEditor";
	private TournamentFormPage mTournamentPage;
	private TournamentDetailsController mTournamentController;
	private ServiceUtility mService = new ServiceUtility();
	private AbstractSystemSettingsFormPage settingsPage;
	private CategoryFormPage categoryPage;
	
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
		categoryPage = new CategoryFormPage(this);
		categoryPage.getCategoryDetailsDirtyManager().addDirtyListener(new IDirtyListener() {
			
			@Override
			public void propertyIsDirty() {
				TournamentEditor.this.getDirtyManager().setDirty(true);
				
			}
		});
		settingsPage = findSettingsPage();
		settingsPage.getDirtyManager().addDirtyListener(new IDirtyListener() {
			
			@Override
			public void propertyIsDirty() {
				TournamentEditor.this.getDirtyManager().setDirty(true);
				
			}
		});
		
		
		try {
			addPage(tournamentPage);
			addPage(categoryPage);
			//addPage(new RoundForm(this, "abcd", "xyz"));
			if (settingsPage != null) addPage(settingsPage);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}
	
	public void init_0(Object parent) {
		mTournamentPage = (TournamentFormPage) parent;
		mTournamentController = mTournamentPage.getController();			
		mTournamentController.setTournament(mDaoInput.getInput());	
		mTournamentController.getDirtyManager().setDirty(mDaoInput.isAlreadyDirty());
		setDaoService(super.getServiceUtil().getDaoServiceByEntity(Tournament.class));
		
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
		mLogger.debug("Saving "+this);
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		try{
			categoryPage.doSave(monitor);
			mTournamentController.updateModel();
			Tournament tournament = mTournamentController.getTournament();
			IDaoService<SettingItem> settingItemPersister = mService.getDaoServiceByEntity(SettingItem.class);
			SettingHelper<ISettingObject> settingHelper = new SettingHelper<ISettingObject>();
			SettingItem settingItem = ModelFactory.getInstance().createSettingItem(tournament);
			settingItem = settingHelper.analyzeSettingObjectToStore(settingsPage.getSettingObject(), settingItem);
			settingItemPersister.save(settingItem);
			
			mDao.save(tournament);
			getDirtyManager().reset();
			setSaved(true);
		} finally {
			monitor.done();
		}
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
	
	private AbstractSystemSettingsFormPage findSettingsPage() {
		Tournament tourn = mDaoInput.getInput();
		String settingsPage = tourn.getSettingsPage();
		IPairingManager manager = mService.getService(IPairingManager.class);
		IPairingAlgorithm pairingAlgorithm = null;
		List<IPairingAlgorithmEntry> pairingSystems = manager.getRegistredEntries();
		for (IPairingAlgorithmEntry pairingAlgorithmEntry : pairingSystems) {
			if (pairingAlgorithmEntry.getSettingsPage().equals(settingsPage)) {
				pairingAlgorithm = pairingAlgorithmEntry.getService();
				break;
			}
		}
		
		return pairingAlgorithm != null ? pairingAlgorithm.getSettingsFormPage(this, tourn) : null;
	}
}
