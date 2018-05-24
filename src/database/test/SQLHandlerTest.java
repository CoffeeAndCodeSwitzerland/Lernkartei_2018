package database.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Test;

import database.Entity;
import database.jdbc.DBDriver;
import database.jdbc.SQLiteDriver;

/**
 * @author WISS
 *
 */
public class SQLHandlerTest {
	
	public static void myTest() throws Exception {
		debug.Debugger.out("Test SQL Handler...");

		DBDriver myTestDB = new SQLiteDriver("test.db"); 

		Entity e = new Entity(myTestDB,"TESTTAB",false);
		e.createTableIfNotExists();
		// @TODO teste ob DB Datei existiert
		

	}

	@Test
	public void test() {
		try {
			myTest();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception found...");
		}
	}

}
