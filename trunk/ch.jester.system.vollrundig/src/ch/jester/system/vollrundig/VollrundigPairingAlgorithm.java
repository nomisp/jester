package ch.jester.system.vollrundig;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.utility.ExceptionUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.PlayerCard;
import ch.jester.model.Round;
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
	
	public VollrundigPairingAlgorithm() {
		mLogger = VollrundigSystemActivator.getDefault().getActivationContext().getLogger();
	}
	
	@Override
	public List<Pairing> executePairings(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException, PairingNotPossibleException {
		List<Pairing> allPairings = new ArrayList<Pairing>();
		for (Category category : tournament.getCategories()) {
			allPairings.addAll(executePairings(category, pMonitor));
		}
		return allPairings;
	}

	@Override
	public List<Pairing> executePairings(Category category, IProgressMonitor pMonitor) throws PairingNotPossibleException, NotAllResultsException {
		this.category = category;
		playedRounds = category.getPlayedRounds();
		List<Pairing> pairings = null;
		if (playedRounds.size() == 0) { // Neues Turnier
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
//			TODO Peter: Erneutes Paaren, wenn wärend einem laufenden Turnier ein neuer Spieler hinzu kommt.
//			checkResults();
			
		}
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
		
		return true;
	}
	
	/**
	 * Überprüfen ob die Anzahl der Spieler gerade oder ungerade ist
	 * @return true, wenn die Anzahl Spieler gerade ist
	 */
	private boolean isNumberOfPlayersEven() {
		int nrPlayers = category.getPlayers().size();
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
		IDaoService<Category> categoryPersister = mServiceUtil.getDaoServiceByEntity(Category.class);
		categoryPersister.save(category);
//		IDaoService<Category> catDao = VollrundigSystemActivator.getDefault().getActivationContext().getService(ICategoryDao.class);
//		catDao.save(category);
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
		boolean secondRound = false;	// Vorerst Keine Rückrunde!
		if (isNumberOfPlayersEven()) {
			int n = numberOfPlayers - 1;
			
			for (int i = 1; i <= n; i++) {
				int white = numberOfPlayers;
				int black = i;
				// weiss, schwarz?
				if(i % 2 != 0) {
					int tmp = white;
					white = black;
					black = tmp;
				}
				System.out.println("White: " + white + " Black: " + black);
				Pairing p = new Pairing();
				p.setWhite(playerCards[white-1].getPlayer());
				p.setBlack(playerCards[black-1].getPlayer());
				p.setRound(rounds.get(i-1));
				pairings.add(p);
				
				for (int k = 1; k <= numberOfPlayers/2-1; k++) {
					if (i-k < 0) {
						white = n+(i-k);
					} else {
						white = (i-k) % n;
						white = white == 0 ? n : white; // 0 -> n-1
					}
					
					black = (i+k) % n;
					black = black == 0 ? n : black; // 0 -> n-1
					
					if (k % 2 == 0) {
						int tmp = white;
						white = black;
						black = tmp;
					}
					p = new Pairing();
					p.setWhite(playerCards[white-1].getPlayer());
					p.setBlack(playerCards[black-1].getPlayer());
					p.setRound(rounds.get(i-1));
					pairings.add(p);
					
					System.out.println("White: " + white + " Black: " + black);
				}
			}
		}
		
		for (Pairing pairing : pairings) {
			System.out.println(pairing.toString());
		}
		return pairings;
	}

	/**
	 * Überprüfen, ob alle Resultate der letzten Runde erfasst wurden
	 * @throws NotAllResultsException
	 */
	private void checkResults() throws NotAllResultsException {
		Round lastRound = playedRounds.get(playedRounds.size()-1);
		for (Pairing pairing : lastRound.getPairings()) {
			if (pairing.getResult() == null) throw new NotAllResultsException();
		}
	}

}
