package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.InputStream;
import java.util.List;


import ch.jester.common.importer.AbstractTableImporter;
import ch.jester.common.importer.VirtualCell;
import ch.jester.common.utility.SubListIterator;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.importmanagerservice.impl.abstracts.FixTextTableProvider;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public class FIDETextImporter extends AbstractTableImporter<String, Player>{
	private ServiceUtility su = new ServiceUtility();
	
	public FIDETextImporter(){
		init_linking();
	}
	private void init_linking() {
		super.mInputLinking.put("lastName","LastName");
		super.mInputLinking.put("firstName","FirstName");
		super.mInputLinking.put("fideCode","CodeFIDE");
		
	}
	@Override
	protected void persist(List<Player> pDomainObjects) {
		SubListIterator<Player> iterator = new SubListIterator<Player>(pDomainObjects, 10000);
		
		while(iterator.hasNext()){
			List<Player> sublist = iterator.next();
			IDaoService<Player> playerpersister = su.getDaoService(Player.class);
			playerpersister.save(sublist);
			playerpersister.close();
		}
		
	}
	@Override
	public String[] getDomainObjectAttributes() {
		return new String[]{"lastName","firstName","fideCode","elo","age","city","nation"};
	}
	public void done(){
		
	}
	@Override
	protected Player createNewDomainObject() {
		return ModelFactory.getInstance().createPlayer();
	}


	@Override
	public IVirtualTable<String> initialize(InputStream pInputStream) {
		FixTextTableProvider provider = new FixTextTableProvider(pInputStream);
		
		provider.setCell("CodeFIDE", 0, 10);
		IVirtualCell cell = new VirtualCell("LastName", 10, 40);
		cell.setDelimiter(",");
		cell.setDelimiterSequence(0);
		provider.addCell(cell);
		
		cell = new VirtualCell("FirstName", 10, 40);
		cell.setDelimiter(",");
		cell.setDelimiterSequence(1);
		provider.addCell(cell);
		return provider;
	}


	
	/*@Override
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
	}*/
	
}
