package ch.jester.commonservices.api.web;


import java.net.Proxy;

public interface IHTTPProxy {
	public void createHTTPProxy(String pProxyAdress, int pProxyPort);
	public Proxy getHTTPProxy();
	public String getAddress();
	public int getPort();
}
