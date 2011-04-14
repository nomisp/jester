package ch.jester.common.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class AbstractPropertyChangeModel implements IPropertyChangeSupport{
	protected transient PropertyChangeSupport mPCS;
	
	public AbstractPropertyChangeModel(){
		mPCS = new PropertyChangeSupport(this);
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener mListener){
		mPCS.addPropertyChangeListener(mListener);
	}
	@Override
	public void addPropertyChangeListener(String mProperty, PropertyChangeListener mListener){
		mPCS.addPropertyChangeListener(mProperty, mListener);
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener mListener){
		mPCS.removePropertyChangeListener(mListener);
	}
	@Override
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
