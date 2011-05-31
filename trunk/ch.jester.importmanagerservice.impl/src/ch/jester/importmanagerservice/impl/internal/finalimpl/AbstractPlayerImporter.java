package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ch.jester.common.importer.AbstractTableImporter;
import ch.jester.common.utility.SubListIterator;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public abstract class AbstractPlayerImporter<T> extends AbstractTableImporter<T, Player> {
	protected ServiceUtility su = new ServiceUtility();
	
	@Override
	public String[] getDomainObjectAttributes() {
		return new String[]{"lastName","firstName","fideCode","elo","age","city","nation"};
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


}
