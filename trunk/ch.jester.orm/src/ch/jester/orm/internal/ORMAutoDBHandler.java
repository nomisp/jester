package ch.jester.orm.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.jester.common.preferences.PreferenceManager;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.orm.IDatabaseManager;
import ch.jester.orm.IORMConfiguration;
import ch.jester.orm.ORMPlugin;

public class ORMAutoDBHandler implements IPreferenceManagerProvider, IPreferencePropertyChanged{
	private ORMPlugin ormPlugin;
	private ILogger mLogger;
	private IORMConfiguration mConfig;
	private IDatabaseManager mDBManager;
	private String dbn;
	private PreferenceManager pManager;
	private IPreferenceProperty mDBProperty;
	private HashMap<String, IORMConfiguration> mConfigs = new HashMap<String, IORMConfiguration>();
	//state = 0 initialize; state = 1 running;
	private int state = 0;
	public ORMAutoDBHandler(ORMPlugin orm){
		ormPlugin = orm;
		mLogger = orm.getActivationContext().getLogger();
		pManager = new PreferenceManager();
		pManager.setDescription("Available Databases");
		pManager.setPrefixKey("ch.jester.orm");
		pManager.registerProviderAtRegistrationService("ch.jester.orm", this);
		String[][] names = ORMDBUtil.getBundleName(ORMDBUtil.getDataBasePlugins());
		mDBProperty = pManager.create("selectedDB", "Selected DataBase", "ch.jester.db.h2");
		mDBProperty.setSelectableValues(names);
		pManager.addListener(this);
	}
	
	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return pManager.checkId(pKey);
	}
	@Override
	public void propertyValueChanged(String internalKey, Object mValue,
			IPreferenceProperty preferenceProperty) {
		if(internalKey.equals("selectedDB")&&state==1){
			changeDataBase(mValue);
		}
		
	}
	public IORMConfiguration getORMConfiguration(){
		return mConfig;
	}
	public IDatabaseManager getDatabaseManager(){
		return mDBManager;
	}
	
	private String getDefaultDataBaseBundleName(){
		return mDBProperty.getValue().toString();
	}
	private void setDefaultDataBaseBundleName(String pBundleid){
		 mDBProperty.setValue(pBundleid);
	}
	public Bundle getConfiguredDatabaseBundle(){
		String dbbundle = getDefaultDataBaseBundleName();
		if(dbbundle.length()==0){
			return null;
		}
		Bundle bundle =  Platform.getBundle(dbbundle);
		return bundle;
	}
	
	/*@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty()==DEFAULT_DATABASE){
			changeDataBase(event.getNewValue());
		}
	}*/

	private void changeDataBase(Object newValue) {
		System.out.println("new default db.plugin "+newValue);
		UIUtility.openRestartConfirmation();
	}
	
	
	public void initialize() {
		start();
		addDBPrefs();
		//mStore.addPropertyChangeListener(this);
	}

	private void addDBPrefs(){
		List<Bundle> bundles = ORMDBUtil.getDataBasePlugins();
		for(Bundle b:bundles){
			BundlePrefProvider prov = new BundlePrefProvider(b);
		}
		
	}

	
	private void start(){
		state = 0;
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
		setDefaultDataBaseBundleName(usedBundle.getSymbolicName());
		dbConfig(usedBundle, usedElement);
		state = 1;
	}

	private void dbConfig(Bundle bundle, IConfigurationElement element) {
		
		String dbmClassName = element
				.getAttribute(ORMPlugin.EP_CONFIGURATION_DATABASEMANAGERCLZ);

		String configClass = element.getAttribute(ORMPlugin.EP_CONFIGURATION_ORMCONFIGURATION);
		try {

			Bundle contributoBundle = bundle;
			mLogger.info(
					"ORMConfiguration is " + configClass
							+ " located in Bundle: " + contributoBundle);
			synchronized(mConfigs){
				if(mConfigs.get(getDefaultDataBaseBundleName())!=null){
					mConfig = mConfigs.get(getDefaultDataBaseBundleName());
				}else{
					mConfig = (IORMConfiguration) element
					.createExecutableExtension(ORMPlugin.EP_CONFIGURATION_ORMCONFIGURATION);
					mConfig.setConfigElement(element);
					mConfigs.put(getDefaultDataBaseBundleName(), mConfig);
				}
			}
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
					mDBManager.editORMConfiguration(mConfig);
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
			IORMConfiguration config = mConfigs.get(name);
			if(config == null){
				try {
					config = (IORMConfiguration) el.createExecutableExtension("ORMConfiguration");
					config.setConfigElement(el);
					manager=config.initializePreferenceManager(name);
					manager.setNeedRestartAfterChange(true);
					
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				manager=config.initializePreferenceManager(name);
				manager.setNeedRestartAfterChange(true);
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

	
}
