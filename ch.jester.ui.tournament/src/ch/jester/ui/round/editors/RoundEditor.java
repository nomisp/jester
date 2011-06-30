package ch.jester.ui.round.editors;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Result;
import ch.jester.model.Round;
import ch.jester.ui.round.form.ResultForm;
import ch.jester.ui.round.form.RoundForm;
import ch.jester.ui.round.form.contentprovider.CategoryNodeModelContentProvider;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;

/**
 * Turnier-Editor
 *
 */
public class RoundEditor extends AbstractEditor<IEntityObject> {
	public static final String ID = "ch.jester.ui.tournament.roundeditor";
	private ServiceUtility mService = new ServiceUtility();
	private ResultForm resForm;
	private ResultController mController = new ResultController();
	public RoundEditor() {
		super(true);
		System.out.println("Editor");
	}


	@Override
	protected void addPages() {
		mController.setInput(mDaoInput.getInput());
		
		
		RoundForm form = new RoundForm(this, "roundeditor", "Graph Overview");
		form.setResultController(mController);
		
		resForm = new ResultForm(this, "resultform", "Results");
		resForm.setResultController(mController);
	
		form.setContentProvider(getContentProvider(mDaoInput.getInput()));
		
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
			RoundNodeModelContentProvider prov = new RoundNodeModelContentProvider(mController);
			prov.setInput(o);
			return prov;
		}
		if(o instanceof Category){
			CategoryNodeModelContentProvider prov = new CategoryNodeModelContentProvider(mController);
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
			
			HashMap<Pairing, Result> map = mController.getChangedResults();
			//List<PairingResult> set = resForm.getChangedPairings();
			Iterator<Pairing> it = map.keySet().iterator();
			
			while(it.hasNext()){
				Pairing p = it.next();
				p.setResult(map.get(p).getShortResult());
				pairingservice.save(p);
			}
			
			/*for(Pairing p:set){
				pr.pairing.setResult(pr.result.getShortResult());
				pairingservice.save(pr.pairing);
			}*/
			pairingservice.notifyEventQueue();
			pairingservice.close();
			getDirtyManager().reset();
			setSaved(true);
		} finally {
			monitor.done();
		}
	}

}
