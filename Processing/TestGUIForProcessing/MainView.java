import g4p_controls.*; // Need G4P library

/**
 * Singleton class. 
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
  
  public void actOnButton (String name, GPanel myPanel, GTextArea myTextArea) {
    switch(name) {
      case "Send...":
          myPanel.setText("Hallo!"); // Window title
          myTextArea.setText("Dies ist eine Text...");  
      break;
    }
  }
  
}
