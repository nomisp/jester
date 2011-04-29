package ch.jester.importmanagerservice.impl.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.importer.IImportHandler;

public abstract class AbstractExcelSheetImporter <T> implements IImportHandler{
	private Sheet mSheet;
	private int workUnits = 10000000;
	private int singleUnitOfWork = -1;
	protected void setSheet(Sheet pSheet) {
		mSheet=pSheet;	
	}
	protected Sheet getSheet(){
		return mSheet;
	}
	
	protected Row getHeader(){
		return getSheet().getRow(0);
	}
	protected String[] getHeaderEntries(){
		Row headerRow = getSheet().getRow(0);
		return convert(headerRow, -1);
	}
	
	/** berechnet getTotalUnitsOfWork() / getSheet().getLastRowNum()
	 * @return
	 */
	protected int getSingleUnitOfWork(){
		if(singleUnitOfWork==-1){
			singleUnitOfWork = workUnits / getSheet().getLastRowNum();
		}
		return singleUnitOfWork;
	}
	/**Eine Default Einstellung von 10000000
	 * @return
	 */
	protected int getTotalUnitsOfWork(){
		return workUnits;
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
	
	/**Erzeugt aus einem Header (Row Einträge) und einer Detail Row ein Property Object
	 * @param header
	 * @param details
	 * @return
	 */
	protected Properties createProperties(String[] header, String[] details) {
		Properties p = new Properties();
		for(int i=0;i<header.length;i++){
			p.put(header[i], details[i]);
		}
		return p;
	}
	
	/**
	 * Print auf Console
	 * @param string
	 */
	protected void console(String[] string) {
		for(String s:string){
			System.out.print(s);
			System.out.print("  -  ");
		}
		System.out.println();
	}
	@Override
	public Object handleImport(InputStream pInputStream, IProgressMonitor pMonitor) {
		try {
			pMonitor.beginTask("Processing Input", getTotalUnitsOfWork());
			Workbook wb = WorkbookFactory.create(pInputStream);
			setSheet(wb.getSheetAt(0));
			
			String[] header = getHeaderEntries();
			console(header);
			pMonitor.worked(getSingleUnitOfWork());

			List<T> domainObjects = new ArrayList<T>();
			for(int i=1;i<getSheet().getLastRowNum();i++){
				Row row = getSheet().getRow(i);
				if(row==null){continue;}
				String[] details;
				console(details = convert(getSheet().getRow(i), header.length));
				Properties domainProperties = createProperties(header, details);
				T t = createDomainObject(domainProperties);
				if(t!=null){
					domainObjects.add(createDomainObject(domainProperties));
				}
				pMonitor.worked(getSingleUnitOfWork());
			}
			persist(domainObjects);
			pMonitor.done();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Speichert die Liste der Domain Objekte
	 * @param pDomainObjects
	 */
	protected abstract void persist(List<T> pDomainObjects);
	
	/**erzeugt die einzelnen DomainObjekte aus den Properties
	 * @param pProperties
	 * @return
	 */
	protected abstract T createDomainObject(Properties pProperties);
}
