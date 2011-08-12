package ch.jester.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Klasse um HTML Seiten zu parsen.
 *
 */
public class PageReader {
	/**
	 * String Filter für PageReader
	 *
	 */
	public interface IPageReaderFilter{
		/**
		 * Filter/Analysiert die ContentLine
		 * @param pContentLine
		 * @param pReader
		 */
		public void filter(String pContentLine, PageReader pReader);

	}
	private IPageReaderFilter mFilter;
	private String  mDL;
	/**
	 * Setzt den Parse Filter mit dem später die Seite auseinandergenommen wird.
	 * @param pFilter
	 */
	public void setFilter(IPageReaderFilter pFilter){
		mFilter = pFilter;
	}

	/**
	 * Setzen einer Root Adresse für Links
	 * @param pDL
	 */
	public void setDownloadRoot(String pDL){
		mDL = pDL;
	}
	
	/**
	 * Erzeugung eines Links unter Berücksichtigung der Root Adresse
	 * @param pText
	 * @param pUrl
	 * @return aLink
	 */
	public Link createLink(String pText, String pUrl){
		Link link = new Link(pText, pUrl);
		link.setRoot(mDL);
		return link;
	}
	
	/**
	 * Lesen der Seite
	 * Resultat wird an den Filter weitergeleitet um zu parsen.
	 * @param pAddress
	 * @throws IOException
	 */
	public void readPage(String pAddress) throws IOException{
		HttpURLConnection uc = HTTPFactory.connect(pAddress);
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));
		while ((line = in.readLine()) != null) {
			mFilter.filter(line, this);
		}
		in.close();
		uc.disconnect();
	}
}