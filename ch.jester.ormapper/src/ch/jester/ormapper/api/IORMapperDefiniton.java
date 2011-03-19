package ch.jester.ormapper.api;

import java.util.HashMap;

public interface IORMapperDefiniton {
	public String getDatabaseName();
	public String getSQLDialectClass();
	public HashMap<String, String> getProperties();
	public IORMapper getORMapper();

}
