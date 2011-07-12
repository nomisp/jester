package ch.jester.commonservices.impl.logging;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.components.ComponentAdapter;
import ch.jester.common.logging.DefaultLogger;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;



public class DefaultLoggerFactory extends ComponentAdapter<IPreferenceRegistration> implements ILoggerFactory, IPreferenceManagerProvider{
	//Preference Stuff
	private static final String PP_ID_DEBUG = "debug";
	private static final String[][] PP_SELECTDEF_DEBUG = new String[][]{{"true","true"},{"false","false"}};
	
	//Class Memebers
	private IPreferenceRegistration mRegistration;
	private IPreferenceManager mPrefManager;
	private ILogger mLogger;
	private boolean mDebug = true;
	private String mName = null;
	
	public DefaultLoggerFactory(){
		mLogger = this.getLogger(this.getClass());
		mLogger.info("LoggerFactory started");
	}
	
	@Override
	public void bind(IPreferenceRegistration pT) {
		initPreferences(pT);
	}

	@Override
	public ILogger getLogger(Class<?> pClass) {
		Bundle bundle = FrameworkUtil.getBundle(pClass);
		DefaultLogger logger = new DefaultLogger(Platform.getLog(bundle), bundle, this);
		logger.addLogListener(new Log4jWrapper(pClass));
		return logger;
	}

	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return mPrefManager.checkId(pKey);
	}
	private void initPreferences(IPreferenceRegistration pT){
		mRegistration = pT;
		mRegistration.registerPreferenceProvider(mName, this);
		mPrefManager = mRegistration.createManager();
		mPrefManager.setId(mName);
		mPrefManager.create(PP_ID_DEBUG, "Debug", this.isDebug()).setSelectableValues(PP_SELECTDEF_DEBUG);
		mPrefManager.addListener(new IPreferencePropertyChanged() {
			@Override
			public void propertyValueChanged(String internalKey, Object mValue,
					IPreferenceProperty preferenceProperty) {
					setDebug(preferenceProperty.getBooleanValue());
			}
		});
	}
	@Override
	public boolean isDebug() {
		return mDebug;
	}
	
	@Override
	public void setDebug(boolean b) {
		mDebug=b;
	}
	@Override
	public void start(ComponentContext pComponentContext) {
		mName = pComponentContext.getProperties().get("component.name").toString();
	}

}
