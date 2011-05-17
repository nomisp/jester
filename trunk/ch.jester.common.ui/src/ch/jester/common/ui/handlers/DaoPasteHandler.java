package ch.jester.common.ui.handlers;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import ch.jester.common.ui.handlers.api.IHandlerAdd;
import ch.jester.common.ui.handlers.api.IHandlerEditor;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.persistency.IEntityObject;

/**
 * Eine Default PasteHandler Implementation.
 * <br>Regeln<br>
 * Die View wird <br>
	 * 1) nach einem {@link IAdaptable} für {@link IHandlerAdd}
	 * und {@link IHandlerEditor} gefragt <br>
	 * 2) das Objekt wird dem {@link IHandlerAdd} übergeben (muss zwingend
	 * regsitriert worden sein)<br>
	 * 3) gibt es ein {@link IHandlerEditor} und ist die Anzahl der einzufügenden Objekte 1 dann wird
	 * {@link IHandlerEditor#handleOpenEditor(Object)} aufgerufen
 */
public class DaoPasteHandler extends ClonePasteHandler<IEntityObject> {

	@Override
	public Object handlePaste(List<IEntityObject> pPasted) {
		@SuppressWarnings("unchecked")
		IHandlerAdd<IEntityObject> addCtrl = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), IHandlerAdd.class);
		@SuppressWarnings("unchecked")
		IHandlerEditor<IEntityObject> editorCtrl = AdapterUtility.getAdaptedObject(getActivePartFromEvent(), IHandlerEditor.class);
		addCtrl.handleAdd(pPasted);
		
		//wenn nur 1 Objekt kopiert wurde, wird der editor geöffnet
		if(getPasteCount()==1){
			// selektiert im UI
			setSelection(getActivePartFromEvent().getSite().getId(), pPasted.get(0));
	
			if(editorCtrl!=null){
				// 	öffne Editor
				editorCtrl.handleOpenEditor(pPasted.get(0));
			}
		}
		return null;
	}

}
