package ch.jester.commonservices.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Interner Standardaktivator.
 *
 */
public class Activator implements BundleActivator {
	private static Activator mInstance;
	private BundleContext mContext;
	public static Activator getDefault(){
		return mInstance;
	}
	
	public BundleContext getBundleContext(){
		return mContext;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		mInstance=this;
		mContext=context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		mInstance=null;
	}

}
