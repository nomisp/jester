package ch.jester.system.vollrundig;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.progress.UIJob;

import ch.jester.common.settings.SettingHelper;
import ch.jester.common.ui.utility.UIUtility;
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
import ch.jester.system.api.pairing.ITournamentEditorConstraintsProvider;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.api.pairing.ui.TournamentEditorConstraints;
import ch.jester.system.exceptions.NoStartingNumbersException;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.pairing.impl.PairingHelper;
import ch.jester.system.ranking.impl.RankingHelper;
import ch.jester.system.vollrundig.internal.VollrundigSystemActivator;
import ch.jester.system.vollrundig.ui.RoundRobinSettingsPage;
import ch.jester.system.vollrundig.ui.nl1.Messages;

/**
 * Implementierung des Round-Robin Paarungsalgorithmuses
 * Erklärung zum Algorithmus: <link>http://www-i1.informatik.rwth-aachen.de/~algorithmus/algo36.php</link>
 *
 */
public class VollrundigPairingAlgorithm implements IPairingAlgorithm, ITournamentEditorConstraintsProvider {

	private ILogger mLogger;
	private Category category;
	private List<Round> playedRounds;
	private ServiceUtility mServiceUtil = new ServiceUtility();
	private RoundRobinSettings settings;
	private TournamentEditorConstraints editorConstraints;
	
	public VollrundigPairingAlgorithm() {
		mLogger = VollrundigSystemActivator.getDefault().getActivationContext().getLogger();
		editorConstraints = new TournamentEditorConstraints();
		editorConstraints.canAddRounds=false;
		editorConstraints.canRemoveRounds=false;
	}
	
	@Override
	public List<Pairing> executePairings(Tournament tournament) throws NotAllResultsException, PairingNotPossibleException {
		loadSettings(tournament);
		List<Pairing> allPairings = new ArrayList<Pairing>();
		for (Category category : tournament.getCategories()) {
			allPairings.addAll(executePairings(category));
		}
		this.settings = null;
		return allPairings;
	}

