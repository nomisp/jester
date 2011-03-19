package ch.jester.ormapper.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.ormapper.api.IDatabaseDefinition;
import ch.jester.ormapper.api.IDatabaseManager;

public class DefaultDatabaseDefiniton extends ExtensionPointSettings implements IDatabaseDefinition{
	IDatabaseManager mManager;
	public DefaultDatabaseDefiniton(IConfigurationElement e) {
		super(e);
	}

	@Override
	public String getDatabaseName() {
		return getExtensionPointValueFromElement("DatabaseName");
	}


	@Override
	public String getJDBCDriverClass() {
		return getExtensionPointValueFromElement("JDBCDriverClass");
	}

	@Override
	public String getSubprotocol() {
		return getExtensionPointValueFromElement("Subprotocol");
	}

	@Override
	public IDatabaseManager getDatabaseManager() {
		checkManager();
		return mManager;
	}

	private void checkManager() {
		String element = getExtensionPointValueFromElement("DatabaseManagerClass");
		if(mManager==null&&element!=null){
			try {
				mManager = (IDatabaseManager) mElement.createExecutableExtension("DatabaseManagerClass");
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	public String toString(){
		return "DatabaseDefiniton: "+getDatabaseName()+"; "+getJDBCDriverClass()+"; "+getSubprotocol()+"; "+getDatabaseManager();
	}


}
