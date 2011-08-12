package ch.jester.commonservices.api.web;

import javax.servlet.http.HttpSession;

/**
 * Getter/Setter Interface um eine ServletSession zu injecten.
 *
 */
public interface IHTTPSessionAware {
	/**
	 * Gibt die Session zurück.
	 * @return aSession oder null
	 */
	public HttpSession getSession();
	/**
	 * Setzt die Session
	 * @param pSession
	 */
	public void setSession(HttpSession pSession);
}
