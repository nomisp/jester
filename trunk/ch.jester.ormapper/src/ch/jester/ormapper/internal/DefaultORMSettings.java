package ch.jester.ormapper.internal;

import java.util.HashMap;

import ch.jester.ormapper.api.IDatabaseDefinition;
import ch.jester.ormapper.api.IDatabaseManager;
import ch.jester.ormapper.api.IORMapper;
import ch.jester.ormapper.api.IORMapperDefiniton;
import ch.jester.ormapper.api.IORMapperSettings;

public class DefaultORMSettings implements IORMapperSettings{
	IDatabaseDefinition mDB;
	IORMapperDefiniton mOR;
	public DefaultORMSettings(IDatabaseDefinition pDBDef, IORMapperDefiniton pORDef){
		mDB=pDBDef;
		mOR=pORDef;
	}
	@Override
	public String getDatabaseName() {
		return mDB.getDatabaseName();
	}

	@Override
	public String getJDBCDriverClass() {
		return mDB.getJDBCDriverClass();
	}

	@Override
	public String getSubprotocol() {
		return mDB.getSubprotocol();
	}

	@Override
	public IDatabaseManager getDatabaseManager() {
		return mDB.getDatabaseManager();
	}

	@Override
	public String getSQLDialectClass() {
		return mOR.getSQLDialectClass();
	}

	@Override
	public HashMap<String, String> getProperties() {
		return mOR.getProperties();
	}

	@Override
	public IORMapper getORMapper() {
		return mOR.getORMapper();
	}

}
