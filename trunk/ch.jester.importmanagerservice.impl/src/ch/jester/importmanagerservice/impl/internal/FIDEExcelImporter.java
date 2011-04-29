package ch.jester.importmanagerservice.impl.internal;

import java.util.List;
import java.util.Properties;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;

public class FIDEExcelImporter extends AbstractExcelSheetImporter<Player>{
	private ServiceUtility su = new ServiceUtility();
	@Override
	protected void persist(List<Player> pDomainObjects) {
		IPlayerPersister playerpersister = su.getExclusiveService(IPlayerPersister.class);
		playerpersister.save(pDomainObjects);
		playerpersister.close();
	}

	@Override
	protected Player createDomainObject(Properties pProperties) {
		// TODO Auto-generated method stub
		return null;
	}

}
