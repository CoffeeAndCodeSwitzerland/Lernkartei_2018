# =================== #
#	  Design Package   #
# =================== #
>	@AUTHOR hugo-lucca
	@DATE	26. April 2018

##	Content
##	-------
>	- to use processings G4P GUI buildner 
>	- processing files, G4P files and DATA
	
##	Dependencies (for Eclipse only)
##	------------
>	- to ConfigApplication in controls (who gives the basic functions for MyControl)
>	- to StartApplication in controls (who starts MyControl)
>	- to classes in views-package, where view functionality is implemented 
      (called by the modified event handlers in MyGUIChanges)

## Classes
## -------
> GUIDesign.pde			Start-sketch for processing (afterwards enable G4P tool if necessary) 
> gui.pde				Auto generated sketch file from G4P (do not perform any change in this file)
> PCGUIClass.java		Copy manually and within the processing IDE all lines from gui.pde into this class 
						(see also comments)
> MyGUIChanges.java		Modify the event handlers having the same name in PCGUIClass.java only here
						(under eclipse)
> MyControl.java		The PApplet Start Class who adds also some basic and init function 

### ======================================================================================================= ###
