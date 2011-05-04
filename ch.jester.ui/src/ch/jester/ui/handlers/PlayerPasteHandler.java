package ch.jester.ui.handlers;

import java.util.List;

import ch.jester.common.ui.handlers.ClonePasteHandler;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.ctrl.PlayerListController;
import ch.jester.ui.player.editor.view.PlayersView;

public class PlayerPasteHandler extends ClonePasteHandler<Player> {

	@Override
	public Object handlePaste(List<Player> pPasted) {
		getServiceUtil().getService(PlayerListController.class).addPlayer(pPasted);

		//wenn nur 1 player kopiert wurde, wird der editor geöffnet
		if(getPasteCount()==1){
			// selektiert im UI
			setSelection(PlayersView.ID, pPasted.get(0));
	
			// öffne Editor
			openEditor(pPasted.get(0));
		}
		return null;
	}

}
