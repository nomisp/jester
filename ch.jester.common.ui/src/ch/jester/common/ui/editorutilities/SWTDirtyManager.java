package ch.jester.common.ui.editorutilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.commonservices.api.logging.ILogger;

public class SWTDirtyManager extends DirtyManager implements DisposeListener{
	private List<Control> mControls = new ArrayList<Control>();
	private HashMap<Control, Listener> mListeners = new HashMap<Control, Listener>();
	private ILogger mLogger = CommonUIActivator.getDefault().getActivationContext().getLogger();
	public void add(Control pControl){
		if(!mControls.contains(pControl)){
			mControls.add(pControl);
			mListeners.put(pControl, installListener(pControl));
		}
		
	}
	private Listener installListener(Control pControl) {
		Listener l = new ModifyListener();
		if (pControl instanceof DateTime || pControl instanceof Button) {
			pControl.addListener(SWT.Selection, l);
		} else {
			pControl.addListener(SWT.Modify, l);
		}
		pControl.addDisposeListener(this);
		return l;
		
	}
	public void remove(Control pControl){
		mControls.remove(pControl);
	}
	
	public void add(Control... pControls){
		for(Control c:pControls){
			add(c);
		}
	}
	@Override
	public void widgetDisposed(DisposeEvent e) {
		Object object = e.widget;
		Listener listener = mListeners.get(object);
		if (object instanceof DateTime) {
			((Control)object).removeListener(SWT.Selection, listener);
		} else {
			((Control)object).removeListener(SWT.Modify, listener);
		}
		((Control)object).removeDisposeListener(SWTDirtyManager.this);
	}
	class ModifyListener implements Listener{
		@Override
		public void handleEvent(Event event) {
			SWTDirtyManager.this.setDirty(true);
			mLogger.debug("Changed widget data! Source =  "+event.widget);
		}
		
	}
}
