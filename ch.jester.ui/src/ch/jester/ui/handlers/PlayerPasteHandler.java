package ch.jester.ui.handlers;

import ch.jester.common.ui.handlers.ClonePasteHandler;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerListController;
import ch.jester.ui.player.editor.PlayersView;

public class PlayerPasteHandler extends ClonePasteHandler {

	@Override
	public Object handlePaste(Object pPasted) {
		Player player = (Player) pPasted;
		getServiceUtil().getService(PlayerListController.class).addPlayer(player);

		// selektiert im UI
		setSelection(PlayersView.ID, player);

		// öffne Editor
		openEditor(player);
		return null;
	}

}
