package ch.jester.ui.player.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import ch.jester.common.ui.utility.IEditorInputAccess;
import ch.jester.model.Player;

public class PlayerInput implements IEditorInputAccess<Player>{
	private Player mPlayer;
	public PlayerInput() {
		
	}
	
	public Player getPlayer(){
		return mPlayer;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return mPlayer.getLastName();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Player Detail";
	}

	@Override
	public Player getInput() {
		return getPlayer();
	}

	@Override
	public void setInput(Player pT) {
		mPlayer=pT;
	}

	public boolean isDirty(){
		return mPlayer.getId()==0;
	}
}
