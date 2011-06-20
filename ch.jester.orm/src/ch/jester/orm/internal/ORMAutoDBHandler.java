package ch.jester.orm.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.Bundle;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.util.ServiceUtility;
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
		UIUtility.openRestartConfirmation();
	}
	
	
	public void initialize() {
		start();
		addDBPrefs();
		mStore.addPropertyChangeListener(this);
	}

	private void addDBPrefs(){
		List<Bundle> bundles = ORMDBUtil.getDataBasePlugins();
		for(Bundle b:bundles){
			BundlePrefProvider prov = new BundlePrefProvider(b);
		}

	}
	class BundlePrefProvider implements IPreferenceManagerProvider{
		Bundle bundle;
		IPreferenceManager manager;
		public BundlePrefProvider(Bundle b){
			bundle = b;
			IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.jester.orm", "Configuration");
			List<Bundle> names = new ArrayList<Bundle>();
			names.add(bundle);
			String name = ORMDBUtil.getBundleName(names)[0][1];
			IConfigurationElement bundleElement=null;
			for(IConfigurationElement e:elements){
				if(e.getContributor().getName().equals(name)){
					bundleElement=e;
					break;
				}
			}
			System.out.println(bundleElement);
			IExtension extension =  getContributorConfig(bundleElement.getContributor());
			IConfigurationElement el = extension.getConfigurationElements()[0];
			IORMConfiguration config;
			try {
				config = (IORMConfiguration) el.createExecutableExtension("ORMConfiguration");
				config.setConfigElement(el);
				manager = config.initializePreferenceManager(name);
				manager.setNeedRestartAfterChange(true);
				
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ServiceUtility mServices = new ServiceUtility();
			IPreferenceRegistration registration = mServices.getService(IPreferenceRegistration.class);
			registration.registerPreferenceProvider(manager.getPrefixKey(), this);
			mServices.closeAllTrackers();
		}

		
		@Override
		public IPreferenceManager getPreferenceManager(String pKey) {
			if(bundle.getSymbolicName().equals(pKey)){
				return manager;
			}
			return null;
		}
		private IExtension getContributorConfig(IContributor pContributor){
			IExtension[] extensions = Platform.getExtensionRegistry().getExtensions(pContributor);
			for(IExtension ex:extensions){
				if(ex.getExtensionPointUniqueIdentifier().equals("ch.jester.orm."+ORMPlugin.EP_CONFIGURATION)){
					return ex;
				}
			}
			return null;
		}
		
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
			//mConfig.setORMStoreHandler(new ORMStoreHandler(mStore, element.getContributor()));
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
