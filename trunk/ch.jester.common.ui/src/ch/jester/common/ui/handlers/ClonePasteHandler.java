package ch.jester.common.ui.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;

import ch.jester.common.ui.utility.GlobalClipBoard;

public abstract class ClonePasteHandler extends AbstractCommandHandler {
	Clipboard board = GlobalClipBoard.getInstance();
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

		IStructuredSelection selection = (IStructuredSelection) board.getContents(transfer);

	
			@SuppressWarnings("unchecked")
			Iterator<Object> iterator = selection.iterator();
			
			while (iterator.hasNext()) {
				handlePaste(iterator.next());
			}
		
		return null;
	}
	public abstract Object handlePaste(Object pPasted);
}
