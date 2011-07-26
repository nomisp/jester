package ch.jester.system.swiss.dutch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.ui.forms.editor.FormEditor;

import ch.jester.common.settings.SettingHelper;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.PlayerCard;
import ch.jester.model.Round;
import ch.jester.model.SettingItem;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.model.util.ColorPreference;
import ch.jester.model.util.PlayerColor;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.exceptions.TournamentFinishedException;
import ch.jester.system.pairing.impl.PairingHelper;
import ch.jester.system.ranking.impl.RankingHelper;
import ch.jester.system.swiss.dutch.internal.SwissDutchSystemActivator;
import ch.jester.system.swiss.dutch.ui.SwissDutchSettingsPage;
import ch.jester.system.swiss.dutch.ui.nl1.Messages;
import ch.jester.system.swiss.dutch.util.FirstRoundColorPref;
import ch.jester.system.swiss.dutch.util.PlayerComparator;

/**
 * Paarungsalgorithmus für Paarungen nach Schweizer System
 * basierend auf dem Rating (Wertungszahl) (The Dutch System)
 * Beschreibung der FIDE: <link>http://www.fide.com/fide/handbook.html?id=83&view=article</link>
 */
public class SwissDutchPairingAlgorithm implements IPairingAlgorithm {
	private ILogger mLogger;
	private Category category;
	private SwissDutchSettings settings;
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private LinkedList<PlayerCard> unpairedPlayers = new LinkedList<PlayerCard>();
	private List<Pairing> pairings = new ArrayList<Pairing>();
	private List<List<PlayerCard>> scoreBrackets = new ArrayList<List<PlayerCard>>();
	private Round nextRound;
	private boolean firstRound = true;
	
	public SwissDutchPairingAlgorithm() {
		mLogger = SwissDutchSystemActivator.getDefault().getActivationContext().getLogger();
	}

	@Override
	public List<Pairing> executePairings(Tournament tournament) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException {
		if (settings == null) loadSettings(tournament);
		List<Pairing> allPairings = new ArrayList<Pairing>();
		for (Category category : tournament.getCategories()) {
			allPairings.addAll(executePairings(category));
		}
		return allPairings;
	}

	@Override
	public List<Pairing> executePairings(Category category) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException {
		this.category = category;
		if (settings == null) loadSettings(category.getTournament());
		firstRound = category.getRounds().get(0).getPairings().size() == 0;
		List<Round> finishedRounds = PairingHelper.getFinishedRounds(category);
		if (!firstRound && (finishedRounds.size() + PairingHelper.getOpenRounds(category).size() != category.getRounds().size())) throw new NotAllResultsException();
		if (finishedRounds.size() == category.getRounds().size()) throw new TournamentFinishedException();
		nextRound = finishedRounds.size() > 0 ? category.getRounds().get(finishedRounds.size()) : category.getRounds().get(0);
		
		List<PlayerCard> playerCards = category.getPlayerCards();
		Collections.sort(playerCards, new PlayerComparator(settings.getRatingType()));
		generateScoreBrackets(playerCards);
		if (firstRound) {
			calculateColorsForFirstRound(playerCards);
		} else {
			for (PlayerCard playerCard : playerCards) {
				calculateColorPreference(playerCard);
			}
		}
		
		for (List<PlayerCard> scoreBracket : scoreBrackets) {
			for (int i = 0; i < scoreBracket.size()/2; i++) {
				unpairedPlayers.add(scoreBracket.get(i));
			}
			int s1 = 0;
			int s2 = scoreBracket.size() / 2;
			while (!unpairedPlayers.isEmpty()) {
				for (int i = s2; i < scoreBracket.size(); i++) {
					PlayerCard player = unpairedPlayers.removeFirst();
					Pairing pair = pairCurrentPlayer(player, scoreBracket, i);
					if (pair != null) {
						pair.setRound(nextRound);
						pairings.add(pair);
					}
				}
			}
		}
		
		return this.pairings;
	}

	/**
	 * Erzeugen der Score-Brackets (Liste mit Punktgleichen Spielern)
	 * @param playerCards	Nach Punkten sortierte Liste der Spieler
	 */
	private void generateScoreBrackets(List<PlayerCard> playerCards) {
		List<PlayerCard> scoreBracket = new ArrayList<PlayerCard>();
		double scoreBracketPoints = playerCards.get(0).getPoints();
		for (PlayerCard playerCard : playerCards) {
			if (playerCard.getPoints() == scoreBracketPoints) {
				scoreBracket.add(playerCard);
			} else {
				this.scoreBrackets.add(scoreBracket);
				scoreBracket = new ArrayList<PlayerCard>();
				scoreBracketPoints = playerCard.getPoints();
				scoreBracket.add(playerCard);
			}
		}
		this.scoreBrackets.add(scoreBracket); // Die letzte Liste
	}
	
