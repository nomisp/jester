package ch.jester.common.ui.editorutilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

public class DirtyManager implements PropertyChangeListener{
	private boolean mDirty;
	private Set<String> mProperties = new HashSet<String>();
	private IDirtyManagerPropertyInvoker mInvoker;
	public DirtyManager(){
	}
	public DirtyManager(IDirtyManagerPropertyInvoker pInvoker){
		mInvoker=pInvoker;
	}
	
	public void addListenerProperty(String p){
		mProperties.add(p);
	}
	public void setDirtyManagerPropertyInvoker(IDirtyManagerPropertyInvoker pInvoker){
		mInvoker=pInvoker;
	}
	
	public void setDirty(boolean b){
		mDirty = b;
		if(mInvoker!=null){
			mInvoker.fireDirtyProperty();
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
