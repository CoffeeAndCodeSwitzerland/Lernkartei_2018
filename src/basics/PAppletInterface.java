package basics;
/**
 * Helps to auto-implement core (processing) basic method calls.
 * This works only, if you implement this interface before extending the new class by PApplet,
 * because PApplet has a default implementation of all methods.
 * 
 * @author WISS
 *
 */
public interface PAppletInterface {

	// some ususlly needed Processing methods:
	void settings();
	void setup();
	void draw();
	void mousePressed();
	void keyPressed();

}