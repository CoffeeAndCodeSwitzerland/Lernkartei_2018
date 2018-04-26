package GUIDesign;

import g4p_controls.GEvent;

/**
 * This class extends the processing compatible gui-class, 
 * by completing last unimplemented functionality, avoiding doing this
 * in MyGUIChanges. You will find same methods in processing main sketch.
 * 
 * @author WISS
 *
 */
public class MyControl extends MyGUIChanges
{
	public void logEvent (String srcName, String scrClass, String srcText, GEvent event ) {
		println(srcName+"-"+scrClass+" ("+srcText+") >> GEvent: " + event.name() +"." + event + " @ " + millis());
	}

	@Override
	public void customGUI() {
	    NextWindow.setVisible(false);
	    me=this;
	} 

}
