package persistence;

import database.DBModel;
import database.columns.Attribute;
import database.columns.ForeignKey;
import database.columns.KeyAttribute;
import database.columns.PrimaryKey;

/**
 * Application DB definitions
 * 
 * @author WISS
 *
 */
public class LernKarteiDB extends DBModel {

	private final static String[] tables = {"Door","Stack","Card","Side","Learn","User","Login"};

	private final static Attribute[] attributes = {
			new PrimaryKey(tables[0]), new Attribute("Name"),
			
			new PrimaryKey(tables[1]), new Attribute("Name"),
			new Attribute("Description"), new ForeignKey(tables[0]), new ForeignKey(tables[5]),
						
			new PrimaryKey(tables[2]), new ForeignKey(tables[1]), new Attribute("Frontside"),
			new Attribute("Backside"), new KeyAttribute("Priority", 0, "1"),
			new Attribute("Color"),new Attribute("Link"), new Attribute("Description"),
			new Attribute("Date"),

			new PrimaryKey(tables[3]), new Attribute("Link"), new Attribute("BackgoundColor"),
			new Attribute("Text"), new Attribute("SideOrder",0),
			
			new PrimaryKey(tables[4]), new Attribute("WasCorrect",0), 
			new Attribute("Date"), new ForeignKey(tables[2]), new ForeignKey(tables[5]),
		
			new PrimaryKey(tables[5]), new Attribute("ActualScore", 0), 
			new Attribute("Username", "def"),new Attribute("Email"),
			new Attribute("Password"), new Attribute("Salz"),
			new Attribute("HighScore", 0), new Attribute("UserType", 0),

			new PrimaryKey(tables[6]), new Attribute("Name"),
			new Attribute("Password")
	};
	
	public LernKarteiDB () {
		super (DBGlobals.db_name, tables, attributes);
	}
}
