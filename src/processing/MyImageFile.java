package processing;
import java.io.File;

import processing.core.PApplet;
import processing.core.PImage; 
/**
 * Is responsible for saving and importing files
 * @author GiBr03
 * @version 1.0
 * @since 9.4.2018
 */
public class MyImageFile extends Processing{
	
	  public DateAndTime dt;
	  public boolean importImage = false;
	  public boolean exportImage = false;
	  public String savePath;
	  public PImage img;
	  //Uncomment the following line as well as the lines 25,26 in UsingProcessing to create a log
	  //PrintWriter output = parent.Writer("Programm/log_"+dr.myFile.dateNow+"_"+dr.myFile.timeNow+".txt");
	  
	  public MyImageFile(PApplet p){
		  super(p);
		  dt = new DateAndTime(p);
	  }
	  
	  /**
	   * Does output a file
	   * @param selection is the selected file path
	   */
	  public void fileSelected(File selection){ 
		  	//If no file path is selected return
	     if(selection == null){
	       return;
	     }
	     else{
	       savePath = selection.getAbsolutePath();//Gets the path of the file
	       exportImage = true; //Activates the function to save the file in UsingProcessing.draw
	     }
	   }
	  /**
	   * Does import a file
	   * @param selection is the selected file path
	   */
	  public void fileSelectedInput(File selection){
	      if(selection == null){ //If no file is selected return                  
	        return;
	      }
	      else{
	        String savePath = selection.getAbsolutePath(); //Gets the path and loads the image into the PImage img
	        img = parent.loadImage(savePath);
	        img.resize(parent.width,parent.height); //Does resize the image to the size of the window
	        importImage = true; //Activates the function to importing the file in UsingProcessing.draw
	      }
	  } 

	  /**
	   * Writes the text into the log file
	   * @param logText
	   */
	  public void log (String logText) {
		  System.out.println(dt.getTimeNow()+"_"+logText); 
	  }
}
