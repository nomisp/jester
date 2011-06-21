package ch.jester.commonservices.impl.logging;

import java.util.Dictionary;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.logging.DefaultLogger;
import ch.jester.common.preferences.PreferenceManager;
import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferencePropertyChanged;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;



public class DefaultLoggerFactory implements ILoggerFactory, IComponentService<IPreferenceRegistration>, IPreferenceManagerProvider{
	private boolean mDebug = true;
	private String name = null;
	private IPreferenceRegistration mRegistration;
	private ILogger mLogger;
	private PreferenceManager mPrefManager;
	public DefaultLoggerFactory(){
		mLogger = getLogger(this.getClass());
		mLogger.info("LoggerFactory started");
	}
	@Override
	public ILogger getLogger(Class<?> pClass) {
		Bundle bundle = FrameworkUtil.getBundle(pClass);
		DefaultLogger logger = new DefaultLogger(Platform.getLog(bundle), bundle, this);
		logger.addLogListener(new Log4jWrapper(pClass));
		return logger;
	}

	@Override
	public void setDebug(boolean b) {
		mDebug=b;
	}

	@Override
	public boolean isDebug() {
		return mDebug;
	}
	@Override
	public void start(ComponentContext pComponentContext) {
		Dictionary props = pComponentContext.getProperties();
		name = props.get("component.name").toString();
		mLogger.debug("ComponentContext injected: Name -> "+name);
		mPrefManager = new PreferenceManager();
		mPrefManager.setId(name);
		String[][] values = new String[][]{{"true","true"},{"false","false"}};
		mPrefManager.create("debug", "Debug", this.isDebug()).setSelectableValues(values);
		mPrefManager.addListener(new IPreferencePropertyChanged() {
			
			@Override
			public void propertyValueChanged(String internalKey, Object mValue,
					IPreferenceProperty preferenceProperty) {
					setDebug((Boolean) mValue);
				
			}
		});
		
		
	}
	@Override
	public void stop(ComponentContext pComponentContext) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void bind(IPreferenceRegistration pT) {
		mRegistration = pT;
		mLogger.debug("IPreferenceRegistration injected");
		mRegistration.registerPreferenceProvider(name, this);
	}
	@Override
	public void unbind(IPreferenceRegistration pT) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public IPreferenceManager getPreferenceManager(String pKey) {
		return mPrefManager.checkId(pKey);
	}


	
}
