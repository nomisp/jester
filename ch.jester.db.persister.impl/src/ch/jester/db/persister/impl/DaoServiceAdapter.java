package ch.jester.db.persister.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import ch.jester.commonservices.api.persistency.IDaoServicePrivateContextAdapter;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.orm.ORMPlugin;

public class DaoServiceAdapter<T extends IEntityObject> implements IDaoServicePrivateContextAdapter<T>{
	private GenericPersister<T> service;
	private Collection<T> mLoad = new ArrayList<T>();
	public DaoServiceAdapter(GenericPersister<T> service) {
		this.service=service;
		this.service.setManager(ORMPlugin.getJPAEntityManagerFactory().createEntityManager());
	}
	
	@Override
	public Class<T> getDaoClass() {
		return service.getDaoClass();
	}

	@Override
	public void saveBatch(Collection<T> pTCollection) {
		mLoad.addAll(pTCollection);
		service.saveBatch(pTCollection);
	}

	@Override
	public void save(Collection<T> pTCollection) {
		mLoad.addAll(pTCollection);
		service.save(pTCollection);
	}

	@Override
	public void save(T pT) {
		mLoad.add(pT);
		service.save(pT);
	}

	@Override
	public void delete(T pT) {
		service.delete(pT);
	}

	@Override
	public void delete(Collection<T> pTCollection) {
		mLoad.addAll(pTCollection);
		service.delete(pTCollection);
	}

	@Override
	public void close() {
		service.getManager().clear();
		service.getManager().close();
		service.close();
	}

	@Override
	public List<T> getAll() {
		return service.getAll();
	}

	@Override
	public int count() {
		return service.count();
	}

	@Override
	public List<T> getFromTo(int from, int to) {
		return service.getFromTo(from, to);
	}

	@Override
	public List<T> executeNamedQuery(String namedQuery) {
		return service.executeNamedQuery(namedQuery);
	}

	@Override
	public List<T> executeNamedQuery(String queryName, String pPara, Object pVal) {
		return service.executeNamedQuery(queryName, pPara, pVal);
	}

	@Override
	public Query createQuery(String query) {
		return service.createQuery(query);
	}

	@Override
	public Query createNamedQuery(String query) {
		return service.createNamedQuery(query);
	}

	@Override
	public void manualEventQueueNotification(boolean pTrue) {
		service.manualEventQueueNotification(pTrue);
	}

	@Override
	public void notifyEventQueue() {
		service.notifyEventQueue();
	}

	@Override
	public void clearEventQueueCache() {
		service.clearEventQueueCache();
	}

	@Override
	public void commit() {
		EntityManager manager = ORMPlugin.getJPAEntityManager();
		synchronized(manager){
		EntityTransaction trx = manager.getTransaction();
		boolean commit = true;
		if(trx.isActive()){
			manager.joinTransaction();
			commit = false;
		}else{
			trx.begin();
		}
		for(T pT:mLoad){   // soweit gekommen? alles ok
			T p = manager.merge(pT);  //mergen mit dem eigentlichen manager
			System.out.println(p);

			
			//System.out.println(p);
		}
		if(commit){
			trx.commit();
		}
		}
	}

	@Override
	public T find(Integer id) {
		return service.find(id);
	}

}
