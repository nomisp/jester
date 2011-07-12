package messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "messages.messages"; //$NON-NLS-1$
	public static String PlayerFormPage_lbl_age;
	public static String PlayerFormPage_lbl_chess_data;
	public static String PlayerFormPage_lbl_city;
	public static String PlayerFormPage_lbl_club;
	public static String PlayerFormPage_lbl_elo;
	public static String PlayerFormPage_lbl_fidecode;
	public static String PlayerFormPage_lbl_firstname;
	public static String PlayerFormPage_lbl_lastname;
	public static String PlayerFormPage_lbl_nation;
	public static String PlayerFormPage_lbl_nationalcode;
	public static String PlayerFormPage_lbl_nationalelo;
	public static String PlayerFormPage_lbl_personal_data;
	public static String PlayerFormPage_lbl_player_det;
	public static String PlayerFormPage_lbl_title;
	public static String PlayersView_lbl_players_titel;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
