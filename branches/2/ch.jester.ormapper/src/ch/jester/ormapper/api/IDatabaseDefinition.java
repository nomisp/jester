package ch.jester.ormapper.api;

public interface IDatabaseDefinition {
	/****abd*/
	public String getDatabaseName();
	public String getJDBCDriverClass();
	public String getSubprotocol();
	public IDatabaseManager getDatabaseManager();
}
