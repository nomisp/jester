package ch.jester.commonservices.api.importer;

import java.util.List;


public interface IVirtualTable<T> {
	
	/**
	 * @author   t117221
	 */
	public interface IVirtualCell{
		/**
		 * @return
		 * @uml.property  name="name"
		 */
		public String getName();
		/**
		 * @param  pDelim
		 * @uml.property  name="delimiter"
		 */
		public void setDelimiter(String pDelim);
		/**
		 * @return
		 * @uml.property  name="delimiter"
		 */
		public String getDelimiter();
		/**
		 * @return
		 * @uml.property  name="delimiterSequence"
		 */
		public int getDelimiterSequence();
		/**
		 * @param  i
		 * @uml.property  name="delimiterSequence"
		 */
		public void setDelimiterSequence(int i);
		public void createCellContent(List<String> detailList, String pInput);
		/**
		 * @param  text
		 * @uml.property  name="name"
		 */
		public void setName(String text);
		/**
		 * @return
		 * @uml.property  name="start"
		 */
		public int getStart();
		/**
		 * @param  i
		 * @uml.property  name="start"
		 */
		public void setStart(int i);
		/**
		 * @return
		 * @uml.property  name="stop"
		 */
		public int getStop();
		/**
		 * @param  i
		 * @uml.property  name="stop"
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
