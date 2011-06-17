package ch.jester.orm.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.orm.IDatabaseManager;
import ch.jester.orm.IORMConfiguration;
import ch.jester.orm.ORMPlugin;

public class ORMAutoDBHandler implements IPropertyChangeListener{
	public static String DEFAULT_DATABASE = "ch.jester.orm.defaultdatabase";
	private IPreferenceStore mStore;
	private ORMPlugin ormPlugin;
	private ILogger mLogger;
	private IORMConfiguration mConfig;
	private IDatabaseManager mDBManager;
	private String dbn;
	public ORMAutoDBHandler(IPreferenceStore pStore, ORMPlugin orm){
		mStore = pStore;
		ormPlugin = orm;
		mLogger = orm.getActivationContext().getLogger();
	}
	public IORMConfiguration getORMConfiguration(){
		return mConfig;
	}
	public IDatabaseManager getDatabaseManager(){
		return mDBManager;
	}
	
	public String getDefaultDataBaseBundleName(){
		return mStore.getString(DEFAULT_DATABASE);
	}
	public void setDefaultDataBaseBundleName(String pBundleid){
		 mStore.putValue(DEFAULT_DATABASE, pBundleid);
	}
	public Bundle getConfiguredDatabaseBundle(){
		String dbbundle = getDefaultDataBaseBundleName();
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
	
	
	public void initialize() {
		start();
		mStore.addPropertyChangeListener(this);
	}

	private void start(){
		Bundle configuredDBBundle = getConfiguredDatabaseBundle();
		Bundle firstDBBundle = null;
		IConfigurationElement firstDBConfig = ExtensionPointUtil.getExtensionPointElement(ormPlugin.getPluginId(), ORMPlugin.EP_CONFIGURATION);
		firstDBBundle = Platform.getBundle(firstDBConfig.getContributor().getName());
		
		IConfigurationElement usedElement = null;
		Bundle usedBundle = null;
		
		if(configuredDBBundle == null){
			 usedElement = firstDBConfig;
			 usedBundle = firstDBBundle;
		}else{
			IConfigurationElement[] elements =  ExtensionPointUtil.getExtensionPointElements(ormPlugin.getPluginId(), ORMPlugin.EP_CONFIGURATION);
			for(IConfigurationElement c:elements){
				if(c.getContributor().getName().equals(configuredDBBundle.getSymbolicName())){
					usedElement = c;
					//String sub = usedElement.getAttribute("Subprotocol");
					usedBundle = configuredDBBundle;
					break;
				}
			}
		}
		if(usedElement==null){
			usedElement = firstDBConfig;
			usedBundle = firstDBBundle;
		}
		this.dbn=usedBundle.getHeaders().get("Bundle-Name").toString();
		//if(mStore.getString(DEFAULT_DATABASE)==null||mStore.getString(DEFAULT_DATABASE).length()==0){
			mStore.putValue(DEFAULT_DATABASE, usedBundle.getSymbolicName());
		//}
		dbConfig(usedBundle, usedElement);
	}

	private void dbConfig(Bundle bundle, IConfigurationElement element) {
		
		String dbmClassName = element
				.getAttribute(ORMPlugin.EP_CONFIGURATION_DATABASEMANAGERCLZ);

		/*element = ExtensionPointUtil.getExtensionPointElement(ormPlugin.getPluginId(),
				ORMPlugin.EP_CONFIGURATION);*/
		String configClass = element.getAttribute(ORMPlugin.EP_CONFIGURATION_ORMCONFIGURATION);
		try {

			//Bundle contributoBundle = Platform.getBundle(element.getContributor().getName());
			Bundle contributoBundle = bundle;
			mLogger.info(
					"ORMConfiguration is " + configClass
							+ " located in Bundle: " + contributoBundle);
			mConfig = (IORMConfiguration) element
					.createExecutableExtension(ORMPlugin.EP_CONFIGURATION_ORMCONFIGURATION);
			mConfig.setConfigElement(element);
			//nach hinten geschoben
			if (dbmClassName != null) {
				try {
					Bundle databaseBundle = Platform.getBundle(element
							.getContributor().getName());
					mLogger.info(
							"DatabaseManagerClass is " + dbmClassName
									+ " located in Bundle: " + databaseBundle);
					mDBManager = (IDatabaseManager) element
							.createExecutableExtension(ORMPlugin.EP_CONFIGURATION_DATABASEMANAGERCLZ);
					mDBManager.start();
				} catch (CoreException e) {

					e.printStackTrace();
				}

			}

		} catch (CoreException e) {

			e.printStackTrace();
		}

	}
	public String getDataBaseTypeName() {
		return this.dbn;
	}
	
}
