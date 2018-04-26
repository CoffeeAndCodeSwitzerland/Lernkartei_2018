package GUIDesign;

// Need G4P library
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GPanel;
import g4p_controls.GTextArea;
import views.MainView;

/**
 * This class extends the processing compatible gui-class, by adding own functionality
 * aiming simple designing changes, without changing a lot of other things.
 * You may just add new functionality by overloading events and adding constructor features.
 * Implement here calls to views and view components.
 *
 * Future porting to a processing version:
 * ======================================
 * - copy (overwriting/merging) the event lines blow to processing gui-tab
 * 
 * @author WISS
 *
 */
public abstract class MyGUIChanges extends PCGUIClass
{
	public enum Command
	{
		SEND,
		DO_IT,
		DONT_DO
	}
	
		public void button1_click1(GButton source, GEvent event) {
		  logEvent("button1",source.getClass().getSimpleName(),source.getText(),event);
		  MainView.getInstance().actOnButton(Command.SEND, panel1, textarea1);
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

		public void textarea2_change1(GTextArea source, GEvent event) {
			logEvent("textarea2",source.getClass().toString(),source.getText(),event);
		}

		public void button3_click1(GButton source, GEvent event) {
			logEvent("button3",source.getClass().toString(),source.getText(),event);
		}

		public void button4_click1(GButton source, GEvent event) { //_CODE_:button4:206151:
			logEvent("button4",source.getClass().toString(),source.getText(),event);
		} //_CODE_:button4:206151:

		public void button5_click1(GButton source, GEvent event) { //_CODE_:button5:222117:
			logEvent("button5",source.getClass().toString(),source.getText(),event);
		} //_CODE_:button5:222117:

}
