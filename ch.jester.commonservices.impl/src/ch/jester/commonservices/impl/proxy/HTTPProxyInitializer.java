package ch.jester.commonservices.impl.proxy;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class HTTPProxyInitializer extends
		AbstractPreferenceInitializer {

	public HTTPProxyInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = new ConfigurationScope().getNode("ch.jester.commonservices.impl");
		prefs.put("proxy.address", "-1");
		prefs.put("proxy.port", "-1");
	}

}
