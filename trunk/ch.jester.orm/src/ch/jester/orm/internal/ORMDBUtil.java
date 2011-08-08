package ch.jester.orm.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.orm.ORMPlugin;

/**
 * Utility Klasse für die DB Konfig.
 *
 */
public class ORMDBUtil {
	
	public static List<Bundle> getDataBasePlugins(){
		IConfigurationElement[] elements = ExtensionPointUtil.getExtensionPointElements(ORMPlugin.getDefault().getActivationContext().getPluginId(), "Configuration");
		List<Bundle> bundles = new ArrayList<Bundle>();
		for(IConfigurationElement e:elements){
			IContributor contributor = e.getContributor();
			Bundle b = Platform.getBundle(contributor.getName());
			//String name = b.getHeaders().get("Bundle-Name").toString();
			bundles.add(b);
		}
		return bundles;
	}
	public static int getIndexFromBundleWithName(List<Bundle> pBundleList, String pName){
		for(int i=0;i<pBundleList.size();i++){
			if(pBundleList.get(i).getHeaders().get("Bundle-Name").toString().equals(pName)){
				return i;
			}
		}
		return -1;
	}
	public static String[][] getBundleName(List<Bundle> pList){
		String[][] names = new String[pList.size()][2];
		for(int i=0;i<pList.size();i++){
			names[i][0]= pList.get(i).getHeaders().get("Bundle-Name").toString();
			names[i][1] = pList.get(i).getSymbolicName();
		}
		return names;
	}
	



}
