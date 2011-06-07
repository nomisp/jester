package ch.jester.commonservices.api.web;

import javax.servlet.http.HttpSession;

public interface IHTTPSessionAware {
	public HttpSession getSession();
	public void setSession(HttpSession pSession);
}
