package ch.jester.ormapper.internal;


import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

public class ORMapperActivator  extends AbstractActivator {
	private static ORMapperActivator mActivator;
	public static ORMapperActivator getDefault(){
		return mActivator;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		mActivator=this;
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}



}
