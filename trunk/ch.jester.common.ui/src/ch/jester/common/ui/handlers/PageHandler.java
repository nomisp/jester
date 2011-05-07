package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.services.IEvaluationService;

import ch.jester.common.ui.databinding.PageController;
import ch.jester.common.ui.filter.GotoPageField;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.util.ServiceUtility;


public class PageHandler extends AbstractCommandHandler {
	ServiceUtility su = new ServiceUtility();
	@Override
	public Object executeInternal(ExecutionEvent event) {
		@SuppressWarnings("rawtypes")
		PageController controller = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), PageController.class);
		
		if(event.getCommand().getId().contains("back")){
			controller.prevPage();
			reevaluate();
		}else{
			GotoPageField gpf = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), GotoPageField.class);
			String entredText = null;
			if(gpf != null){
				entredText = gpf.getText();
			}
			if(entredText!=null && entredText.length()!=0){
				try{
					int i = Integer.parseInt(entredText);
					controller.gotoPage(i);
					reevaluate();
					return null;
				}catch(Exception e){}
			}
			controller.nextPage();
			reevaluate();
		}
		return null;
	}
	private void reevaluate(){
		UIUtility.reevaluateProperty("ch.jester.properties.controlled");
	}

}
