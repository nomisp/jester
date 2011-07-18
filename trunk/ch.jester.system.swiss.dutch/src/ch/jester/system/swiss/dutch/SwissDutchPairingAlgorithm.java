package ch.jester.system.swiss.dutch;

import java.util.Collections;
import java.util.List;

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
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.exceptions.TournamentFinishedException;
import ch.jester.system.pairing.impl.PairingHelper;
import ch.jester.system.swiss.dutch.internal.SwissDutchSystemActivator;
import ch.jester.system.swiss.dutch.ui.SwissDutchSettingsPage;
import ch.jester.system.swiss.dutch.ui.nl1.Messages;
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
	
	public SwissDutchPairingAlgorithm() {
		mLogger = SwissDutchSystemActivator.getDefault().getActivationContext().getLogger();
	}

	@Override
	public List<Pairing> executePairings(Tournament tournament) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pairing> executePairings(Category category) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException {
		List<Round> openRounds = PairingHelper.getOpenRounds(category);
		List<Round> finishedRounds = PairingHelper.getFinishedRounds(category);
		if (openRounds.size() + finishedRounds.size() != category.getRounds().size()) throw new NotAllResultsException();
		
		Round nextRound = openRounds != null && openRounds.size() > 0 ? openRounds.get(0) : null;
		if (nextRound == null && finishedRounds.size() == category.getRounds().size()) throw new TournamentFinishedException();
		List<PlayerCard> playerCards = category.getPlayerCards();
		Collections.sort(playerCards, new PlayerComparator(settings.getRatingType()));
		
		return null;
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
