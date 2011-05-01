package ch.jester.importmanagerservice.impl.abstracts;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FixTextTableProvider implements ITableProvider<String>{
	private String[] mHeader;
	//private InputStream mInput;
	private Scanner mScanner;
	List<String> mContent = new ArrayList<String>();
	List<Cell> mCells = new ArrayList<Cell>();
	public FixTextTableProvider(InputStream pInstream) {
	//	mInput=pInstream;
		BufferedInputStream pis = new BufferedInputStream(pInstream);
		mScanner = new Scanner(pis);
		while(mScanner.hasNextLine()){
			mContent.add(mScanner.nextLine());
		}
		mScanner.close();
	}
	
	public void setHeaderEntries(String...strings){
		mHeader=strings;
	}
	
	@Override
	public String[] getHeaderEntries() {
		return mHeader;
	}

	@Override
	public String getRow(int i) {
		return mContent.get(i);
	}

	@Override
	public int getTotalRows() {
		return mContent.size();
	}

	public void setCell(int indexBegin, int indexEnd){
		mCells.add(new Cell(indexBegin,indexEnd));
	}
	
	@Override
	public String[] processRow(String pRow, int pLenght) {
		String[] detail = new String[mCells.size()];
		int i = 0;
		for(Cell c:mCells){
			detail[i]= pRow.substring(c.start, c.stop);
			i++;
		}
		return detail;
	}

	class Cell{
		public Cell(int s1, int s2){
			start = s1;
			stop = s2;
		}
		int start;
		int stop;
	}
}
