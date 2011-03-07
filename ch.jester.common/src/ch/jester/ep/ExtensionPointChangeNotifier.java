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

public class ExtensionPointChangeNotifier implements IExtensionChangeHandler, IRegistryChangeListener{
	ExtensionTracker tracker;
	Object o = new Object();
	public ExtensionPointChangeNotifier(String id, String name){
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		tracker = new ExtensionTracker(reg);
		IExtensionPoint ep = reg.getExtensionPoint(id+"."+name);
		IFilter filter = ExtensionTracker.createExtensionPointFilter(ep);
		tracker.registerHandler(this, filter);
		 IExtension[] extensions = ep.getExtensions();
		   for (int i = 0; i < extensions.length; ++i){
		      addExtension(tracker, extensions[i]);
		   }
		reg.addRegistryChangeListener(this,id);
	}
	public void addExtension(IExtensionTracker tracker,
		      IExtension extension) {
		   IConfigurationElement[] configs = 
		      extension.getConfigurationElements();
		   for (int i = 0; i < configs.length; ++i) {
		      // use configuration properties for something
		      // ...

		      System.out.println("added: "+(configs[i]+" bundle: "+configs[i].getContributor().getName()));
		      // register association between object and extension
		      // with the tracker
		      /*tracker.registerObject(extension,o,
		                             IExtensionTracker.REF_WEAK);*/
		   }
		}

		public void removeExtension(IExtension extension,
		                            Object[] objects) {
		   // stop using objects associated with
		   // the removed extension
		   for (int i = 0; i < objects.length; ++i)
		      System.out.println("removed: "+(objects[i]));
		}
		@Override
		public void registryChanged(IRegistryChangeEvent event) {
			IExtensionDelta[] delta = event.getExtensionDeltas();
			for(IExtensionDelta d:delta){
				if(d.getKind() == IExtensionDelta.ADDED){
					this.addExtension(tracker, d.getExtension());
				}else{
					this.removeExtension(d.getExtension(), d.getExtension().getConfigurationElements());
				}
			}
			
		}

}
