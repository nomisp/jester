package ch.jester.common.ui.utility;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.PlatformUI;

public class GlobalClipBoard {
	static Clipboard cb = new Clipboard(PlatformUI.getWorkbench().getDisplay());;

	public static Clipboard getInstance(){
		if(cb.isDisposed()){
			cb = new Clipboard(PlatformUI.getWorkbench().getDisplay());
		}
		return cb; 
	}
}
