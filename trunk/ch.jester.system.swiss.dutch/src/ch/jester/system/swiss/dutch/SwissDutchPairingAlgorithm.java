package ch.jester.system.swiss.dutch;

import java.util.List;

import org.eclipse.ui.forms.editor.FormEditor;

import ch.jester.common.settings.ISettingObject;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Tournament;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.swiss.dutch.internal.SwissDutchSystemActivator;

/**
 * Paarungsalgorithmus für Paarungen nach Schweizer System
 * basierend auf dem Rating (Wertungszahl) (The Dutch System)
 * Beschreibung der FIDE: <link>http://www.fide.com/fide/handbook.html?id=83&view=article</link>
 * @author Peter
 *
 */
public class SwissDutchPairingAlgorithm implements IPairingAlgorithm {
	private ILogger mLogger;
	private Category category;
	
	public SwissDutchPairingAlgorithm() {
		mLogger = SwissDutchSystemActivator.getDefault().getActivationContext().getLogger();
	}

	@Override
	public List<Pairing> executePairings(Tournament tournament) throws NotAllResultsException, PairingNotPossibleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pairing> executePairings(Category category) throws NotAllResultsException, PairingNotPossibleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractSystemSettingsFormPage<ISettingObject> getSettingsFormPage(FormEditor editor, Tournament tournament) {
		// TODO Auto-generated method stub
		return null;
	}

}
