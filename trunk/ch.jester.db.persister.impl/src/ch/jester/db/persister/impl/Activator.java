package ch.jester.db.persister.impl;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.commonservices.api.persistency.IDaoServiceFactory;

public class Activator extends AbstractActivator{
	IDaoServiceFactory factory;
	private static Activator mActivator;
	public Activator(){
		mActivator=this;
	}
	protected static Activator getDefault(){
		return mActivator;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {

	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		
	}



}
