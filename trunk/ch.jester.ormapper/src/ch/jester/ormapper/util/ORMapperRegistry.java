package ch.jester.ormapper.util;

import java.util.HashMap;

import ch.jester.ormapper.api.IDatabaseManager;

public class ORMapperRegistry {
private static HashMap<String, IDatabaseManager> dbs = new HashMap<String, IDatabaseManager>();
	public static boolean exists(String pDataBaseName){
		return dbs.get(pDataBaseName)!=null;
	}
	public static void add(String pDataBaseName, IDatabaseManager pDbManager){
		if(exists(pDataBaseName)){
			throw new RuntimeException("DB Already exisiting");
		}
		dbs.put(pDataBaseName, pDbManager);
	}
	public static IDatabaseManager get(String pDbName){
		return dbs.get(pDbName);
	}
}
