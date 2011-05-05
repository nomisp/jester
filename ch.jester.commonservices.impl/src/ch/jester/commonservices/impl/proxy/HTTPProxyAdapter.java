package ch.jester.commonservices.impl.proxy;

import java.net.Proxy;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import ch.jester.common.web.HTTPFactory;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.proxy.IHTTPProxy;
import ch.jester.commonservices.impl.internal.Activator;

public class HTTPProxyAdapter implements IHTTPProxy{
	private String mAddress;
	private int mPort;
	private String NULL_STRING ="-1";
	private ILogger mLogger;
	public HTTPProxyAdapter(){
		mLogger = Activator.getDefault().getActivationContext().getLogger();
		mAddress =Platform.getPreferencesService().getString("ch.jester.commonservices.impl", "proxy.address", NULL_STRING, null);
		mPort = Integer.parseInt(Platform.getPreferencesService().getString("ch.jester.commonservices.impl", "proxy.port", NULL_STRING, null));
		mLogger.debug("HTTPProxyInit: "+mAddress+" / "+mPort);
		if(mAddress!=NULL_STRING && mPort!=-1){
			createHTTPProxy(mAddress, mPort);
		}else{
			mLogger.info("no HTTPProxy created");
		}
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			String address, port;
			boolean[] created = {false, false};
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getProperty()=="proxy.address"){
					address = event.getNewValue().toString();
					created[0] = true;
				}
				if(event.getProperty()=="proxy.port"){
					port = event.getNewValue().toString();
					created[1] = true;
				}
				if(created[0]==true && created[1] == true){
					created[0] = false;
					created[1] = false;
					mAddress = address;
					mPort = Integer.parseInt(port);
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

}
