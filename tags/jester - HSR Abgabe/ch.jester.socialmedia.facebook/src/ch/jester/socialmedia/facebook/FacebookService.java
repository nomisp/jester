package ch.jester.socialmedia.facebook;

import java.util.Set;

import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.socialmedia.api.ISocialStatusUpdater;
import ch.jester.socialmedia.auth.OAuthServiceComponent;
import ch.jester.socialmedia.facebook.internal.authflow.FacebookAuthorisationFlow;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

/**
 * Implementation eines Facebook Services
 *
 */
public class FacebookService extends OAuthServiceComponent implements ISocialStatusUpdater{
	private FacebookClient facebookClient;
	private FacebookAuthorisationFlow accessFlow;
	private boolean sendRequest = true;
	private IPreferenceProperty expIn, expInTimeStamp;
	@Override
	public void initializeService() {
		Set<IPreferenceProperty> props = mPreferenceManager.getProperties();
		for(IPreferenceProperty p:props){
			p.setEnabled(false);
		}
		//mAuthAccessToken.setEnabled(true);
		initializeAuthorisationFlow();
		
	}
	@Override
	protected void createAdditionalProperties() {
		expIn = mPreferenceManager.create("expiresIn", "TokenExpiresIn", 0).setEnabled(false);
		expInTimeStamp = mPreferenceManager.create("expiresInTimeStamp", "Token received", 0L).setEnabled(false);
		expInTimeStamp.setType(Long.class);
	}
	/**
	 * Initialisiert Auth.Flow
	 */
	private void initializeAuthorisationFlow() {
		accessFlow = new FacebookAuthorisationFlow(mAuthAccessToken.getStringValue(), expIn.getIntegerValue(), (Long)expInTimeStamp.getValue(),true);
		
	}

	
	/**
	 * überprüfen der Authorisierung... allenfalls einloggen.
	 */
	private void checkCredentials() {
		if(accessFlow.established()){return;}
		accessFlow.authorizeUser();
		mAuthAccessToken.setValue(accessFlow.getAccessToken());
		expIn.setValue(accessFlow.getExpiresIn());
		expInTimeStamp.setValue(accessFlow.getExpiresInTimeStamp());
		facebookClient = new DefaultFacebookClient(mAuthAccessToken.getStringValue());
	}
	@Override
	public void updateStatus(String pStatus){
		checkCredentials();
		getLogger().debug("Sending Message to Facebook: "+pStatus);
		if(!sendRequest){return;}
		@SuppressWarnings("unused")
		FacebookType publishMessageResponse =
			  facebookClient.publish("me/feed", FacebookType.class,
			    Parameter.with("message", pStatus));
	}
	@Override
	public int getMaxCharacterForStatus() {
		return 160;
	}
	public void checkAuthorisation() {
		checkCredentials();
		
	}
	@Override
	protected String getPreferenceKey() {
		return "ch.jester.socialmedia.facebook";
	}

}
