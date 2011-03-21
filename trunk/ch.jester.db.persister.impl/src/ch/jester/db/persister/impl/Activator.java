package ch.jester.db.persister.impl;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

public class Activator extends AbstractActivator{

	@Override
	public void startDelegate(BundleContext pContext) {
		//getActivationContext().getServiceUtil().registerService(IPlayerPersister.class, new DBPlayerPersister());
		
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}



}
