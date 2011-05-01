package ch.jester.db.persister.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.persistencyevent.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistencyevent.PersistencyEvent;
import ch.jester.dao.IDaoObject;
import ch.jester.dao.IDaoService;

import ch.jester.orm.ORMPlugin;

public class GenericPersister<T extends IDaoObject> implements IDaoService<T> {
	EntityManagerFactory mFactory;
	EntityManager mManager;
	IPersistencyEventQueue mEventQueue;
	Query QQ;
	private void fireEvent(Object pLoad, PersistencyEvent.Operation pOperation){
		mEventQueue.dispatch(new PersistencyEvent(this, pLoad, pOperation));
	}
	private void fireDeleteEvent(Object pLoad){
		fireEvent(pLoad, PersistencyEvent.Operation.DELETED);
	}
	private void fireSaveEvent(Object pLoad){
		fireEvent(pLoad, PersistencyEvent.Operation.SAVED);
	}
	private void check(){
		if(mFactory==null){
			mFactory = ORMPlugin.getJPAEntitManagerFactor();
		}
		if(mManager==null){
			mManager = mFactory.createEntityManager();
			QQ = mManager.createQuery("SELECT player FROM Player player");
			QQ.setMaxResults(50);
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
			if(p.getId()!=0){
				mManager.merge(p);
			}else{
				mManager.persist(p);
			}
		}
		trx.commit();
		fireSaveEvent(pTCollection);
	
	}

	protected Class<?> getTargetClass(Object o){
		if(o instanceof Collection){
			return ((Collection<?>)o).iterator().next().getClass();
		}
		return o.getClass();
	}
	

	
	@Override
	public void save(T pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		if(pT.getId()!=0){
			mManager.merge(pT);
		}else{
			mManager.persist(pT);
		}
		trx.commit();
		fireSaveEvent(pT);
	
	}

	@Override
	public void delete(T pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		IDaoObject p = mManager.find(pT.getClass(), pT.getId());
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
			IDaoObject p = mManager.find(pT.getClass(), pT.getId());
			mManager.remove(p);
			
		}
		trx.commit();
		fireDeleteEvent(pTCollection);
	}

	@Override
	public void close() {
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
		List<T> result = mManager.createNamedQuery("getAll").getResultList();
		trx.commit();
		return result;
	}

	@Override
	public List<T> getAll(String namedQuery) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		@SuppressWarnings("unchecked")
		List<T> result = mManager.createNamedQuery(namedQuery).getResultList();
		trx.commit();
		return result;
	}

	public List<T> findByParameter(String queryName, String pPara, Object pVal){
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		@SuppressWarnings("unchecked")
		List<T> result = mManager.createNamedQuery(queryName).setParameter(pPara, pVal).getResultList();
		trx.commit();
		return result;
	}

	/**
	 * Vorbereiten eines Suchparameters f�r eine Like Suche
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
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<T> getFromTo(int from, int to) {
		check();
		System.out.println("maxResults: "+(to-from)+" - firstResult "+from);
		StopWatch watch = new StopWatch();
		watch.start();
		List<T> result =  QQ.setFirstResult(from).getResultList();
		watch.stop();
		System.out.println("Query took "+watch.getElapsedTime());
		return (List<T>) result;
	}
}
