package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.ui.GotoPageField;
import ch.jester.ui.contentprovider.PageController;
import ch.jester.ui.player.editor.view.PlayersView;

public class PageHandler extends AbstractCommandHandler {
	ServiceUtility su = new ServiceUtility();
	@Override
	public Object executeInternal(ExecutionEvent event) {

		
		
		@SuppressWarnings("rawtypes")
		PageController controller = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), PageController.class);
		GotoPageField gpf = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), GotoPageField.class);
		System.out.println(gpf.getText());
		PlayersView view = (PlayersView) getActivePartFromEvent();
		view.ggg();
		
		if(event.getCommand().getId().contains("back")){
			controller.prevPage();
		}else{
			controller.nextPage();
		}
		return null;
	}

}
