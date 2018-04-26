package views;

import g4p_controls.*; // Need G4P library

/**
 * Singleton class. 
 * 
 * Porting to processing:
 * ======================
 * - must be an outer-class in processing, that means name the processing-tab with *.java!
 * - do not copy the package line 
 * 
 * @author WISS
 *
 */
public class MainView {
	
	/**
	 * Singelton Code:
	 */
	static MainView myInstance;
	private MainView() {};
	public static MainView getInstance () {
		if (myInstance == null) myInstance = new MainView();
		return myInstance;
	}
	
	/**
	 * Special Action Handler for the main view:
	 * @param name
	 * @param myPanel
	 * @param myTextArea
	 */
	public void actOnButton (String name, GPanel myPanel, GTextArea myTextArea) {
	    switch(name) {
	      case "Send...":
		          myPanel.setText("Hallo!"); // Window title
		          myTextArea.setText("Dies ist eine Text...");  
		  break;
	    }
	}
	
}
