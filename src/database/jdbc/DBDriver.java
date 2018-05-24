package database.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import debug.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class DB Driver
 * 
 * @author hugo-lucca
 */
public abstract class DBDriver {

	public enum ResultType {
		DONE, NOTDONE, WARNING, FATAL_ERROR, NO_CONNECTION_ERROR, WRONG_PRARMETER_ERROR
	}

	private String sqlDriver = null;
	private Connection connection;
	private Statement stmt;
	private @Getter @Setter String dbURL = null;
	private @Getter @Setter ResultSet lastResultSet;
	private @Getter @Setter String lastSQLCommand;

	/**
	 * Protected Constructor
	 * @param newSqlDriver
	 */
	protected DBDriver(String newSqlDriver) {
		sqlDriver = newSqlDriver;
		connection = null;
		stmt = null;
	}

	/**
	 * For the automatic close of the ResultSet
	 */
	private void closeLastResultSet() {
		try {
			if (lastResultSet != null)
				lastResultSet.close();
		} catch (Exception e) {
		}
		lastResultSet = null;
	}

	public void closeDB() {
		// main.debug.Debugger.out("close connection...");
		try {
			if (stmt != null)
				stmt.close();
			if (connection != null) {
				connection.setAutoCommit(true);
				connection.close();
			}
		} catch (Exception e) {
		}
		;
		stmt = null;
		connection = null;
	}

	/**
	 * To execute a SQL query (like SELECT)
	 * 
	 * @param query
	 * @return the result set or null for errors
	 * @throws Exception 
	 */
	protected ResultType doQuery(String query) throws Exception {
		if (stmt == null) {
			Logger.out("connection was closed before query " + query);
			setConnection();
		}
		try {
			closeLastResultSet();
			setLastSQLCommand(query);
			connection.setAutoCommit(false);
			lastResultSet = stmt.executeQuery(query); // returns a RestultSet,
														// is never null
			connection.setAutoCommit(true);
			return ResultType.DONE;
		} catch (Exception e) {
			throw new Exception (e.getMessage()+"/"+query);
		}
	}

	/**
	 * To execute a SQL command for Create, Insert, Update, Delete
	 * but except a query like SELECT (Read)
	 * 
	 * @param SQLcommand
	 * @return 0    nothing done
	 * 		   n>0  row count (number of operations)
	 *         -1 	on error
	 * @throws Exception 
	 */
	protected int doCommand(String SQLcommand) throws Exception {
		if (stmt == null) {
			Logger.out("connection was closed before command " + SQLcommand);
			setConnection();
		}
		try {
			setLastSQLCommand(SQLcommand);
			String cmd = getLastSQLCommand(); 
			debug.Debugger.out("CMD: {"+cmd+"}");
			// Returns: either (1) the row count for SQL Data Manipulation Language (DML) statements or 
			//                 (2) 0 for SQL statements that return nothing
			return stmt.executeUpdate(SQLcommand);  // for CREATE,
													// UPDATE, DELETE,
													// INSERT
		} catch (SQLTimeoutException e) {
			throw new Exception ("SQL CMD Timeout Exception: " + e.getCause()+SQLcommand);
		} catch (SQLException e2) {
			throw new Exception ("SQL CMD Exception: " + e2.getMessage() + "/" + e2.getCause()+SQLcommand);
		} catch (Exception e3) {
			throw new Exception ("Exception: " + e3.getMessage() + "/" + e3.getCause()+SQLcommand);
		}
	}

	/**
	 * To check if there is a valid result set
	 * 
	 * @return true if ok, false if empty or on errors
	 * @throws Exception 
	 */
	public boolean isThereAResult() throws Exception {
		try {
			if (lastResultSet != null) {
				return lastResultSet.next();
			}
			throw new Exception ("did not have any resultset ready");
		} catch (SQLException e) {
		}
		return false;
	}

