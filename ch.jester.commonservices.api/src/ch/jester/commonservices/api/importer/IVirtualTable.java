package ch.jester.commonservices.api.importer;

import java.util.List;


public interface IVirtualTable<T> {
	
	public interface IVirtualCell{
		public String getName();
		public void setDelimiter(String pDelim);
		public String getDelimiter();
		public int getDelimiterSequence();
		public void setDelimiterSequence(int i);
		public void createCellContent(List<String> detailList, String pInput);
		public void setName(String text);
		public int getStart();
		public void setStart(int i);
		public int getStop();
		public void setStop(int i);
	}
	/**Column Text
	 * @return
	 */
	public String[] getHeaderEntries();
	
	/**
	 * Die ganze Row
	 * @param i
	 * @return
	 */
	public T getRow(int i);
	
	/**Total Rows
	 * @return
	 */
	public int getTotalRows();
	/**
	 * Parsen der Row
	 * @param pRow
	 * @param pLenght
	 * @return
	 */
	public String[] processRow(T pRow, int pLenght);
	
	public boolean canAddCells();
	
	public void clearCells();
	
	public void addCell(IVirtualCell cell);

	public List<IVirtualCell> getCells();
	
	public String[] getDynamicInput(int pCount);
}
