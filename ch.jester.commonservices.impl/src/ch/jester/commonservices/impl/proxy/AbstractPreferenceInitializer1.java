package ch.jester.commonservices.impl.proxy;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class AbstractPreferenceInitializer1 extends
		AbstractPreferenceInitializer {

	public AbstractPreferenceInitializer1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = new ConfigurationScope().getNode("ch.jester.commonservices.impl");
		prefs.put("proxy.address", "-1");
		prefs.put("proxy.port", "-1");
		System.out.println("HTTPProxyInit Prefs");
	}

}
