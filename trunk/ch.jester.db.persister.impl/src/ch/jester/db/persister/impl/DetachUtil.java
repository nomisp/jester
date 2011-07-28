package ch.jester.db.persister.impl;

import javax.persistence.EntityManager;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.Tournament;
import ch.jester.orm.ORMPlugin;

/**
 * 
 * 
		Tournament detached = privateService.getDetachedInstance(mDaoInput.getInput(), "select t from Tournament t join fetch t.categories where t.id="+mDaoInput.getInput().getId()+
				"");
 * @author matthias
 *
 */

public class DetachUtil {
	public static <T extends IEntityObject> T getDetachedInstance (T t, Class<T> clz){
		EntityManager manager =ORMPlugin.getJPAEntityManagerFactory().createEntityManager();
		manager.getTransaction().begin();
		T find = manager.find(clz, t.getId());
		//manager.merge(find);
		manager.getTransaction().commit();
		
		manager.detach(find);
		manager.close();
		return find;
	}
}
