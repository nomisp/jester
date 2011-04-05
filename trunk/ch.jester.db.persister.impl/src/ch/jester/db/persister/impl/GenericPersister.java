package ch.jester.db.persister.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import ch.jester.dao.IDAO;
import ch.jester.dao.IPersister;
import ch.jester.orm.ORMPlugin;

public class GenericPersister<T extends IDAO> implements IPersister<T> {
	EntityManagerFactory mFactory;
	EntityManager mManager;
	
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
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		for(T p:pTCollection){
			mManager.persist(p);
		}
		trx.commit();
	}

	@Override
	public void save(T pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		mManager.persist(pT);
		trx.commit();
	}

	@Override
	public void delete(T pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		IDAO p = mManager.find(pT.getClass(), pT.getId());
		mManager.remove(p);
		trx.commit();
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



}
