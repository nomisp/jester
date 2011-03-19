package ch.jester.ormapper.internal;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.ormapper.api.IDatabaseManager;
import ch.jester.ormapper.api.IORMapper;
import ch.jester.ormapper.api.IORMapperDefiniton;

public class ORMappingDefiniton extends ExtensionPointSettings implements IORMapperDefiniton{
	IORMapper mMapper;
	public ORMappingDefiniton(IConfigurationElement e) {
		super(e);
	}

	@Override
	public String getDatabaseName() {
		return getExtensionPointValueFromElement("DatabaseName");
	}

	@Override
	public String getSQLDialectClass() {
		return getExtensionPointValueFromElement("SQLDialectClass");
	}

	@Override
	public HashMap<String, String> getProperties() {
		return getAllProperties("Configuration");
	}

	public String toString(){
		return "ORMappingDefiniton: "+getDatabaseName()+"; "+getSQLDialectClass();
	}

	@Override
	public IORMapper getORMapper() {
		String element = getExtensionPointValueFromElement("IORMapper");
		if(mMapper==null&&element!=null){
			try {
				mMapper = (IORMapper) mElement.createExecutableExtension("IORMapper");
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return mMapper;
	}
}
