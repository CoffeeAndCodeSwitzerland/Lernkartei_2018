// Need G4P library
import g4p_controls.*;


  // Use this method to add additional statements
  // to customise the GUI controls
  public void customGUI(){
      NextWindow.setVisible(false);
  }

  public void logEvent (String srcName, String srcText, GEvent event ) {
      println(srcName+" ("+srcText+") >> GEvent: " + event.name() +"." + event + " @ " + millis());
  }
  
  @Override
  public void settings() {
      size(480, 320, JAVA2D);
  }

 // abstract public void createGUI();

  @Override
  public void setup() {
    
      createGUI();
      customGUI();
      // Place your setup code here
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