	/**
	 * Paaren des aktuellen Spielers
	 * @param player
	 * @param s2 Index in Scorebracket Subgroup S2
	 * @return
	 */
	private Pairing pairCurrentPlayer(PlayerCard player, List<PlayerCard> scoreBracket, int s2) {
		Pairing pair = new Pairing();
		PlayerCard opponent = scoreBracket.get(s2);
		List<PlayerCard> playedOpponents = RankingHelper.getOpponents(player, category.getRounds());
		if (!firstRound && playedOpponents.contains(opponent)) {
			return null;	// Die Spieler haben bereits gegeneinander gespielt
		}
		
		/*
		 * E. Colour Allocation Rules
		 * For each pairing apply (with descending priority):
		 * E.1
		 * Grant both colour preferences.
		 * E.2
		 * Grant the stronger colour preference.
		 * E.3
		 * Alternate the colours to the most recent round in which they played with different colours.
		 * E.4
		 * Grant the colour preference of the higher ranked player.
		 * E.5
		 * In the first round all even numbered players in S1 will receive a colour different 
		 * from all odd numbered players in S1.	-> calculateColorsForFirstRound
		 */
		ColorPreference playerColorPref = player.getColorPref();
		ColorPreference opponentColorPref = opponent.getColorPref();
		if (playerColorPref == opponentColorPref) { // -> E.3
			// E3
			String playerColors = player.getColors();
			String opponentColors = opponent.getColors();
			if (playerColors != null && !playerColors.isEmpty() && opponentColors != null && !opponentColors.isEmpty()) {
				for (int i = playerColors.length() - 1; i > 0; i--) {
					char lastColorPlayer = playerColors.charAt(i);
					if (lastColorPlayer != opponentColors.charAt(i)) {
						if (lastColorPlayer == 'w') {
							pair.setWhite(player);
							pair.setBlack(opponent);
							return pair;
						} else {
							pair.setWhite(opponent);
							pair.setBlack(player);
							return pair;
						}
					}
				}
			}
			
			// E4
			if (player.getColorPref() == ColorPreference.ABSOLUTE_WHITE 
					|| player.getColorPref() == ColorPreference.STRONG_WHITE 
					|| player.getColorPref() == ColorPreference.MILD_WHITE) {
				pair.setWhite(player);
				pair.setBlack(opponent);
				return pair;
			} else {
				pair.setWhite(opponent);
				pair.setBlack(player);
				return pair;
			}
		} else {
			// Absolute ColorPreference werden direkt zugeteilt
			if (playerColorPref == ColorPreference.ABSOLUTE_WHITE) {
				pair.setWhite(player);
				pair.setBlack(opponent);
			} else if (playerColorPref == ColorPreference.ABSOLUTE_BLACK) {
				pair.setWhite(opponent);
				pair.setBlack(player);
			}
			if (opponentColorPref == ColorPreference.ABSOLUTE_WHITE) {
				pair.setWhite(opponent);
				pair.setBlack(player);
			} else if (opponentColorPref == ColorPreference.ABSOLUTE_BLACK) {
				pair.setWhite(player);
				pair.setBlack(opponent);
			}
			switch (playerColorPref) {
			case STRONG_WHITE:
				if (opponentColorPref == ColorPreference.NONE 
						|| opponentColorPref == ColorPreference.STRONG_BLACK
						|| opponentColorPref == ColorPreference.MILD_BLACK
						|| opponentColorPref == ColorPreference.MILD_WHITE) {
					pair.setWhite(player);
					pair.setBlack(opponent);
				}
				break;
			case STRONG_BLACK:
				if (opponentColorPref == ColorPreference.NONE 
						|| opponentColorPref == ColorPreference.STRONG_WHITE
						|| opponentColorPref == ColorPreference.MILD_WHITE
						|| opponentColorPref == ColorPreference.MILD_BLACK) {
					pair.setWhite(opponent);
					pair.setBlack(player);
				}
				break;
			case MILD_WHITE:
				if (opponentColorPref == ColorPreference.NONE 
						|| opponentColorPref == ColorPreference.STRONG_BLACK
						|| opponentColorPref == ColorPreference.MILD_BLACK) {
					pair.setWhite(player);
					pair.setBlack(opponent);
				} else if (opponentColorPref == ColorPreference.STRONG_WHITE) {
					pair.setWhite(opponent);
					pair.setBlack(player);
				}
				break;
			case MILD_BLACK:
				if (opponentColorPref == ColorPreference.NONE 
						|| opponentColorPref == ColorPreference.STRONG_WHITE
						|| opponentColorPref == ColorPreference.MILD_WHITE
						|| opponentColorPref == ColorPreference.MILD_BLACK) {
					pair.setWhite(opponent);
					pair.setBlack(player);
				} else if (opponentColorPref == ColorPreference.STRONG_BLACK) {
					pair.setWhite(player);
					pair.setBlack(opponent);
				}
				break;
			case NONE:
				if (opponentColorPref == ColorPreference.STRONG_WHITE
						|| opponentColorPref == ColorPreference.MILD_WHITE) {
					pair.setWhite(opponent);
					pair.setBlack(player);
				} else if (opponentColorPref == ColorPreference.STRONG_BLACK
						|| opponentColorPref == ColorPreference.MILD_BLACK) {
					pair.setWhite(player);
					pair.setBlack(opponent);
				}
				break;
	
			default:
				break;
			}
		}

		return pair;
	}
	
