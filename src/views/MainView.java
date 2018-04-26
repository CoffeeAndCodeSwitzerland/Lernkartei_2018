package views;

import GUIDesign.MyGUIChanges.Command;
import g4p_controls.*; // Need G4P library

/**
 * Implement Views as singletons. 
 * Functionality for the main window. 
 * Actions may depend on designed values.
 * 
 * Porting to processing:
 * ======================
 * Must be an outer-class in processing, that means name the processing-tab with *.java!
 * 
 * @author WISS
 *
 */
public class MainView {
  
  static MainView myInstance;
  private MainView() {};
  public static MainView getInstance () {
    if (myInstance == null) myInstance = new MainView();
    return myInstance;
  }
  
  public boolean actOnButton (Command cmd, GPanel myPanel, GTextArea myTextArea) {
    switch(cmd) {
      case SEND: // designed button Text
    	  if (myPanel != null)
    		  myPanel.setText("Hallo!"); // Window title
    	  if (myTextArea != null) {
    		  myTextArea.setText("Dies ist ein Text...");
    		  return true;
    	  }
      break;
	  default: break;
    }
    return false;
  }
  
}