	/**
	 * special SETTERs and GETTERs
	 * @throws Exception 
	 */
	public void setConnection(Connection connection) throws Exception {
		this.connection = connection;
		try {
			if (this.connection != null) {
				stmt = this.connection.createStatement();
			} else {
				stmt = null;
				throw new Exception ("connection is null (no Driver found... add JDBC-JAR)!");
			}
		} catch (Exception e) {
			throw new Exception ("connection does not work: "+e.getMessage());
		}
	}

	/**
	 * To open a SQL DB connection
	 * 
	 * @return 1, 0 or -1 on error
	 * @throws Exception 
	 */
	public ResultType setConnection() throws Exception {
		if (getDbURL() != null) {
			try {
				if (stmt == null || connection == null) {
					//System.out.println("1.set connection..."+sqlDriver);
					Class.forName(sqlDriver);
					//System.out.println("2.set connection..."+dbURL);
					this.setConnection(DriverManager.getConnection(this.getDbURL()));
					return ResultType.DONE;
				}
				return ResultType.NOTDONE;
			} catch (Exception e) {
				closeDB();
				throw new Exception ("connection error (JDBC?)->Build-Path with JAR-Driver?" + e.getCause());
			}
		}
		throw new Exception ("connection name is null");
	}

	/*
	public void setLastSQLCommand(String newLastSQLCommand) {
		lastSQLCommand = newLastSQLCommand;
		//return lastSQLCommand;
	}
	 */
	
	/**
	 * To get a value of specific attribute of the SQL result set
	 * 
	 * @param attr
	 * @return the value or null if not found or on errors
	 * @throws Exception 
	 */
	public String getResultValueOf(String attr) throws Exception {
		if (attr == null) {
			throw new Exception ("may not seek a {null} attribute in the resultset");
		}
		try {
			if (lastResultSet != null) {
				return lastResultSet.getString(attr);
			}
			throw new Exception ("did not have any resultset ready to get an attribute "+attr);
		} catch (SQLException e) {
			throw new Exception ("did not found the attribute in the resulset:"+e.getMessage()+attr);
		}
	}

	/**
	 * To get a positive integer value of specific attribute of the SQL result
	 * set
	 * 
	 * @param attr
	 * @return the integer value >= 0 or -1, -2 on errors
	 * @throws Exception 
	 */
	public String getResultPIntValueOf(String attr) throws Exception {
		if (attr == null) {
			throw new Exception ("may not seek a {null} attribute in the resultset");
		}
		try {
			if (lastResultSet != null) {
				// Returns: the column value; 
				//			if the value is SQL NULL, the value returned is 0
				System.out.println("Result("+attr+"):"+lastResultSet.getInt(attr)+"/"+lastResultSet.getNString(attr));
				if (lastResultSet.getInt(attr) > 0) {
					if (lastResultSet.getNString(attr) == null) 
						return Integer.toString(lastResultSet.getInt(attr));
					return lastResultSet.getNString(attr);
				}
				return "0";
			}
			throw new Exception ("did not have any resultset ready to get the integer value of the attribute "+attr);
		} catch (SQLException e) {
			throw new Exception ("did not found the integer value of the attribute in the resulset "+attr);
		}
	}

	/**
	 * To get the first attribute value of the SQL result set
	 * 
	 * @return the value or null if not found or on errors
	 * @throws Exception 
	 */
	public String getFirstResultValue() throws Exception {
		try {
			if (lastResultSet != null) {
				return lastResultSet.getString(1);
			}
			throw new Exception ("did not have any resultset ready to get the first attribute");
		} catch (SQLException e) {
			throw new Exception ("did not found the first attribute in the resulset "+e.getMessage());
		}
	}

	/**
	 * ABSTRACT methods
	 * @throws Exception 
	 */
	public abstract int executeCommand(String SQLcommand) throws Exception;
	public abstract ResultType executeQuery(String query) throws Exception;
}
