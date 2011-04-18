package ch.jester.db.persister.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import ch.jester.common.utility.persistency.PersistencyEvent;
import ch.jester.common.utility.persistency.PersistencyEventQueue;
import ch.jester.dao.IDAO;
import ch.jester.dao.IPersister;

import ch.jester.orm.ORMPlugin;

public class GenericPersister<T extends IDAO> implements IPersister<T> {
	EntityManagerFactory mFactory;
	EntityManager mManager;
	
	private void fireEvent(Object pLoad){
		PersistencyEventQueue.getInstance().dispatch(new PersistencyEvent(this, pLoad));
	}
	
	private void check(){
		if(mFactory==null){
			mFactory = ORMPlugin.getJPAEntitManagerFactor();
		}
		if(mManager==null){
			mManager = mFactory.createEntityManager();
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
			mManager.persist(p);
		}
		trx.commit();
		fireEvent(pTCollection);
	
	}

	protected Class<?> getTargetClass(Object o){
		if(o instanceof Collection){
			return ((Collection)o).iterator().next().getClass();
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
		fireEvent(pT);
	
	}

	@Override
	public void delete(T pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		IDAO p = mManager.find(pT.getClass(), pT.getId());
		mManager.remove(p);
		trx.commit();
		fireEvent(pT);
	}
	
	@Override
	public void delete(Collection<T> pTCollection) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		for(T pT:pTCollection){
			IDAO p = mManager.find(pT.getClass(), pT.getId());
			mManager.remove(p);
			
		}
		trx.commit();
		fireEvent(pTCollection);
	}

	@Override
	public void close() {
		mManager.close();
		mFactory.close();
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

	public List<T> findByParameter(String queryName, String pPara, Object pVal){
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		@SuppressWarnings("unchecked")
		List<T> result = mManager.createNamedQuery(queryName).setParameter(pPara, pVal).getResultList();
		trx.commit();
		return result;
	}

}
