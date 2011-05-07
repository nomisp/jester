package ch.jester.commonservices.api.persistency;

import java.util.Collection;



public interface IPersistencyEvent {
	public static enum Operation{
		SAVED, DELETED
	}
	public abstract Class<?> getLoadClass();

	public abstract Operation getCRUD();

	public abstract Object getSource();

	public abstract Collection<?> getLoad();

}