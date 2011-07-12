package ch.jester.common.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.eclipse.core.internal.net.ProxyData;
import org.eclipse.core.internal.net.ProxyManager;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;

import ch.jester.commonservices.util.ServiceUtility;

public class HTTPFactory {
	private static Proxy mProxy; 
	private static ServiceUtility su = new ServiceUtility();
	public static void createHTTPProxy(String pProxyAdress, int pProxyPort) {
		mProxy =  new Proxy(Proxy.Type.HTTP, new InetSocketAddress(pProxyAdress,
				pProxyPort));

	}
	public static void createHTTPProxyFromEnvSettings(){
		String host = System.getProperty("http.proxyHost");
		String port = System.getProperty("http.proxyPort");
		if(host==null&&port==null){
			reset();
			return;
		}
		createHTTPProxy(host, Integer.parseInt(port));
	}
	public static void createHTTPProxy(){
		IProxyService service = su.getService(IProxyService.class);
		if(service!=null){
			IProxyData da = service.getProxyData("HTTP");
			if(service.isProxiesEnabled()){
				createHTTPProxy(da.getHost(), da.getPort());
			}
			createHTTPProxyFromEnvSettings();
		}else{
			createHTTPProxyFromEnvSettings();
		}
	}
	
	public static Proxy getHTTPProxy(){
		return mProxy;
	}
	public static void reset(){
		mProxy=null;
	}
	public static HttpURLConnection connect(String pUrl, boolean open) throws IOException{
		URL url = new URL(pUrl);
		createHTTPProxy();
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
