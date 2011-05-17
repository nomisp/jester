package ch.jester.ui.tournament.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.jester.common.ui.handlers.AbstractCommandHandler;

public class AddPlayerHandler extends AbstractCommandHandler implements IHandler {

	@Override
	public Object executeInternal(ExecutionEvent event) {
		MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "AddPlayer", "Adding Player to Category");
		return null;
	}

}
