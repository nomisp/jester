package messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "messages.messages"; //$NON-NLS-1$
	public static String StartServerPrefPage_btn_start;
	public static String StartServerPrefPage_btn_stop;
	public static String StartServerPrefPage_lbl_desc;
	public static String StartServerPrefPage_lbl_status;
	public static String StartServerPrefPage_status_offline;
	public static String StartServerPrefPage_status_online;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
