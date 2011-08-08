package ch.jester.importmanagerservice.abstractimporter;

import java.util.List;

import javax.persistence.Query;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.importer.AbstractTableImporter;
import ch.jester.common.utility.SubListIterator;
import ch.jester.commonservices.api.importer.IDuplicateChecker;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

/**
 * Abstracter Importer für Spieler
 *
 * @param <T>
 */
public abstract class AbstractPlayerImporter<T> extends AbstractTableImporter<T, Player> implements IDuplicateChecker<Player> {
	String[] exposedProperties = new String[]{"lastName","firstName","fideCode","nationalCode","elo","nationalElo","estimatedElo","age","city","nation","club"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
	String[] exposedLabels;
	/*
	 * 		mPropertyTranslator.create("club", "Club");
		mPropertyTranslator.create("estimatedElo", "Estimated Elo");
	 * 
	 */
	public AbstractPlayerImporter(){
		 exposedLabels= new String[]{
				 Messages.AbstractPlayerImporter_lastname,
				 Messages.AbstractPlayerImporter_firstname,
				 Messages.AbstractPlayerImporter_fidecode,
				 Messages.AbstractPlayerImporter_nationalcode,
				 Messages.AbstractPlayerImporter_elo,
				 Messages.AbstractPlayerImporter_nationalelo,
				 Messages.AbstractPlayerImporter_estimatedelo,
				 Messages.AbstractPlayerImporter_age,
				 Messages.AbstractPlayerImporter_city,
				 Messages.AbstractPlayerImporter_nation,
				 Messages.AbstractPlayerImporter_club};
		for(int i=0;i<exposedLabels.length;i++){
			mPropertyTranslator.create(exposedProperties[i], exposedLabels[i]);
		}
		
	}
	
	@Override
	public void resetInputMatching() {
		mInputLinking.clear();
		init_linking();
		
	}
	public abstract void init_linking();
	
	@Override
	public String[] getDomainObjectProperties() {
		return exposedProperties;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void persist(List<Player> pDomainObjects, IProgressMonitor pMonitor) {
		int chunkSize = 10000;
		SubListIterator<Player> iterator = new SubListIterator<Player>(pDomainObjects, chunkSize);
		
		IDaoService<Player> checker = getServiceUtility().getDaoServiceByEntity(Player.class);
		checker.getNotifier().manualEventQueueNotification(true);
		boolean checkDoubleEntries = checkDuplicates(checker);
		Query duplQuery = createDuplicationCheckingQuery(checker);
		//duplQuery.setHint("org.hibernate.cacheable", true); //$NON-NLS-1$
		int chunkCount = 0;
		while(iterator.hasNext()){
			List<Player> origList = iterator.next();
			List<Player> duplicates=null;
			if(checkDoubleEntries){
				pMonitor.subTask(Messages.AbstractPlayerImporter_checkingduplicates);
				List<?> codes = getDuplicationKeys(origList);
				duplicates = duplQuery.setParameter(getParameter(), codes).getResultList();
				getLogger().info("Duplicates found >>>>"+duplicates.size()); //$NON-NLS-1$
				
			}
			pMonitor.subTask(Messages.AbstractPlayerImporter_savingplayers+(chunkSize*chunkCount)+" - "+(chunkSize*(chunkCount+1))); //$NON-NLS-2$
			
			if(duplicates!=null){
				origList.removeAll(duplicates);
			}
			
			if(!origList.isEmpty()){
				checker.saveBatch(origList);
				if(iterator.hasNext()){
					checker.getNotifier().clearEventQueueCache();
				}

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
