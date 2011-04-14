package ch.jester.ui;

import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerEditor;
import ch.jester.ui.player.editor.PlayerInput;

public class Activator extends AbstractActivator{

	private static Activator mInstance;

	public static Activator getDefault(){
		return mInstance;
	}

	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
		IEditorService openService = getActivationContext().getService(IEditorService.class);
		
		openService.register(Player.class, PlayerInput.class, PlayerEditor.ID);
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

}
