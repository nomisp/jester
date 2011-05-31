package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.InputStream;

import ch.jester.common.importer.VirtualCell;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;
import ch.jester.importmanagerservice.impl.abstracts.FixTextTableProvider;

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
}
