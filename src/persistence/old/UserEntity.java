package persistence.old;

import database.Entity;
import database.SQLHandler;
import database.columns.Attribute;
import database.jdbc.DBDriver;
import database.jdbc.DBDriver.ResultType;

public class UserEntity extends Entity {

	private static Integer anzahlLeben;
	private static String currentLifes;

	/**
	 * @param tabName
	 * @throws Exception 
	 */
	public UserEntity(DBDriver dbDriver, String tabName) throws Exception {
		super(dbDriver, tabName, false);
	}

	/**
	 * 
	 * Fragt den Score einer Kartei ab
	 * 
	 * @param Kartei
	 *            --> Welche Kartei, welche abgefragt werden soll
	 * @return --> Returned einen Double Wert des Scores, returned -1, wenn kein
	 *         Score vorhanden
	 * @throws Exception 
	 */
	public void correctCard() throws Exception {
		currentLifes = "0";
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), "ActualScore", null, null));
		// String getCurrent = "SELECT Lifecount FROM Lifes";
		if (myDBDriver.isThereAResult()) {
			currentLifes = myDBDriver.getResultPIntValueOf("ActualScore");
		} else {
			getMyAttributes().getAttributeNamedAs("ActualScore").setValue(0);
			// "INSERT INTO Lifes (Lifecount) VALUES (0)";
			myDBDriver.executeCommand(SQLHandler.insertIntoTableCommand(getMyTableName(), getMyAttributes()));
		}
		getMyAttributes().getAttributeNamedAs("ActualScore").setValue(currentLifes + 1);
		Attribute k = new Attribute("Username", "def");
		// "UPDATE Lifes SET Lifecount = " + (currentLifes+1);
		myDBDriver.executeCommand(SQLHandler.updateInTableCommand(getMyTableName(), getMyAttributes(), k));
	}

	public int getLifecount() throws Exception {
		currentLifes = "0";
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), "ActualScore", null, null));
		// String getCurrent = "SELECT Lifecount FROM Lifes";
		if (myDBDriver.isThereAResult()) {
			currentLifes = myDBDriver.getResultPIntValueOf("ActualScore");
		}
		float notRounded = Integer.parseInt(currentLifes) / 30;
		anzahlLeben = Math.round(notRounded);
		return anzahlLeben;
	}

	public void death() throws Exception {
		currentLifes = "0";
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), "ActualScore", null, null));
		if (myDBDriver.isThereAResult()) {
			currentLifes = myDBDriver.getResultPIntValueOf("ActualScore");
		}
		if (Integer.parseInt(currentLifes) >= 30) {
			getMyAttributes().getAttributeNamedAs("ActualScore").setValue(Integer.parseInt(currentLifes) - 30);
			Attribute k = new Attribute("Username", "def");
			myDBDriver.executeCommand(SQLHandler.updateInTableCommand(getMyTableName(), getMyAttributes(), k));
		}
	}

	public int getCorrectCards() throws Exception {
		currentLifes = "0";
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), "ActualScore", null, null));
		if (myDBDriver.isThereAResult()) {
			currentLifes = myDBDriver.getResultPIntValueOf("ActualScore");
		}
		return Integer.parseInt(currentLifes);
	}

	// Überprüft, ob ein Eintrag bereits vorhanden ist gesteuert mit dem
	// Usernamen. Somit ist der Username einmalig
	public boolean checkDatabase(String attribut, String nameToCheck) throws Exception {
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), attribut, attribut, nameToCheck));
		if (myDBDriver.isThereAResult()) {
			return true;
		}
		return false;
	}

	/**
	 * insertIntoTableCommand(String tabName, AttributeList attributes, String
	 * att1, String val1, String att2, String val2)
	 * 
	 * @param attribute
	 *            Welche Eigenschaft
	 * @param keyName
	 * @param oldValue
	 * @param newValue
	 * @return
	 * @throws Exception 
	 */
	public ResultType setData(String attribute, String newValue) throws Exception {
		return myDBDriver.executeQuery(SQLHandler.insertIntoTableCommand(getMyTableName(), getMyAttributes(), attribute, newValue, null, null));
	}

	public String loadData(String attribute, String username) throws Exception {
		myDBDriver.executeQuery(SQLHandler.selectCommand(getMyTableName(), attribute, attribute, username));
		if (myDBDriver.isThereAResult())
			return myDBDriver.getResultValueOf(attribute);
		return "leer";
	}
}
