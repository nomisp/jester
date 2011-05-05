package ch.jester.commonservices.impl.internal;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.commonservices.api.proxy.IHTTPProxy;
import ch.jester.commonservices.impl.proxy.HTTPProxyAdapter;

public class Activator extends AbstractActivator {
	private static Activator mInstance;
	public static Activator getDefault(){
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
