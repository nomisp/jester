package ch.jester.importmanagerservice.tableprovider;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ch.jester.common.importer.VirtualCell;
import ch.jester.commonservices.api.importer.IVirtualTable;

public class FixTextTableProvider implements IVirtualTable<String>{
	private Scanner mScanner;
	List<String> mContent = new ArrayList<String>();
	List<IVirtualCell> mCells = new ArrayList<IVirtualCell>();
	public FixTextTableProvider(InputStream pInstream) {
		BufferedInputStream pis = new BufferedInputStream(pInstream);
		mScanner = new Scanner(pis);
		while(mScanner.hasNextLine()){
			mContent.add(mScanner.nextLine());
		}
		mScanner.close();
	}

	
	@Override
	public String[] getHeaderEntries() {
		List<String> mCellNames = new ArrayList<String>();
		for(IVirtualCell c:mCells){
			mCellNames.add(c.getName());
		}
		return mCellNames.toArray(new String[mCellNames.size()]);
	}

	@Override
	public String getRow(int i) {
		return mContent.get(i);
	}

	@Override
	public int getTotalRows() {
		return mContent.size();
	}

	public void setCell(String string, int indexBegin, int indexEnd){
		mCells.add(new VirtualCell(string, indexBegin,indexEnd));
	}
	

	@Override
	public String[] processRow(String pRow, int pLenght) {
		List<String> details = new ArrayList<String>();
		for(IVirtualCell c:mCells){
			c.createCellContent(details, pRow);
		}
		return details.toArray(new String[details.size()]);
	}
	
	@Override
	public boolean canAddCells() {
		return true;
	}

	@Override
	public String[] getRowInput(int pCount) {
		String[] s = new String[pCount];
		for(int i=0;i<pCount;i++){
			s[i] = mContent.get(i);
		}
		return s;
	
	}

	@Override
	public void clearCells() {
		mCells.clear();
		
	}

	@Override
	public void addCell(
			ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell cell) {
		mCells.add(cell);
		
	}

	@Override
	public List<ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell> getCells() {
		return mCells;
	}


	

}
