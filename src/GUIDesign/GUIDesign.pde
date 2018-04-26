// Need G4P library
import g4p_controls.*;

public void logEvent (String srcName, String scrClass, String srcText, GEvent event ) {
    println(srcName+"-"+scrClass+" ("+srcText+") >> GEvent: " + event.name() +"." + event + " @ " + millis());
}

// Use this method to add additional statements
// to customise the GUI controls
public void customGUI() {
    NextWindow.setVisible(false);
}

@Override
public void settings() {
    size(480, 320, JAVA2D);
}

@Override
public void setup() {
    createGUI();
    customGUI();
}

@Override
public void draw() {
      background(230);
      // place here other app code
}

@Override
public void mousePressed() {
    // TODO Auto-generated method stub
    
}
