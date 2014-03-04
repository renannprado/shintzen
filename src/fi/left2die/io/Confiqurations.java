package fi.left2die.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * The Configuration class is a global class that load game configurations from an ini file to memory.
 * 
 * @author TeeMuki
 */
public class Confiqurations {
	
	/**
	 * Flag indicated that configurations are loaded to memory.
	 */
	private static boolean isLoaded = false;
	
	/**
	 * File path to configuration file
	 */
	private static String configurationfilePath;
	
	/**
	 * Static property
	 */
	private static Properties properties;
	
	//Corresponding keys can be find from configuration file.
	
	public final static String DEAD_ZONE = "deadzone";
	public final static String RECORD_FILE_PATH = "records_path";
	public final static String SOUNDS = "sound_enabled";
	public final static String MUSIC = "music_enabled";
	public final static String GAMECONTROLLERINDEX = "controller_no";	
	public final static String FULLSCREEN = "fullscreen";	
	public final static String SCREEN_WIDTH= "screen_width";
	public final static String SCREEN_HEIGHT = "screen_height";	
	public final static String KEYBOARD_CONF_FILE = "keyboardc_onfiguration_filepath";	
	public final static String SOUND_VOLUME = "sound_volume";
	public final static String MUSIC_VOLUME = "music_volume";	
	public final static String PLAYER_NAME = "player_name";	
	public final static String FPS = "fps";
	
		
	/**
	 * This constructor should be call before using class static methods
	 * 
	 * @param path
	 * @throws IOException Thrown if file is not readable
	 * @throws FileNotFoundException Thrown is file is not found
	 * @throws InvalidPropertiesFormatException Thrown if given file is some how incompatible
	 */
	public Confiqurations( String path ) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {		
		properties = new Properties();
		loadProperties( path );
		configurationfilePath = path;
		isLoaded = true;
	}
	
	public Confiqurations( File file ) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {		
		properties = new Properties();
		properties.load( new FileInputStream(file));		
		configurationfilePath = file.getAbsolutePath();
		isLoaded = true;
	}	
	
	/**
	 * Load configuration files from XML file.
	 * @throws IOException Thrown if file is not readable
	 * @throws FileNotFoundException Thrown is file is not found
	 * @throws InvalidPropertiesFormatException Thrown if given file is some how incompatible
	 */
	public void loadProperties( String path ) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {    						
		FileInputStream stream = new FileInputStream(path);		
		properties.load(stream);
	}
	
	
	
	
	/**
	 * Saves configurations to XML file.
	 * 
	 * @param path Path to configuration file-
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void saveProperies() throws FileNotFoundException, IOException {
		properties.store(new FileOutputStream(configurationfilePath), 
							  "This is left2die game configuration file.");
	}

	public static void setProperty(String key, String value) {
		properties.setProperty( key, String.valueOf(value) );		
	}
	
	/**
	 * Set a property
	 * 
	 * @param key
	 * @param value
	 */
	public static void setProperty(String key, float value) {
		properties.setProperty( key, String.valueOf(value) );		
	}
	
	/**
	 * Returns property
	 * 
	 * @param key
	 * @return Return corresponding value to given key
	 */
	public static String getProperty(String key) {
		
		String value = "";
		
		try {					
			value = properties.getProperty(key).trim();
		} catch( Exception exp ) {
			System.out.println("Can't find property with a key: " + key );
		}
		
		return value;
	}
	
	/**
	 * Returns boolean value from resources
	 * 
	 * @param key
	 * @return True if property written like 'true' or '1' else false is returned.
	 */
	public static boolean getBoolean(String key) {		
		
		String propetry = getProperty(key);
		
		if( propetry.equals("true") || propetry.equals("1") )
			return true;
		else
			return false;
	}
}
