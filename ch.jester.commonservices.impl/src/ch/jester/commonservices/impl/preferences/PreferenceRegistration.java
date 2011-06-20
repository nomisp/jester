package ch.jester.commonservices.impl.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.osgi.service.component.ComponentContext;

import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;

public class PreferenceRegistration implements IPreferenceRegistration, IComponentService<ILoggerFactory>{
	private ILogger mLogger;
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
	public void start(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bind(ILoggerFactory pT) {
		mLogger = pT.getLogger(this.getClass());
		mLogger.info("PreferenceRegistration Component started");
		
	}

	@Override
	public void unbind(ILoggerFactory pT) {
		// TODO Auto-generated method stub
		
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

}
