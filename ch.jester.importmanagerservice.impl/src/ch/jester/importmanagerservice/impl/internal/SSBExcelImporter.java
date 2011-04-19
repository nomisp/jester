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
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.model.factories.PlayerFactory;

public class SSBExcelImporter implements IImportHandler{
	private int workUnits = 10000000;
	private ServiceUtility su = new ServiceUtility();

	@Override
	public Object handleImport(InputStream pInputStream, IProgressMonitor pMonitor) {
		try {
			pMonitor.beginTask("Processing zip file", workUnits);
			Workbook wb = WorkbookFactory.create(pInputStream);
			
			Sheet sheet = wb.getSheetAt(0);
			int worked = workUnits / sheet.getLastRowNum();
			Row headerRow = sheet.getRow(0);
			String[] header = convert(headerRow, -1);
			pMonitor.worked(worked);
			out(header);
			List<Player> newPlayers = new ArrayList<Player>();
			for(int i=1;i<sheet.getLastRowNum();i++){
				Row row = sheet.getRow(i);
				if(row==null){continue;}
				String[] details;
				out(details = convert(sheet.getRow(i), header.length));
				Properties playerProperties = createProperties(header, details);
				Player player = createPlayer(playerProperties);
				if(player!=null){
					newPlayers.add(createPlayer(playerProperties));
				}
				pMonitor.worked(worked);
			}
			IPlayerPersister playerpersister = su.getExclusiveService(IPlayerPersister.class);
			playerpersister.save(newPlayers);
			playerpersister.close();
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

	private Player createPlayer(Properties playerProperties) {
		String name = playerProperties.getProperty("Name");
		String firstName = playerProperties.getProperty("Vorname");
		if(name.equals("")&&firstName.equals("")){
			return null;
		}
		Player player = PlayerFactory.createPlayer();
		player.setLastName(playerProperties.getProperty("Name"));
		player.setFirstName(playerProperties.getProperty("Vorname"));
		String fidecode = playerProperties.getProperty("CodeFIDE");
		if(fidecode.equals("")){
			fidecode="0";
		}
		player.setFideCode(Integer.parseInt(fidecode));
		String elo = playerProperties.getProperty("Elo neu");
		if(elo.equals("")){
			elo="0";
		}
		player.setElo(Integer.parseInt(elo));
		player.setNation("Schweiz");
		return player;
	}

	private Properties createProperties(String[] header, String[] details) {
		Properties p = new Properties();
		for(int i=0;i<header.length;i++){
			p.put(header[i], details[i]);
		}
		return p;
	}

	private void out(String[] string) {
		for(String s:string){
			System.out.print(s);
			System.out.print("  -  ");
		}
		System.out.println();
	}

	public String[] convert(Row pRow, int headerLength){

		List<String> list = new ArrayList<String>(); 
		if(headerLength==-1){
			headerLength=0;
			Iterator<Cell> cellIterator = pRow.cellIterator();
			while(cellIterator.hasNext()){
				cellIterator.next();
				headerLength++;
			}
		}
		
		/*Iterator<Cell> cellIterator = pRow.cellIterator();
		while(cellIterator.hasNext()){*/
		for(int i=0;i<headerLength;i++){
			//Cell cell = cellIterator.next();
			Cell cell = pRow.getCell(i);
			int cellType = Cell.CELL_TYPE_BLANK;
			if(cell!=null){
				cellType = cell.getCellType();
			}
			String value = null;
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
					System.out.println("CellType: "+cell.getCellType());
					
					
			}
			if(value == null){
				System.err.println("Value not parsed");
			}else{
				list.add(value);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	
}
