package viewcontrols;

import g4p_controls.*; // Need G4P library
import processing.core.PApplet;
import views.MainView;

/**
 * This class extends the processing compatible gui-class, by adding own functionality
 * aiming simple designing changes, without changing a lot of other things.
 * You may just add new functionality by overloading events and adding constructor features.
 *
 * Porting to processing:
 * ======================
 * - make sure, all from the G4P designed event handlers within the processing gui-tab 
 *   are represented here (else program copy and merge them to the code below)
 * - copy (overwriting) all the event lines blow to the gui-tab
 * 
 * @author WISS
 *
 */
public class MyControl extends gui
{
	/**
	 * The following code may be merged to processing (within second Sketch named gui)
	 */

		public void button1_click1(GButton source, GEvent event) {
		  logEvent("button1",source.getClass().getSimpleName(),source.getText(),event);
		  MainView.getInstance().actOnButton(source.getText(), panel1, textarea1);
		}

		public void panel1_Click1(GPanel source, GEvent event) {
		  logEvent("panel1",source.getClass().toString(),source.getText(),event);
		}

		public void textarea1_change1(GTextArea source, GEvent event) {
		  logEvent("textarea1",source.getClass().toString(),source.getText(),event);
		}

		public void button2_click1(GButton source, GEvent event) {
		  logEvent("button2",source.getClass().toString(),source.getText(),event);
		  NextWindow.setVisible(true); // Navigate to View 2
		}

		synchronized public void win_draw2(PApplet appc, GWinData data) {
		  appc.background(230);
		}

		public void textarea2_change1(GTextArea source, GEvent event) {
			logEvent("textarea2",source.getClass().toString(),source.getText(),event);
		}

		public void button3_click1(GButton source, GEvent event) {
			logEvent("button3",source.getClass().toString(),source.getText(),event);
		} 

		/**
		 * The following code may be merged to processing (first Sketch named ...)
		 */
		// Use this method to add additional statements
		// to customise the GUI controls
		public void customGUI(){
			NextWindow.setVisible(false);
		}

		public void logEvent (String srcName, String scrClass, String srcText, GEvent event ) {
			println(srcName+"-"+scrClass+" ("+srcText+") >> GEvent: " + event.name() +"." + event + " @ " + millis());
		}

}
