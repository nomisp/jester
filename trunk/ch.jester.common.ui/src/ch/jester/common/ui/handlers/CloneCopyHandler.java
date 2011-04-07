package ch.jester.common.ui.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.IEvaluationService;

import ch.jester.common.ui.handlers.CloneUtility.StructuredTransferSelection;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.GlobalClipBoard;
import ch.jester.common.ui.utility.SelectionUtility;

public class CloneCopyHandler extends AbstractCommandHandler {
	SelectionUtility su = new SelectionUtility(null);
	Clipboard cb = GlobalClipBoard.getInstance();
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
	private void copyStructuredSelection() {
		//Clibboard resetten
		cb.clearContents();
		
		//Selection klonen
		IStructuredSelection selection = su.getAsStructuredSelection();
		StructuredTransferSelection transferableSelection = CloneUtility.copyClonesForTransfer(selection);

		//Die Klone ins Clipboard schmeissen
		cb.setContents(transferableSelection.getCopiedObjects().toArray(), transferableSelection.getCopiedTransferObjects());
	
		//Property neu evaluieren; so dass der paste button aktiv wird; passiert sonst erst beim Fokuwechsel
		try {
			IEvaluationService evalservice = (IEvaluationService) HandlerUtil.getActiveWorkbenchWindowChecked(mEvent).getService(IEvaluationService.class);
			evalservice.requestEvaluation("ch.jester.properties.clipboardFilled");
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Info an die Statuszeile
		getServiceUtil().getService(IExtendedStatusLineManager.class).setMessage("Copied "+getSelectionCount()+" Item(s)",750);
	}


}
