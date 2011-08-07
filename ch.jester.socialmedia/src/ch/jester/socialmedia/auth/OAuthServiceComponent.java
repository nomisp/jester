package ch.jester.socialmedia.auth;



import java.util.Map;

import ch.jester.common.components.InjectedLogFactoryComponentAdapter;

import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;

/**
 * Abstrakte OAuth Service Component.
 * Getter für die typischen OAuth Properties.
 *
 */
public abstract class OAuthServiceComponent extends InjectedLogFactoryComponentAdapter<IPreferenceRegistration> implements IPreferenceManagerProvider{

	protected IPreferenceManager mPreferenceManager;
	protected IPreferenceProperty mAuthConsumerKey, mAuthConsumerSecret,
	mAuthRequestTokenURL, mAuthAccessTokenURL, mAuthAuthoriziationURL, mAuthAccessToken,
	mAuthAccessTokenSecret;

	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return mPreferenceManager;
	}
	

	public void bind(IPreferenceRegistration pT) {
		initPreferences(pT.createManager());
	}

	private void initPreferences(IPreferenceManager createManager) {
		mPreferenceManager = createManager;
		mPreferenceManager.setId(getPreferenceKey());
		createAdditionalProperties();
		mAuthConsumerKey=mPreferenceManager.create("OAuthConsumerKey", "OAuthConsumerKey", "");
		mAuthConsumerSecret=mPreferenceManager.create("OAuthConsumerSecret", "OAuthConsumerSecret", "");
		mAuthRequestTokenURL=mPreferenceManager.create("OAuthRequestTokenURL","OAuthRequestTokenURL","");
		mAuthAccessTokenURL=mPreferenceManager.create("OAuthAccessTokenURL","OAuthAccessTokenURL","");
		mAuthAuthoriziationURL=mPreferenceManager.create("OAuthAuthorizationURL","OAuthAuthorizationURL","");
		mAuthAccessToken=mPreferenceManager.create("OAuthAccessToken","OAuthAccessToken","");
		mAuthAccessTokenSecret=mPreferenceManager.create("OAuthAccessTokenSecret","OAuthAccessTokenSecret","");
		mPreferenceManager.registerProviderAtRegistrationService(mPreferenceManager.getId(), this);
		initializeService();
		
	}
	
	/**
	 * zusätzliche Erzeugung von Properties fürs UI.
	 */
	protected void createAdditionalProperties(){}
	
	/**
	 * @return den PreferenceKey für den Manager
	 */
	protected abstract String getPreferenceKey();

	/*private  Twitter createTwitter(){
	ConfigurationBuilder builder= new ConfigurationBuilder();
	builder.setOAuthConsumerKey("2RTYpIOiwuTbeVaVT2iyA");
	builder.setOAuthConsumerSecret("xAA0zYiFkE1ne2NMvgbmfJlBAD9vFo0tBBiK28Z1rBc");
	builder.setOAuthRequestTokenURL("https://api.twitter.com/oauth/request_token");
	builder.setOAuthAccessToken("https://api.twitter.com/oauth/access_token");
	builder.setOAuthAuthorizationURL("https://api.twitter.com/oauth/authorize");
	builder.setOAuthAccessToken("331121808-jTq6sKVIPY07qAhxHGcg6cU5C5z2oNhtsmSBwAG6");
	builder.setOAuthAccessTokenSecret("3jLEoaXegJFERubkVpfR93zdabDwsfw3k24lLBY7Gg");
	
	return twitter;
}*/

	public abstract void initializeService();

	public Map<String, String> getProperties(){
		return mPreferenceManager.getPropertiesAsStringMap();
	}


	public String getAuthConsumerKey() {
		return mAuthConsumerKey.getStringValue();
	}

	public void setAuthConsumerKey(String mAuthConsumerKey) {
		this.mAuthConsumerKey.setDefaultValue(mAuthConsumerKey);
	}

	public String getAuthConsumerSecret() {
		return mAuthConsumerSecret.getStringValue();
	}

	public void setAuthConsumerSecret(String mAuthConsumerSecret) {
		this.mAuthConsumerSecret.setValue(mAuthConsumerSecret);
	}

	public String getAuthRequestTokenURL() {
		return mAuthRequestTokenURL.getStringValue();
	}

	public void setAuthRequestTokenURL(String mAuthRequestTokenURL) {
		this.mAuthRequestTokenURL.setValue(mAuthRequestTokenURL);
	}

	public String getAuthAccessTokenURL() {
		return mAuthAccessTokenURL.getStringValue();
	}

	public void setAuthAccessTokenURL(String mAuthAccessTokenURL) {
		this.mAuthAccessTokenURL.setValue(mAuthAccessTokenURL);
	}

	public String getAuthAuthoriziationURL() {
		return mAuthAuthoriziationURL.getStringValue();
	}

	public void setAuthAuthoriziationURL(String mAuthAuthoriziationURL) {
		this.mAuthAuthoriziationURL.setValue(mAuthAuthoriziationURL);
	}

	public String getAuthAccessToken() {
		return mAuthAccessToken.getStringValue();
	}

	public void setAuthAccessToken(String mAuthAccessToken) {
		this.mAuthAccessToken.setValue(mAuthAccessToken);
	}

	public String getmAuthAccessTokenSecret() {
		return mAuthAccessTokenSecret.getStringValue();
	}

	public void setAuthAccessTokenSecret(String mAuthAccessTokenSecret) {
		this.mAuthAccessTokenSecret.setValue(mAuthAccessTokenSecret);
	}
	
}
