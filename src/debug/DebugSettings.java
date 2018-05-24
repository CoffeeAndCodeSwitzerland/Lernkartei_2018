package debug;

import java.time.format.DateTimeFormatter;

/**
 * Contains centralized generic Globals 
 * 
 * @author hugo-lucca
 */
public abstract class DebugSettings
{
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
	// The logger should always remain active, to track user problems.
	// The log-file is stored at the same place of both databases (LK and config)
	public static final boolean LoggerIsOn 	  = true;  // Logger ON/OFF (should remain ON)
	
	// Do any change manually here:
	// - the debugger may be on while software development, but not needed as runnable JAR version: 
	public static final boolean DebuggerIsOn = true; // Debugger ON/OFF (deactivate before release)
	// - to make tests easier: 
	public static final boolean TestingIsOn  = true; // TestConditions ON/OFF (deactivate before release)

}

