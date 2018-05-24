package persistence;

import java.io.File;
import java.time.format.DateTimeFormatter;

import basics.Environment;
import basics.Environment.EnvironementParameterName;

/**
 * Database Globals
 * 
 * @author hugo-lucca
 */
public abstract class DBGlobals
{
	private static final String db_Path = "WISS_Learncards_db";
	public static final String db_name = "Lernkarten";
	public static final String config_db_name = "config";

	public static String username = "";
	public static String lastRegisteredUser = "";
	public static enum loginState  {LOGGEDIN,LOGGEDOUT};
	public static loginState loginStatus = loginState.LOGGEDOUT;
	//Example for change the status in a other class => Globals.loginStatus = loginState.LOGGEDOUT;

	/*--- globals for the MYSQL-Driver*/
	public static String mysqldriver = "com.mysql.jdbc.Driver";
	public static String mysqlpath = "jdbc:mysql://";
	public static String mysqluser = "";
	public static String mysqlpassword = "";
	public static String mysqldb = "";
	public static String user_Table = "";
	/*--- globals for the MYSQL-Driver*/
	
	/*--- globals for the MYSQL-Driver_WISS*/
	public static String mysqldriver_wiss = "com.mysql.jdbc.Driver";
	public static String mysqlpath_wiss = "jdbc:mysql://192.168.2.106/wisslearncards";
	public static String mysqluser_wiss = "wisslearncards";
	public static String mysqlpassword_wiss = "wisslearncards";
	public static String mysqldb_wiss = "wisslearncards";
	public static String user_Table_wiss = "dbuser";
	/*--- globals for the MYSQL-Driver_WISS*/
	
	public static boolean firstOpen = true;
	
	public static final int defaultScrollerWidth = 400;
	
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
	// To simplify structural changes of the data base
	// but the user will lose his cards. 
	// TODO to avoid this own card's exports should be possible (to XML, CSV or other DB)
	// Do any change manually here:
	// TODO is a future expansion
	public static final boolean ForceNewDB    = false; // activate to delete old db (not the config!)
	// ... and this value should be saved in config.db and updated after an old LK-db delete+rebuild
	public static final String ForDBVersionLT = "1.0"; // increase this to delete only the older DB's
	
	
	private static String getDBAbsolutePath () {
		return Environment.getParameter(EnvironementParameterName.HOME_PATH) + Environment.getParameter(EnvironementParameterName.FILE_SEP);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDatabasePath() {
		File theDir = new File(getDBAbsolutePath() + db_Path);
		if (!theDir.exists()) {
			theDir.mkdirs();
		}
		return getDBAbsolutePath() + db_Path + Environment.getParameter(EnvironementParameterName.FILE_SEP);
	}
	
	/**
	 * 
	 * @return
	 */
	/*
	public static String getDatabaseLocation() {
		File theDir = new File(System.getenv("APPDATA") + Environment.getFileSep() + db_Path);
		if (!theDir.exists()) {
			theDir.mkdirs();
		}		
		return System.getenv("APPDATA") + Environment.getFileSep() + db_Path;
	}
	*/
}

