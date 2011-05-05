package ch.jester.commonservices.impl.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.proxy.IHTTPProxy;
import ch.jester.commonservices.impl.proxy.HTTPProxyAdapter;

public class Activator extends AbstractUIActivator {
	private static AbstractUIActivator mInstance;
	public static AbstractUIActivator getDefault(){
		return mInstance;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
		getActivationContext().getServiceUtil().registerService(IHTTPProxy.class, new HTTPProxyAdapter());
	}

	@Override
	public void stopDelegate(BundleContext pContext) {}

}
