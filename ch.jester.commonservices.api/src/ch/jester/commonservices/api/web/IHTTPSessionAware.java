package ch.jester.commonservices.api.web;

import javax.servlet.http.HttpSession;

/**
 * @author   t117221
 */
public interface IHTTPSessionAware {
	/**
	 * @return
	 * @uml.property  name="session"
	 */
	public HttpSession getSession();
	/**
	 * @param  pSession
	 * @uml.property  name="session"
	 */
	public void setSession(HttpSession pSession);
}
