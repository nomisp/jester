package ch.jester.commonservices.api.persistency;

public interface IDaoServicePrivateContextAdapter<T extends IEntityObject> extends IDaoService<T> {

	public void commit();
	
}
