package ch.jester.socialmedia.twitter;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class Twitter4JWrapper {
	Twitter twitter;
	public  Twitter4JWrapper(ConfigurationBuilder builder) {
	
		twitter = new TwitterFactory(builder.build()).getInstance();
		//notifyFollowers(twitter, "java test");
		//showFriendsTimeLine(twitter);
		//postStatus(twitter,"Message from a OSGI-Plugin");
	}
	
	public  void postStatus(String string) throws TwitterException {
		twitter.updateStatus(string);
	}
	
	public  void notifyFollowers(String msg) throws TwitterException{
		long id0 = twitter.getId();
		IDs ids = twitter.getFollowersIDs(id0, -1);
		long ll[] = ids.getIDs();
		for(long l:ll){
			twitter.sendDirectMessage(l, msg);
		}
	}

	@SuppressWarnings("unused")
	private  void showFriendsTimeLine() throws TwitterException{
	    @SuppressWarnings("deprecation")
		List<Status> statuses = twitter.getFriendsTimeline();
	    System.out.println("Showing friends timeline.");
	    for (Status status : statuses) {
	        System.out.println(status.getUser().getName() + ":" +
	                           status.getText());
	    }
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

}
