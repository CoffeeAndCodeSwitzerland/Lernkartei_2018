package persistence.old;

import java.util.ArrayList;

import database.Entity;
import database.SQLHandler;
import database.columns.Attribute;
import database.jdbc.DBDriver;
import persistence.LernKarteiDB;

public class DoorEntity extends Entity {

	public DoorEntity(DBDriver dbDrive, String tabName) throws Exception {
		super(dbDrive, tabName, false);
	}

	/**
	 * Methode, zum Erstellen einer neuen Türe
	 * 
	 * @param eingabe
	 *            --> String Name der Tür
	 * 
	 * @return --> True, wenn Eintrag eingefügt, false, wenn Eintrag bereits
	 *         vorhanden
	 * @throws Exception 
	 *
	 */
	public boolean newDoor(String eingabe) throws Exception {
		boolean worked = false;
		// Do "SELECT Doorname FROM Doors WHERE Doorname = " + "'" + eingabe +
		// "'"
		myDBDriver.executeQuery(SQLHandler.selectCommand(this.getMyTableName(), "name", "name", eingabe));
		// Überprüft, ob bereits ein Eintrag mit dem Selben Namen enthalten ist
		if (!myDBDriver.isThereAResult()) {
			// Einfügen des Datensatzes in Doors
			getMyAttributes().getAttributeNamedAs("name").setValue(eingabe);
			// Do "INSERT INTO Doors (Doorname)" + "VALUES ('" + eingabe + "')";
			worked = (myDBDriver.executeCommand(SQLHandler.insertIntoTableCommand(getMyTableName(), getMyAttributes())) >= 0) ? true : false;
		}
		return worked; 
	}

	/**
	 * 
	 * Methode, welche alle Türen in einer Liste ausgibt
	 * 
	 * @return --> Retourniert die Liste mit allen Türennamen
	 * @throws Exception 
	 */
	public ArrayList<String> getDoors() throws Exception {
		Attribute key = new Attribute("name", null);
		return getDataList(key);
	}

	/**
	 * Gibt boolean zurück, obs funktioniert hat oder nicht
	 * 
	 * @param delName
	 *            --> Name, welcher gelöscht werden soll
	 * @return --> True, Gelöscht / false, nicht Gelöscht / vorhanden
	 * @throws Exception 
	 */
	public boolean delDoor(LernKarteiDB lk, String delName) throws Exception {
		boolean worked = false;
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), "PK_DOOR", "name", delName));
		if (myDBDriver.isThereAResult()) {
			String doorID = myDBDriver.getResultPIntValueOf("PK_DOOR");

			// Do "SELECT * FROM Kategorie WHERE PK_Door = " + doorID
			myDBDriver.executeQuery(SQLHandler.selectCommand("STACK", null, "PK_DOOR", doorID));
			ArrayList<String> setsToDel = new ArrayList<String>();
			while (myDBDriver.isThereAResult()) {
				setsToDel.add(myDBDriver.getResultValueOf("name"));
			}
			for (String s : setsToDel) {
				lk.getMyTables().get("Stack").delKeyValue(s, delName);
			}
			myDBDriver.executeCommand(SQLHandler.deleteEntryCommand(getMyTableName(), "PK_DOOR", doorID));
			// Do "DELETE FROM Doors WHERE Doorname = '" + delName + "'";
			myDBDriver.executeCommand(SQLHandler.deleteEntryCommand("STACK", "PK_DOOR", doorID));

			// TODO Delete all cards of those stacks...
			// Do "DELETE FROM Kategorie WHERE FK_Door = " + doorID;
			// setLastSQLCommand(SQLHandler.deleteEntryCommand("STACK",
			// "PK_DOOR", doorID));
			// setLastResultSet(executeQuery(getLastSQLCommand()));
			// String delSets = "DELETE FROM Kategorie WHERE FK_Door = " +
			// doorID;
			worked = true;
		} else {
			worked = false;
		}
		return worked;
	}

	public boolean update(String oldName, String newName) throws Exception {
		boolean worked = true;
		// Do "SELECT * FROM Doors WHERE Doorname = '" + oldName + "';"
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), "name", "name", oldName));
		if (myDBDriver.isThereAResult()) {
			Attribute k = new Attribute("name", oldName);
			// Do "UPDATE Doors SET Doorname = '" + newName + "' WHERE
			// Doorname = '" + oldName + "';"
			getMyAttributes().getAttributeNamedAs("name").setValue(newName);
			myDBDriver.executeCommand(SQLHandler.updateInTableCommand(getMyTableName(), getMyAttributes(), k));
			worked = true;
		} else {
			worked = false;
		}
		return worked;
	}
}