	/**
	 * Ermitteln der Farbpräferenz eines Spielers
	 * 
	 * Colour differences and colour preferences
	 * 
	 * The colour difference of a player is the number of games played with white minus the number of games played with black by this player.
	 * After a round the colour preference can be determined for every player.
	 * 
	 * a.    An absolute colour preference occurs when a player's colour difference is greater that 1 or less than -1, 
	 *       or when a player played with the same colour in the two latest rounds. The preference is white when the 
	 *       colour difference is << 0 or when the last two games were played with black, otherwise black. In this 
	 *       case the (obligatory) colour is already written down on the score card. (This rule may not be in effect 
	 *       when pairing players with a score of over 50% in the last round if this is necessary to avoid additional 
	 *       floaters).
	 * b.    A strong colour preference occurs when a player's colour difference is unequal to zero. The preference is 
	 * 		 white when the colour difference is < 0, black otherwise.
	 * c.    A mild colour preference occurs when a player's colour difference is zero, the preference being to 
	 * 		 alternate the colour with respect to the previous game. In this case the colour difference is written down 
	 * 		 as +0 or -0 depending on the colour of the previous game (white or black respectively).
	 *       Before the first round the colour preference of one player (often the highest one) is determined by lot.
	 * d.    While pairing an odd-number round players having a strong colour preference (players who have had an 
	 * 		 odd number of games before by any reason) shall be treated like players having an absolute colour 
	 * 		 preference as long as this does not result in additional downfloaters (GA 2001)
	 * e.    While pairing an even-numbered round players having a mild colour preference (players who have had 
	 * 		 an even number of games before by any reason) shall be treated and counted as if they would have a 
	 *       mild colour preference of that kind (white resp. Black) which reduces the value of x (see A.8) as long as 
	 *       this does not result in additional downfloaters, (GA 2001)
	 * 
	 * Quelle:<link>http://www.fide.com/fide/handbook.html?id=83&view=article</link>
	 * @param player
	 * @return ColorPreference
	 */
	private ColorPreference calculateColorPreference(PlayerCard player) {
		// Bestimmen der Color-Difference
		int colorDifference = 0;
		char[] colors = player.getColors() != null ? player.getColors().toCharArray() : new char[0];
		for (char c : colors) {
			if (c == 'w') {
				colorDifference++;
			} else if (c == 'b') {
				colorDifference--;
			}
		}
		// Absolute color preference
		if (colors.length > 1 && colors[colors.length-2] == colors[colors.length-1]) {
			if (colors[colors.length] == 'w') {
				player.setColorPref(ColorPreference.ABSOLUTE_BLACK);
				return ColorPreference.ABSOLUTE_BLACK;
			} else {
				player.setColorPref(ColorPreference.ABSOLUTE_WHITE);
				return ColorPreference.ABSOLUTE_WHITE;
			}
		} else if ((player.getColorPref() != ColorPreference.ABSOLUTE_WHITE 
				&& player.getColorPref() != ColorPreference.ABSOLUTE_BLACK) && colorDifference < -1) {
			player.setColorPref(ColorPreference.ABSOLUTE_WHITE);
			return ColorPreference.ABSOLUTE_WHITE;
		} else if ((player.getColorPref() != ColorPreference.ABSOLUTE_WHITE 
				&& player.getColorPref() != ColorPreference.ABSOLUTE_BLACK) && colorDifference > 1) {
			player.setColorPref(ColorPreference.ABSOLUTE_BLACK);
			return ColorPreference.ABSOLUTE_BLACK;
		}
		// strong color preference
		if (colorDifference < 0) {
			player.setColorPref(ColorPreference.STRONG_WHITE);
			return ColorPreference.STRONG_WHITE;
		} else if (colorDifference > 0) {
			player.setColorPref(ColorPreference.STRONG_BLACK);
			return ColorPreference.STRONG_BLACK;
		}
		// mild color preference
		if (colorDifference == 0) {
			if (colors.length > 0) {
				if (colors[colors.length-1] == 'w') {
					player.setColorPref(ColorPreference.MILD_BLACK);
				} else if (colors[colors.length-1] == 'b') {
					player.setColorPref(ColorPreference.MILD_WHITE);
				}
			}
		}
		// TODO Peter: d und e kann erst später gemacht werden!
		return ColorPreference.NONE;
	}
	
