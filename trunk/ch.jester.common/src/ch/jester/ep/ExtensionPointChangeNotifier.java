package ch.jester.ep;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IFilter;

import ch.jester.common.activator.internal.CommonActivator;
import ch.jester.commonservices.api.logging.ILogger;

public class ExtensionPointChangeNotifier implements IExtensionChangeHandler,
		IRegistryChangeListener {
	ExtensionTracker tracker;
	private String mId, mName;
	private boolean open;
	private ILogger mLogger = CommonActivator.getInstance().getActivationContext().getLogger();
	public ExtensionPointChangeNotifier(String id, String name) {
		mId = id;
		mName = name;
	}

	public void start() {
		if (open) {
			return;
		}
		open = true;
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		tracker = new ExtensionTracker(reg);
		IExtensionPoint ep = reg.getExtensionPoint(mId + "." + mName);

		IFilter filter = ExtensionTracker.createExtensionPointFilter(ep);
		tracker.registerHandler(this, filter);
		IExtension[] extensions = ep.getExtensions();
		for (int i = 0; i < extensions.length; ++i) {
			addExtension(tracker, extensions[i]);
		}
		reg.addRegistryChangeListener(this, mId);

	}

	public void addExtension(IExtensionTracker tracker, IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		for (int i = 0; i < configs.length; ++i) {
			added(configs[i]);
			mLogger.debug("added: "
					+ (configs[i] + " bundle: " + configs[i].getContributor()
							.getName()));
		}
	}

	protected void added(IConfigurationElement iConfigurationElement) {

	}

	public void removeExtension(IExtension extension, Object[] objects) {
		// stop using objects associated with
		// the removed extension
		for (int i = 0; i < objects.length; ++i) {
			removed(objects[i]);
			mLogger.debug("removed: " + (objects[i]));
		}
	}

	protected void removed(Object object) {

	}

	@Override
	public void registryChanged(IRegistryChangeEvent event) {
		IExtensionDelta[] delta = event.getExtensionDeltas();
		for (IExtensionDelta d : delta) {
			if (d.getKind() == IExtensionDelta.ADDED) {
				this.addExtension(tracker, d.getExtension());
			} else {
				this.removeExtension(d.getExtension(), d.getExtension()
						.getConfigurationElements());
			}
		}

	}

}
