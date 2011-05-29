package ch.jester.importmanagerservice.impl.abstracts;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import ch.jester.commonservices.api.importer.IVirtualTable;

public class FixTextTableProvider implements IVirtualTable<String>{
	private String[] mHeader;
	//private InputStream mInput;
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
		mCells.add(new SubStringCell(string, indexBegin,indexEnd));
	}
	

	@Override
	public String[] processRow(String pRow, int pLenght) {
		List<String> details = new ArrayList<String>();
		for(IVirtualCell c:mCells){
			c.createCellContent(details, pRow);
		}
		return details.toArray(new String[details.size()]);
	}

	public class SubStringCell implements IVirtualCell{
		String name, delim;
		int seq;
		public SubStringCell(String string, int s1, int s2){
			cellStart = s1;
			cellStop = s2;
			name = string;
		}
		int cellStart;
		int cellStop;
		@Override
		public void createCellContent(List<String> detailList, String pInput) {
			String src = pInput.substring(cellStart, cellStop);
			if(delim==null){
				detailList.add(src);
			}else{
				if(src.startsWith(delim)){
					src = " "+src;
				}
				StringTokenizer token = new StringTokenizer(src, delim);
				for(int i=0;i<seq;i++){
					if(token.hasMoreTokens()){
						token.nextToken();
					}
				}
				if(token.hasMoreTokens()){
					detailList.add(token.nextToken());
				}else{
					detailList.add("");
				}
			}
		}
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}
		@Override
		public void setDelimiter(String pDelim) {
			delim = pDelim;
			
		}
		@Override
		public String getDelimiter() {
			return delim;
		}
		@Override
		public int getDelimiterSequence() {
			return -1;
		}
		@Override
		public void setDelimiterSequence(int i) {
			seq=i;
		}
	}
	
	
	@Override
	public boolean canAddCells() {
		return true;
	}

	@Override
	public String[] getDynamicInput(int pCount) {
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
