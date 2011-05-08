package ch.jester.common.ui.handlers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.ui.utility.GlobalClipBoard;
import ch.jester.common.utility.AdapterUtility;

public class ClipboardPropertyTester extends PropertyTester {

	public ClipboardPropertyTester() {

	}

	private boolean isAssignable(Object arg, Class<?>[] classes){
		for(Class<?> c:classes){
			if(c.getCanonicalName().equals(arg)){
				return true;
			}
		}
		return false;
		
	}
	
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		Clipboard clipBoard =GlobalClipBoard.getInstance();
		LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
		IStructuredSelection selection = (IStructuredSelection) clipBoard.getContents(transfer);
		if(selection==null){
			//System.out.println("Clipboard Test is: "+false);
			return false;
		}
		Class<?> firsttransferedClass = selection.getFirstElement().getClass();
		Class<?> all[] = firsttransferedClass.getClasses();
		all = firsttransferedClass.getDeclaredClasses();
		Class<?> interfaces[] = firsttransferedClass.getInterfaces();
		for(Object argument:args){
			boolean b = isAssignable(argument, interfaces);
			if(!b){
				//System.out.println("Clipboard Test is: "+false);
				return false;
			}
		}
		IWorkbenchPart part = safeGetActivePart();
		if(part==null){
			return false;
		}
		DaoController<?> targetController = AdapterUtility.getAdaptedObject(part,DaoController.class);
		
		if(targetController==null){return false;}
		Class<?> targetClass = targetController.getTargetClass();
		if(targetClass.isAssignableFrom(firsttransferedClass)){
			System.out.println("Clipboard Test is: "+true);
			return true;
		}
		System.out.println("Clipboard Test is: "+false);
		return false;
	}
	
	private IWorkbenchPart safeGetActivePart(){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if(window==null){return null;}
		IWorkbenchPage page = window.getActivePage();
		if(page==null){return null;}
		return page.getActivePart();
		
	}
}
