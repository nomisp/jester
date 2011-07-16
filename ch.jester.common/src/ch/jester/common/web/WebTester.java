package ch.jester.common.web;

import java.io.IOException;

public class WebTester {
	static String address_fide = "http://ratings.fide.com/download.phtml";
	static String address_ssb = "http://www.schachbund.ch/schachsport/fldownload.php";
	static String address_ssb_dl = "http://www.schachbund.ch/schachsport";
	public static void main(String args[]) throws IOException{
		HTTPFactory.createHTTPProxy("zh-netchild.web.proxy.ubs.com", 8080);;
	}
	

}





