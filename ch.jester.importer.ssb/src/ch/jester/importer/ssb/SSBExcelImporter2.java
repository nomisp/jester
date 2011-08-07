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
	private IDaoService<Club> mClubDao;
	private List<Club> clubs;
	public SSBExcelImporter2(){
		init_linking();
		mClubDao = getServiceUtility().getDaoServiceByEntity(Club.class);
		mClubDao.getNotifier().manualEventQueueNotification(true);
	}
	public void init_linking(){
		mInputLinking.put("lastName", "Name");
		mInputLinking.put("firstName", "Vorname");
		mInputLinking.put("fideCode", "CodeFIDE");
		mInputLinking.put("nationalCode", "Code");
		mInputLinking.put("nationalElo", "Elo neu");
		mInputLinking.put("club", "Klub");
		mInputLinking.put("estimatedElo","ELO provisorisch");

	}
	@Override
	protected void initialize() {
		 clubs = new ArrayList<Club>();
		 clubs.addAll(mClubDao.getAll());
	}
	@Override
	protected void finished() {
		super.finished();
		mClubDao.getNotifier().clearEventQueueCache();
		clubs = null;
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
		if(isTestRun()){return;}
		
		
		//SSB = Schweiz
		if(vnew.getNation()==null||vnew.getNation().isEmpty()){
			vnew.setNation(CODED_NATIONALITY);
		}
		
		//Club
		String clubInputId = super.mInputLinking.get("club");
		String clubName = domainProperties.getProperty(clubInputId).trim();
		if(clubName == null || clubName.isEmpty()){return;}

		Integer sektion = null;
		if(domainProperties.get("Sektion")!=null){
			try{
			sektion = Integer.parseInt(domainProperties.getProperty("Sektion"));
			}catch(Exception e){							
			}
		}
		Club club = getClub(clubName, sektion);
		vnew.addClub(club);
		
	}
	

	
	private Club getClub(String clubName, Integer sekId) {
		Club foundClub = null;
		for(Club c:clubs){
			if(c.getName().equals(clubName)){
				foundClub = c;
				break;
			}
		}
		if(foundClub==null){
			Club newClub =  ModelFactory.getInstance().createClub(clubName);
			if(sekId!=null){
				newClub.setCode(sekId);
			}
			foundClub =  newClub;
			clubs.add(foundClub);
		}
		return foundClub;
	}


}
