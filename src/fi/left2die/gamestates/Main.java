package fi.left2die.gamestates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import fi.left2die.io.Confiqurations;

/**
 * Main class contains entry point to program.
 * 
 * @author TeeMuki
 */
public class Main extends StateBasedGame {
	
	public static long counter = 0;
	
	/**
	 * Loading state identification number
	 */
	public final static int LOADING_STATE = 0;
	
	/**
	 * Main menu state identification number
	 */
	public final static int MAINMENU_STATE = 1;
	
	/**
	 * Playing state identification number
	 */
    public final static int PLAYING_STATE = 2;
    
    /**
	 * High score state identification number
	 */
    public final static int HIGHSCORE_STATE = 3;
    
    /**
	 * About state identification number
	 */
    public final static int ABOUT_STATE = 4;
    
    /**
	 * Option state identification number
	 */
    public final static int OPTION_STATE = 5;
	
    /**
     * Main class constructor
     * 
     * @param title Titlebar text
     * @throws SlickException
     */
	public Main(String title) throws SlickException {
		super(title);
				
		this.addState(new LoadingState(LOADING_STATE));
		//highscorestate is required in playingstate constructor		
		HighscoreState highscoreState = new HighscoreState(HIGHSCORE_STATE);
		PlayingState playingState = new PlayingState(PLAYING_STATE, highscoreState );
		//adds different states together
		this.addState(playingState);
		this.addState(highscoreState);
		this.addState(new MainMenuState(MAINMENU_STATE));
		this.addState(new OptionState(OPTION_STATE) );
		this.addState(new AboutState(ABOUT_STATE));
        this.enterState( LOADING_STATE );
	}
	
	/**
	 * Initialies game states
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
	}
	
	/**
	 * Load xml configuration file.
	 * @param args
	 */
	private static void loadConfigurations( String args[] ) {
		
		File configfile = null;

		//Look for configuration file from arguments
		if( args.length > 0 )
			configfile = new File( args[0] );
		else
			configfile = new File("src/resources/config.ini");
				
		//Load configuration file
		try {
			new Confiqurations(configfile);
		} catch (InvalidPropertiesFormatException e1) {
			Log.error("Configuration file is somehow corrupted, using now defaut values.");
		} catch (FileNotFoundException e1) {
			Log.error("Configuration file was not found, using now defaut values.");
		} catch (IOException e1) {
			Log.error("Configuration file was not found or it is unaccesable, using now defaut values.");
		}		
		
		//Shut down verbose loggin
		Log.setVerbose(false);
	}
	
	/**
	 * Start game and set resolution, fps and fullscreen mode.
	 * @throws SlickException
	 */
	private static void startGame() throws SlickException {
				
		 //Default values are used if there is error in configuration file.
		 int width = 1024;
		 int height = 768;
		 boolean fullscreen = false;
		 int fps = 100;
		 boolean sounds = true;
		 boolean music = true;
		 
		 try {//hopefully users can configure the ini files properly
			 width = new Integer(Confiqurations.getProperty( Confiqurations.SCREEN_WIDTH ));
			 height = new Integer(Confiqurations.getProperty( Confiqurations.SCREEN_HEIGHT ));		 
			 fullscreen = Confiqurations.getBoolean( Confiqurations.FULLSCREEN );
			 fps = new Integer(Confiqurations.getProperty( Confiqurations.FPS));
			 sounds = Confiqurations.getBoolean(Confiqurations.SOUNDS);
			 music = Confiqurations.getBoolean(Confiqurations.MUSIC);
		 } catch( NumberFormatException e ) {
			 e.printStackTrace();
		 }
		
		 AppGameContainer app = new AppGameContainer(new Main("Left2Die"));
		 app.setDisplayMode( width,  height, fullscreen );
		 app.setTargetFrameRate(fps);
		 app.setShowFPS(fullscreen);//shows fps only if played in fullscreen
		 app.setSoundOn(sounds);
		 app.setMusicOn(music);
		 app.start();//finally starts the game :)
	}
	
		
	/**
	 * Game entry point
	 * 
	 * @param args Configuration file path can be given as an argument.
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException
	{
		loadConfigurations(args);
		startGame();	
	}
}
