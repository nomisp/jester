package ch.jester.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * Abstract Model, das PropertyChanges unterstütz
 *
 */
public class AbstractPropertyChangeModel{
	protected transient PropertyChangeSupport mPCS;
	
	public AbstractPropertyChangeModel(){
		mPCS = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener mListener){
		mPCS.addPropertyChangeListener(mListener);
	}
	
	public void addPropertyChangeListener(String mProperty, PropertyChangeListener mListener){
		mPCS.addPropertyChangeListener(mProperty, mListener);
	}

	public void removePropertyChangeListener(PropertyChangeListener mListener){
		mPCS.removePropertyChangeListener(mListener);
	}

	public void removePropertyChangeListener(String mProperty, PropertyChangeListener mListener){
		mPCS.removePropertyChangeListener(mProperty, mListener);
	}
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		mPCS.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue){
		mPCS.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}
}
