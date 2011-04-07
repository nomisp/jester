package ch.jester.common.ui.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.GlobalClipBoard;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerListController;
import ch.jester.ui.player.editor.PlayersView;

public class ClonePasteHandler extends AbstractCommandHandler {
	Clipboard board = GlobalClipBoard.getInstance();

	@Override
	public Object executeInternal(ExecutionEvent event) {
		IPlayerPersister persister = getServiceUtil().getExclusiveService(
				IPlayerPersister.class);

		LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

		IStructuredSelection selection = (IStructuredSelection) board
				.getContents(transfer);

		try {
			@SuppressWarnings("unchecked")
			Iterator<Player> playerIterator = selection.iterator();
			while (playerIterator.hasNext()) {
				Player player = playerIterator.next();
				getServiceUtil().getService(PlayerListController.class)
						.addPlayer(player);

				// selektiert im UI
				PlayersView viewpart = (PlayersView) UIUtility
						.getActiveWorkbenchWindow().getActivePage()
						.findView(PlayersView.ID);
				setSelection(viewpart, player);

				// öffne Editor
				getServiceUtil().getService(IEditorService.class).openEditor(
						player);
				persister.save(player);
			}
		} finally {
			persister.close();
		}
		return null;
	}

}
