package ch.jester.socialmedia.facebook.internal.authflow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * Impl von Facebooks Auth-Flow.
 *
 */
public class FacebookAuthorisationFlow {
	public static String APP_ID = "161639000575963";
	public static String userAuthURL ="https://www.facebook.com/dialog/oauth?client_id=YOUR_APP_ID&auth_type=reauthenticate&scope=publish_stream&redirect_uri=https://www.facebook.com/connect/login_success.html&response_type=token&force_login=true";
	public static String APP_ID_Param = "YOUR_APP_ID";
	public static String expiresIn_Param ="&expires_in=";
//	public static int expires;
	private String accessToken;
	private TokenExpiryManager expiryManager = new TokenExpiryManager();
	private boolean mClearSession;
	public FacebookAuthorisationFlow(){
		this(null, 0, 0, true);
	}
	
	public FacebookAuthorisationFlow(String token, int expires_in, long expiryTimeStamp, boolean clearSession) {
		accessToken=token;
		expiryManager.setExpires_in(expires_in, expiryTimeStamp);
		mClearSession=clearSession;
	}

	/**
	 * User Auth.
	 */
	public void authorizeUser() {
		String url = prepareUrl();
		FaceBookAuthorisationFlowDialog dialog = new FaceBookAuthorisationFlowDialog(Display.getDefault().getActiveShell(), SWT.BORDER);
		dialog.clearSession(mClearSession);
		dialog.setURL(url);
		dialog.open();
		String result = dialog.getToken();
		if(result == null){return;}
		if(result.contains(expiresIn_Param)){
			accessToken = result.substring(0, result.indexOf(expiresIn_Param));
			String exp = result.substring(result.indexOf(expiresIn_Param)+expiresIn_Param.length());
			expiryManager.setExpires_in(exp);
		}
		else{
			accessToken =  result;
		}
	}

	/**
	 * Zugriff aufs AccessToken
	 * @return
	 */
	public String getAccessToken(){
		return accessToken;
	}
	/**
	 * Zeit bis zum Expiry
	 * @return
	 */
	public int getExpiresIn(){
		return expiryManager.expires_in_inseconds;
	}
	public long getExpiresInTimeStamp(){
		return expiryManager.expires_in_ts;
	}
	
	private String prepareUrl() {
		return userAuthURL.replace(APP_ID_Param, APP_ID);
	}

	
	static class TokenExpiryManager{
		/*
		 * From Facebook API
		 * expires_in	
			The duration in seconds of the access token lifetime. E.g., the value 3600 specifies an expiration time of one hour. This is included only if access_token is.
		 * 
		 * 
		 */
		int expires_in_inseconds = 0;
		long expires_in_ts = 0;
		void setExpires_in(String s){
			int i = Integer.parseInt(s);
			setExpires_in(i, System.currentTimeMillis());
		}
		
		void setExpires_in(int i, long tsd){
			expires_in_ts = tsd;
			expires_in_inseconds = i;
		}
		boolean isExpired(){
			long expires_in_millis = expires_in_inseconds * 1000;
			long expire_time = expires_in_millis+expires_in_ts;
			if(System.currentTimeMillis()>expire_time){
				return true;
			}
			
			return false;
		}
		
		
	}


	public boolean established() {
		return !expiryManager.isExpired();
	}
}
