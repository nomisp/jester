package ch.jester.ui.round.editors;

import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PartInitException;

import ch.jester.common.settings.ISettingObject;
import ch.jester.common.settings.SettingHelper;
import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Round;
import ch.jester.model.SettingItem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.IPairingAlgorithmEntry;
import ch.jester.system.api.pairing.IPairingManager;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.ui.round.form.ResultForm;
import ch.jester.ui.round.form.ResultForm.PairingResult;
import ch.jester.ui.round.form.RoundForm;
import ch.jester.ui.round.form.contentprovider.CategoryNodeModelContentProvider;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;
import ch.jester.ui.tournament.ctrl.TournamentDetailsController;
import ch.jester.ui.tournament.editors.TournamentEditor;
import ch.jester.ui.tournament.forms.CategoryFormPage;
import ch.jester.ui.tournament.forms.TournamentFormPage;

/**
 * Turnier-Editor
 *
 */
public class RoundEditor extends AbstractEditor<IEntityObject> {
	public static final String ID = "ch.jester.ui.tournament.roundeditor";
	private ServiceUtility mService = new ServiceUtility();
	private ResultForm resForm;
	
	public RoundEditor() {
		super(true);
	}


	@Override
	protected void addPages() {
		RoundForm form = new RoundForm(this, "roundeditor", "Graph Overview");
		resForm = new ResultForm(this, "resultform", "Results");
		form.setContentProvider(getContentProvider(mDaoInput.getInput()));
		resForm.setInput(mDaoInput.getInput());
		setDirtyManager(resForm.getDirtyManager());
		getDirtyManager().addDirtyListener(this);
		
		try {
			addPage(form);
			addPage(resForm);
		} catch (PartInitException e) {
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
		mLogger.debug("Saving "+this);
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		try{
			IDaoService<Pairing> pairingservice = mService.getDaoServiceByEntity(Pairing.class);
			pairingservice.manualEventQueueNotification(true);
			List<PairingResult> set = resForm.getChangedPairings();
			for(PairingResult pr:set){
				pr.pairing.setResult(pr.result.getShortResult());
				pairingservice.save(pr.pairing);
			}
			pairingservice.notifyEventQueue();
			pairingservice.close();
			getDirtyManager().reset();
			setSaved(true);
		} finally {
			monitor.done();
		}
	}

}
