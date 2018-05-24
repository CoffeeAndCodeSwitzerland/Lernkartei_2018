package persistence.old;

import java.util.ArrayList;

import database.Entity;
import database.SQLHandler;
import database.columns.Attribute;
import database.jdbc.DBDriver;
import debug.Logger;
import persistence.LernKarteiDB;

public class StackEntity extends Entity {

	// Connectioninformationen URL & Driver

	/**
	 * @param tabName
	 * @throws Exception 
	 */
	public StackEntity(DBDriver dbDriver, String tabName) throws Exception {
		super(dbDriver, tabName, false);
	}

	/**
	 * 
	 * Methode, zum Einfügen einer neuen Kategorie
	 *
	 * @param eingabe
	 *            --> String Kategorie
	 * @param fk_door
	 *            --> String Doorname, zu welcher die Kategorie gehört
	 * @throws Exception 
	 */
	public int newStack(LernKarteiDB lk, String eingabe, String doorName) throws Exception {
		String FK_ID = lk.getMyTables().get("Door").getEntityID("name", doorName);
		// Überprüft, ob die Tür exisitert oder nicht
		// "SELECT * FROM STACK WHERE name = '" + eingabe + "'" +"AND PK_DOOR =
		// "+FK_ID
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), null, "name", eingabe, "PK_DOOR", FK_ID));
		debug.Debugger.out("NEW-STACK1:"+myDBDriver.getLastSQLCommand());
		if (myDBDriver.isThereAResult()) { // existiert schon
			debug.Debugger.out("alread exists");
			return -2;
		}
		// Erstellt die neue Kategorie als Eintrag in der Datenbank mit
		// einem Fremdkey für die Tür
		// Do "INSERT INTO Kategorie (Kategorie, FK_Door)" + "VALUES ('" +
		// eingabe + "', " + FK_ID + ")";
		getMyAttributes().getAttributeNamedAs("PK_DOOR").setValue(FK_ID);
		getMyAttributes().getAttributeNamedAs("PK_USER")
				.setValue(1 /* TODO add user ID */);
		getMyAttributes().getAttributeNamedAs("name").setValue(eingabe);
		return myDBDriver.executeCommand(SQLHandler.insertIntoTableCommand(getMyTableName(), getMyAttributes()));
	}

	public ArrayList<String> getKategorien(LernKarteiDB lk, String doorName) throws Exception {
		String FK_ID = lk.getMyTables().get("Door").getEntityID("name", doorName);
		debug.Debugger.out("STACK1: ("+FK_ID+")");
		ArrayList<String> datensatz = new ArrayList<String>();
		// Do "SELECT * FROM Stack WHERE FK_Door = " + FK_ID + ";"
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), null, "PK_DOOR", FK_ID));
		debug.Debugger.out("STACK3: '" + myDBDriver.getLastSQLCommand() +"'");
		while (myDBDriver.isThereAResult()) {
			String res = myDBDriver.getResultValueOf("name");
			datensatz.add(res);
			debug.Debugger.out("ADD:" +	res);
		}
		return datensatz;
	}

	/**
	 * Löscht den gewählten Eintrag
	 * 
	 * @param category
	 *            --> Name der zu löschenden Kategorie
	 * @throws Exception 
	 */
	public boolean delStack(LernKarteiDB lk, String category, String doorName) throws Exception {
		boolean worked = false;
		String FK_ID = lk.getMyTables().get("Door").getEntityID("name", doorName);
		// Überprüft, ob die Tür exisitert oder nicht
		// Abfragen, ob zu löschende Kategorie vorhanden ist oder nicht.
		// Wenn ja, wird gelöscht
		// Do "SELECT * FROM STACK WHERE "+ " name = '" + category + "'" +
		// "AND PK_DOOR =" +FK_ID
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), null, "name", category, "PK_DOOR", FK_ID));
		debug.Debugger.out("DEL-STACK1:" + myDBDriver.getLastSQLCommand());
		if (myDBDriver.isThereAResult()) {
			String setID = myDBDriver.getResultPIntValueOf("PK_STACK");
			// Do "DELETE FROM Stack WHERE PK_STACK = " + setID
			myDBDriver.executeCommand(SQLHandler.deleteEntryCommand("Card", "PK_STACK", setID));
			// debug.Debugger.out("DEL-STACK2:"+getLastSQLCommand());
			// Do "DELETE FROM DOOR WHERE name = '" + category + "'"
			myDBDriver.executeCommand(SQLHandler.deleteEntryCommand(getMyTableName(), "PK_STACK", setID));
			// debug.Debugger.out("DEL-STACK3:"+getLastSQLCommand());
			worked = true;
		}
		return worked;
	}

	public ArrayList<String> getStacknames(LernKarteiDB lk, String doorName) throws Exception {
		ArrayList<String> stacks = new ArrayList<String>();
		String FK_ID = lk.getMyTables().get("Door").getEntityID("name", doorName);
		// Do "SELECT Kategorie FROM Kategorie"
		String cmd;
		if (!FK_ID.equals("{null}")) {
			cmd = SQLHandler.selectCommand(getMyTableName(), null, "PK_DOOR", FK_ID);
		} else {
			cmd = SQLHandler.selectCommand(getMyTableName(), null);
		}
		myDBDriver.executeQuery(cmd);
		try {
			if (myDBDriver.getLastResultSet().isAfterLast()) {
				stacks = null;
			} else {
				while (myDBDriver.isThereAResult()) {
					myDBDriver.getLastResultSet().getRow();
					stacks.add(myDBDriver.getResultValueOf("name"));
				}
			}
		} catch (Exception e) {
			Logger.out(e.getMessage());
		}
		return stacks;
	}

	public boolean possible(String boxName) throws Exception {
		// Do "SELECT * FROM STACK WHERE name = '" + boxName + "';"
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), null, "name", boxName));
		if (!myDBDriver.isThereAResult()) {
			return true;
		}
		return false;
	}

	public boolean update(LernKarteiDB lk, String oldName, String newName, String doorName) throws Exception {
		boolean worked = true;
		String FK_ID = lk.getMyTables().get("Door").getEntityID("name", doorName);
		// Do "SELECT * FROM Stack WHERE name = '" + oldName + "' AND
		// PK_DOOR ="+doorID
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), null, "name", oldName, "PK_DOOR", FK_ID));
		if (myDBDriver.isThereAResult()) {
			Attribute k = new Attribute("name", oldName);
			// Do "UPDATE Stack SET name = '" + newName + "' WHERE name = '"
			// + oldName + "';";
			System.out.println("$$$ SQL: "+SQLHandler.updateInTableCommand(getMyTableName(), getMyAttributes(), k));
			getMyAttributes().getAttributeNamedAs("name").setValue(newName);
			getMyAttributes().getAttributeNamedAs("PK_DOOR").setValue(FK_ID);
			getMyAttributes().getAttributeNamedAs("PK_USER").setValue(1);
			System.out.println("$$$ SQL: "+SQLHandler.updateInTableCommand(getMyTableName(), getMyAttributes(), k));
			myDBDriver.executeCommand(SQLHandler.updateInTableCommand(getMyTableName(), getMyAttributes(), k));
			worked = true;
		} else {
			worked = false;
		}
		return worked;
	}
}
