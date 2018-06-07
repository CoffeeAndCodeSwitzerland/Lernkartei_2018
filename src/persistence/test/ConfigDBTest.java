package persistence.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import basics.Environment;
import basics.Environment.EnvironementParameterName;
import database.Entity;
import persistence.ConfigDB;

class ConfigDBTest {
	
	static ConfigDB conf = new ConfigDB();
	static Entity e = conf.getMyTables().get("Config");
	
	public static void cleanTable(String name) throws Exception {
		e = conf.getMyTables().get(name);
		int res = e.delValues();
		
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		assertEquals("PK_"+e.getMyTableName(),e.getMyAttributes().getPrimaryKey().getName());
		
		e.getMyDBDriver().closeDB();
	}
	

	public static void myTest() throws Exception {
		debug.Debugger.out("Test Config DB (@HOME_PATH:"+Environment.getParameter(EnvironementParameterName.HOME_PATH)+")...");
		Environment.debug();
		debug.Debugger.out("Test DB cleaning...");
		cleanTable("Config");

		e.getMyAttributes().debug();
		
		debug.Debugger.out("Test Config Attributes...");
		conf.getMyTables().get("Config").getMyAttributes().debug();
		assertEquals(3,conf.getMyTables().get("Config").getMyAttributes().getSize());
		String seekKey = Entity.KEY_NAME;
		assertEquals("KEY_NAME",seekKey);

		String keyName = "test_key";
		String cmd, gets;
		// TODO check here if DB and key already exists then check for update else for insert
		e = conf.getMyTables().get("Config");
		gets = e.getValue(keyName);
		e.setKeyValue(keyName, "1234");
		cmd = e.getMyDBDriver().getLastSQLCommand();
		debug.Debugger.out("SQL-C: {"+cmd+"}");
		if (gets == null) {
			assertEquals("INSERT INTO Config ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+") VALUES ('test_key','1234')",cmd);
		} else {
			assertEquals("UPDATE Config SET "+Entity.VALUE_NAME+" = '1234' WHERE "+Entity.KEY_NAME+" = '"+keyName+"' ",cmd);
		}
		// TODO do not accept two keys of same name (UNIQUE...)
		int res = e.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);

		gets = e.getValue("TestKey");
		debug.Debugger.out("SQL: {"+gets+"}");
		assertEquals(null,gets);
		gets = e.getValue(keyName);
		debug.Debugger.out("SQL: {"+gets+"}");
		assertEquals("1234",gets);

		e.getMyDBDriver().closeDB();
	}
	
	@Test
	void test() {
		try {
			myTest();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception found...");
		}
	}

}
