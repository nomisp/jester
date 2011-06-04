package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ch.jester.common.importer.VirtualCell;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.importmanagerservice.impl.abstracts.FixTextTableProvider;
import ch.jester.model.Player;

public class FIDETextImporter extends AbstractPlayerImporter<String>{

	public FIDETextImporter(){
		init_linking();
	}
	private void init_linking() {
		mInputLinking.put("lastName","LastName");
		mInputLinking.put("firstName","FirstName");
		mInputLinking.put("fideCode","CodeFIDE");
		
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
	@Override
	public Query createDuplicationCheckingQuery(
			IDaoService<? extends IEntityObject> pDaoService) {
		return pDaoService.createQuery("SELECT player FROM Player player WHERE player.fideCode in (:fideCode)");
	}
	@Override
	public String getParameter() {
		return "fideCode";
	}

	@Override
	public List<?> getDuplicationKeys(List<Player> sublist) {
		List<Integer> fide = new ArrayList<Integer>();
		for(Player p:sublist){
			fide.add(p.getFideCode());
		}
		return fide;
	}
	@Override
	public void handleDuplicates(
			IDaoService<? extends IEntityObject> pDaoService, List<Player> pList) {
		// TODO Auto-generated method stub
		
	}
}
