package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;

import ch.jester.common.ui.handlers.CloneUtility.StructuredTransferSelection;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.GlobalClipBoard;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.utility.UIUtility;

/**
 * Handler um aktuelle Selektion zu klonen.
 *
 */
public class CloneCopyHandler extends AbstractCommandHandler {
	SelectionUtility su = new SelectionUtility(null);
	Clipboard cb = GlobalClipBoard.getInstance();
	public CloneCopyHandler(){
		
	}
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		su.setSelection(getSelection());
		if(su.isIStructuredSelection()){
			copyStructuredSelection();
		}else{
			throw new RuntimeException("Not Supported Yet");
		}
		return null;
	}
	/**
	 * Klont mit Hilfe von CloneUtility eine Selection
	 */
	private void copyStructuredSelection() {
		//Clibboard resetten
		cb.clearContents();
		
		//Selection klonen
		IStructuredSelection selection = su.getAsStructuredSelection();
		StructuredTransferSelection transferableSelection = CloneUtility.copyClonesForTransfer(selection);

		//Die Klone ins Clipboard schmeissen
		Object[] copiedObjects = transferableSelection.getCopiedObjects().toArray();
		cb.setContents(copiedObjects, transferableSelection.getCopiedTransferObjects());

		UIUtility.reevaluateProperty("ch.jester.properties.clipboardFilled");

		//Info an die Statuszeile
		getServiceUtil().getService(IExtendedStatusLineManager.class).setMessage("Copied "+getSelectionCount()+" Item(s)",750);
	}


}
