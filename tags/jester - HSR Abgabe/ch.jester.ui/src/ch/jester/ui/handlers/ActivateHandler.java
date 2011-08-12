package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.handlers.BeanStringPropertyTester;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Tournament;

/**
 * Handler um Turniere aktiv/inaktiv zu setzten.
 *
 */
public class ActivateHandler extends AbstractCommandHandler {
	public final String ACTIVATION_ID ="ch.jester.ui.tournamentactivation";
	@Override
	public Object executeInternal(ExecutionEvent event) {
		String parameter = mEvent.getParameter(ACTIVATION_ID);
		boolean setActive = Boolean.parseBoolean(parameter);
		Tournament selectedTournament = mSelUtility.getFirstSelectedAs(Tournament.class);
		selectedTournament.setActive(setActive);
		IDaoService<Tournament> tdao = getServiceUtil().getDaoServiceByEntity(Tournament.class);
		tdao.save(selectedTournament);
		tdao.close();
		UIUtility.reevaluateProperty(BeanStringPropertyTester.ID);
		return null;
	}

}
