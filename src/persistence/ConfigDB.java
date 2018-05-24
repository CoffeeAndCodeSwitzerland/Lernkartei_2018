package persistence;

import database.DBModel;
import database.columns.Attribute;
import database.columns.PrimaryKey;

/**
 * The config Table may be in separated DB
 * 
 * @author WISS
 *
 */
public class ConfigDB extends DBModel {

	private final static String[] tables = {"Config"};

	private final static Attribute[] attributes = {
			new PrimaryKey(tables[0]) // PK_Config, P_VAL and P_KEY are automatically created
	};
	
	public ConfigDB() {
		super(DBGlobals.config_db_name, tables, attributes);
	}
}
