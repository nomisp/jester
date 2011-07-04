package ch.jester.ui.exporter.nl1;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "ch.jester.ui.exporter.nl1.messages"; //$NON-NLS-1$
	public static String PlayerXMLPage_extension1;
	public static String PlayerXMLPage_extension2;
	public static String PlayerXMLPage_filterpath;
	public static String PlayerXMLPage_lbl_browse;
	public static String PlayerXMLPage_lbl_btn_export;
	public static String PlayerXMLPage_lbl_btn_save;
	public static String PlayerXMLPage_lbl_export;
	public static String PlayerXMLPage_lbl_export_data;
	public static String PlayerXMLPage_lbl_tournament;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
