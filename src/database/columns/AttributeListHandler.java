package database.columns;

import debug.Logger;


/**
 * Simple Table 
 * - besitzt automatisch und immer eine Primary Key 
 * - besitzt automatisch einen Index (für zum Bsp. config.db) 
 * - bietet Debug informationen, wie lastSQLCommand und lastResultSet an
 * 
 * @author hugo-lucca
 *
 */
public class AttributeListHandler {

	private  AttributeList myAttributes = new AttributeList();

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	public AttributeList getMyAttributes() {
		return myAttributes;
	}

	private boolean isCreated = false;


	/*
	 * To add a single attribute (only possible, if table is not created yet)
	 */
	public void addAttribute(Attribute a) throws Exception {
		if (isCreated) {
			Logger.out("table is created yet, no more attributes may be added", a.getAttributeDefinition());
		} else {
			myAttributes.addUnique(a);
		}
	}

	/**
	 * To add a list of attributes (only possible, if table is not created yet)
	 * 
	 * @param aList
	 * @throws Exception 
	 */
	public void addAttributes(AttributeList aList) throws Exception {
		if (isCreated) {
			Logger.out("table is created yet, no more attributes may be added", aList.getCommaSeparatedList(true));
		} else {
			myAttributes.append(aList);
		}
	}

	/**
	 * To add the primary key
	 * 
	 * @param pkName
	 * @throws Exception 
	 */
	protected void addPrimaryKey(String pkName) throws Exception {
		if (isCreated) {
			Logger.out("table is created yet, no primary key may be added", pkName);
		} else {
			PrimaryKey pk = getMyAttributes().getPrimaryKey();
			if (pk != null) { // found a p-key in the list
				pk.setValue(pkName); // then just rename it
			} else {
				pk = new PrimaryKey(pkName); // create it and 
				myAttributes.addUnique(pk);  // add it to the list
			}
		}
	}

	/**
	 * Constructor
	 */
	public AttributeListHandler() {
		isCreated = false;	
	}

}
