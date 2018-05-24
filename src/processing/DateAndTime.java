package processing;

import processing.core.PApplet;
/**
 * Gets the time and date and saves it as a string
 * @version 1.0
 * @author GiBr03
 * @since 18.4.2018
 */
public class DateAndTime extends Processing{
	
	private String timeNow;
	private String dateNow;
	
	public DateAndTime(PApplet p) {
		super(p);
	}	
	  /**
	   * Gets the time and saves it as string
	   */
	  public void timeNowFunction() {
	    int h = PApplet.hour();                        
	    int m = PApplet.minute();
	    int s = PApplet.second();
	    
	    timeNow = h+""+m+""+s;
	  }  
	  /**
	   * Gets the date and saves it as string
	   */
	  public void dateNowFunction(){
	    int y = PApplet.year();
	    int m = PApplet.month();
	    int d = PApplet.day();
	    
	    dateNow = d+""+m+""+y;
	  }
	  
	 /**
	  * Getter and setter 
	  */
	public String getTimeNow() {
		return timeNow;
	}
	public void setTimeNow(String timeNow) {
		this.timeNow = timeNow;
	}
	public String getDateNow() {
		return dateNow;
	}
	public void setDateNow(String dateNow) {
		this.dateNow = dateNow;
	}
}
