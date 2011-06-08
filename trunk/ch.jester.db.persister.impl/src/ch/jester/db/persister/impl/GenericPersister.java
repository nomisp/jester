
package ch.jester.db.persister.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.eclipse.core.runtime.Assert;

import ch.jester.common.persistency.util.PersistencyEvent;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.orm.ORMPlugin;

public class GenericPersister<T extends IEntityObject> implements IDaoService<T> {
	private StopWatch watch = new StopWatch();
	protected EntityManagerFactory mFactory;
	protected EntityManager mManager;
	private IPersistencyEventQueue mEventQueue;
	private Query mPagingQuery;
	private Query mCountQuery;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	private Class<T> mClz;
	private boolean mManualNotify;
	private List<T> mNotificationCache;
	@SuppressWarnings("unchecked")
	public GenericPersister(){
		Type actualTypeArgument = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		mClz = (Class<T>) actualTypeArgument;
	}
	public GenericPersister(Class<T> clz){
		mClz = clz;
	}	
	private void fireEvent(Object pLoad, PersistencyEvent.Operation pOperation){
		Assert.isNotNull(mEventQueue, "The PersistencyEventQueue is not running");
		mEventQueue.dispatch(new PersistencyEvent(this, pLoad, pOperation));
	}
	private void fireDeleteEvent(Object pLoad){
		fireEvent(pLoad, IPersistencyEvent.Operation.DELETED);
	}
	private void fireSaveEvent(Object pLoad){
		fireEvent(pLoad, IPersistencyEvent.Operation.SAVED);
	}
	private void check(){
		if(mFactory==null){
			mFactory = ORMPlugin.getJPAEntitManagerFactor();
		}
		if(mManager==null){
			mManager = mFactory.createEntityManager();
			mPagingQuery = getPagingQuery();
			mCountQuery = getCountQuery();
		}
		if(mEventQueue==null){
			mEventQueue = Activator.getDefault().getActivationContext().getService(IPersistencyEventQueue.class);
		}
	}
	
	@Override
	public void save(Collection<T> pTCollection) {
		if(pTCollection.isEmpty()){
			return;
		}
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		for(T p:pTCollection){
			//if(p.getId()!=0){
				mManager.merge(p);
			//}else{
			//	mManager.persist(p);
			//}
		}
		trx.commit();
		if(!mManualNotify){
			fireSaveEvent(pTCollection);
		}else{
			if(mNotificationCache==null){
				mNotificationCache = new ArrayList<T>(100);
			}
			mNotificationCache.addAll(pTCollection);
		}
	
	}
	
	
	@Override
	public void save(T pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		if(pT.getId()!=null){
			mManager.merge(pT);
		}else{
			mManager.persist(pT);
		}
		trx.commit();
		if(!mManualNotify){
			fireSaveEvent(pT);
		}else{
			if(mNotificationCache==null){
				mNotificationCache = new ArrayList<T>(100);
			}
			mNotificationCache.add(pT);
		}
	
	}

	@Override
	public void delete(T pT) {
		if(pT.getId()==null){return;}
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		IEntityObject p = mManager.find(pT.getClass(), pT.getId());
		mManager.remove(p);
		trx.commit();
		fireDeleteEvent(pT);
	}
	
	@Override
	public void delete(Collection<T> pTCollection) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		for(T pT:pTCollection){
			if(pT.getId()==null){continue;}
			IEntityObject p = mManager.find(pT.getClass(), pT.getId());
			mManager.remove(p);
			
		}
		trx.commit();
		fireDeleteEvent(pTCollection);
	}

	@Override
	public void close() {
		fireManualNotification();
		if(mManager!=null){
			mManager.close();
		}
		//mFactory.close();
	}

	@Override
	public List<T> getAll() {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		@SuppressWarnings("unchecked")
		List<T> result = mManager.createNamedQuery(mClz.getSimpleName()+".getAll").getResultList();
		trx.commit();
		return result;
	}

	@Override
	public List<T> executeNamedQuery(String namedQuery) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		@SuppressWarnings("unchecked")
		List<T> result = mManager.createNamedQuery(namedQuery).getResultList();
		trx.commit();
		return result;
	}

	public List<T> executeNamedQuery(String queryName, String pPara, Object pVal){
		check();
	//	EntityTransaction trx = mManager.getTransaction();
	//	trx.begin();
	//	@SuppressWarnings("unchecked")
		@SuppressWarnings("unchecked")
		List<T> result = mManager.createNamedQuery(queryName).setParameter(pPara, pVal).getResultList();
	//	trx.commit();
		return result;
	}

	/**
	 * Vorbereiten eines Suchparameters für eine Like Suche
	 * @param pParam Suchparameter
	 * @param mode	MatchMode
	 * @return aufbereiteter Suchparameter (mit %-Zeichen)
	 */
	public String prepareLikeSearch(String pParam, MatchMode mode) {
		StringBuffer sb = new StringBuffer();
		switch (mode) {
			case EXACT: return pParam;
			case START: sb.append("%");
						sb.append(pParam);
						break;
			case END:	sb.append(pParam);
						sb.append("%");
						break;
			case ANYWHERE:	sb.append("%");
							sb.append(pParam);
							sb.append("%");
							break;
			default: return pParam;
		}
		return sb.toString();
	}
	@Override
	public int count() {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		int result = ((Long) mCountQuery.getSingleResult()).intValue();
		trx.commit();
		return result;
	}
	@Override
	public List<T> getFromTo(int from, int to) {
		check();
		watch.start();
		mManager.getTransaction().begin();
		List<T> result =  mPagingQuery.setMaxResults(to-from).setFirstResult(from).getResultList();
		mManager.getTransaction().commit();
		watch.stop();
		mLogger.debug("Query Time - getFromTo: "+watch.getElapsedTime());
		return (List<T>) result;
	}
	protected  Query getPagingQuery(){
		return null;
	}
	protected Query getCountQuery(){
		return null;
	}
	
	public Query createQuery(String query) {
		return mManager.createQuery(query);
	}

	@Override
	public Class<T> getDaoClass() {
		return mClz;
	}
	@Override
	public Query createNamedQuery(String query) {
		check();
		return mManager.createNamedQuery(query);
	}
	@Override
	public void manualEventQueueNotification(boolean pBoolean) {
		mManualNotify = pBoolean;
		fireManualNotification();
	}
	
	@Override
	public void notifyEventQueue() {
		fireManualNotification();
		
	}
	private void fireManualNotification(){
		if(mManualNotify&&mNotificationCache!=null&&!mNotificationCache.isEmpty()){
			fireSaveEvent(mNotificationCache);
			mNotificationCache = null;
		}
	}
}
