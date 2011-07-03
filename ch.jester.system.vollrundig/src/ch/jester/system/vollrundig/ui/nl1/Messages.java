package ch.jester.system.vollrundig.ui.nl1;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "ch.jester.system.vollrundig.ui.nl1.messages"; //$NON-NLS-1$
	public static String RoundRobinSettingsPage_AddingOrder;
	public static String RoundRobinSettingsPage_DoubleRounded;
	public static String RoundRobinSettingsPage_Elo;
	public static String RoundRobinSettingsPage_Random;
	public static String RoundRobinSettingsPage_StartingNumberGeneration;
	public static String RoundRobinSettingsPage_Title;
	public static String VollrundigPairingAlgorithm__settingsPage_title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
