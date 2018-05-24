package database.columns;

import java.util.ArrayList;

import debug.Logger;


/**
 * To handle the column list of a table with the content of one line for all
 * purposes.
 * 
 * The restrictions are: - the first tree columns are reserved for own purposes
 * (index an Primary key at third position) - the primary key's are always
 * integer - the primary key must be auto incremented - only one primary key, is
 * supported!
 * 
 * @author WISS
 *
 */
public class AttributeList {

	final private ArrayList<Attribute> myAttributes = new ArrayList<>();
	private int numberOfAttributes = -1; // -1 : not actual, >=0 actual
											// (replaces myAttributes.size() for
											// more performance)
	private int countPrimary = 0; //
	private PrimaryKey myPrimary = null;


	public ArrayList<Attribute> getMyAttributes() {
		return myAttributes;
	}

	public void debug () {
		for (Attribute a : myAttributes) {
			System.out.println("A:"+a.getName()+"/"+a.isPrimary());
		}
	}
	
	public int getCountPrimary() { // for debug only
		return countPrimary;
	}

	private void resetCounters() {
		numberOfAttributes = -1;
		countPrimary = 0;
		myPrimary = null;
	}

	public AttributeList() {
		resetCounters();
	}

	/**
	 * To handle the size of the list more efficiently
	 * 
	 * @return
	 */
	public int getSize() {
		if (numberOfAttributes < 0) {
			numberOfAttributes = myAttributes.size();
		}
		return numberOfAttributes;
	}

	/**
	 * To faster handle the primary and key counter
	 * 
	 * @return
	 * @throws Exception 
	 */
	public void incremetPrimaryCount(Attribute a) throws Exception {
		if (a.isPrimary()) {
			countPrimary++;
			if (countPrimary > 1) {
				throw new Exception("more than one primary key "+a.getName()+" is not supported yet");
			}
			if (myPrimary == null) {
				myPrimary = (PrimaryKey) a;
				System.out.println(">>set primary:"+a.getName());
			}
		}
	}

	/**
	 * To get my primary key (fast version)
	 * 
	 * @return primary key or null if no found
	 * @throws Exception 
	 */
	public PrimaryKey getPrimaryKey() throws Exception {
		if (myPrimary == null) {
			int size = getSize();
			if (size >= 1) {
				for (int i = 0; i < size; i++) {
					if (myAttributes.get(i) != null) {
						Attribute a = myAttributes.get(i);
						if (a.isPrimary()) {
							Logger.out("getting a pkey...");
							myPrimary = (PrimaryKey) a;
							return myPrimary;
						}
					}
				}
				if (size > 2) {
					throw new Exception("pimary key not found in list of "+size+" attributes / pcount:"+countPrimary);
				}
			} else {
				//throw new Exception("list with "+size+" attributes -> pimary key not found / pcount:"+countPrimary);
				return null;
			}
		}
		return myPrimary;
	}

	/**
	 * To add a new attribute to the list (column names are always unique!)
	 * 
	 * @param a
	 *            : the attribute to add
	 * @throws Exception 
	 */
	public void addUnique(Attribute a) throws Exception {
		if (a != null) {
			incremetPrimaryCount(a);
			if (getSize() == 0) {
				numberOfAttributes = -1;
				myAttributes.add(a);
			} else {
				// check if unique
				String newName = a.getName();
				boolean found = false;
				for (int i = 0; i < getSize(); i++) {
					if (myAttributes.get(i).getName().equals(newName)) {
						found = true;
						break;
					}
				}
				if (!found) {
					numberOfAttributes = -1;
					myAttributes.add(a);
				}
			}
		} else {
			throw new Exception("tried to add an invalid null attribute");
		}
	}

	/**
	 * To append a list of new attributes
	 * 
	 * @param newAttributeList
	 * @throws Exception 
	 */
	public void append(AttributeList newAttributeList) throws Exception {
		for (int i = 0; i < newAttributeList.getSize(); i++) {
			addUnique(newAttributeList.get(i));
		}
	}

