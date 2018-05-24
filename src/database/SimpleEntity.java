package database;

import java.util.ArrayList;

import database.columns.Attribute;
import database.columns.AttributeList;
import database.columns.AttributeListHandler;
import database.columns.KeyAttribute;
import database.columns.PrimaryKey;
import database.columns.AttributeInterface.Datatype;
import database.jdbc.DBDriver;
import database.jdbc.DBDriver.ResultType;
import debug.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 * Simple Entity (Table)
 * - besitzt automatisch und immer eine Primary Key 
 * - besitzt automatisch einen Index (für zum Bsp. config.db) 
 * - bietet Debug informationen, wie lastSQLCommand und lastResultSet an
 * 
 * @author hugo-lucca
 *
 */
public class SimpleEntity extends AttributeListHandler {

	public @Getter DBDriver myDBDriver; // should never be null
	protected @Getter @Setter String myTableName;  // should never be null

	/**
	 * To create a DB table if it does not exist yet
	 * 
	 * @param tableName
	 * @param attributes
	 * @return 0, row count or -1 for error
	 * @throws Exception 
	 */
	public ResultType createTableIfNotExists() throws Exception {
		if (isCreated() == true)
			return ResultType.DONE;
		int result = -1;
		result = myDBDriver.executeCommand(SQLHandler.createTableIfNotExistsCommand(myTableName, getMyAttributes()));
		if (result >= 0) {
			setCreated(true);
			return ResultType.DONE;
		}
		return ResultType.NOTDONE;
	}

