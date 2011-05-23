package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.jobs.Job;

import ch.jester.importmanagerservice.impl.abstracts.AbstractTableImporter;
import ch.jester.importmanagerservice.impl.abstracts.FixTextTableProvider;
import ch.jester.importmanagerservice.impl.abstracts.ITableProvider;
import ch.jester.importmanagerservice.impl.abstracts.JobUtil;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public class FIDETextImporter extends AbstractTableImporter<String, Player>{
	@Override
	protected void persist(List<Player> pDomainObjects) {
		Job[] jobs = JobUtil.paralleliseJob(1, pDomainObjects);
		for(Job j:jobs){
			j.schedule();
		}
		for(Job j:jobs){
			try {
				j.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void done(){
		
	}
	@Override
	protected Player createDomainObject(Properties playerProperties) {
		String name = playerProperties.getProperty("Name").trim();
		//String firstName = playerProperties.getProperty("Vorname").trim();
		Player player = ModelFactory.getInstance().createPlayer();
		if(name.indexOf(",")!=-1){
			String sfn = name.substring(name.indexOf(",")+1).trim();
			name = name.substring(0, name.indexOf(",")).trim();
			player.setFirstName(sfn);
		}
		player.setLastName(name);
		
		String fidecode = playerProperties.getProperty("CodeFIDE");
		if(fidecode.equals("")){
			fidecode="0";
		}
		player.setFideCode(Integer.parseInt(fidecode.trim()));
		String elo = playerProperties.getProperty("Elo neu");
		if(elo==null||elo.equals("")){
			elo="0";
		}
		player.setElo(Integer.parseInt(elo));
		player.setNation("Schweiz");
		return player;
	}

	@Override
	public ITableProvider<String> initialize(InputStream pInputStream) {
		FixTextTableProvider provider = new FixTextTableProvider(pInputStream);
		provider.setCell(0, 10);
		provider.setCell(10, 40);
		provider.setHeaderEntries("CodeFIDE","Name");
		return provider;
	}
	
}
