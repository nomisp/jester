package ch.jester.db.persister.impl;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;

public class Activator extends AbstractActivator{
	PersisterFactory factory;
	private static Activator mActivator;
	public Activator(){
		mActivator=this;
	}
	protected static Activator getDefault(){
		return mActivator;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
	//	factory = new PersisterFactory(getActivationContext().getServiceUtil());
	//	factory.addServiceHandling(IPlayerPersister.class, DBPlayerPersister.class);
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		
	}



}
