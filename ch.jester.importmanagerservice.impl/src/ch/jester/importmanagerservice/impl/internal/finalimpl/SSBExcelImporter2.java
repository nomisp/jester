package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ch.jester.common.importer.AbstractTableImporter;
import ch.jester.common.utility.SubListIterator;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.importmanagerservice.impl.abstracts.ExcelSheetTableProvider;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public class SSBExcelImporter2 extends AbstractTableImporter<Row, Player>{
	private ServiceUtility su = new ServiceUtility();

	
	public SSBExcelImporter2(){
		init_Linking();
	}
	

	@Override
	public String[] getDomainObjectAttributes() {
		return new String[]{"lastName","firstName","fideCode","elo","age","city","nation"};
	}
	
	private void init_Linking(){
		mInputLinking.put("lastName", "Name");
		mInputLinking.put("firstName", "Vorname");
		mInputLinking.put("fideCode", "CodeFIDE");
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
	protected void persist(List<Player> pDomainObjects) {
		SubListIterator<Player> iterator = new SubListIterator<Player>(pDomainObjects, 10000);
		
		IDaoService<Player> checker = su.getDaoService(Player.class);
		boolean checkDoubleEntries = checker.count()>0;
		Query fideQuery = checker.createQuery("SELECT player FROM Player player WHERE player.fideCode in (:fideCode)");
		
		
		while(iterator.hasNext()){
			List<Player> sublist = iterator.next();
			IDaoService<Player> playerpersister = su.getDaoService(Player.class);
			
			if(checkDoubleEntries){
				List<Integer> fideCodes = createFideList(sublist);
				List<Player> duplicates = fideQuery.setParameter("fideCode", fideCodes).getResultList();
				System.out.println("Duplicates found >>>>"+duplicates.size());
				
			}
			playerpersister.save(sublist);
			playerpersister.close();
		}
		checker.close();
		
	}
	
	private List<Integer> createFideList(List<Player> sublist) {
		List<Integer> fide = new ArrayList<Integer>();
		for(Player p:sublist){
			fide.add(p.getFideCode());
		}
		return fide;
	}
	

	@Override
	protected Player createNewDomainObject() {
		return ModelFactory.getInstance().createPlayer();
	}

	@Override
	public IVirtualTable<Row> initialize(InputStream pInputStream) {
		ExcelSheetTableProvider provider = new ExcelSheetTableProvider();
		try {
			provider.setSheet(WorkbookFactory.create(pInputStream).getSheetAt(0));
			return provider;
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}




	/*@Override
	protected Player createDomainObject(Properties playerProperties) {
		return createDomainObject2(playerProperties);
		
		String name = playerProperties.getProperty("Name");
		String firstName = playerProperties.getProperty("Vorname");
		if(name.equals("")&&firstName.equals("")){
			return null;
		}
		Player player = ModelFactory.getInstance().createPlayer();
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
	}*/




}