	/**
	 * Constructors
	 * @throws Exception 
	 */
	public SimpleEntity(DBDriver newDBdriver, String tabName) throws Exception {
		myDBDriver = newDBdriver;
		myTableName = tabName;
		addPrimaryKey("PK_" + myTableName);
		setCreated(false);	
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int delValues() throws Exception {
		return getMyDBDriver().executeCommand("DELETE FROM "+getMyTableName());
	}
	/**
	 * to delete a key with a specific value
	 * 
	 * @param key
	 * @return >0 for success, 0 for NOP or -1 for errors
	 * @throws Exception 
	 */
	public int delKeyValue(String key, String value) throws Exception {
		return myDBDriver.executeCommand(SQLHandler.deleteEntryCommand(myTableName, key, value)); // -1, 0, 1, 2
	}

	/**
	 * to delete a key
	 * 
	 * @param key
	 * @return >0 for success, 0 for NOP or -1 for errors
	 * @throws Exception 
	 */
	public int delKey(String key) throws Exception {
		return delKeyValue(key, null);
	}

	/**
	 * @param tabName
	 * @param attName
	 * @param pkey
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	protected ResultType seekInTable(String pKey, String value) throws Exception {
		// return seekSQL("SELECT " + attName + " FROM " + tabName + " WHERE " +
		// pKey + " = '" + value + "'");
		return myDBDriver.executeQuery(SQLHandler.selectCommand(myTableName, pKey, value));
	}

	public String seekInTable(String returnKey, String seekKey, String seekValue) throws Exception {
		// return seekSQL("SELECT " + returnKey + " FROM " + tabName + " WHERE "
		// +
		// seekKey + " = '" + seekValue + "'");
		myDBDriver.executeQuery(SQLHandler.selectCommand(myTableName, null, seekKey, seekValue));
		try {
			if (myDBDriver.isThereAResult()) {
				return myDBDriver.getResultValueOf(returnKey);
			}
			Logger.out("no values found!", myDBDriver.getLastSQLCommand());
		} catch (Exception e) {
			Logger.out("Resultset is corrupt", myDBDriver.getLastSQLCommand());
		}
		return null;
	}

	/**
	 * Seek Value
	 * 
	 * @param value
	 * @return ResultSet or null on errors
	 * @throws Exception 
	 */
	public ResultType seekAttribute(AttributeList attributes) throws Exception {
		return myDBDriver.executeQuery(SQLHandler.selectCommand(myTableName, attributes));
	}

	/**
	 * set or update an new key with that value
	 * 
	 * @param key
	 * @param value
	 * @return >0 for success, 0 for NOP or -1 for errors
	 * @throws Exception 
	 */
	protected int replaceOrInsertToken(AttributeList aList, String kName, String key, String vName, String value) throws Exception {
		KeyAttribute k = new KeyAttribute(kName, key, Datatype.TEXT);
		Attribute a = new Attribute(vName, value);
		Attribute s = aList.getAttributeNamedAs(kName);
		if (s != null)
			s.setValue(key);
		else
			aList.addUnique(k);
		Attribute sa = aList.getAttributeNamedAs(vName);
		if (sa != null)
			sa.setValue(value);
		else
			aList.addUnique(a);
		// Überprüfen ob bereits ein Token vorhanden ist, wenn ja, überschreiben
		myDBDriver.executeQuery(SQLHandler.selectCommand(myTableName, aList, k, null));
		String cmd;
		if (myDBDriver.isThereAResult()) {
			// mindestens einen Eintrag gefunden:
			// TODO prüfen, dass es nur einer ist (sollte eigentlich, wenn unique)...
			//
			// "UPDATE " + tabName + " SET " + vName + " = '" + value + "'"
			// + " WHERE " + kName + " = '" + key + "'";
			cmd = SQLHandler.updateInTableCommand(myTableName, vName, value, kName, key);
		} else {
			// Kein Eintrag gefunden:
			// "INSERT INTO " + tabName + " (" + kName + ", " + vName + ") "
			// + "VALUES ('" + key
			// + "','" + value + "')";
			cmd = SQLHandler.insertIntoTableCommand(myTableName, getMyAttributes(), kName, key, vName, value);
		}
		return myDBDriver.executeCommand(cmd);
	}

	/**
	 * special SETTERs and GETTERs
	 * @throws Exception 
	 */
	public PrimaryKey getPrimaryKey() throws Exception {
		return getMyAttributes().getPrimaryKey();
	}

	public String getEntityID(String key, String value) throws Exception {
		Attribute a = new Attribute(key, value);
		return getEntityID(a);
	}

	public String getEntityID(String key, int value) throws Exception {
		Attribute a = new Attribute(key, Integer.toString(value), Datatype.INT);
		return getEntityID(a);
	}

	/**
	 * Methode, welche alle key.namen in einer Liste ausgibt
	 * 
	 * @return --> Retourniert die Liste mit allen key.namen werte
	 * @throws Exception 
	 */
	public ArrayList<String> getDataList(Attribute key) throws Exception {
		return getDataList(SQLHandler.selectCommand(getMyTableName(), key.getName(), null, null));
	}

	/**
	 * To get the PK_ID of this table using a unique key to find the row
	 * 
	 * @param key
	 * @return PK_ID as String
	 * @throws Exception 
	 */
	public String getEntityID(Attribute key) throws Exception {
		if (key != null) {
			String pk_name = getMyAttributes().getPrimaryKey().getName();
			String keyName = key.getName();
			String cmd;
			//if (key.isTEXT()) {
				cmd = SQLHandler.selectCommand(myTableName, pk_name, keyName, key.getValue());
			/*} else {
				cmd = SQLHandler.selectCommand(myTableName, pk_name, keyName, key.getIntValue());
			}*/
			debug.Debugger.out("Entity: '" + cmd);
			// Do "SELECT "+key.name+" FROM myTableName WHERE "+key.name+" =
			// " + key.value)
			myDBDriver.executeQuery(cmd);
			// assumed is only one key and only first returned (no check is
			// done)
			return myDBDriver.getResultPIntValueOf(pk_name);
		}
		throw new Exception("invalid {null}-key for the SELECT PK_... command");
	}

	/**
	 * To get a list of values from a table or sql-view
	 * 
	 * @param sql-query
	 * @return the list of values or null
	 * @throws Exception 
	 */
	public ArrayList<String> getDataList(String query) throws Exception {
		ArrayList<String> values = new ArrayList<String>();
		myDBDriver.executeQuery(query);
		while (myDBDriver.isThereAResult()) {
			values.add(myDBDriver.getFirstResultValue());
		}
		return values;
	}

}
