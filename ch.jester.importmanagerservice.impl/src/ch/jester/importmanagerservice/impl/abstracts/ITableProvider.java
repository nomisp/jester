package ch.jester.importmanagerservice.impl.abstracts;


public interface ITableProvider<T> {
	public String[] getHeaderEntries();
	public T getRow(int i);
	public int getTotalRows();
	public String[] processRow(T pRow, int pLenght);
}
