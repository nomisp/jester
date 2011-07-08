package ch.jester.socialmedia.twitter;

import twitter4j.conf.ConfigurationBuilder;
import ch.jester.common.components.ComponentAdapter;

import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;

public class TwitterService extends ComponentAdapter<IPreferenceRegistration> implements IPreferenceManagerProvider{

	private IPreferenceManager mPreferenceManager;
	private Twitter4JWrapper mWrapper;

	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return mPreferenceManager;
	}
	
	@Override
	public void bind(IPreferenceRegistration pT) {
		super.bind(pT);
		initPreferences(pT.createManager());
	}

	private void initPreferences(IPreferenceManager createManager) {
		mPreferenceManager = createManager;
		mPreferenceManager.setId("ch.jester.socialmedia.twitter");
		mPreferenceManager.create("OAuthConsumerKey", "OAuthConsumerKey", "2RTYpIOiwuTbeVaVT2iyA");
		mPreferenceManager.create("OAuthConsumerSecret", "OAuthConsumerSecret", "xAA0zYiFkE1ne2NMvgbmfJlBAD9vFo0tBBiK28Z1rBc");
		mPreferenceManager.create("OAuthRequestTokenURL","OAuthRequestTokenURL","https://api.twitter.com/oauth/request_token");
		mPreferenceManager.create("OAuthAccessTokenURL","OAuthAccessTokenURL","https://api.twitter.com/oauth/access_token");
		mPreferenceManager.create("OAuthAuthorizationURL","OAuthAuthorizationURL","https://api.twitter.com/oauth/authorize");
		mPreferenceManager.create("OAuthAccessToken","OAuthAccessToken","331121808-jTq6sKVIPY07qAhxHGcg6cU5C5z2oNhtsmSBwAG6");
		mPreferenceManager.create("OAuthAccessTokenSecret","OAuthAccessTokenSecret","3jLEoaXegJFERubkVpfR93zdabDwsfw3k24lLBY7Gg");
		mPreferenceManager.registerProviderAtRegistrationService(this);
		
	}
	
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

	public ConfigurationBuilder getConfigurationBuilder(){
		ConfigurationBuilder builder= new ConfigurationBuilder();
		builder.setOAuthConsumerKey(mPreferenceManager.getPropertyByInternalKey("OAuthConsumerKey").getStringValue());
		builder.setOAuthConsumerSecret(mPreferenceManager.getPropertyByInternalKey("OAuthConsumerSecret").getStringValue());
		builder.setOAuthRequestTokenURL(mPreferenceManager.getPropertyByInternalKey("OAuthRequestTokenURL").getStringValue());
		builder.setOAuthAccessTokenURL(mPreferenceManager.getPropertyByInternalKey("OAuthAccessTokenURL").getStringValue());
		builder.setOAuthAuthorizationURL(mPreferenceManager.getPropertyByInternalKey("OAuthAuthorizationURL").getStringValue());
		builder.setOAuthAccessToken(mPreferenceManager.getPropertyByInternalKey("OAuthAccessToken").getStringValue());
		builder.setOAuthAccessTokenSecret(mPreferenceManager.getPropertyByInternalKey("OAuthAccessTokenSecret").getStringValue());
		return builder;
	}
	
	public Twitter4JWrapper getTwitter(){
		if(mWrapper == null){
			mWrapper = new Twitter4JWrapper(getConfigurationBuilder());
		}
		return mWrapper;
	}

}
