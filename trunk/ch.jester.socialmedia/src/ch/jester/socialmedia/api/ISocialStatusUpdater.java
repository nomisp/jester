package ch.jester.socialmedia.api;

public interface ISocialStatusUpdater {
	public void updateStatus(String pStatus);
	public int getMaxCharacterForStatus();
}
