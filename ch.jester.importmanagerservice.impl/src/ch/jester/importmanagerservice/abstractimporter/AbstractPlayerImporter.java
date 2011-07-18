package ch.jester.importmanagerservice.abstractimporter;

import java.util.List;

import javax.persistence.Query;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.importer.AbstractTableImporter;
import ch.jester.common.utility.SubListIterator;
import ch.jester.commonservices.api.importer.IDuplicateChecker;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

/**
 */
public abstract class AbstractPlayerImporter<T> extends AbstractTableImporter<T, Player> implements IDuplicateChecker<Player> {
	@Override
	public void resetInputLinking() {
		mInputLinking.clear();
		init_linking();
		
	}
	public abstract void init_linking();
	
	@Override
	public String[] getDomainObjectAttributes() {
		return new String[]{"lastName","firstName","fideCode","nationalCode","elo","nationalElo","age","city","nation"};
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void persist(List<Player> pDomainObjects, IProgressMonitor pMonitor) {
		int chunkSize = 10000;
		SubListIterator<Player> iterator = new SubListIterator<Player>(pDomainObjects, chunkSize);
		
		IDaoService<Player> checker = getServiceUtility().getDaoServiceByEntity(Player.class);
		checker.manualEventQueueNotification(true);
		boolean checkDoubleEntries = checkDuplicates(checker);
		Query duplQuery = createDuplicationCheckingQuery(checker);
		
		int chunkCount = 0;
		while(iterator.hasNext()){
			List<Player> origList = iterator.next();
			List<Player> duplicates=null;
			if(checkDoubleEntries){
				pMonitor.subTask("Checking Duplicates");
				List<?> codes = getDuplicationKeys(origList);
				duplicates = duplQuery.setParameter(getParameter(), codes).getResultList();
				getLogger().info("Duplicates found >>>>"+duplicates.size());
				
			}
			pMonitor.subTask("Saving Players: "+(chunkSize*chunkCount)+" - "+(chunkSize*(chunkCount+1)));
			
			if(duplicates!=null){
				origList.removeAll(duplicates);
			}
			
			if(!origList.isEmpty()){
				checker.saveBatch(origList);
			}
			if(checkDoubleEntries){
				handleDuplicates(checker, duplicates);
			}
			

			//playerpersister.close();
			chunkCount++;
		}
		checker.close();
		
	}

	@Override
	public boolean checkDuplicates(
			IDaoService<? extends IEntityObject> pDaoService) {
		return pDaoService.count()>0;
	}
	@Override
	protected Player createNewDomainObject() {
		return ModelFactory.getInstance().createPlayer();
	}


}
