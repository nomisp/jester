package ch.jester.commonservices.impl.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.jester.common.components.InjectedLogFactoryComponentAdapter;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;

/**
 * DefaultImpl einer {@link IPreferenceRegistration}
 *
 */
public class PreferenceRegistration extends InjectedLogFactoryComponentAdapter<Void> implements IPreferenceRegistration{
	private List<IPreferenceManagerProvider> mAnonyReg = new ArrayList<IPreferenceManagerProvider>();
	private Map<String, IPreferenceManagerProvider> mReg = new HashMap<String, IPreferenceManagerProvider>();
	@Override
	public void registerPreferenceProvider(String pId,
			IPreferenceManagerProvider pManager) {
		mReg.put(pId, pManager);
	}

	@Override
	public void unregisterPreferenceProvider(String pId) {
		mReg.remove(pId);
		
	}

	@Override
	public IPreferenceManagerProvider findProvider(String pId) {
		IPreferenceManagerProvider prov =  mReg.get(pId);
		if(prov==null){
			for(IPreferenceManagerProvider mp:mAnonyReg){
				if(mp.getPreferenceManager(pId)!=null){
					prov = mp;
					break;
				}
			}
			
		}
		
		return prov;
	}

	@Override
	public void registerPreferenceProvider(IPreferenceManagerProvider pProvider) {
		mAnonyReg.add(pProvider);
	}

	@Override
	public void unregisterPreferenceProvider(
			IPreferenceManagerProvider pProvider) {
		mAnonyReg.remove(pProvider);
	}

	@Override
	public IPreferenceManager createManager() {
		return new PreferenceManager();
	}

}
