package ch.jester.socialmedia.api;

/**
 * Interface für einen Status Update.
 *
 */
public interface ISocialStatusUpdater {
	/**
	 * Sendet einen Status Update
	 * @param pStatus
	 */
	public void updateStatus(String pStatus);
	/**
	 * @return Max Länge des StatusTextes
	 */
	public int getMaxCharacterForStatus();
}
