package ch.jester.common.ui.listeners;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.model.AbstractPropertyChangeModel;

public class SWTModifyPropertyChangeAdapter extends AbstractPropertyChangeModel implements Listener{
	HashMap<Text, String> mctrl = new HashMap<Text, String>();
	public void add(Text text, String mId){
		mctrl.put(text, mId);
		text.addListener(SWT.Modify, this);
	}
	
	@Override
	public void handleEvent(Event event) {
		String id = mctrl.get(event.widget);
		mPCS.firePropertyChange(id, null, ((Text)event.widget).getText());
		
	}
	

}
