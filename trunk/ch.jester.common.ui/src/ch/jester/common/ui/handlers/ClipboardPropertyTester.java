package ch.jester.common.ui.handlers;

import java.lang.reflect.Type;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;

import ch.jester.common.ui.utility.GlobalClipBoard;

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
			return false;
		}
		Class<?> firsttransferedClass = selection.getFirstElement().getClass();
		Class<?> all[] = firsttransferedClass.getClasses();
		all = firsttransferedClass.getDeclaredClasses();
		Class<?> interfaces[] = firsttransferedClass.getInterfaces();
		for(Object argument:args){
			boolean b = isAssignable(argument, interfaces);
			if(!b){
				return false;
			}
		}
		return true;
	}
}
