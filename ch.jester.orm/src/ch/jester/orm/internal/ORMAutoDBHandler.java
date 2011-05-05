package ch.jester.orm.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

public class ORMAutoDBHandler implements IPropertyChangeListener{
	public static String DEFAULT_DATABASE = "ch.jester.orm.defaultdatabase";
	IPreferenceStore mStore;
	
	public ORMAutoDBHandler(IPreferenceStore pStore){
		mStore = pStore;
		mStore.addPropertyChangeListener(this);
	}
	public String getDefaultDataBaseBundleName(){
		return mStore.getString(DEFAULT_DATABASE);
	}
	public Bundle getDefaultDataBaseBundle(){
		String dbbundle = mStore.getString(DEFAULT_DATABASE);
		if(dbbundle.length()==0){
			return null;
		}
		Bundle bundle =  Platform.getBundle(dbbundle);
		return bundle;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty()==DEFAULT_DATABASE){
			changeDataBase(event.getNewValue());
		}
	}

	private void changeDataBase(Object newValue) {
		System.out.println("new default db.plugin "+newValue);
		PlatformUI.getWorkbench().restart();
	}

}
