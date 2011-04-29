package ch.jester.importmanagerservice.impl.internal;

import java.util.List;
import java.util.Properties;

import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public class SSBExcelImporter extends AbstractExcelSheetImporter<Player> implements IImportHandler{
	
	private ServiceUtility su = new ServiceUtility();

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
	protected void persist(List<Player> pDomainObjects) {
		IPlayerPersister playerpersister = su.getExclusiveService(IPlayerPersister.class);
		playerpersister.save(pDomainObjects);
		playerpersister.close();
	}



	@Override
	protected Player createDomainObject(Properties pProperties) {
		return createPlayer(pProperties);
	}





	
	
	
}
