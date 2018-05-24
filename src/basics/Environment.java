package basics;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Purpose:	
 * - to simplify environment variable access (system properties)
 * - you may get file-separator, endOfLine-char, user-path's etc. 
 * 
 * @AUTHOR Hugo Lucca
 */
public abstract class Environment {

	public enum EnvironementParameterName {
		HOME_PATH,
		USER_PATH,
		USER_NAME,
		FILE_SEP,
		CLASS_PATH,
		EOF_LINE,
		ACTUAL_PATH,
		APP_DATA
	};
	
	private static final HashMap<EnvironementParameterName,String> myEnvParameters = new HashMap<>();
	
	/*-    (this squences deactivates the comment formatter!)
	 * 
	 * ============================
	 * How to get System Properties:
	 * ============================
	 * Key 					Meaning
	 * ---					-------
	 * "file.separator"		Character that separates components of a file
	 * 						path. This is "/" on UNIX and "\" on Windows.
	 * "java.class.path"	Path used to find directories and JAR archives
	 * 						containing class files. Elements of the class 
	 * 						path are separated by a platform-specific 
	 * 						character specified in the path.separator property.
	 * "java.home"			Installation directory for the JRE
	 * "java.vendor"		JRE vendor name "java.vendor.url" JRE vendor URL
	 * "java.version"		JRE version number "line.separator" Sequence used
	 * 						by the operating system to separate lines in text
	 * 						files
	 * "os.arch"			Operating system architecture
	 * "os.name"			Operating system name
	 * "os.version"			Operating system version
	 * "path.separator"		Path separator character used in java.class.path
	 * "user.dir"			User working directory
	 * "user.home"			User home directory "user.name" User account name
	 * APP_DATA				Location of %APPDATA%
	 */
	public static void init() {
		myEnvParameters.put(EnvironementParameterName.HOME_PATH,System.getProperty("user.dir"));
		myEnvParameters.put(EnvironementParameterName.USER_PATH,System.getProperty("user.home"));
		myEnvParameters.put(EnvironementParameterName.USER_NAME,System.getenv().get("USERNAME"));  // evtl. nur für Windows
		myEnvParameters.put(EnvironementParameterName.FILE_SEP,System.getProperty("file.separator"));
		myEnvParameters.put(EnvironementParameterName.CLASS_PATH,System.getProperty("java.class.path"));
		myEnvParameters.put(EnvironementParameterName.EOF_LINE,System.getProperty("line.separator"));
		myEnvParameters.put(EnvironementParameterName.ACTUAL_PATH,Paths.get(".").toAbsolutePath().normalize().toString());
		myEnvParameters.put(EnvironementParameterName.APP_DATA,System.getenv("APPDATA"));
	}
	
	public static void debug() {
		for (Map.Entry<EnvironementParameterName,String> entry : myEnvParameters.entrySet()) {
		    EnvironementParameterName key = entry.getKey();
		    String e = entry.getValue();
			System.out.println("EnvParam("+key.toString()+"): "+e);
		}
	}

	// Username geht evtl. nur bei Windows, 
	// ansonsten TODO nimm getPath und extract Username

	public static String getParameter(EnvironementParameterName environementParameterName) {
		if (myEnvParameters.size() == 0) init();
		return myEnvParameters.get(environementParameterName);
	}

}
