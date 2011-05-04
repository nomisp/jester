package ch.jester.ui.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.jester.common.ui.handlers.AbstractUndoRedoCommandHandler;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.player.editor.ctrl.PlayerListController;
import ch.jester.ui.player.editor.view.PlayersView;

public class NewUndoRedoPlayerHandler extends AbstractUndoRedoCommandHandler{
	
	
	public NewUndoRedoPlayerHandler(){
		System.out.println("NewUndoRedoPlayerHandler");
	}

	
	@Override
	protected IUndoableOperation getOperation() {
		return new AbstractOperation("Add Player") {
			Player mPlayer;
			@Override
			public IStatus undo(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				getServiceUtil().getService(PlayerListController.class).removePlayer(mPlayer);
				mPlayer.setId(0);
				return Status.OK_STATUS;
			}
			
			@Override
			public IStatus redo(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				getServiceUtil().getService(PlayerListController.class).addPlayer(mPlayer);
				return Status.OK_STATUS;
			}
			
			@Override
			public IStatus execute(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				//Defaultplayer
				Player player = ModelFactory.getInstance().createPlayer();
				mPlayer = player;
				
				PlayerListController ctrl = getServiceUtil().getService(PlayerListController.class);
				
				//hinzufügen vom Player
				ctrl.addPlayer(player);
			
				//selektiert im UI
				setSelection(PlayersView.ID, player);

				//öffne Editor
				ctrl.openEditor(player);

				
				return Status.OK_STATUS;
			}
		};
	}

}
