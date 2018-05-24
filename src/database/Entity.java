package database;

import database.columns.Attribute;
import database.columns.AttributeList;
import database.columns.KeyAttribute;
import database.columns.AttributeInterface.Datatype;
import database.jdbc.DBDriver;

/**
 * Entity - Table 
 * - besitzt automatisch und immer eine Primary Key 
 * - besitzt automatisch einen Index (für zum Bsp. config.db) 
 * - bietet Debug informationen, wie lastSQLCommand und lastResultSet an
 * 
 * @author hugo-lucca
 *
 */
public class Entity extends SimpleEntity {

	public final static String KEY_NAME = "KEY_NAME";
	public final static String VALUE_NAME = "KEY_VALUE";

	/*
	 * TODO add a getSize() method... 
	 *      is needed in many cases, this is now performed in a more complicated way!
	 */
	
	/**
	 * To add basic attributes
	 * @throws Exception 
	 */
	private void addBaseAttributes() throws Exception {
		KeyAttribute k = new KeyAttribute(KEY_NAME, "", Datatype.TEXT);
		getMyAttributes().addUnique(k);
		Attribute a = new Attribute(VALUE_NAME, "");
		getMyAttributes().addUnique(a);
	}

	/**
	 * Constructors
	 * @throws Exception 
	 */
	public Entity(DBDriver newDBdriver, String tabName, boolean createIt) throws Exception {
		super(newDBdriver, tabName);
		addBaseAttributes();
		//setDriver(Globals.db_name); // set other DB name, than default
		// table will possibly not be created at DB at this moment!
		if (createIt)
			createTableIfNotExists();
	}

	public Entity(DBDriver newDBdriver, String tabName) throws Exception {
		super(newDBdriver, tabName);
		addBaseAttributes();
		createTableIfNotExists();
	}

/*
	public Entity(DBDriver newDBdriver, String tabName, String pkName, boolean createIt) {
		this(newDBdriver, tabName, false);
		if (createIt)
			// table will possibly not be created at DB at this moment!
			createTableIfNotExists();
	}

	public Entity(DBDriver newDBdriver, String dbName, String tabName, String pkName, boolean createIt) {
		this(newDBdriver, tabName, pkName, false);
		//setDriver(dbName); // set other DB name, than default
		// table will not be created here!
		if (createIt)
			createTableIfNotExists();
	}
*/
	/**
	 * to delete a value
	 * 
	 * @param key
	 * @return >0 for success, 0 for NOP or -1 for errors
	 * @throws Exception 
	 */
	public int delValue(String value) throws Exception {
		return delKeyValue(VALUE_NAME, value);
	}

	/**
	 * special SETTERs and GETTERs
	 */

	/**
	 * Set a value
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public int setValue(String value) throws Exception {
		AttributeList attributes = new AttributeList();
		Attribute a = new Attribute(VALUE_NAME, value);
		attributes.addUnique(a);
		seekAttribute(attributes);
		if (myDBDriver.isThereAResult()) {
			// Einfügen des Datensatze, wenn keins da
			return myDBDriver.executeCommand(SQLHandler.insertIntoTableCommand(myTableName, attributes));
		}
		return -1;
	}

	/**
	 * To set a single value using the index
	 * 
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public int setValue(int value) throws Exception {
		return setValue(Integer.toString(value));
	}

	/**
	 * To get a single value from the index
	 * 
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public String getValue(String query) throws Exception {
		return seekInTable(VALUE_NAME, KEY_NAME, query);
	}

	/**
	 * set the value of a key (column 1 and 2)
	 * 
	 * @param key
	 *            --> the key name
	 * @return the value of that key, or null on errors or if key name not found
	 * @throws Exception 
	 */
	public int setKeyValue(String rowkey, String value) throws Exception {
		return replaceOrInsertToken(getMyAttributes(), KEY_NAME, rowkey, VALUE_NAME, value);
	}

}
