package ch.jester.ormapper.api;

import java.util.HashMap;

public interface IORMapperSettings {
	public HashMap<String, String> getConfiguration();
	
	public String getPassword();
	
	public String getUser();
	
	public String getConnectionurl();
	
	public String getDbname();
	
	public String getConnectiondriverclass();
	
	public String getSqldialect();
	
	public String getSubprotocol();
}
