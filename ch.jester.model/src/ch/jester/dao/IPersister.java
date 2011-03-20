package ch.jester.dao;

import java.util.Collection;

public interface IPersister<T> {
	public void save(Collection<T> pTCollection);
	public void save(T pT);
	public void delete(T pT);
	public void close();
}
