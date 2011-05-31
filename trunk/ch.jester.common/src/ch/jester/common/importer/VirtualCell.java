package ch.jester.common.importer;

import java.util.List;
import java.util.StringTokenizer;

import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;

public class VirtualCell implements IVirtualCell {
		String delim, mName;
		int seq = -1, cellStart, cellStop;

		public VirtualCell(String name, int pCellStart, int pCellStop) {
			cellStart = pCellStart;
			cellStop = pCellStop;
			mName = name;
		}

		@Override
		public void createCellContent(List<String> detailList, String pInput) {
			String src = pInput.substring(cellStart, cellStop);
			if (delim == null) {
				detailList.add(src);
			} else {
				if (src.startsWith(delim)) {
					src = " " + src;
				}
				StringTokenizer token = new StringTokenizer(src, delim);
				for (int i = 0; i < seq; i++) {
					if (token.hasMoreTokens()) {
						token.nextToken();
					}
				}
				if (token.hasMoreTokens()) {
					detailList.add(token.nextToken());
				} else {
					detailList.add("");
				}
			}
		}

		@Override
		public String getName() {
			return mName;

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
			return seq;
		}

		@Override
		public void setDelimiterSequence(int i) {
			seq = i;

		}

		@Override
		public void setName(String text) {
			mName = text;

		}

		@Override
		public int getStart() {
			return cellStart;
		}

		@Override
		public void setStart(int i) {
			cellStart = i;
		}

		@Override
		public int getStop() {
			return cellStop;
		}

		@Override
		public void setStop(int i) {
			cellStop = i;
		}
	
}
