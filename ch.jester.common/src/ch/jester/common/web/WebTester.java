package ch.jester.common.web;

import java.io.IOException;
import java.util.List;

import ch.jester.commonservices.api.importer.ILink;

public class WebTester {
	static String address_fide = "http://ratings.fide.com/download.phtml";
	static String address_ssb = "http://www.schachbund.ch/schachsport/fldownload.php";
	static String address_ssb_dl = "http://www.schachbund.ch/schachsport";
	public static void main(String args[]) throws IOException{
		HTTPFactory.createHTTPProxy("zh-netchild.web.proxy.ubs.com", 8080);
		ssb(null);
		fide(null);
	}
	
	
	public static void ssb(Object object) throws IOException {
	
		PageReader reader = new PageReader();
		reader.setDownloadRoot(address_ssb_dl);
		LinkFilter linkfilter = LinkFilter.createSSBFilter();
		reader.setFilter(new ExtensionFilter( ".zip", linkfilter));
		reader.readPage(address_ssb);
		List<ILink> links = linkfilter.getLinks();
		for(ILink l:links){
			System.out.println(l.getText()+" >> "+l.getURL());
			//l.download("D:/Documents and Settings/t117221/Desktop/down/"+l.getText().replaceAll("/", "_")+".zip");
		}
		
	}


	public static void fide(String args[]) throws IOException {
		PageReader reader = new PageReader();
		LinkFilter linkfilter;
		reader.setFilter(new ExtensionFilter(".zip", linkfilter=LinkFilter.createFIDEFilter()));
		reader.readPage(address_fide);
		List<ILink> links = linkfilter.getLinks();
		for(ILink l:links){
			System.out.println(l.getText()+" >> "+l.getURL());
			l.download("D:/Documents and Settings/t117221/Desktop/down/"+l.getText()+".zip");
		}
		
	}

}





