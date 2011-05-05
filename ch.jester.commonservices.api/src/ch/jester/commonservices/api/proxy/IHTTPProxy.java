package ch.jester.commonservices.api.proxy;


import java.net.Proxy;

public interface IHTTPProxy {
	public void createHTTPProxy(String pProxyAdress, int pProxyPort);
	public Proxy getHTTPProxy();
	public String getAddress();
	public int getPort();
}
