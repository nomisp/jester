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
		 * getter für die definierte Delim Sequenz.
		 * @return int Delim Seq.
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
		 * getter für name
		 * @param  text
		 */
		public void setName(String text);
		/**
		 * getter für startposition
		 * @return die startposition
		 */
		public int getStart();
		/**
		 * setter für die startposition
		 * @param  die startposition.
		 */
		public void setStart(int i);
		/**
		 * getter für die stopposition
		 * @return die stopposition
		 */
		public int getStop();
		/**
		 * 
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
	 * @return die Headers
	 */
	public T getRow(int i);
	
	/**Total Rows
	 * @return die Row
	 */
	public int getTotalRows();
	/**
	 * Parsen der Row
	 * @param pRow
	 * @param pLenght
	 * @return Anzahl aller Rows
	 */
	public String[] processRow(T pRow, int pLenght);
	
	/**Können Zellen hinzugefügt werden?
	 * @return true oder false
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
	 * @return die ZeilenInputs
	 */
	public String[] getRowInput(int pCount);
}
