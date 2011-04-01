package ch.jester.db.persister.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.orm.ORMPlugin;

public class DBPlayerPersister implements IPlayerPersister {
	EntityManagerFactory mFactory;
	EntityManager mManager;
	public DBPlayerPersister(){
		
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
	public void save(Player pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		mManager.persist(pT);
		trx.commit();
		
	}

	@Override
	public void delete(Player pT) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		Player p = mManager.find(Player.class, pT.getId());
		mManager.remove(p);
		trx.commit();
		
	}
	@Override
	public void close() {
		mManager.close();
		mFactory.close();
		
	}
	@Override
	public void save(Collection<Player> pTCollection) {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		for(Player p:pTCollection){
			mManager.persist(p);
			//mManager.flush();
		}
		trx.commit();
	}
	@Override
	public List<Player> getAll() {
		check();
		EntityTransaction trx = mManager.getTransaction();
		trx.begin();
		@SuppressWarnings("unchecked")
		List<Player> result = mManager.createNamedQuery("getAllPlayers").getResultList();
		trx.commit();
		return result;
	}

}
