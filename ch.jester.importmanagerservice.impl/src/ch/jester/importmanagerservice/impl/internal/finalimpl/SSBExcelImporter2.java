package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.importmanagerservice.impl.abstracts.ExcelSheetTableProvider;
import ch.jester.model.Player;

public class SSBExcelImporter2 extends AbstractPlayerImporter<Row>{
	public SSBExcelImporter2(){
		init_Linking();
	}
	
	
	private void init_Linking(){
		mInputLinking.put("lastName", "Name");
		mInputLinking.put("firstName", "Vorname");
		mInputLinking.put("fideCode", "CodeFIDE");
		mInputLinking.put("nationalCode", "Code");
		mInputLinking.put("elo", "Elo neu");
	}
	
	@Override
	protected boolean addToCollection(Player v) {
		if(v.getLastName().equals("")&&v.getFirstName().equals("")){
			return false;
		}
		return true;
	}
	
	
	@Override
	public IVirtualTable<Row> initialize(InputStream pInputStream) {
		ExcelSheetTableProvider provider = new ExcelSheetTableProvider();
		try {
			provider.setSheet(WorkbookFactory.create(pInputStream).getSheetAt(0));
			return provider;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}



	@Override
	public Query createDuplicationCheckingQuery(
			IDaoService<? extends IEntityObject> pDaoService) {
		return pDaoService.createQuery("SELECT player FROM Player player WHERE player.nationalCode in (:nationalCode)");
	}


	@Override
	public List<?> getDuplicationKeys(List<Player> pList) {
		List<Integer> nationcode = new ArrayList<Integer>();
		for(Player p:pList){
			nationcode.add(p.getNationalCode());
		}
		return nationcode;
	}


	@Override
	public String getParameter() {
		return "nationalCode";
	}


	@Override
	public void handleDuplicates(
			IDaoService<? extends IEntityObject> pDaoService, List<Player> pList) {
		// TODO Auto-generated method stub
		
	}


}