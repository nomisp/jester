package ch.jester.db.persister.impl;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.commonservices.api.persistency.IDaoServiceFactory;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.ICategoryDao;
import ch.jester.dao.IPlayerDao;
import ch.jester.dao.IRoundDao;
import ch.jester.dao.ITournamentDao;

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
		ServiceUtility utility = getActivationContext().getServiceUtil();
		IDaoServiceFactory factory = utility.getService(IDaoServiceFactory.class);
		//factory.addServiceHandling(IPlayerDao.class, DBPlayerPersister.class);
		//factory.addServiceHandling(ITournamentDao.class, DBTournamentPersister.class);
		//factory.addServiceHandling(ICategoryDao.class, DBCategoryPersister.class);
		//factory.addServiceHandling(IRoundDao.class, DBRoundPersister.class);
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		
	}



}
