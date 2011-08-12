package ch.jester.common.ui.editorutilities;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Hilfsklasse um TitleLeisten zu initialisieren.
 *
 */
public abstract class SitePartNameHandler implements PropertyChangeListener{
	public final static PropertyChangeEvent INITIALIZE_PROPERTYEVENT= new PropertyChangeEvent("init", "init", null, "init");


	/**
	 * Sendet ein INITIALIZE_PROPERTYEVENT als Initialisierung.
	 * Ruft propertyChange auf
	 */
	public void init(){
		propertyChange(INITIALIZE_PROPERTYEVENT);
	}
}
