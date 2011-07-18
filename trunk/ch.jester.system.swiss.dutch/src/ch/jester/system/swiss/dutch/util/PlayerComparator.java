package ch.jester.system.swiss.dutch.util;

import java.util.Comparator;

import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Title;

/**
 * Vergleichen von 2 Spielern nach den FIDE Regeln
 * für das Schweizer System<br/>
 * <link>http://www.fide.com/fide/handbook.html?id=83&view=article</link><br/>
 * A.2 Order:<br/>
 * a. score<br/>
 * b. rating<br/>
 * c. FIDE-title<br/>
 * d. alphabetically<br/>
 * 
 * @author Peter
 * 
 */
public class PlayerComparator implements Comparator<PlayerCard> {
	
	private RatingType ratingType;
	
	public PlayerComparator(RatingType ratingType) {
		this.ratingType = ratingType;
	}

	@Override
	public int compare(PlayerCard p1, PlayerCard p2) {
		Double p1Points = p1.getPoints();
		Double p2Points = p2.getPoints();
		if (p2Points.compareTo(p1Points) == 0) {	// Spieler sind punktgleich
			switch (ratingType) {
			case ELO:
				return compareElo(p1, p2);

			case NWZ:
				return compareNWZ(p1, p2);
				
			case ESTIMATED:
				return compareEstimatedElo(p1, p2);
			default:
				break;
			}
		} else {
			return p2Points.compareTo(p1Points);
		}
		return 0;
	}

	private int compareElo(PlayerCard p1, PlayerCard p2) {
		Player player1 = p1.getPlayer();
		Player player2 = p2.getPlayer();

		if (player2.getElo().compareTo(player1.getElo()) == 0) {
			return compareTitle(player1, player2);
		} else {
			return player2.getElo().compareTo(player1.getElo());
		}
	}

	private int compareNWZ(PlayerCard p1, PlayerCard p2) {
		Player player1 = p1.getPlayer();
		Player player2 = p2.getPlayer();

		if (player2.getNationalElo().compareTo(player1.getNationalElo()) == 0) {
			return compareTitle(player1, player2);
		} else {
			return player2.getNationalElo().compareTo(player1.getNationalElo());
		}
	}

	private int compareEstimatedElo(PlayerCard p1, PlayerCard p2) {
		Player player1 = p1.getPlayer();
		Player player2 = p2.getPlayer();

		if (player2.getEstimatedElo().compareTo(player1.getEstimatedElo()) == 0) {
			return compareTitle(player1, player2);
		} else {
			return player2.getEstimatedElo().compareTo(player1.getEstimatedElo());
		}
	}
	
	private int compareTitle(Player player1, Player player2) {
		Title titlePlayer1 = player1.getTitle();
		Title titlePlayer2 = player2.getTitle();
		if (titlePlayer1.compareTo(titlePlayer2) == 0) {
			return compareAlpabeticaly(player1, player2);
		} else {
			return titlePlayer1.compareTo(titlePlayer2);
		}
	}

	private int compareAlpabeticaly(Player player1, Player player2) {
		String lastNameP1 = player1.getLastName();
		String lastNameP2 = player2.getLastName();
		if (lastNameP1.compareToIgnoreCase(lastNameP2) == 0) {
			return player1.getFirstName().compareToIgnoreCase(player2.getFirstName());
		} else return lastNameP1.compareToIgnoreCase(lastNameP2);
	}

}
