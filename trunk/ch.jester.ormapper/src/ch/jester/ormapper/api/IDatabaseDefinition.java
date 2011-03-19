package ch.jester.ormapper.api;

public interface IDatabaseDefinition {
	public String getDatabaseName();
	public String getJDBCDriverClass();
	public String getSubprotocol();
	public IDatabaseManager getDatabaseManager();
}
