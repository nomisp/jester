package ch.jester.ui.round.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.jester.common.persistency.EventLoadMatchingFilter;
import ch.jester.common.persistency.PersistencyListener;
import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.editorutilities.IDirtyManagerProvider;
import ch.jester.common.ui.editorutilities.SWTDirtyManager;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistency.IPersistencyFilter;
import ch.jester.commonservices.api.persistency.IPersistencyListener;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Result;
import ch.jester.model.Round;

public class ResultController implements IDirtyManagerProvider{
	PropertyChangeSupport pcs;
	private Object mInput;
	private HashMap<Pairing, Result> mResultMap = new HashMap<Pairing, Result>();
	private ServiceUtility mServices = new ServiceUtility();
	private SWTDirtyManager mDirtyManager = new SWTDirtyManager();
	private IPersistencyListener queueListener = null;
	public ResultController() {
		pcs = new PropertyChangeSupport(this);
		EventLoadMatchingFilter filter = new EventLoadMatchingFilter(Pairing.class);
		mServices.getService(IPersistencyEventQueue.class).addListener(queueListener = new PersistencyListener(filter) {
			@Override
			public void persistencyEvent(IPersistencyEvent pEvent) {
				mDirtyManager.setDirty(true);
				pcs.firePropertyChange("input",null, mInput);
			}
		});
	
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l){
		pcs.addPropertyChangeListener(l);
	}
	public void addPropertyChangeListener(String pProperty, PropertyChangeListener l){
		pcs.addPropertyChangeListener(pProperty, l);
	}

	public void setInput(Object pInput){
		mDirtyManager.setDirty(true);
		pcs.firePropertyChange("input",mInput, mInput = pInput);
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

	public void addChangedResults(Pairing pairing, Result result) {
		String lastStringResult = getLastPairingResultAsString(pairing);
		mResultMap.put(pairing, result);
		PropertyChangeEvent pe = new PropertyChangeEvent(pairing, "changedResults", lastStringResult, result.getShortResult());
		mDirtyManager.setDirty(true);
		System.out.println("propertychangeevent: "+pe+" -- "+pairing.getResult()+" -- "+result.getShortResult());
		pcs.firePropertyChange(pe);

	}
	public HashMap<Pairing, Result> getChangedResults(){
		return mResultMap;
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	public void removePropertyChangeListener(String string,
			PropertyChangeListener setter) {
		pcs.removePropertyChangeListener(string, setter);
		
	}

	@Override
	public DirtyManager getDirtyManager() {
		return mDirtyManager;
	}

	public SWTDirtyManager getSWTDirtyManager() {
		return mDirtyManager;
	}
	public void dispose(){
		mServices.getService(IPersistencyEventQueue.class).removeListener(queueListener);
	}
	
}
