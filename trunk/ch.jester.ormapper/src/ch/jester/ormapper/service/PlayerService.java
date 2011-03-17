package ch.jester.ormapper.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import ch.jester.common.utility.ThreadContext;
import ch.jester.model.Player;

public class PlayerService {
	EntityManagerFactory factory;
	EntityManager em;
	ThreadContext tc = new ThreadContext();
	public PlayerService(ClassLoader clz){
		tc.set(clz);
		factory = Persistence.createEntityManagerFactory("jester");
		em = factory.createEntityManager();
		tc.restore();
	}
	public void create(Player player){
		tc.set(this.getClass().getClassLoader());
		EntityTransaction trx = em.getTransaction();
		trx.begin();
		em.persist(player);
		trx.commit();
		tc.restore();
		
		
	}
}
