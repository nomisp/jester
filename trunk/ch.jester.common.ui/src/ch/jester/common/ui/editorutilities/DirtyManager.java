package ch.jester.common.ui.editorutilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DirtyManager implements PropertyChangeListener{
	private boolean mDirty;
	private Set<String> mProperties = new HashSet<String>();
	private List<IDirtyListener> mInvokers = new ArrayList<IDirtyListener>();
	public DirtyManager(){
	}

	public void addListenerProperty(String p){
		mProperties.add(p);
	}
	public void addDirtyListener(IDirtyListener pInvoker){
		mInvokers.add(pInvoker);
	}
	
	public void setDirty(boolean b){
		mDirty = b;
		for(IDirtyListener inv:mInvokers){	
			inv.propertyIsDirty();
		}
	}
	public void reset(){
		setDirty(false);
	}
	
	public boolean isDirty(){
		return mDirty;
	}
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
			if(mProperties.isEmpty()||mProperties.contains(arg0.getPropertyName())){
				setDirty(true);
			}
	}

}
