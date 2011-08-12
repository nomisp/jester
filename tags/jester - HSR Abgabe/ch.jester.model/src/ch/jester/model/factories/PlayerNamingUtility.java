package ch.jester.model.factories;

import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;

/**
 * Utility Klasse um die Spielernamen zu verallgemeinen
 *
 */
public class PlayerNamingUtility {
	private static String mDefault_Delim =", ";
	public static String createName(Player pPlayer, String pDelim){
		return createLastFirst(pPlayer, pDelim);
	}
	public static String createName(PlayerCard pPlayerCard, String pDelim){
		return createLastFirst(pPlayerCard.getPlayer(), pDelim);
	}
	public static String createName(Player pPlayer){
		return createLastFirst(pPlayer, mDefault_Delim);
	}
	public static String createName(PlayerCard pPlayerCard){
		return createLastFirst(pPlayerCard.getPlayer(), mDefault_Delim);
	}
	
	public static String createPairingName(Player p1, Player p2, String pAddinLabel){
		return createPairingName(p1, p2, mDefault_Delim, pAddinLabel);
	}
	public static String createPairingName(PlayerCard p1, PlayerCard p2, String pAddinLabel){
		return createPairingName(p1.getPlayer(), p2.getPlayer(), mDefault_Delim, pAddinLabel);
	}
	public static String createPairingName(Pairing pairing,  String pAddinLabel){
		return createPairingName(pairing.getWhite(), pairing.getBlack(),  pAddinLabel);
	}
	
	public static String createPairingName(Player p1, Player p2, String pDelim, String pAddinLabel){
		StringBuilder builder = new StringBuilder();
		String p1String = createName(p1, pDelim);
		String p2String = createName(p2, pDelim);
		if(p1String!=null){
			builder.append(p1String);
		}
		if(p2String!=null&&p1String!=null){
			builder.append(pAddinLabel);
		}
		if(p2String!=null){
			builder.append(p2String);
		}
		return builder.toString();
	
	}
	
	
	private static String createLastFirst(Player p, String pDelim){
		if(p!=null){
			return p.getLastName()+pDelim+p.getFirstName();
		}
		return null;
	}
}
