package ch.jester.common.model;

import java.beans.PropertyChangeListener;

/**
 * Interface für die Java standard PropertyChange Konvention
 *
 */
public interface IPropertyChangeSupport {

	public abstract void addPropertyChangeListener(
			PropertyChangeListener mListener);

	public abstract void addPropertyChangeListener(String mProperty,
			PropertyChangeListener mListener);

	public abstract void removePropertyChangeListener(
			PropertyChangeListener mListener);

	public abstract void removePropertyChangeListener(String mProperty,
			PropertyChangeListener mListener);

}