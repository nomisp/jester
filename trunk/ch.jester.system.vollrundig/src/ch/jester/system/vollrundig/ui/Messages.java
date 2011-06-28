package ch.jester.system.vollrundig.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "ch.jester.system.vollrundig.ui.messages"; //$NON-NLS-1$
	public static String RoundRobinSettingsPage_AddingOrder;
	public static String RoundRobinSettingsPage_DoubleRounded;
	public static String RoundRobinSettingsPage_Elo;
	public static String RoundRobinSettingsPage_Random;
	public static String RoundRobinSettingsPage_StartingNumberGeneration;
	public static String RoundRobinSettingsPage_Title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
