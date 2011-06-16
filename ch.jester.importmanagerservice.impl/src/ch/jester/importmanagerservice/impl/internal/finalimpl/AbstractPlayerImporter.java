package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.util.List;

import javax.persistence.Query;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.importer.AbstractTableImporter;
import ch.jester.common.utility.SubListIterator;
import ch.jester.commonservices.api.importer.IDuplicateChecker;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

/**
 * @author  t117221
 */
public abstract class AbstractPlayerImporter<T> extends AbstractTableImporter<T, Player> implements IDuplicateChecker<Player> {
	/**
	 * @uml.property  name="su"
	 * @uml.associationEnd  
	 */
	protected ServiceUtility su = new ServiceUtility();
	
	@Override
	public String[] getDomainObjectAttributes() {
		return new String[]{"lastName","firstName","fideCode","nationalCode","elo","age","city","nation"};
	}

	@Override
	protected void persist(List<Player> pDomainObjects, IProgressMonitor pMonitor) {
		int chunkSize = 10000;
		SubListIterator<Player> iterator = new SubListIterator<Player>(pDomainObjects, chunkSize);
		
		IDaoService<Player> checker = su.getDaoServiceByEntity(Player.class);
		checker.manualEventQueueNotification(true);
		boolean checkDoubleEntries = checkDuplicates(checker);
		Query duplQuery = createDuplicationCheckingQuery(checker);
		
		int chunkCount = 0;
		while(iterator.hasNext()){
			List<Player> origList = iterator.next();
			//IDaoService<Player> playerpersister = su.getDaoServiceByEntity(Player.class);
			
			List<Player> duplicates=null;
			if(checkDoubleEntries){
				pMonitor.subTask("Checking Duplicates");
				List<?> codes = getDuplicationKeys(origList);
				duplicates = duplQuery.setParameter(getParameter(), codes).getResultList();
				System.out.println("Duplicates found >>>>"+duplicates.size());
				
			}
			pMonitor.subTask("Saving Players: "+(chunkSize*chunkCount)+" - "+(chunkSize*(chunkCount+1)));
			
			if(duplicates!=null){
				origList.removeAll(duplicates);
			}
			
			if(!origList.isEmpty()){
				checker.save(origList);
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
	/*private List<Integer> createFideList(List<Player> sublist) {
		List<Integer> fide = new ArrayList<Integer>();
		for(Player p:sublist){
			fide.add(p.getFideCode());
		}
		return fide;
	}*/
	@Override
	protected Player createNewDomainObject() {
		return ModelFactory.getInstance().createPlayer();
	}


}
