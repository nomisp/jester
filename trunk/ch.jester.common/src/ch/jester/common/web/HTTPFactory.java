package ch.jester.common.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HTTPFactory {
	private static Proxy mProxy; 
	public static void createHTTPProxy(String pProxyAdress, int pProxyPort) {
		mProxy =  new Proxy(Proxy.Type.HTTP, new InetSocketAddress(pProxyAdress,
				pProxyPort));
	}
	public static Proxy getHTTPProxy(){
		return mProxy;
	}
	public static void reset(){
		mProxy=null;
	}
	public static HttpURLConnection connect(String pUrl, boolean open) throws IOException{
		URL url = new URL(pUrl);
		HttpURLConnection uc = null;
		if(HTTPFactory.getHTTPProxy()==null){
			uc = (HttpURLConnection) url.openConnection();
		}else{
			uc =  (HttpURLConnection) url.openConnection(HTTPFactory.getHTTPProxy());
		}
		if(open){
			uc.connect();
		}
		return uc;
	}
	public static HttpURLConnection connect(String pUrl) throws IOException{
		return connect(pUrl, true);
	}
}
