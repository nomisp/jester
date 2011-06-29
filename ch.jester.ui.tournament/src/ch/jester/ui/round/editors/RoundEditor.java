package ch.jester.ui.round.editors;

import java.math.RoundingMode;
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
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.SettingItem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.ui.round.form.RoundForm;
import ch.jester.ui.round.form.contentprovider.CategoryNodeModelContentProvider;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;
import ch.jester.ui.tournament.ctrl.TournamentDetailsController;
import ch.jester.ui.tournament.forms.CategoryFormPage;
import ch.jester.ui.tournament.forms.TournamentFormPage;

/**
 * Turnier-Editor
 *
 */
public class RoundEditor extends AbstractEditor<IEntityObject> {
	
	public static final String ID = "ch.jester.ui.tournament.roundeditor";
	private TournamentFormPage mTournamentPage;
	private TournamentDetailsController mTournamentController;
	private ServiceUtility mService = new ServiceUtility();

	
	public RoundEditor() {
		super(true);
		//mLogger.debug("Round Editor: " + mDaoInput.getInput().getName());
	}
	
//	protected FormToolkit createToolkit(Display display) {
//		return new FormToolkit(ExamplePlugin.getDefault().getFormColors(display));
//	}

	@Override
	protected void addPages() {
		RoundForm form = new RoundForm(this, "roundeditor", "Graph Overview");
		form.setContentProvider(getContentProvider(mDaoInput.getInput()));
		
		try {
			addPage(form);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private RoundNodeModelContentProvider getContentProvider(Object o){
		if(o instanceof Round){
			RoundNodeModelContentProvider prov = new RoundNodeModelContentProvider();
			prov.setInput(o);
			return prov;
		}
		if(o instanceof Category){
			CategoryNodeModelContentProvider prov = new CategoryNodeModelContentProvider();
			prov.setInput(o);
			return prov;
		}
		
		return null;
	}
	
	public void init_0(Object parent) {
		/*mTournamentPage = (TournamentFormPage) parent;
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
		setPartName(mTournamentPage.getNameText().getText()+", "+mTournamentPage.getDescriptionText().getText());*/
	
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	/*	mLogger.debug("Saving "+this);
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		try{
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
		}*/
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
