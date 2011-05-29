package ch.jester.importmanagerservice.impl.abstracts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import ch.jester.commonservices.api.importer.IVirtualTable;

public class ExcelSheetTableProvider implements IVirtualTable<Row>{
	private Sheet mSheet;
	public void setSheet(Sheet pSheet) {
		mSheet=pSheet;	
	}
	public Sheet getSheet(){
		return mSheet;
	}
	
	@Override
	public String[] getHeaderEntries() {
		Row headerRow = getSheet().getRow(0);
		return convert(headerRow, -1);
	}

	@Override
	public int getTotalRows() {
		return getSheet().getLastRowNum();
	}

	@Override
	public String[] processRow(Row pRow, int pLength) {
		return convert(pRow, pLength);
	}
	private String[] convert(Row pRow, int headerLength){

		List<String> list = new ArrayList<String>(); 
		if(headerLength==-1){
			headerLength=0;
			Iterator<Cell> cellIterator = pRow.cellIterator();
			while(cellIterator.hasNext()){
				cellIterator.next();
				headerLength++;
			}
		}

		for(int i=0;i<headerLength;i++){
			Cell cell = pRow.getCell(i);
			int cellType = Cell.CELL_TYPE_BLANK;
			if(cell!=null){
				cellType = cell.getCellType();
			}
			String value = null;
			value = convertCellValue(cell, cellType, value);
			if(value == null){
				System.err.println("Value not parsed");
			}else{
				list.add(value);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	/**
	 * Holt mit der korrekten Methode den Wert aus der Cell.<br>
	 * Handelt Numeric, String und Blank ab.
	 * @param cell
	 * @param cellType
	 * @param value
	 * @return den String des CellValues
	 */
	protected String convertCellValue(Cell cell, int cellType, String value) {
		switch(cellType){
			case Cell.CELL_TYPE_NUMERIC:
				value = Integer.toString((int)cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue().toString();
				break;
			case Cell.CELL_TYPE_BLANK:
				value = "";
				break;
			default:
				
		}
		return value;
	}
	@Override
	public Row getRow(int i) {
		return getSheet().getRow(i);
	}
	@Override
	public boolean canAddCells() {
		return false;
	}
	@Override
	public String[] getDynamicInput(int pCount) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void clearCells() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addCell(
			ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell cell) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell> getCells() {
		// TODO Auto-generated method stub
		return null;
	}
}
