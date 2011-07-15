package ch.jester.ui.round.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import messages.Messages;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.util.PlayerColor;
import ch.jester.model.util.Result;
import ch.jester.ui.tournament.internal.Activator;

public class ResultController implements IDirtyManagerProvider{
	private static class ControllerSynchronizer{
		private List<ResultController> mOpenedControllers = new ArrayList<ResultController>();
		
		void addController(ResultController ctrl){
			mOpenedControllers.add(ctrl);
		}
		void remController(ResultController ctrl){
			mOpenedControllers.remove(ctrl);
		}
		
		void changed(ResultController ctrl, Pairing pairing, Result result){
			for(ResultController c:mOpenedControllers){
				if(c==ctrl){continue;}
				if(c.getLastPairingResult(pairing)!=result){
					c.addChangedResults(pairing, result);
				}
			}
		}
		public void changesSaved(ResultController resultController,
				HashMap<Pairing, Result> changedResults) {
			for(ResultController c:mOpenedControllers){
				if(c==resultController){continue;}
				c.clearChangedResults(changedResults);
			}
			
		}
	}
	
	
	
	PropertyChangeSupport mPcs;
	private Object mInput;
	private HashMap<Pairing, Result> mResultMap = new HashMap<Pairing, Result>();
	private ServiceUtility mServices = new ServiceUtility();
	private IDaoService<Pairing> mPairingDao = mServices.getDaoServiceByEntity(Pairing.class);
	private SWTDirtyManager mDirtyManager = new SWTDirtyManager();
	//private IPersistencyListener mQueueListener = null; x
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	private static ControllerSynchronizer mSync = new ControllerSynchronizer();
	public ResultController() {
		mSync.addController(this);
		mPcs = new PropertyChangeSupport(this);
		//EventLoadMatchingFilter filter = new EventLoadMatchingFilter(Pairing.class);
		/*mServices.getService(IPersistencyEventQueue.class).addListener(mQueueListener = new PersistencyListener(filter) {
			@Override
			public void persistencyEvent(final IPersistencyEvent pEvent) {
				Object oo = pEvent.getLoad();
				System.out.println(oo);
				UIUtility.syncExecInUIThread(new Runnable(){
					@Override
					public void run() {
						//mDirtyManager.setDirty(true);
						//pcs.firePropertyChange("input",null, mInput);
						
					}
				});

				
			}
		});*/
	
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l){
		mPcs.addPropertyChangeListener(l);
	}
	public void addPropertyChangeListener(String pProperty, PropertyChangeListener l){
		mPcs.addPropertyChangeListener(pProperty, l);
	}

	public void setInput(Object pInput){
		mDirtyManager.setDirty(true);
		mPcs.firePropertyChange("input",mInput, mInput = pInput);
	}
	
	public Object getInput(){
		return mInput;
	}
	
	public List<Round> getRounds(){
		if(mInput instanceof Category){
			Category cat = (Category) mInput;
			return cat.getRounds();
		}
		if(mInput instanceof Round){
			List<Round> list = new ArrayList<Round>();
			list.add((Round) mInput);
			return list;
		}
		return null;
	}
	
	public Tournament getTournamentInput(){
		if(mInput instanceof Tournament ){
			return (Tournament)mInput;
		}
		if(mInput instanceof Category){
			return ((Category)mInput).getTournament();
		}
		if(mInput instanceof Round){
			return ((Round)mInput).getCategory().getTournament();
		}
		return null;
	}
	public Category getCategoryInput(){
		if(mInput instanceof Category ){
			return (Category)mInput;
		}
		if(mInput instanceof Round){
			return ((Round)mInput).getCategory();
		}
		return null;
	}
	public Round getRoundInput(){
		if(mInput instanceof Round ){
			return (Round)mInput;
		}

		return null;
	}
	public String getTitlePath(){
		StringBuilder builder = new StringBuilder();
		builder.append(getTournamentInput().getName());
		builder.append(" > ");
		builder.append(getCategoryInput().getDescription());
		if(getRoundInput()!=null){
			builder.append(" > ");
			builder.append(Messages.TournamentLabelProvider_lbl_round);
			builder.append(": ");
			builder.append(getRoundInput().getId());
		}
		return builder.toString();
	}
	
	public Result getLastPairingResult(Pairing p){
		Result r = null;
		if((r=mResultMap.get(p))!=null){
			return r;
		}
		String sr = p.getResult();
		if(sr==null){return null;}
		r = Result.findByShortResult(sr);
		return r;
	}
	public String getLastPairingResultAsString(Pairing p){
		Result r = getLastPairingResult(p);
		return r==null?null:r.getShortResult();
	}

	//internal call with result changed to pairingperspective!
	private void addChangedResults(Pairing pairing, Result result) {
		String lastStringResult = getLastPairingResultAsString(pairing);
		mResultMap.put(pairing, result);
		PropertyChangeEvent pe = new PropertyChangeEvent(pairing, "changedResults", lastStringResult, result.getShortResult());
		mDirtyManager.setDirty(true);
		mLogger.debug("fire PropertyChangeEvent: "+pe+" -- "+pairing.getResult()+" -- "+result.getShortResult());
		mPcs.firePropertyChange(pe);
		mSync.changed(this, pairing, result);

	}
	
	public void addChangedResults(Pairing pairing, Result result, Pairing source) {
		addChangedResults(pairing, result);

	}
	public void addChangedResults(Pairing pairing, Result result, PlayerColor source) {
		//wechseln zum result aus paarungssicht.
		if(source==PlayerColor.BLACK){
			result = result.getOpposite();
		}
		addChangedResults(pairing, result);


	}
	public HashMap<Pairing, Result> getChangedResults(){
		return mResultMap;
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		mPcs.removePropertyChangeListener(l);
	}

	public void removePropertyChangeListener(String string,
			PropertyChangeListener setter) {
		mPcs.removePropertyChangeListener(string, setter);
		
	}

	@Override
	public DirtyManager getDirtyManager() {
		return mDirtyManager;
	}

	public SWTDirtyManager getSWTDirtyManager() {
		return mDirtyManager;
	}
	public void dispose(){
		//mServices.getService(IPersistencyEventQueue.class).removeListener(mQueueListener);
		mSync.remController(this);
	}
	private void clearChangedResults(HashMap<Pairing, Result> mMap) {
		if(mMap==mResultMap){
			mResultMap.clear();
			return;
		}
		Iterator<Pairing> pIt = mMap.keySet().iterator();
		while(pIt.hasNext()){
			mResultMap.remove(pIt.next());
		}
		if(mResultMap.isEmpty()){
			getDirtyManager().reset();
		}
		
	}


	public void saveChangedResults() {
		
		mPairingDao.manualEventQueueNotification(true);
		
		HashMap<Pairing, Result> map = getChangedResults();
		Iterator<Pairing> it = map.keySet().iterator();
		
		while(it.hasNext()){
			Pairing p = it.next();
			p.setResult(map.get(p).getShortResult());
			p.getWhite().addResult(map.get(p).getPointsWhite());
			p.getBlack().addResult(map.get(p).getPointsBlack());
			mPairingDao.save(p);
		}
		mSync.changesSaved(this, getChangedResults());
		
		mPairingDao.notifyEventQueue();
		mPairingDao.close();
		clearChangedResults(mResultMap);
		getDirtyManager().reset();
		
	}


	
}
