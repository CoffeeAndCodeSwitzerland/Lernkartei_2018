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
import database.columns.Attribute;
import database.columns.AttributeList;
import persistence.LernKarteiDB;

/**
 * @author WISS
 *
 */
public class LKDatabaseTest {

	final static LernKarteiDB dataBaseModel = new LernKarteiDB();
	static String cmd;
	static int result;
	static Entity e;

	public static void cleanTableTest(String name) throws Exception {
		e = dataBaseModel.getMyTables().get(name);
		int res = e.delValues();
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		assertEquals("PK_"+e.getMyTableName(),e.getMyAttributes().getPrimaryKey().getName());
		e.getMyDBDriver().closeDB();
	}
	
	/**
	 * Crate a new entry in a table
	 * DB objects are predefined in dataBaseModel
	 * 
	 * @param tableName
	 * @param values
	 * @throws Exception
	 */
	public static int createNewEntry(String tableName, Attribute[] newValues) throws Exception {
		Entity tab = dataBaseModel.getMyTables().get(tableName); // seek Table-Object
		AttributeList tabAttributes = tab.getMyAttributes();   // get his Attributes (Objects)
		for (Attribute a : newValues) {
			tabAttributes.getAttributeNamedAs(a.getName()).setValue(a.getValue()); // set the new values
		}
		cmd = SQLHandler.insertIntoTableCommand(tableName,tabAttributes); // execute the insert command
		return tab.getMyDBDriver().executeCommand(cmd); // execute Insert Command
	}
	
	public static void createNewEntryTest(String tableName, Attribute[] newValues) throws Exception {

		result = createNewEntry(tableName, newValues); // generate SQL Insert Command into cmd

		// check results:
		debug.Debugger.out("SQL-D: {" + cmd + "}->RESULT: {" + result + "}");
		assertEquals(	"INSERT INTO " + tableName + " (" + Entity.KEY_NAME+", " + Entity.VALUE_NAME +
						", Name) VALUES ('','',"+newValues+")",cmd);
		assertEquals(true, (result >= 0) );
	}

		
	public static void clearDBTest () throws Exception {
		
		// @TODO Delete DB-file if exists
		
		debug.Debugger.out("Test DB cleaning...");
		cleanTableTest("Card");
		cleanTableTest("Stack");
		cleanTableTest("Door");
		cleanTableTest("User");
		cleanTableTest("Side");
		cleanTableTest("Learn");
		cleanTableTest("Login");
	}
	
	public static void myTest() throws Exception {
		debug.Debugger.out("Test LKDatabase (@HOME_PATH:"+Environment.getParameter(EnvironementParameterName.HOME_PATH)+")...");
		Environment.debug();
		clearDBTest();

		debug.Debugger.out("Test Doors...");
		Attribute[] attrs = {new Attribute("Name","hallo!!!")};
		createNewEntryTest("Door", attrs);
		
		ArrayList<String> aList = e.getDataList("SELECT name FROM Door");
		cmd = e.getMyDBDriver().getLastSQLCommand();
		debug.Debugger.out("DATA: {"+cmd+"}");
		assertEquals("SELECT name FROM Door",cmd);
		assertEquals(false,e.getMyDBDriver().isThereAResult()); // no more data is ok after get
		aList = e.getDataList("SELECT name FROM Door"); // again
		debug.Debugger.out("SQL: {"+aList.get(0)+"}");
		assertEquals("hallo!!!",aList.get(0));

		e.getMyAttributes().getAttributeNamedAs("name").setValue("hallo2");
		cmd = SQLHandler.insertIntoTableCommand(e.getMyTableName(),e.getMyAttributes()); 
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("INSERT INTO Door ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+", Name) VALUES ('','','hallo2')",cmd);
		int res = e.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);

		String FK_ID = e.getEntityID("name","hallo2");
		debug.Debugger.out("SQL: ("+FK_ID+")");
		//assertEquals(2,FK_ID); wechselt immer, solange kein DROP TABLE, evtl. mit MIN(ID) herausfindbar
		e.getMyDBDriver().closeDB();
	
		debug.Debugger.out("Test Users...");
		e = dataBaseModel.getMyTables().get("User");
		e.getMyAttributes().getAttributeNamedAs("Username").setValue("local");
		cmd = SQLHandler.insertIntoTableCommand(e.getMyTableName(),e.getMyAttributes()); 
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("INSERT INTO User ("+Entity.KEY_NAME+", "+Entity.VALUE_NAME+", ActualScore, Username, Email, Password, Salz, HighScore, UserType) VALUES ('','',0,'local','','','',0,0)",cmd);
		res = e.getMyDBDriver().executeCommand(cmd);
		debug.Debugger.out("RESULT: {"+res+"}");
		assertEquals(true,res>=0);
		e.getMyDBDriver().closeDB();

		debug.Debugger.out("Test Stacks...");
		e = dataBaseModel.getMyTables().get("Stack");
		//e.closeDB();
		String door_id = dataBaseModel.getMyTables().get("Door").getEntityID("name", "hallo2");
		cmd = e.getMyDBDriver().getLastSQLCommand();
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("SELECT PK_Door FROM Door WHERE name = 'hallo2' ",cmd);
		String user_id = dataBaseModel.getMyTables().get("User").getEntityID("Username", "local");
		dataBaseModel.getMyTables().get("Door").getMyDBDriver().closeDB();
		dataBaseModel.getMyTables().get("User").getMyDBDriver().closeDB();
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
		e=dataBaseModel.getMyTables().get("Card");
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
