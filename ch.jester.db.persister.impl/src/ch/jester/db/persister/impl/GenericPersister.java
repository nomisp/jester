
package ch.jester.db.persister.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.eclipse.core.runtime.Assert;

import ch.jester.common.persistency.PersistencyEvent;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.orm.ORMPlugin;

public class GenericPersister<T extends IEntityObject> implements IDaoService<T> {
	private StopWatch watch = new StopWatch();
	protected EntityManagerFactory mFactory;
	private EntityManager mManager;

	private IPersistencyEventQueue mEventQueue;
	private Query mPagingQuery;
	private Query mCountQuery;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	private Class<T> mClz;
	private boolean mManualNotify;
	private List<T> mNotificationCache;
	private boolean doCommit;
	@SuppressWarnings("unchecked")
	public GenericPersister(){
		Type actualTypeArgument = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		mClz = (Class<T>) actualTypeArgument;
		check();
	}
	GenericPersister(EntityManager pManager){
		Type actualTypeArgument = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		mClz = (Class<T>) actualTypeArgument;
		setManager(pManager);
		check();
	}

	public GenericPersister(Class<T> clz){
		mClz = clz;
		check();
	}	
	public EntityManager getManager() {
		return mManager;
	}
	public void setManager(EntityManager mManager) {
		this.mManager = mManager;
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
			mFactory = ORMPlugin.getJPAEntityManagerFactory();
		}
		if(getManager()==null){
			setManager(ORMPlugin.getJPAEntityManager());
		}
		mPagingQuery = getPagingQuery();
		mCountQuery = getCountQuery();
		if(mEventQueue==null){
			mEventQueue = Activator.getDefault().getActivationContext().getService(IPersistencyEventQueue.class);
		}
	}
	
	@Override
	public void save(Collection<T> pTCollection) {
		save(pTCollection,false);
	}
	@Override
	public void saveBatch(Collection<T> pTCollection) {
		save(pTCollection, true);
	}
	
	protected void save(Collection<T> pTCollection, boolean batch) {
		if(pTCollection.isEmpty()){
			return;
		}
		EntityManager mManager = null;
		if(batch){
			mManager = mFactory.createEntityManager();
			mManager.getTransaction().begin();
		}else{
			mManager = this.getManager();
			openOrJoinTrx();
		}
		
		try{
		
		for(T p:pTCollection){
			if(p.isUnsafed()){
				mManager.persist(p);
			}else{
				IEntityObject p0 =  mManager.find(p.getClass(), p.getId());
				mManager.merge(p0);
			}
		}
	    if(batch){
			mManager.flush();
			mManager.getTransaction().commit();
		    mManager.clear();
	    }else{
	    	closeTrx();
	    }
		}catch(Exception e){
			if(e instanceof RollbackException){
				throw new ProcessingException(e.getCause());
			}
			throw new ProcessingException(e);
		}
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
		save(createCollection(pT), false);
	}
	private Collection<T> createCollection(T pT){
		List<T> list = new LinkedList<T>();
		list.add(pT);
		return list;
	}

	@Override
	public void delete(T pT) {
		delete(createCollection(pT));
	}
	
	@Override
	public void delete(Collection<T> pTCollection) {
		final EntityManager deleteManager = mFactory.createEntityManager(); //neuen entity manager, falls was schief geht, wird nur dieser context detached
		EntityTransaction trx = deleteManager.getTransaction();
		trx.begin();
		try{
		List<T> mergeList = new LinkedList<T>();
		for(T pT:pTCollection){
			T p = deleteManager.merge(pT);
			deleteManager.remove(p);
			mergeList.add(p);
		}
		
		trx.commit();
		getManager().getTransaction().begin();
		for(T pT:pTCollection){   // soweit gekommen? alles ok
			T p = getManager().merge(pT);  //mergen mit dem eigentlichen manager
			
			
			//System.out.println(p);
		}
		getManager().getTransaction().commit();
	
		}catch(Exception e){
			
			if(e instanceof RollbackException){
				throw new ProcessingException(e.getCause());
			}
			throw new ProcessingException(e);
		}finally{
			deleteManager.close();
		}
		
		fireDeleteEvent(pTCollection);
	}

	@Override
	public void close() {
		fireManualNotification();
	}

	@Override
	public List<T> getAll() {
		openOrJoinTrx();
		@SuppressWarnings("unchecked")
		List<T> result = getManager().createNamedQuery(mClz.getSimpleName()+".getAll").getResultList();
		closeTrx();
		return result;
	}

	@Override
	public List<T> executeNamedQuery(String namedQuery) {
		//check();
		openOrJoinTrx();
		@SuppressWarnings("unchecked")
		List<T> result = getManager().createNamedQuery(namedQuery).getResultList();
		closeTrx();
		return result;
	}

	protected void openOrJoinTrx(){
		EntityTransaction trx = getManager().getTransaction();
		if (!trx.isActive()) {
			trx.begin();
			doCommit = true;
		} else {
			getManager().joinTransaction();
			doCommit= false;
		}
	}
	protected void closeTrx(){
		if(doCommit){
			getManager().getTransaction().commit();
		}
	}
	public List<T> executeNamedQuery(String queryName, String pPara, Object pVal){
		check();
		openOrJoinTrx();
		@SuppressWarnings("unchecked")
		List<T> result = getManager().createNamedQuery(queryName).setParameter(pPara, pVal).getResultList();
		closeTrx();
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
		//check();
		openOrJoinTrx();
		int result = ((Long) mCountQuery.getSingleResult()).intValue();
		closeTrx();
		return result;
	}
	@Override
	public List<T> getFromTo(int from, int to) {
		//check();
		watch.start();
		openOrJoinTrx();
		@SuppressWarnings("unchecked")
		List<T> result =  mPagingQuery.setMaxResults(to-from).setFirstResult(from).getResultList();
		closeTrx();
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
		return getManager().createQuery(query);
	}

	@Override
	public Class<T> getDaoClass() {
		return mClz;
	}
	@Override
	public Query createNamedQuery(String query) {
		//check();
		return getManager().createNamedQuery(query);
	}
	@Override
	public void manualEventQueueNotification(boolean pBoolean) {
		mManualNotify = pBoolean;
		fireManualNotification();
	}
	
	public void clearEventQueueCache(){
		if(mNotificationCache!=null){
			mNotificationCache.clear();
		}
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
	@Override
	public T find(Integer id) {
		return getManager().find(mClz, id);
	}

}
