package ch.jester.system.vollrundig;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.settings.SettingHelper;
import ch.jester.common.utility.ExceptionUtility;
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
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.exceptions.NoStartingNumbersException;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.pairing.impl.PairingHelper;
import ch.jester.system.vollrundig.internal.VollrundigSystemActivator;

/**
 * Implementierung des Round-Robin Paarungsalgorithmuses
 * Erklärung zum Algorithmus: <link>http://www-i1.informatik.rwth-aachen.de/~algorithmus/algo36.php</link>
 * @author Peter
 *
 */
public class VollrundigPairingAlgorithm implements IPairingAlgorithm {

	private ILogger mLogger;
	private Category category;
	private List<Round> playedRounds;
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private RoundRobinSettings settings;
	
	public VollrundigPairingAlgorithm() {
		mLogger = VollrundigSystemActivator.getDefault().getActivationContext().getLogger();
	}
	
	@Override
	public List<Pairing> executePairings(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException, PairingNotPossibleException {
		loadSettings(tournament);
		List<Pairing> allPairings = new ArrayList<Pairing>();
		for (Category category : tournament.getCategories()) {
			allPairings.addAll(executePairings(category, pMonitor));
		}
		return allPairings;
	}

	@Override
	public List<Pairing> executePairings(Category category, IProgressMonitor pMonitor) throws PairingNotPossibleException, NotAllResultsException {
		this.category = category;
		loadSettings(category.getTournament());
		playedRounds = category.getPlayedRounds();
		List<Pairing> pairings = null;
		if (playedRounds.size() == 0) { // Neues Turnier bei einem Round-Robin Turnier können direkt alle Paarunugen ausgelost werden. 
			if (isPairingPossible()) {
				initPlayerNumbers();
				try {
					pairings = createPairings();
				} catch (Exception e) {
					Throwable realException = ExceptionUtility.getRealException(e);
					if (realException instanceof NoStartingNumbersException) {
						mLogger.info(realException.getMessage(), realException);
						throw new PairingNotPossibleException(realException.getMessage());
					} else {
						e.printStackTrace();
					}
				}
			}
		} else {
//			TODO Peter: Erneutes Paaren, wenn während einem laufenden Turnier ein neuer Spieler hinzu kommt.
//			checkResults();
			
		}
		IDaoService<Category> categoryPersister = mServiceUtil.getDaoServiceByEntity(Category.class);
		categoryPersister.save(category);
		return pairings;
	}
	
	/**
	 * Überprüfen ob die Anzahl Runden für die Anzahl Spieler stimmt.
	 * Gegebenenfalls fehlende Anzahl Runden anlegen
	 * @return false wenn weder Runden noch Spieler 
	 */
	private boolean isPairingPossible() throws PairingNotPossibleException {
		if (category.getPlayerCards().size() == 0) {
			throw new PairingNotPossibleException("No players available in Category: " + category.getDescription());
		}
		if (isNumberOfPlayersEven()) {
			createRounds((category.getPlayerCards().size()-1)-category.getRounds().size());
		} else {
			createRounds(category.getPlayerCards().size()-category.getRounds().size());
		}
		// Falls es eine Rückrunde gibt brauch es nochmals soviele Runden
		if (settings.getDoubleRounded()) {
			createRounds(category.getRounds().size());
		}
		
		return true;
	}
	
	/**
	 * Überprüfen ob die Anzahl der Spieler gerade oder ungerade ist
	 * @return true, wenn die Anzahl Spieler gerade ist
	 */
	private boolean isNumberOfPlayersEven() {
		int nrPlayers = category.getPlayerCards().size();
		return nrPlayers % 2 == 0;
	}
	
	/**
	 * Anlegen der fehlenden Runden
	 * Bei gerader Anzahl Spieler: n-1 bei ungerader Anzahl Spieler n
	 * @param numberOfRounds Anzahl anzulegender Runden
	 * @return true wenn Runden angelegt werden mussten.
	 */
	private boolean createRounds(int numberOfRounds) {
		if (numberOfRounds == 0) return false;
		int cnt = category.getRounds().size();
		ModelFactory modelFactory = ModelFactory.getInstance();
		for (int i = 0; i < numberOfRounds; i++) {
			Round round = modelFactory.createRound(++cnt);
			round.setCategory(category);
			category.addRound(round);
		}
		return true;
	}
	
	/**
	 * Zuweisen der Startnummern zu den Spielern
	 * Darf nur gemacht werden, wenn es sich um die erste Runde handelt!
	 */
	private void initPlayerNumbers() {
		List<PlayerCard> playerCards = category.getPlayerCards();
		int numberOfPlayers = playerCards.size();
		List<Integer> startingNumbers = PairingHelper.createRandomStartingNumbers(numberOfPlayers);
		for (int i = 0; i < numberOfPlayers; i++) {
			playerCards.get(i).setNumber(startingNumbers.get(i));
		}
	}
	
	/**
	 * Erzeugt die Paarungen nach dem Round-Robin Algortithmus
	 * @return Liste mit den Paarungen
	 * @throws NoStartingNumbersException Falls die Startnummern noch nicht zugewiesen wurden kann keine Auslosung gemacht werden
	 */
	private List<Pairing> createPairings() throws NoStartingNumbersException {
		List<Pairing> pairings = new ArrayList<Pairing>();
		List<Round> rounds = category.getRounds();
		PlayerCard[] playerCards = PairingHelper.getOrderedPlayerCards(category.getPlayerCards());
		int numberOfPlayers = playerCards.length;
//		boolean secondRound = settings.getDoubleRounded();	// Vorerst Keine Rückrunde!
		
		if (isNumberOfPlayersEven()) {
			int nrOfRounds = numberOfPlayers - 1;
			
			for (int i = 1; i <= nrOfRounds; i++) {
				int white = numberOfPlayers;
				int black = i;
				// weiss, schwarz?
				if(i % 2 != 0) {
					int tmp = white;
					white = black;
					black = tmp;
				}
				mLogger.debug("White: " + white + " Black: " + black);
				pairings.add(createPairing(rounds, playerCards, i-1, white-1, black-1));
				
				for (int k = 1; k <= numberOfPlayers/2-1; k++) {
					if (i-k < 0) {
						white = nrOfRounds+(i-k);
					} else {
						white = (i-k) % nrOfRounds;
						white = white == 0 ? nrOfRounds : white; // 0 -> n-1
					}
					
					black = (i+k) % nrOfRounds;
					black = black == 0 ? nrOfRounds : black; // 0 -> n-1
					
					if (k % 2 == 0) {
						int tmp = white;
						white = black;
						black = tmp;
					}
					pairings.add(createPairing(rounds, playerCards, i-1, white-1, black-1));
					
					mLogger.debug("White: " + white + " Black: " + black);
				}
			}
		}
		
		// Rückrunde mit vertauschten Farben?
		if (settings.getDoubleRounded()) {
			createRounds(rounds.size());	// Nochmals soviele Runden anlegen für die Rückrunde
			int pcnt = 0;
			ModelFactory modelFactory = ModelFactory.getInstance();
			for (int i = pairings.size(); i < rounds.size(); i++) {
				Pairing pairing = pairings.get(pcnt++);
				pairings.add(modelFactory.createPairing(pairing.getBlack(), pairing.getWhite(), rounds.get(i)));
			}
		}
		
		for (Pairing pairing : pairings) {
			System.out.println(pairing.toString());
		}
		return pairings;
	}

	/**
	 * Erzeugen der Pairing Entitäten
	 * @param rounds		Liste mit den Runden
	 * @param playerCards	Sortiertes Array mit den PlayerCards
	 * @param roundIndex	Index der Runde (List)
	 * @param whiteIndex	Index des weissen Spielers
	 * @param blackIndex	Index des schwarzen Spielers
	 * @return	Pairing
	 */
	private Pairing createPairing(List<Round> rounds, PlayerCard[] playerCards, int roundIndex, int whiteIndex, int blackIndex) {
		ModelFactory modelFactory = ModelFactory.getInstance();
		Pairing p = modelFactory.createPairing(playerCards[whiteIndex].getPlayer(), 
				playerCards[blackIndex].getPlayer(), 
				rounds.get(roundIndex));
		return p;
	}

	/**
	 * Laden der Einstellungen aus der Datenbank
	 * @param tournament
	 */
	private void loadSettings(Tournament tournament) {
		if (settings == null) settings = new RoundRobinSettings();
		IDaoService<SettingItem> settingItemPersister = mServiceUtil.getDaoServiceByEntity(SettingItem.class);
		Query namedQuery = settingItemPersister.createNamedQuery("SettingItemByTournament");
		namedQuery.setParameter("tournament", tournament);
		try {
			SettingItem settingItem = (SettingItem)namedQuery.getSingleResult();
			SettingHelper<RoundRobinSettings> settingHelper = new SettingHelper<RoundRobinSettings>();
			if (settingItem != null) settings = settingHelper.restoreSettingObject(settings, settingItem);
		} catch (NoResultException e) {
			// Nothing to do
			mLogger.info("SettingItem not found in Database");
		}
	}
}
