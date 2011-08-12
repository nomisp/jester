package ch.jester.db.persister.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import ch.jester.commonservices.api.persistency.IPrivateContextDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IQueueNotifier;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.orm.ORMPlugin;

/**
 * Adapter für einen {@link GenericPersister} um diesen auf einen privaten EntityManager umzuleiten
 *
 * @param <T>
 */
public class PrivateContextDaoServiceAdapter<T extends IEntityObject> implements IPrivateContextDaoService<T>{
	private GenericPersister<T> service;
	private Collection<T> mLoad = new ArrayList<T>();
	private EntityManager privateManager;
	private EntityTransaction trx;
	public PrivateContextDaoServiceAdapter(GenericPersister<T> service) {
		this.service=service;
		this.service.setManager(privateManager = ORMPlugin.getJPAEntityManagerFactory().createEntityManager());
		trx = privateManager.getTransaction();
		trx.begin();
		this.service.getNotifier().manualEventQueueNotification(true);
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
		//service.save(pTCollection);
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
	public void delete(Object o) {
		service.getManager().remove(o);
		
	}
	
	@Override
	public void delete(Collection<T> pTCollection) {
		mLoad.addAll(pTCollection);
		service.delete(pTCollection);
	}

	@Override
	public void close() {
		if(trx.isActive()){
			rollback(null);
		}
		service.getManager().clear();
		service.getManager().close();
		service.getNotifier().manualEventQueueNotification(false);
		service.setManager(ORMPlugin.getJPAEntityManager());
		//service.close();
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
	public void commit() {
		trx.commit();
		service.getManager().clear();
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
		for(T pT:mLoad){
			//T tt = service.getManager().find(service.getDaoClass(), pT.getId());
			T t0 =manager.merge(pT);
			manager.refresh(t0);
		}
		if(commit){
			trx.commit();
		}
		}
		
		
		this.service.getNotifier().notifyEventQueue();
		this.mLoad.clear();
		privateManager.clear();
		trx = privateManager.getTransaction();
		trx.begin();
	}

	@Override
	public T find(Integer id) {
		T t =  service.find(id);
		return t;
	}

	@Override
	public IQueueNotifier getNotifier() {
		return service.getNotifier();
	}

	@Override
	public void rollback(T t) {
		trx.rollback();
		privateManager.clear();
		
		this.mLoad.clear();
	}

	@Override
	public IPrivateContextDaoService<T> privateContext() throws ProcessingException{
		throw new ProcessingException("Invalid Call");
	}



}
