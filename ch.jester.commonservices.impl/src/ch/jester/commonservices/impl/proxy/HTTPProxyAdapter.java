package ch.jester.commonservices.impl.proxy;

import java.net.Proxy;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.proxy.IHTTPProxy;
import ch.jester.commonservices.impl.internal.Activator;

public class HTTPProxyAdapter implements IHTTPProxy{
	private IEclipsePreferences mPrefs = new InstanceScope().getNode("ch.jester.commonservices.impl");
	private String mAddress;
	private int mPort;
	private String NULL_STRING ="-1";
	public HTTPProxyAdapter(){
		mAddress = mPrefs.get("proxy.address", NULL_STRING);
		mPort = Integer.parseInt(mPrefs.get("proxy.port", NULL_STRING));
		if(mAddress!=NULL_STRING && mPort!=-1){
			createHTTPProxy(mAddress, mPort);
		}
		mPrefs.addPreferenceChangeListener(new IPreferenceChangeListener() {
			@Override
			public void preferenceChange(PreferenceChangeEvent event) {
				System.out.println("Prefs changed: "+event.getKey()+" --> "+event.getNewValue());
				
			}
		});
	}
	
	
	@Override
	public void createHTTPProxy(String pProxyAdress, int pProxyPort) {
		HTTPFactory.createHTTPProxy(pProxyAdress, pProxyPort);
		mAddress=pProxyAdress;
		mPort=pProxyPort;
		
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

}