	@Override
	public List<Pairing> executePairings(Category category) throws PairingNotPossibleException, NotAllResultsException {
		this.category = category;
		if(alreadyPaired()){
			if(askToContinue()){
				resetPairings(category);
			}else{
				return null;
			}
			
		}
		
		loadSettings(category.getTournament()); // Falls das Paaren auf einem Turnier passiert

		playedRounds = RankingHelper.getFinishedRounds(category);
		List<Pairing> pairings = null;
		if (playedRounds.size() == 0) { // Neues Turnier bei einem Round-Robin Turnier können direkt alle Paarunugen ausgelost werden. 
			if (isPairingPossible()) {
				initPlayerNumbers();
				if (!isNumberOfPlayersEven()) { // Bei einer ungeraden Anzahl Spieler braucht es einen Dummy-Spieler für Freilose!
					PlayerCard dummyPlayer = ModelFactory.getInstance().createPlayerCard(category, null);
					dummyPlayer.setNumber(category.getPlayers().size()+1); // Die letzte Startnummer erhält der Dummy
					category.addPlayerCard(dummyPlayer);
				}
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
		}
		this.settings = null;
		return pairings;
	}
	
	/**
	 * Überprüfen ob die Anzahl Runden für die Anzahl Spieler stimmt.
	 * Gegebenenfalls fehlende Anzahl Runden anlegen
	 * @return false wenn weder Runden noch Spieler 
	 */
	private boolean isPairingPossible() throws PairingNotPossibleException {
		if (category.getPlayerCards().size() <= 1) {
			throw new PairingNotPossibleException("No players available in Category: " + category.getDescription()); //$NON-NLS-1$
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
		
		if (isNumberOfPlayersEven()) {	// Sollte immer der Fall sein, da ja ein Dummy Player eingefügt wurde bei ungerader Anzahl Spieler
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
				mLogger.debug("White: " + white + " Black: " + black); //$NON-NLS-1$ //$NON-NLS-2$
				Pairing p = createPairing(rounds, playerCards, i-1, white-1, black-1);
				if (p != null) pairings.add(p);
				
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
				}
			}
		}
		
		// Rückrunde mit vertauschten Farben?
		if (settings.getDoubleRounded()) {
			createRounds(rounds.size());	// Nochmals soviele Runden anlegen für die Rückrunde
			int rCnt = rounds.size()/2;
			ModelFactory modelFactory = ModelFactory.getInstance();
			for (int i = 0; i < rounds.size()/2; i++) {
				List<Pairing> roundPairings = rounds.get(i).getPairings();
				for (int p = 0; p < roundPairings.size(); p++) {
					Pairing pair = roundPairings.get(p);
					pairings.add(modelFactory.createPairing(pair.getBlack(), pair.getWhite(), rounds.get(rCnt+i)));
				}
			}
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
		PlayerCard white = playerCards[whiteIndex];
		PlayerCard black = playerCards[blackIndex];
		return white != null && black != null ? modelFactory.createPairing(white, black, rounds.get(roundIndex)) : null;
	}

	/**
	 * Laden der Einstellungen aus der Datenbank
	 * @param tournament
	 */
	private void loadSettings(Tournament tournament) {
		if (settings == null) settings = new RoundRobinSettings();
		IDaoService<SettingItem> settingItemPersister = mServiceUtil.getDaoServiceByEntity(SettingItem.class);
		Query namedQuery = settingItemPersister.createNamedQuery("SettingItemByTournament"); //$NON-NLS-1$
		namedQuery.setParameter("tournament", tournament); //$NON-NLS-1$
		try {
			SettingItem settingItem = (SettingItem)namedQuery.getSingleResult();
			SettingHelper<RoundRobinSettings> settingHelper = new SettingHelper<RoundRobinSettings>();
			if (settingItem != null) settings = settingHelper.restoreSettingObject(settings, settingItem);
		} catch (NoResultException e) {
			// Nothing to do
			mLogger.info("SettingItem not found in Database"); //$NON-NLS-1$
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AbstractSystemSettingsFormPage getSettingsFormPage(FormEditor editor, Tournament tournament) {
		this.settings = null;
		if (settings == null) loadSettings(tournament);
		return new RoundRobinSettingsPage(settings, editor, !tournament.getStarted(), "RoundRobinSettingsPage", Messages.VollrundigPairingAlgorithm__settingsPage_title); //$NON-NLS-1$
	}
	
	private boolean alreadyPaired() {
		
		if (category.getRounds().size() > 0 && category.getRounds().get(0).getPairings().size() > 0) {
			return true;
		}
		return false;
	}
	private boolean askToContinue(){
		final Shell shell = UIUtility.getActiveWorkbenchWindow().getShell();
		boolean continuePairing = showWarningAlreadyPaired(shell);
		return continuePairing;
	}
	
	/**
	 * Anzeigen der Warnmeldung wenn bereits gepaart wurde
	 * @param shell
	 * @return true wenn die Paarung fortgesetzt werden soll
	 */
	private boolean showWarningAlreadyPaired(final Shell shell) {
		//final boolean retVal;
		UIJob uiJob = new UIJob("Question-Message") { //$NON-NLS-1$

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				boolean retVal = MessageDialog.openQuestion(shell, "Category / Tournament already paired", "Should the category or tournament be paired again?\nAll pairings created before will be lost.");
				return retVal ? Status.OK_STATUS : Status.CANCEL_STATUS;
			}
		};
		uiJob.schedule();
		try {
			uiJob.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return uiJob.getResult().isOK();
	}

	/**
	 * Zurücksetzen der bereits gemachten Paarungen
	 * Es werden alle Pairings aus der Runde gelöscht.
	 * @param cat
	 */
	private void resetPairings(Category cat) {
		for (Round round : cat.getRounds()) {
			round.removeAllPairings(round.getPairings());
		}
	}

	@Override
	public TournamentEditorConstraints getTournamentEditorConstraints() {
		return editorConstraints;
	}
}
