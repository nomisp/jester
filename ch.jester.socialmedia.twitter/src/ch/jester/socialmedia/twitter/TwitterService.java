package ch.jester.socialmedia.twitter;

import org.eclipse.jface.dialogs.MessageDialog;

import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.socialmedia.api.ISocialStatusUpdater;
import ch.jester.socialmedia.auth.OAuthServiceComponent;

/**
 * Impl eines TwitterServices als ServiceComponent
 *
 */
public class TwitterService extends OAuthServiceComponent implements IPreferenceManagerProvider, ISocialStatusUpdater{

	private Twitter4JWrapper mWrapper;
	@Override
	protected String getPreferenceKey() {
		return "ch.jester.socialmedia.twitter";
	}

	@Override
	public void initializeService() {
		//DEMO Settings JesterRCPApp on Twitter
		/*mAuthConsumerKey.setValue("2RTYpIOiwuTbeVaVT2iyA");
		mAuthConsumerSecret.setValue("xAA0zYiFkE1ne2NMvgbmfJlBAD9vFo0tBBiK28Z1rBc");
		mAuthRequestTokenURL.setValue("https://api.twitter.com/oauth/request_token");
		mAuthAccessTokenURL.setValue("https://api.twitter.com/oauth/access_token");
		mAuthAuthoriziationURL.setValue("https://api.twitter.com/oauth/authorize");
		mAuthAccessToken.setValue("331121808-jTq6sKVIPY07qAhxHGcg6cU5C5z2oNhtsmSBwAG6");
		mAuthAccessTokenSecret.setValue("3jLEoaXegJFERubkVpfR93zdabDwsfw3k24lLBY7Gg");*/
		getTwitter();
		
	}

	public ConfigurationBuilder getConfigurationBuilder() throws RuntimeException{
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
		try{
			if(mWrapper == null){
				mWrapper = new Twitter4JWrapper(getConfigurationBuilder());
			}
		}catch(RuntimeException e){
			//Nix machen hier... vlt. ist noch gar keine Config vorhanden
		}
		
		return mWrapper;
	}

	@Override
	public void updateStatus(String pStatus) {
		if(mWrapper==null){
			MessageDialog.openError(UIUtility.getActiveWorkbenchWindow().getShell(), "Error", "Bitte Konfiguration überprüfen!");
			return;
		}
		try {
			mWrapper.postStatus(pStatus);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public int getMaxCharacterForStatus() {
		return 140;
	}





}
