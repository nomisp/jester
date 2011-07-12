package ch.jester.commonservices.api.importer;

import java.util.List;


public interface IVirtualTable<T> {
	public interface IVirtualCell{
		/**
		 * @return
		 */
		public String getName();
		/**
		 * @param  pDelim
		 */
		public void setDelimiter(String pDelim);
		/**
		 * @return
		 */
		public String getDelimiter();
		/**
		 * @return
		 */
		public int getDelimiterSequence();
		/**
		 * @param  i
		 */
		public void setDelimiterSequence(int i);
		public void createCellContent(List<String> detailList, String pInput);
		/**
		 * @param  text
		 */
		public void setName(String text);
		/**
		 * @return
		 */
		public int getStart();
		/**
		 * @param  i
		 */
		public void setStart(int i);
		/**
		 * @return
		 */
		public int getStop();
		/**
		 * @param  i
		 */
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
