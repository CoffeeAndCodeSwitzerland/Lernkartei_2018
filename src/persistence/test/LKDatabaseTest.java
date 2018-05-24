package persistence.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.Test;

import basics.Environment;
import basics.Environment.EnvironementParameterName;
import database.Entity;
import database.SQLHandler;
import persistence.LernKarteiDB;

/**
 * @author WISS
 *
 */
public class LKDatabaseTest {

	final static LernKarteiDB lk = new LernKarteiDB();
	static String cmd;
	static Entity e;

	public static void cleanTable(String name) throws Exception {
		e = lk.getMyTables().get(name);
		int res = e.delValues();
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		assertEquals("PK_"+e.getMyTableName(),e.getMyAttributes().getPrimaryKey().getName());
		e.getMyDBDriver().closeDB();
	}
	
	public static void myTest() throws Exception {
		debug.Debugger.out("Test LKDatabase (@HOME_PATH:"+Environment.getParameter(EnvironementParameterName.HOME_PATH)+")...");
		Environment.debug();
		// @TODO Del DB if exists
		debug.Debugger.out("Test DB cleaning...");
		cleanTable("Card");
		cleanTable("Stack");
		cleanTable("Door");
		cleanTable("User");
		cleanTable("Side");
		cleanTable("Learn");
		cleanTable("Login");

		debug.Debugger.out("Test Doors...");
		Entity de = lk.getMyTables().get("Door");
		de.getMyAttributes().debug();
		de.getMyAttributes().getAttributeNamedAs("Name").setValue("hallo!!!");
		cmd = SQLHandler.insertIntoTableCommand(de.getMyTableName(),de.getMyAttributes());
		debug.Debugger.out("SQL-D: {"+cmd+"}");
		assertEquals("INSERT INTO Door ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+", Name) VALUES ('','','hallo!!!')",cmd);
		int res = de.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		ArrayList<String> aList = de.getDataList("SELECT name FROM Door");
		cmd = de.getMyDBDriver().getLastSQLCommand();
		debug.Debugger.out("DATA: {"+cmd+"}");
		assertEquals("SELECT name FROM Door",cmd);
		assertEquals(false,de.getMyDBDriver().isThereAResult()); // no more data is ok after get
		aList = de.getDataList("SELECT name FROM Door"); // again
		debug.Debugger.out("SQL: {"+aList.get(0)+"}");
		assertEquals("hallo!!!",aList.get(0));

		de.getMyAttributes().getAttributeNamedAs("name").setValue("hallo2");
		cmd = SQLHandler.insertIntoTableCommand(de.getMyTableName(),de.getMyAttributes()); 
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("INSERT INTO Door ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+", Name) VALUES ('','','hallo2')",cmd);
		res = de.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);

		String FK_ID = de.getEntityID("name","hallo2");
		debug.Debugger.out("SQL: ("+FK_ID+")");
		//assertEquals(2,FK_ID); wechselt immer, solange kein DROP TABLE, evtl. mit MIN(ID) herausfindbar
		de.getMyDBDriver().closeDB();
	
		debug.Debugger.out("Test Users...");
		e = lk.getMyTables().get("User");
		e.getMyAttributes().getAttributeNamedAs("Username").setValue("local");
		cmd = SQLHandler.insertIntoTableCommand(e.getMyTableName(),e.getMyAttributes()); 
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("INSERT INTO User ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+", ActualScore, Username, Email, Password, Salz, HighScore, UserType) VALUES ('','',0,'local','','','',0,0)",cmd);
		res = e.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		e.getMyDBDriver().closeDB();

		debug.Debugger.out("Test Stacks...");
		e = lk.getMyTables().get("Stack");
		//e.closeDB();
		String door_id = lk.getMyTables().get("Door").getEntityID("name", "hallo2");
		cmd = de.getMyDBDriver().getLastSQLCommand();
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("SELECT PK_Door FROM Door WHERE name = 'hallo2' ",cmd);
		String user_id = lk.getMyTables().get("User").getEntityID("Username", "local");
		lk.getMyTables().get("Door").getMyDBDriver().closeDB();
		lk.getMyTables().get("User").getMyDBDriver().closeDB();
		assertNotEquals(door_id,"{null}");
		assertNotEquals(user_id,"{null}");
		assertNotEquals(door_id,null);
		assertNotEquals(user_id,null);
		e.getMyAttributes().getAttributeNamedAs("FK_DOOR").setValue(door_id);
		e.getMyAttributes().getAttributeNamedAs("FK_USER").setValue(user_id);
		e.getMyAttributes().getAttributeNamedAs("name").setValue("hh1");
		cmd = SQLHandler.insertIntoTableCommand(e.getMyTableName(),e.getMyAttributes()); 
		debug.Debugger.out("SQL: {"+cmd+"}"+door_id+"/"+user_id);
		assertEquals("INSERT INTO Stack ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+", Name, Description, FK_Door, FK_User) VALUES ('','','hh1','',"+door_id+","+user_id+")",cmd);
		res = e.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		e.getMyDBDriver().closeDB();

		debug.Debugger.out("Test Cards...");
		e=lk.getMyTables().get("Card");
		e.getMyAttributes().getAttributeNamedAs("Frontside").setValue("hallo!!!");
		cmd = SQLHandler.insertIntoTableCommand(e.getMyTableName(),e.getMyAttributes()); 
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("INSERT INTO Card ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+", FK_Stack, Frontside, Backside, Priority, Color, Link, Description, Date) VALUES ('','',0,'hallo!!!','',0,'','','','')",cmd);
		res = e.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		e.getMyDBDriver().closeDB();
	}

	@Test
	public void test() {
		try {
			myTest();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception found...");
		}
	}

}