	/**
	 * To set all values of the columns following the primary key at third
	 * position The new values must be in ordinal way compatible to the
	 * instantiated order of that attributes.
	 * 
	 * @param values:
	 *            the value list as strings, the data types will be converted
	 *            according to the instantiated
	 */
	public void setValuesAfterPrimaryKey(String[] values) {
		for (int i = 1; i < values.length; i++) {
			myAttributes.get(i + 3).setValue(values[i]);
		}
	}

	/**
	 * To get a specific attribute with the ordinal way of an index
	 * 
	 * @param i
	 *            the index (starts with 0)
	 * @return the attribute at i or null
	 */
	private Attribute get(int i) {
		if (i < getSize())
			return myAttributes.get(i);
		return null;
	}

	/**
	 * To a comma separated list of the attributes for INSERT etc.
	 * 
	 * @param addPrimary
	 * @return the CS list or ""
	 * @throws Exception 
	 */
	public String getCommaSeparatedList(boolean addPrimary) throws Exception {
		int size = getSize();
		int commas = 0;
		if (size >= 1) {
			String attributeList = "";
			for (int i = 0; i < size; i++) {
				Attribute a = myAttributes.get(i);
				int o;
				switch (a.getType()) {
				case INT:
					o = AttributeInterface.Datatype.INT.ordinal();
					break;
				case FKEY:
					o = AttributeInterface.Datatype.FKEY.ordinal();
					break;
				case PKEY:
					o = AttributeInterface.Datatype.PKEY.ordinal();
					break;
				case TEXT:
				default:
					o = AttributeInterface.Datatype.TEXT.ordinal();
					break;
				}
				if (!(a.isPrimary() && addPrimary == false)) {
					attributeList += a.getName() + " " + Attribute.SQLDataTypes[o];
					if (commas < size - 1 - ((addPrimary == true) ? 0 : countPrimary)) {
						attributeList += ",";
						commas++;
					}
				}
			}
			return attributeList;
		} else {
			throw new Exception("invalid data to build a list of attributes!");
		}
	}

	/**
	 * To build a comma separated key list: for "SELECT " + keyList + ...
	 * 
	 * @return the comma separates list or "*"
	 * @throws Exception 
	 */
	public String getCSResultAttributeList() throws Exception {
		int size = getSize();
		if (size >= 1) {
			String attributeList = "";
			for (int i = 0; i < size; i++) {
				if (myAttributes.get(i) != null) {
					Attribute a = myAttributes.get(i);
					if (a.isKey()) {
						attributeList += a.getName();
						int o;
						switch (a.getType()) {
						case INT:
							o = AttributeInterface.Datatype.INT.ordinal();
							break;
						case FKEY:
							o = AttributeInterface.Datatype.FKEY.ordinal();
							break;
						case PKEY:
							o = AttributeInterface.Datatype.PKEY.ordinal();
							break;
						case TEXT:
						default:
							o = AttributeInterface.Datatype.TEXT.ordinal();
							break;
						}
						attributeList += Attribute.SQLDataTypes[o];
						if (i < size - 1) {
							attributeList += ",";
						}
					}
				}
			}
			if (!attributeList.equals(""))
				return attributeList;
		} else {
			throw new Exception("invalid data to build a CS list of seeked attributes");
		}
		return "*";
	}

	/**
	 * To seek an attribute name (sql column name)
	 * 
	 * @param seekName
	 * @return the name or null if not found
	 * @throws Exception 
	 */
	public Attribute getAttributeNamedAs(String seekName) throws Exception {
		int size = getSize();
		if (size >= 1 && seekName != null) {
			String seekS = seekName.toLowerCase();
			for (int i = 0; i < size; i++) {
				if (myAttributes.get(i) != null) {
					Attribute a = myAttributes.get(i);
					// debug.Debugger.out("ATT("+a.getName().toLowerCase()+") ?=
					// '"+seekName.toLowerCase()+"'");
					if (a.getName().toLowerCase().equals(seekS)) {
						return a;
					}
				}
			}
			throw new Exception("key not found in list of (" + size + ")!" +seekName);
		} else {
			throw new Exception("list is empty, key not found!"+seekName);
		}
	}

