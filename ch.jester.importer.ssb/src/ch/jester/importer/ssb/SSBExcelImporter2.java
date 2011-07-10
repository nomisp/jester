package ch.jester.importer.ssb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.Query;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.importmanagerservice.abstractimporter.AbstractPlayerImporter;
import ch.jester.importmanagerservice.tableprovider.ExcelSheetTableProvider;
import ch.jester.model.Club;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public class SSBExcelImporter2 extends AbstractPlayerImporter<Row>{
	private String CODED_NATIONALITY = "Schweiz";
	public SSBExcelImporter2(){
		init_linking();
	}
	public String[] getDomainObjectAttributes() {
		String[] origAtts = super.getDomainObjectAttributes();
		String[] newAtts = new String[origAtts.length+1];
		System.arraycopy(origAtts, 0, newAtts, 0, origAtts.length);
		newAtts[newAtts.length-1] = "club";
		return newAtts;
		//return new String[]{"lastName","firstName","fideCode","nationalCode","elo","nationalElo","age","city","nation"};
	}
	
	public void init_linking(){
		mInputLinking.put("lastName", "Name");
		mInputLinking.put("firstName", "Vorname");
		mInputLinking.put("fideCode", "CodeFIDE");
		mInputLinking.put("nationalCode", "Code");
		mInputLinking.put("nationalElo", "Elo neu");
		mInputLinking.put("club", "Klub");
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

		
	}
	@Override
	protected boolean doAutoMatching(Player pDomainObject, String pDomainProperty, String pInputProperty, Properties p) {
		if(pDomainProperty.equals("club")){
			return false;
		}
		return true;
	}
	@Override
	protected void doModifications(Player vnew, Properties domainProperties) {
		//SSB = Schweiz
		vnew.setNation(CODED_NATIONALITY);
		
		//Club
		String clubInputId = super.mInputLinking.get("club");
		String clubName = domainProperties.getProperty(clubInputId).trim();
		if(clubName == null || clubName.isEmpty()){return;}
		Club club = ModelFactory.getInstance().createClub(clubName);
		vnew.setClub(club);
		if(domainProperties.get("Sektion")!=null){
			try{
				int sektId = Integer.parseInt(domainProperties.getProperty("Sektion"));
				
				club.setCode(sektId);
			}catch(Exception e){
				club.setCode(0);
				e.printStackTrace();
				
			}
		}
		
	}
	
/*	@Override
	public void enrichDomainObject(Properties pProperties, Player pObject) {
		System.out.println(pObject);
		pObject.setNation(CODED_NATIONALITY);
		//pProperties.get("Klub")
		
		
	}*/


}
