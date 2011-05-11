package ch.jester.common.ui.handlers;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;

import ch.jester.common.ui.utility.GlobalClipBoard;
import ch.jester.common.ui.utility.SelectionUtility;

public abstract class ClonePasteHandler<T> extends AbstractCommandHandler {
	Clipboard board = GlobalClipBoard.getInstance();
	SelectionUtility mSelectionUtility = new SelectionUtility(null);
	@SuppressWarnings("unchecked")
	@Override
	public Object executeInternal(ExecutionEvent event) {
		LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
		mSelectionUtility.setSelection((IStructuredSelection) board.getContents(transfer));
		// TODO NPE abfangen bei startup / Handler aktiv! warum?
		return handlePaste(mSelectionUtility.getAsStructuredSelection().toList());
	}
	
	public int getPasteCount(){
		return mSelectionUtility.getSelectionCount();
	}
	public ISelection getPasteSelection(){
		return mSelectionUtility.getSelection();
	}
	
	public abstract Object handlePaste(List<T> pPastedList);
}
