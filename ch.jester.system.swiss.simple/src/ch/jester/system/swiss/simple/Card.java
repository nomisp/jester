package ch.jester.system.swiss.simple;

import java.util.ArrayList;
import java.util.List;

import ch.jester.model.PlayerCard;

/**
 * WrapperKlasse für PlayerCards / temporäre Pairings
 * Um der PlayerCard Systemspezifische Erweiterungen zu ermöglichen
 * @author Peter
 *
 */
public class Card {

	private PlayerCard playerCard;
	private PlayerCard opponent;
	private List<PlayerCard> alreadyTried;
	
	public Card(PlayerCard playerCard) {
		this.playerCard = playerCard;
		this.alreadyTried = new ArrayList<PlayerCard>();
	}

	public PlayerCard getPlayerCard() {
		return playerCard;
	}

	public void setPlayerCard(PlayerCard playerCard) {
		this.playerCard = playerCard;
	}

	public List<PlayerCard> getAlreadyTried() {
		return alreadyTried;
	}

	public void setAlreadyTried(List<PlayerCard> alreadyTried) {
		this.alreadyTried = alreadyTried;
	}

	public PlayerCard getOpponent() {
		return opponent;
	}

	public void setOpponent(PlayerCard opponent) {
		this.opponent = opponent;
	}
	
}
