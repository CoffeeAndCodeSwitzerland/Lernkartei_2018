package database.jdbc;

import persistence.DBGlobals;


/**
 * @author WISS
 *
 */
final public class SQLiteDriver extends DBDriver {

	private String urlBase = "jdbc:sqlite:" + DBGlobals.getDatabasePath();

	public SQLiteDriver(String dbName) {
		super("org.sqlite.JDBC");
		setDbURL(urlBase + dbName);
	}

	/**
	 * To execute a SQL query (like SELECT)
	 * 
	 * @param query
	 * @return the result set or null for errors
	 * @throws Exception 
	 */
	public ResultType executeQuery(String query) throws Exception {
		if (query != null) {
			if (query.startsWith("SELECT")) {
				return doQuery(query);
			} else {
				throw new Exception ("invalid SQL query {null}!");
			}
		} else {
			throw new Exception ("query is {null}!");
		}
	}

	/**
	 * To execute a SQL command (except a query like SELECT)
	 * 
	 * @param SQLcommand
	 * @return 0, 1,2 or -1 on error
	 * @throws Exception 
	 */
	public int executeCommand(String SQLcommand) throws Exception {
		if (SQLcommand != null) {
			if (!SQLcommand.startsWith("SELECT")) {
				return doCommand(SQLcommand);  // for CREATE, UPDATE, DELETE, INSERT
			} else {
				throw new Exception ("Invalid SQL command to execute "+SQLcommand);
			}
		} else {
			throw new Exception ("Command is null");
		}
	}
}
