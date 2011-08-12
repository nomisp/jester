package ch.jester.common.ui.editorutilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Default Impl. eines DirtyManagers
 * 
 *
 */
public class DirtyManager/* implements PropertyChangeListener*/{
	private boolean mDirty;
	//private Set<String> mProperties = new HashSet<String>();
	private List<IDirtyListener> mInvokers = new ArrayList<IDirtyListener>();
	/*public DirtyManager(){
	}*/

/*	public void addListenerProperty(String p){
		mProperties.add(p);
	}*/
	protected DirtyManager(){
		
	}
	/**
	 * Adden von Listener
	 * @param pInvoker
	 */
	public void addDirtyListener(IDirtyListener pInvoker){
		mInvokers.add(pInvoker);
	}
	
	/**
	 * Setzen von Dirty.
	 * Wenn b == true, werden die Listener informiert.
	 * @param b
	 */
	public void setDirty(boolean b){
		mDirty = b;
		for(IDirtyListener inv:mInvokers){	
			inv.propertyIsDirty();
		}
	}
	/**
	 * Dirty wird auf false gesetzt
	 */
	public void reset(){
		setDirty(false);
	}
	
	/**
	 * Is dirty?
	 * @return true | false
	 */
	public boolean isDirty(){
		return mDirty;
	}
/*	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
			if(mProperties.isEmpty()||mProperties.contains(arg0.getPropertyName())){
				setDirty(true);
			}
	}*/

}
