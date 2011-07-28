package ch.jester.ui.tournament.editors;

import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PartInitException;

import ch.jester.common.settings.ISettingObject;
import ch.jester.common.settings.SettingHelper;
import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IDaoServiceFactory;
import ch.jester.commonservices.api.persistency.IPrivateContextDaoService;
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
	
	public static final String ID = "ch.jester.ui.tournament.tournamentEditor"; //$NON-NLS-1$
	private TournamentFormPage mTournamentPage;
	private TournamentDetailsController mTournamentController;
	private ServiceUtility mService = new ServiceUtility();
	@SuppressWarnings("rawtypes")
	private AbstractSystemSettingsFormPage settingsPage;
	private CategoryFormPage categoryPage;
	private IPrivateContextDaoService<Tournament> privateService;
	IDaoService<Tournament> origService;
	Tournament original;
	public TournamentEditor() {
		super(true);
	}
	

	
	
	@Override
	protected void addPages() {


		TournamentFormPage tournamentPage = new TournamentFormPage(this, "TournamentFormPage", Messages.TournamentEditor_title); //$NON-NLS-1$
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
		
		try {
			addPage(tournamentPage);
			addPage(categoryPage);
			//addPage(new RoundForm(this, "abcd", "xyz"));
			if (settingsPage != null) {
				settingsPage.getDirtyManager().addDirtyListener(new IDirtyListener() {
					
					@Override
					public void propertyIsDirty() {
						TournamentEditor.this.getDirtyManager().setDirty(true);
						
					}
				});
				addPage(settingsPage);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}



	public void init_0(Object parent) {
		mTournamentPage = (TournamentFormPage) parent;
		mTournamentController = mTournamentPage.getController();		
		mTournamentController.getDirtyManager().setDirty(mDaoInput.isAlreadyDirty());
		
		setDirtyManager(mTournamentController.getDirtyManager());
		getDirtyManager().addDirtyListener(this);
		mTournamentController.getDirtyManager().addDirtyListener(new IDirtyListener() {
			@Override
			public void propertyIsDirty() {
				setPartName(mTournamentPage.getNameText().getText());
				
			}
		});
		setPartName(mTournamentPage.getNameText().getText()+", "+mTournamentPage.getDescriptionText().getText()); //$NON-NLS-1$
	
		setDaoService(privateService);
		
		initializePrivateContext();
		
		
	}
	
	private void initializePrivateContext(){
		origService = getServiceUtil().getDaoServiceByEntity(Tournament.class);
		privateService = origService.privateContext();
		//privateService = getServiceUtil().getService(IDaoServiceFactory.class).adaptPrivate(origService);
		original = mDaoInput.getInput();
		Tournament tournamentInPrivateContext = privateService.find(original.getId());
		setPrivateContextTournament(tournamentInPrivateContext);
	}
	
	private void setPrivateContextTournament(Tournament t){
		 mDaoInput.setInput(t);
		 categoryPage.setTournament(t);
		 mTournamentController.setTournament(t);	
		 mTournamentController.getDirtyManager().setDirty(false);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		mLogger.debug("Saving "+this); //$NON-NLS-1$
		monitor.beginTask(Messages.TournamentEditor_progress_saving, IProgressMonitor.UNKNOWN);
		try{
			categoryPage.doSave(monitor);
			mTournamentController.updateModel();
			Tournament tournament = mTournamentController.getTournament();
			SettingItem origItem = tournament.getSettingItem();
			if (settingsPage != null) {
				SettingItem itemToStore = null;
					if(origItem!=null){
						privateService.delete(origItem);
					}
				
					SettingHelper<ISettingObject> settingHelper = new SettingHelper<ISettingObject>();
					SettingItem settingItem = ModelFactory.getInstance().createSettingItem(tournament);
					itemToStore = settingHelper.analyzeSettingObjectToStore(settingsPage.getSettingObject(), settingItem);
					itemToStore = settingItem;
				itemToStore.setTournament(tournament);
				tournament.setSettingItem(itemToStore);
			}
			debug(tournament);
			privateService.save(tournament);
			privateService.commit();
			debug(tournament);
			
			getDirtyManager().reset();
			setSaved(true);
			
			origService.getNotifier().notifyEventQueue();
			initializePrivateContext();
			mTournamentController.getDirtyManager().setDirty(false);
			//initializePrivateContext();
		} finally {
			monitor.done();
		}
	}
	@Override
	public void editorClosed() {
		//origService.refresh(mDaoInput.getInput());
		if (!wasSaved()) {
			mTournamentController.getTournament();
			mTournamentController.setTournament(mDaoInput.getInput());
		}
		privateService.close();
		mTournamentController.updateUI();

	}
	
	private AbstractSystemSettingsFormPage findSettingsPage() {
		Tournament tourn = mDaoInput.getInput();
		String settingsPage = tourn.getSettingsPage();
		IPairingManager manager = mService.getService(IPairingManager.class);
		IPairingAlgorithm pairingAlgorithm = null;
		List<IPairingAlgorithmEntry> pairingSystems = manager.getRegistredEntries();
		for (IPairingAlgorithmEntry pairingAlgorithmEntry : pairingSystems) {
			if (pairingAlgorithmEntry.getSettingsPage() != null && 
					pairingAlgorithmEntry.getSettingsPage().equals(settingsPage)) {
				pairingAlgorithm = pairingAlgorithmEntry.getService();
				break;
			}
		}
		
		return pairingAlgorithm != null ? pairingAlgorithm.getSettingsFormPage(this, tourn) : null;
	}
	
	private void debug(Tournament t){
		mLogger.debug("Tournament: "+t+" Name: "+t.getName());
		mLogger.debug("#Categories: "+t.getCategories().size());
	}
}