	/**
	 * Bestimmen der Farben für die erste Runde
	 * Je nach Einstellung in den Settings
	 * @param playerCards
	 */
	private void calculateColorsForFirstRound(List<PlayerCard> playerCards) {
		FirstRoundColorPref firstRoundColor = FirstRoundColorPref.WHITE;
		if (settings != null) {
			firstRoundColor = settings.getFirstRoundColor();
		}
		if (firstRoundColor == FirstRoundColorPref.RANDOM) {
			Random rdm = new Random();
			firstRoundColor = rdm.nextInt(2) == 0 ? FirstRoundColorPref.WHITE : FirstRoundColorPref.BLACK;
		}
		
		for (int i = 0; i < playerCards.size(); i++) {
			PlayerCard playerCard = playerCards.get(i);
			if ((i+1) % 2 == 0) { // gerade Spielernummer
				if (firstRoundColor == FirstRoundColorPref.WHITE) { // Spieler 1; 1. Runde weiss
					playerCard.setColorPref(ColorPreference.ABSOLUTE_BLACK);
				} else {
					playerCard.setColorPref(ColorPreference.ABSOLUTE_WHITE);
				}
			} else { // Ungerade Spielernummer
				if (firstRoundColor == FirstRoundColorPref.WHITE) {
					playerCard.setColorPref(ColorPreference.ABSOLUTE_WHITE); // Spieler 1; 1. Runde weiss
				} else {
					playerCard.setColorPref(ColorPreference.ABSOLUTE_BLACK);
				}
			}
		}
	}
	
	/**
	 * Ermitteln des x-Wertes
	 * 
	 * Definition of "x"
	 *
	 * The number of pairings which can be made in a score bracket, either homogeneous or heterogeneous, 
	 * not fulfilling all colour preferences, is represented by the symbol x.
	 * x can be calculated as follows:
	 * w = number of players having a colour preference white.
	 * b = number of players having a colour preference black.
	 * q = number of players in the score bracket divided by 2, rounded upwards.
	 * If b >> w then x = b-q, else x = w-q.
	 * 
	 * Quelle:<link>http://www.fide.com/fide/handbook.html?id=83&view=article</link>
	 * 
	 * @param scoreBracket Liste mit den punktgleichen Spielern
	 * @return x-Wert
	 */
	private int calculateXValue(List<PlayerCard> scoreBracket) {
		int x = 0;
		int w = 0;
		int b = 0;
		for (PlayerCard playerCard : scoreBracket) {
			switch(playerCard.getColorPref()) {
				case ABSOLUTE_WHITE: w++;
				break;
				case STRONG_WHITE: w++;
				break;
				case MILD_WHITE: w++;
				break;
				case ABSOLUTE_BLACK: b++;
				break;
				case STRONG_BLACK: b++;
				break;
				case MILD_BLACK: b++;
				break;
				case NONE: // Nothing to do
			}
		}
		int q = Math.round(scoreBracket.size() / 2);
		x = b > w ? b - q : w - q;
		return x;
	}

	/**
	 * Laden der Einstellungen aus der Datenbank
	 * @param tournament
	 */
	private void loadSettings(Tournament tournament) {
		if (settings == null) settings = new SwissDutchSettings();
		IDaoService<SettingItem> settingItemPersister = mServiceUtil.getDaoServiceByEntity(SettingItem.class);
		Query namedQuery = settingItemPersister.createNamedQuery("SettingItemByTournament"); //$NON-NLS-1$
		namedQuery.setParameter("tournament", tournament); //$NON-NLS-1$
		try {
			SettingItem settingItem = (SettingItem)namedQuery.getSingleResult();
			SettingHelper<SwissDutchSettings> settingHelper = new SettingHelper<SwissDutchSettings>();
			if (settingItem != null) settings = settingHelper.restoreSettingObject(settings, settingItem);
		} catch (NoResultException e) {
			// Nothing to do
			mLogger.info("SettingItem not found in Database"); //$NON-NLS-1$
		}
	}

	@Override
	public AbstractSystemSettingsFormPage getSettingsFormPage(FormEditor editor, Tournament tournament) {
		if (settings == null) loadSettings(tournament);
		return new SwissDutchSettingsPage(settings, editor, "SwissDutchSettingsPage", Messages.SwissDutchPairingAlgorithm_settingsPage_title); //$NON-NLS-1$
	}
}
