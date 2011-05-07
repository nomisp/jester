package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.services.IEvaluationService;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.ui.GotoPageField;
import ch.jester.ui.contentprovider.PageController;

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
		IEvaluationService evalservice = (IEvaluationService) getActivePartFromEvent().getSite().getService(IEvaluationService.class);
		evalservice.requestEvaluation("ch.jester.properties.controlled");
	}

}
