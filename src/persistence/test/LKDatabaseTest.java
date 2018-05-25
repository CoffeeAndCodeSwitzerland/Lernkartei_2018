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

	public static int cleanTable(String name) throws Exception {
		Entity e = dataBaseModel.getMyTables().get(name);
		return e.delValues();
	}

	public static Entity cleanTableTest(String name) throws Exception {

		int result = cleanTable(name);

		// check results:
		debug.Debugger.out("RESULT: {"+result+"}");
		assertEquals(true,result>=0);
		Entity e = dataBaseModel.getMyTables().get(name);
		assertEquals("PK_"+e.getMyTableName(),e.getMyAttributes().getPrimaryKey().getName());
		return e;
	}

	public static void clearDBTest () throws Exception {
		debug.Debugger.out("Test DB cleaning...");
		
		// @TODO Delete DB-file if exists
		
		cleanTableTest("Card");
		cleanTableTest("Stack");
		cleanTableTest("Door");
		cleanTableTest("User");
		cleanTableTest("Side");
		cleanTableTest("Learn");
		Entity e = cleanTableTest("Login");
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
		e = dataBaseModel.getMyTables().get(tableName); // seek Table-Object
		AttributeList tabAttributes = e.getMyAttributes();   // get his Attributes (Objects)
		for (Attribute a : newValues) {
			tabAttributes.getAttributeNamedAs(a.getName()).setValue(a.getValue()); // set the new values
		}
		cmd = SQLHandler.insertIntoTableCommand(tableName,tabAttributes); // execute the insert command
		return e.getMyDBDriver().executeCommand(cmd); // execute Insert Command
	}
	
	public static void createNewEntryTest(String tableName, Attribute[] newValues) throws Exception {

		result = createNewEntry(tableName, newValues); // generate SQL Insert Command into cmd

		// check results:
		debug.Debugger.out("SQL-D: {" + cmd + "}->RESULT: {" + result + "}");
		String expectedCmd =	"INSERT INTO " + tableName + " (" ;
		boolean isFirst = true;
		for (Attribute a : e.getMyAttributes().getMyAttributes()) {
			if (!a.isPrimary()) {
				if (isFirst) {
					expectedCmd += a.getName(); 
					isFirst = false;
				} else {
					expectedCmd += ", " + a.getName();
				}
			}
		}
		expectedCmd += ") VALUES (";
		isFirst = true;
		for (Attribute a : e.getMyAttributes().getMyAttributes()) {
			if (!a.isPrimary()) {
				if (isFirst) {
					if (a.isValue())
						 expectedCmd += a.getValue(); 
					else expectedCmd += "'" + a.getValue() + "'"; 
					isFirst = false;
				} else {
					if (a.isValue())
						 expectedCmd += "," + a.getValue(); 
					else expectedCmd += ",'" + a.getValue() + "'"; 
				}
			}
		}
		expectedCmd += ")";
		debug.Debugger.out("SQL-Exp: {" + expectedCmd + "}");
		assertEquals( expectedCmd, cmd);
		assertEquals(true, (result >= 0) );
	}

		
	public static void myTest() throws Exception {
		debug.Debugger.out("Test LKDatabase (@HOME_PATH:"+Environment.getParameter(EnvironementParameterName.HOME_PATH)+")...");

		Environment.debug();  // just show environment var's
		clearDBTest();

		debug.Debugger.out("Test Insert values into Doors (1 line/entry)...");
		Attribute[] newAttrs = {new Attribute("Name","hallo!!!")}; // only changing values
		String tabName = "Door";
		createNewEntryTest(tabName, newAttrs);

		debug.Debugger.out("Test reading Door data...");
		ArrayList<String> aList = e.getDataList("SELECT * FROM Door");
		// check results for reading data:
		cmd = e.getMyDBDriver().getLastSQLCommand();
		debug.Debugger.out("DATA: {" + cmd + "}");
		assertEquals("SELECT * FROM Door",cmd);	// check SQL command and result-flag
		assertEquals(false, e.getMyDBDriver().isThereAResult()); // no more data is ok after get

		aList = e.getDataList("SELECT Name FROM Door"); // read again...
		// check results for reading data:
		debug.Debugger.out("SQL: {"+aList.get(0)+"}"); // check cursor read
		assertEquals("hallo!!!",aList.get(0));

		debug.Debugger.out("Test Insert values into Doors after reading...");
		Attribute[] attrs2 = {new Attribute("Name","hallo2")};
		createNewEntryTest("Door", attrs2);

		debug.Debugger.out("Test PK ID...");
		String PK_ID = e.getEntityID("Name","hallo2");
		debug.Debugger.out("SQL: (" + PK_ID + ")");
		assertEquals(true,Integer.parseInt(PK_ID) > 0);// ID wechselt immer, solange kein DROP TABLE, evtl. mit MIN(ID) herausfindbar
		e.getMyDBDriver().closeDB();
	
		debug.Debugger.out("Test Insert values into User (for 2 users)...");
		Attribute[] attrs3 = {new Attribute("Username","local"), new Attribute("Email","user1@mail.ch")};
		createNewEntryTest("User", attrs3);
		Attribute[] attrs4 = {new Attribute("Username","global"), new Attribute("Email","user2@mail.ch")};
		createNewEntryTest("User", attrs4);
		
		debug.Debugger.out("Test Get Door Ref...");
		e = dataBaseModel.getMyTables().get("Door");
		String door_id = e.getEntityID("name", "hallo2"); // get Door-ID where name='hallo2'
		// check results:
		cmd = e.getMyDBDriver().getLastSQLCommand();
		e.getMyDBDriver().closeDB();
		debug.Debugger.out("SQL: {"+cmd+"}");
		assertEquals("SELECT PK_Door FROM Door WHERE name = 'hallo2' ",cmd);
		
		debug.Debugger.out("Test Get User Ref...");
		e = dataBaseModel.getMyTables().get("User");
		String user_id = e.getEntityID("Username", "local");
		// check results:
		e.getMyDBDriver().closeDB();
		assertNotEquals(door_id,null);
		assertNotEquals(user_id,null);

		debug.Debugger.out("Test Insert values into Stack...");
		Attribute[] attrs5 = {new Attribute("FK_Door",door_id), 
							  new Attribute("FK_User",user_id),
							  new Attribute("Name","hh1")};
		createNewEntryTest("Stack", attrs5);

		debug.Debugger.out("Test Insert values into Card...");
		Attribute[] attrs6 = {new Attribute("Frontside","hallo...")};
		createNewEntryTest("Card", attrs6);

		// @TODO test update
		
		// @TODO test delete one entry from table
		
		// @TODO test delete recursive
		
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
