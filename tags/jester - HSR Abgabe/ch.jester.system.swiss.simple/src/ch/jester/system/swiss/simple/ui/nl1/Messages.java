package ch.jester.system.swiss.simple.ui.nl1;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "ch.jester.system.swiss.simple.ui.nl1.messages"; //$NON-NLS-1$
	public static String SwissSimplePairingAlgorithm_settingsPage_title;
	public static String SwissSimpleSettingsPage_black;
	public static String SwissSimpleSettingsPage_elo;
	public static String SwissSimpleSettingsPage_estimatedElo;
	public static String SwissSimpleSettingsPage_lbl_FirstRoundColor;
	public static String SwissSimpleSettingsPage_lbl_ratingType;
	public static String SwissSimpleSettingsPage_nationalElo;
	public static String SwissSimpleSettingsPage_title;
	public static String SwissSimpleSettingsPage_lblByepoints_text;
	public static String SwissSimpleSettingsPage_random;
	public static String SwissSimpleSettingsPage_white;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
