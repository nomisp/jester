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
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.importmanagerservice.impl.abstracts.ExcelSheetTableProvider;
import ch.jester.model.Player;

public class SSBExcelImporter2 extends AbstractPlayerImporter<Row>{
	public SSBExcelImporter2(){
		System.out.println("-----------------ssbimporter2");
		init_linking();
	}
	
	
	 void init_linking(){
		mInputLinking.put("lastName", "Name");
		mInputLinking.put("firstName", "Vorname");
		mInputLinking.put("fideCode", "CodeFIDE");
		mInputLinking.put("nationalCode", "Code");
		mInputLinking.put("nationalElo", "Elo neu");
	}
	
	@Override
	protected boolean addToCollection(Player v) {
		if(v.getLastName().equals("")&&v.getFirstName().equals("")){
			return false;
		}
		return true;
	}
	
	
	@Override
	public IVirtualTable<Row> initialize(InputStream pInputStream) throws ProcessingException {
		ExcelSheetTableProvider provider = new ExcelSheetTableProvider();
		try {
			provider.setSheet(WorkbookFactory.create(pInputStream).getSheetAt(0));
			return provider;
		} catch (InvalidFormatException e) {
			throw new ProcessingException(e);
		} catch (IOException e) {
			throw new ProcessingException(e);
		}
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
