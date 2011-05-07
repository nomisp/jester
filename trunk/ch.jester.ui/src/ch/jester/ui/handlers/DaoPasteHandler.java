package ch.jester.ui.handlers;

import java.util.List;

import ch.jester.common.ui.handlers.ClonePasteHandler;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.ui.player.editor.ctrl.DaoController;

public class DaoPasteHandler extends ClonePasteHandler<IDaoObject> {

	@Override
	public Object handlePaste(List<IDaoObject> pPasted) {
		DaoController<IDaoObject> controller = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), DaoController.class);
		controller.addDaoObject(pPasted);
		
		//wenn nur 1 Objekt kopiert wurde, wird der editor geöffnet
		if(getPasteCount()==1){
			// selektiert im UI
			setSelection(getActivePartFromEvent().getSite().getId(), pPasted.get(0));
	
			// öffne Editor
			controller.openEditor(pPasted.get(0));
		}
		return null;
	}

}
