package ch.jester.commonservices.impl.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.web.IHTTPProxy;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.impl.web.HTTPProxyAdapter;
import ch.jester.commonservices.impl.web.JavaPingService;

public class Activator extends AbstractUIActivator {
	private static AbstractUIActivator mInstance;
	public static AbstractUIActivator getDefault(){
		return mInstance;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
		getActivationContext().getServiceUtil().registerService(IHTTPProxy.class, new HTTPProxyAdapter());
		JavaPingService jps;
		getActivationContext().getServiceUtil().registerService(IPingService.class, jps = new JavaPingService());
		jps.ping("http://www.google.com", 500, 1000);
	}

	@Override
	public void stopDelegate(BundleContext pContext) {}

}
