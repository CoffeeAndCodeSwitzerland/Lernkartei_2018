package database;

import java.util.HashMap;
import java.util.Map;

import database.columns.Attribute;
import database.columns.AttributeList;
import database.jdbc.DBDriver;
import database.jdbc.SQLiteDriver;
/**
 * A DB has one or more tables with 3 to many attributes
 * 
 * @author WISS
 *
 */
public class DBModel {

	private final DBDriver myWLCDB;
	private final HashMap<String,Entity> myTables = new HashMap<String,Entity>();


	public HashMap<String, Entity> getMyTables() {
		return myTables;
	}

	private void createAllTableObjectsFor(DBDriver dbDriver, String[] tables) {
		for (String tab : tables) {
			try {
				myTables.put(tab,new Entity(dbDriver,tab,false));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createAllTables() {
		for(Map.Entry<String, Entity> entry : myTables.entrySet()) {
		    //String key = entry.getKey();
		    Entity e = entry.getValue();
			try {
				e.createTableIfNotExists();
			    System.out.println("Create: "+e.getMyTableName());
				String cmd = e.getMyDBDriver().getLastSQLCommand(); 
				debug.Debugger.out("Create: {"+cmd+"}");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void addAllAttributes (Attribute[] attributes) {
		AttributeList myAttributes = null;
		String tabName;
		//myAttributes.debug();
		for (Attribute a : attributes) {
			if (a.isPrimary()) {
				tabName = a.getName();
			    myAttributes = myTables.get(tabName).getMyAttributes();
				//myAttributes.debug();
			    // @TODO check if there are no PK insert it as unique
			} else {
				//System.out.println("ADD:"+a.getName()+"/"+a.isPrimary());
				try {
					myAttributes.addUnique(a);
					//myAttributes.debug();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public DBModel (String dbName, String[] tables, Attribute[] attributes) {
		myWLCDB = new SQLiteDriver(dbName + ".db"); 
		debug.Debugger.out("Create Objects...");
		createAllTableObjectsFor(myWLCDB,tables);
		debug.Debugger.out("Create Attributes...");
		addAllAttributes(attributes);
		createAllTables();	
	}	
	
}
