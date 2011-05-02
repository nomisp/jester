package ch.jester.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class PageReader {
	public interface IPageReaderFilter{
		public void filter(String pContentLine, PageReader pReader);

	}
	private IPageReaderFilter mFilter;
	private String mAddress, mDL;
	public void setFilter(IPageReaderFilter pFilter){
		mFilter = pFilter;
	}

	public void setDownloadRoot(String pDL){
		mDL = pDL;
	}
	
	public Link createLink(String pText, String pUrl){
		Link link = new Link(pText, pUrl);
		link.setRoot(mDL);
		return link;
	}
	
	public void readPage(String pAddress) throws IOException{
		mAddress = pAddress;
		HttpURLConnection uc = HTTPFactory.connect(pAddress);
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));
		while ((line = in.readLine()) != null) {
			//System.out.println(line);
			mFilter.filter(line, this);
		}
		in.close();
		uc.disconnect();
	}
}