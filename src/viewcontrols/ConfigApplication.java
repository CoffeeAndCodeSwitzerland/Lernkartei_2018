package viewcontrols;

import basics.PAppletInterface;
import processing.core.PApplet;
/**
 * Container for the processing sketch code part:
 *
 * Porting to processing:
 * ======================
 * - copy only the lines marked below to the main sketch
 * - add "import g4p_controls.*;" to the main sketch
 * 
 * @author WISS
 *
 */
public abstract class ConfigApplication extends PApplet implements PAppletInterface 
{
	abstract public void createGUI();
	abstract public void customGUI();

	/**
	 * The following code may be ported 1:1 to processing (copy-past in main Sketch named ...)
	 */
	
	@Override
	public void settings() {
		  size(480, 320, JAVA2D);
	}

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

	/**
	 */
	
} // End of the processing-code container
