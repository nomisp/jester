package ch.jester.common.ui.handlers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;

import ch.jester.common.ui.utility.GlobalClipBoard;

public class ClipboardPropertyTester extends PropertyTester {

	public ClipboardPropertyTester() {

	}

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		Clipboard clipBoard =GlobalClipBoard.getInstance();
		LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
		IStructuredSelection selection = (IStructuredSelection) clipBoard.getContents(transfer);
		if(selection==null){
			return false;
		}
		return selection.getFirstElement().getClass().getName().equals(args[0]);
	}
}
