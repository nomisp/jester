package ch.jester.commonservices.api.persistency;

public interface IPrivateContextDaoService<T extends IEntityObject> extends IDaoService<T> {

	public void commit();

	public void delete(Object o);
	
	public void rollback(T t);
	
}
