package ch.jester.commonservices.impl.web;

import java.net.Proxy;

import ch.jester.common.preferences.PreferenceManager;
import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.web.IHTTPProxy;
import ch.jester.commonservices.impl.internal.Activator;

public class HTTPProxyAdapter implements IHTTPProxy, IPreferenceManagerProvider{
	private String mAddress;
	private int mPort;
	private String NULL_STRING ="";
	private ILogger mLogger;
	private PreferenceManager pm = new PreferenceManager();
	public HTTPProxyAdapter(){
		pm.setPrefixKey("ch.jester.ui.proxy");
		pm.registerProviderAtRegistrationService(this);
		mAddress = pm.create("address", "Address", NULL_STRING).getValue().toString();
		mPort = (Integer)pm.create("port", "Port", 0).getValue();
		
		mLogger = Activator.getDefault().getActivationContext().getLogger();
		mLogger.debug("HTTPProxyInit: "+mAddress+" / "+mPort);
		if(mAddress!=NULL_STRING && mPort!=0){
			createHTTPProxy(mAddress, mPort);
		}else{
			mLogger.info("no HTTPProxy created");
		}
		pm.addListener(new IPreferencePropertyChanged() {
			boolean[] created = {false, false};
			String address;
			int port;
			@Override
			public void propertyValueChanged(String internalKey, Object mValue,
					IPreferenceProperty preferenceProperty) {
				System.out.println(internalKey);
				if(internalKey=="address"){
					address = mValue.toString();
					created[0] = true;
				}
				if(internalKey=="port"){
					port = (Integer) mValue;
					created[1] = true;
				}
				if(created[0]==true && created[1] == true){
					created[0] = false;
					created[1] = false;
					if(port==0&&address.equals(NULL_STRING)){
						HTTPProxyAdapter.this.deleteProxy();
						mLogger.info("Deleted HTTP proxy: "+mAddress+" / "+mPort);
						return;
					}
					
					mAddress = address;
					mPort = (Integer) pm.getPropertyByInternalKey("port").getValue();
					createHTTPProxy(mAddress, mPort);
				}
				
			}
		});
		
	}
	
	
	@Override
	public void createHTTPProxy(String pProxyAdress, int pProxyPort) {
		
		HTTPFactory.createHTTPProxy(pProxyAdress, pProxyPort);
		mAddress=pProxyAdress;
		mPort=pProxyPort;
		mLogger.info("Created HTTP proxy: "+pProxyAdress+" / "+pProxyPort);
		
	}
	@Override
	public Proxy getHTTPProxy() {
		return HTTPFactory.getHTTPProxy();
	}
	@Override
	public String getAddress() {
		return mAddress;
	}
	@Override
	public int getPort() {
		return mPort;
	}


	@Override
	public void deleteProxy() {
		HTTPFactory.reset();
	}


	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return pm.checkId(pKey);
	}

}
