package ch.jester.ui;

import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.common.ui.editor.GenericDaoInputAccess;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerEditor;

public class Activator extends AbstractUIActivator{

	private static AbstractUIActivator mInstance;

	public static AbstractUIActivator getDefault(){
		return mInstance;
	}

	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
		
		//Editor an InputTyp binden
		IEditorService openService = getActivationContext().getService(IEditorService.class);
		openService.register(Player.class, GenericDaoInputAccess.class, PlayerEditor.ID);
		
	}


	@Override
	public void stopDelegate(BundleContext pContext) {
		
	}

}
