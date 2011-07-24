package messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "messages.messages"; //$NON-NLS-1$
	public static String DaoDeleteHandler_deleting;
	public static String DaoDeleteHandler_still_referenced;
	public static String DefaultSelectionCountListener_items_selected;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
