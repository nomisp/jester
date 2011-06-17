package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.handlers.BeanStringPropertyTester;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Tournament;

public class ActivateHandler extends AbstractCommandHandler {

	@Override
	public Object executeInternal(ExecutionEvent event) {
		Tournament selectedTournament = mSelUtility.getFirstSelectedAs(Tournament.class);
		selectedTournament.setActive(true);
		IDaoService<Tournament> tdao = getServiceUtil().getDaoServiceByEntity(Tournament.class);
		tdao.save(selectedTournament);
		tdao.close();
		UIUtility.reevaluateProperty(BeanStringPropertyTester.ID);
		return null;
	}

}
