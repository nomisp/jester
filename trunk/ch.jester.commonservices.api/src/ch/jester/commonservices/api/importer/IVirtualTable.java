package ch.jester.commonservices.api.importer;

import java.util.List;


/**
 * Definiert eine Tabelle
 *
 * @param <T>
 */
public interface IVirtualTable<T> {
	/**
	 * Definiert eine Zelle
	 *
	 */
	public interface IVirtualCell{
		/**
		 * @return den Name der Zelle
		 */
		public String getName();
		/**
		 * Setzen eine Delimiters, für überlappende -nicht eindeutig identifizierbare Zellen.
		 * @param  pDelim
		 */
		public void setDelimiter(String pDelim);
		/**
		 * @return den Delimiter oder null
		 */
		public String getDelimiter();
		/**
		 * @return
		 */
		public int getDelimiterSequence();
		/**
		 * Die Sequenz
		 * @param  i
		 */
		public void setDelimiterSequence(int i);
		/**
		 * Erstellen des Contents der Zelle
		 * @param detailList
		 * @param pInput
		 */
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
	
	/**Können Zellen hinzugefügt werden?
	 * @return
	 */
	public boolean canAddCells();
	
	/**
	 * Zellen löschen
	 */
	public void clearCells();
	
	/**
	 * Zelle hinzufügen
	 * @param cell
	 */
	public void addCell(IVirtualCell cell);

	/**
	 * Alle Zellen zurückgeben
	 * @return
	 */
	public List<IVirtualCell> getCells();
	
	/**
	 * Gibt die übergebene Anzahl Reihen zurück
	 * @param pCount
	 * @return
	 */
	public String[] getRowInput(int pCount);
}
