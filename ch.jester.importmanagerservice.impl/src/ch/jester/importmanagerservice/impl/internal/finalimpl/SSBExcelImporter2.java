package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.importmanagerservice.impl.abstracts.ExcelSheetTableProvider;
import ch.jester.importmanagerservice.impl.abstracts.ITableProvider;
import ch.jester.importmanagerservice.impl.abstracts.AbstractTableImporter;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public class SSBExcelImporter2 extends AbstractTableImporter<Row, Player>{
	private ServiceUtility su = new ServiceUtility();
	@Override
	protected void persist(List<Player> pDomainObjects) {
		
		//Collections.sort(pDomainObjects);
		
		IPlayerPersister playerpersister = su.getExclusiveService(IPlayerPersister.class);
		playerpersister.save(pDomainObjects);
		playerpersister.close();
	}
	@Override
	protected Player createDomainObject(Properties pProperties) {
		return createPlayer(pProperties);
	}

	private Player createPlayer(Properties playerProperties) {
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
	}
	
	@Override
	public ITableProvider<Row> initialize(InputStream pInputStream) {
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

}
