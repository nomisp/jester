package ch.jester.ormapper.internal;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.ormapper.api.IDatabaseDefinition;
import ch.jester.ormapper.api.IORMapperDefiniton;

public class ORMapperActivator  extends AbstractActivator {
	private static ORMapperActivator mActivator;
	public static ORMapperActivator getDefault(){
		return mActivator;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		mActivator=this;
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}
	
	public List<IDatabaseDefinition> getDatabaseDefinitions(){
		ArrayList<IDatabaseDefinition> defs = new ArrayList<IDatabaseDefinition>();
		IConfigurationElement[] elements = ExtensionPointUtil.getExtensionPointElements(getActivationContext().getPluginId(), "DataBaseDefinition");
		for(IConfigurationElement e:elements){
			IDatabaseDefinition def = new DefaultDatabaseDefiniton(e);
			defs.add(def);
		}
		
		return defs;
		
	}

	public List<IORMapperDefiniton> getORMappingDefinitions() {
		ArrayList<IORMapperDefiniton> defs = new ArrayList<IORMapperDefiniton>();
		IConfigurationElement[] elements = ExtensionPointUtil.getExtensionPointElements(getActivationContext().getPluginId(), "ORMappingDefinition");
		for(IConfigurationElement e:elements){
			IORMapperDefiniton def = new ORMappingDefiniton(e);
			defs.add(def);
		}
		
		return defs;
	}


}