	/**
	 * To build a comma separated list of keys without data types
	 * 
	 * @param addPK:
	 *            if true the primary key id added else omitted (if found)
	 * @return the CS list or ""
	 * @throws Exception 
	 */
	public String toKeyList(boolean addPK) throws Exception {
		int size = getSize();
		if (size >= 1) {
			String keyList = "";
			int commas = 0;
			for (int i = 0; i < size; i++) {
				if (myAttributes.get(i) != null) {
					Attribute a = myAttributes.get(i);
					if (a != null) {
						if (!(addPK == false && a.isPrimary())) {
							keyList += a.getName();
							if (commas < size - 1 - ((addPK == true) ? 0 : countPrimary)) {
								keyList += ", ";
								commas++;
							}
							// debug.Debugger.out("KLIST:",a.getName()+"
							// P:"+a.isPrimary());
						}
					}
				}
			}
			return keyList;
		} else {
			throw new Exception("invalid data to build a list of keys");
		}
	}

	/**
	 * To build a list of values from a data array (with mixed String and
	 * integer)
	 * 
	 * @return list of values or "" for problems
	 * @throws Exception 
	 */
	public String toValueList(boolean addPrimary) throws Exception {
		int size = getSize();
		if (size >= 1) {
			String valueList = "";
			int commas = 0;
			for (int i = 0; i < size; i++) {
				Attribute a = myAttributes.get(i);
				if (a != null) {
					if (!(addPrimary == false && a.isPrimary())) {
						String value = a.getValue();
						if (a.isValue() == false) {
							if (value == null)
								value = "";
							valueList += "'" + value + "'";
						} else {
							if (value == null)
								value = "0";
							valueList += value;
						}
						if (commas < (size - 1 - ((addPrimary == true) ? 0 : countPrimary))) {
							// debug.Debugger.out("VAL:"+value+":"+i+"/"+size+"/"+addPrimary+"/V:"+a.isValue());
							valueList += ",";
							commas++;
						}
						// debug.Debugger.out("("+i+")");
					}
				}
			}
			return valueList;
		} else {
			throw new Exception("invalid data to build a list of values");
		}
	}

	/**
	 * To build a clause sequence from data
	 * 
	 * @param sep:
	 *            the separator (must be AND or OR)
	 * @return the clause or ""
	 * @throws Exception 
	 */
	public String toClause(String sep) throws Exception {
		int size = getSize();
		if (size >= 1 && sep != null) {
			String separator = sep.toUpperCase();
			if (separator.equals("AND") || separator.equals("OR") || separator.equals(",")) {
				String clause = "";
				for (int i = 0; i < size; i++) {
					if (myAttributes.get(i) != null) {
						Attribute a = myAttributes.get(i);
						String value = a.getValue();
						String name = a.getName();
						if (i > 0) {
							clause += separator + " ";
						}
						if (a.isValue() == false) {
							if (value == null)
								value = "";
							clause += name + " = '" + value + "' ";
						} else {
							if (value == null)
								value = "0";
							clause += name + " = " + value + " ";
						}
					}
				}
				return clause;
			} else {
				throw new Exception("invalid separator "+separator+" to build a clause");
			}
		} else {
			throw new Exception("invalid data to build a clause");
		}
	}

	/**
	 * To build a default list of value-type-flags
	 * 
	 * @param attributes
	 * @param defaultValue
	 * @return the flag-list or null for errors
	 */
	// protected boolean[] toIsValues(String[] attributes, boolean defaultValue)
	// {
	// if (attributes != null & attributes.length >= 1) {
	// boolean[] isValues = new boolean[attributes.length];
	// for (int i = 0; i < isValues.length; i++) {
	// isValues[i] = defaultValue;
	// }
	// return isValues;
	// }
	// Logger.out("invalid data to build a flag list!");
	// return null;
	// }
	/**
	 * 
	 * @return
	 */
	// private String getKeyName() {
	// int size = myAttributes.size();
	// if (size >= 1) {
	// for (int i = 0; i < size; i++) {
	// if (myAttributes.get(i) != null) {
	// Attribute a = myAttributes.get(i);
	// if (a.isKey()) {
	// return a.getName();
	// }
	// }
	// }
	// } else {
	// Logger.out("key name not found!");
	// }
	// return "";
	// }

}
