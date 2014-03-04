package fi.left2die.gamestates;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


import fi.left2die.io.Confiqurations;
import fi.left2die.io.Resources;
import fi.left2die.io.ScoreRecorder;
import fi.left2die.model.Record;
import fi.left2die.ui.Button;
import fi.left2die.ui.Button.ButtonEvent;
import fi.left2die.ui.Button.ButtonListener;
import fi.left2die.ui.inputpanel.InputPanel;
import fi.left2die.ui.inputpanel.InputPanelEvent;
import fi.left2die.ui.inputpanel.InputPanelListener;

/**
 * HighscoreState is a game state where player can look old top scores.
 * 
 * @author TeeMuki
 */
public class HighscoreState extends BasicGameState implements InputPanelListener, ButtonListener {

	/**
	 * State identification number
	 */
	private int gameStateID;
	
	/**
	 * State where player is inserting new record
	 */
	private static final int NEW_RECORD = 1; 
	
	/**
	 * State where gamestate is showing records
	 */
	private static final int SHOW_RECORS = 2;
	
	/**
	 * Current state
	 */
	private int state;
	
	/**
	 * Unicode font
	 */
	private UnicodeFont font;
	
	/**
	 * Path where records are saved
	 */
	private String recordFilePath;
	
	/**
	 * List of top records
	 */
	private List<Record>records = new ArrayList<Record>();
	
	/**
	 * New record
	 */
	private Record record;
	
	/**
	 * Game state background
	 */
	private Image background;
	
	/**
	 * Indicates is inputpanel enabled
	 */
	private boolean isInputPanelEnable = true;
	
	/**
	 * Input panel is used to input player name
	 */
	private InputPanel inputPanel;
	
	/**
	 * When button is pressed game return to main menu
	 */
	private Button backMainMenuButton;
	
	/**
	 * This indicates that back button is pressed and game return to back to maim menu.
	 * For keyboard and joystick support.
	 */
	private boolean enterBackMainMenu = false;
	
	public HighscoreState( int gameStateID ) {
		this.gameStateID = gameStateID;
	}
	
	
	@Override
	public int getID() {
		return gameStateID;
	}	
	
	
	@Override
	public void init(GameContainer cont, StateBasedGame state )
			throws SlickException {
		
		font = (UnicodeFont)Resources.getFont("sketch font");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
		
		this.background = Resources.getImage("hi background");
				
		//Load file path from a configuration manager
		recordFilePath = Confiqurations.getProperty( Confiqurations.RECORD_FILE_PATH );
		
		this.state = SHOW_RECORS;
				
		inputPanel = new InputPanel( new Vector2f(20,20));
		inputPanel.addInputPanelListener(this);
		
		
			
		String name = Confiqurations.getProperty(Confiqurations.PLAYER_NAME);
			
		if( name != null ) {
			inputPanel.setText(name);
		}
		
		
		Image buttonBg = Resources.getImage("hi button");
		
		Rectangle rec = new Rectangle( 10 , cont.getHeight() - 60, 270, 50);
		
		backMainMenuButton = new Button("backMainMenu", "Main Menu", buttonBg, rec );
		backMainMenuButton.addListener(this);

		loadRecords();
	}
	
	
	/**
	 * Initialize a new record
	 * 	
	 * @param score Player scores
	 * @param survivingTime Player surviving time
	 */
	public void newRecord( long score, float survivingTime ) {
		record = new Record(null, score, survivingTime);
		state = NEW_RECORD;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		enterBackMainMenu = false;
		container.getInput().addListener(backMainMenuButton);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
	
		super.leave(container, game);
		
		container.getInput().removeListener(backMainMenuButton);
	}
	
	/**
	 * Load records to memory
	 */
	private void loadRecords() {
		ScoreRecorder recorder = new ScoreRecorder(recordFilePath);
		
		try {
			this.records = recorder.loadScores();
		} catch( FileNotFoundException ex ) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if( records == null ) {
			records = new ArrayList<Record>();
		}
	}
		
	@Override
	public void render(GameContainer cont, StateBasedGame state, Graphics g)
			throws SlickException {
				
		g.setFont(font);
			
		background.draw(0, 0, cont.getWidth(), cont.getHeight());
		
		if( this.state == NEW_RECORD )
			inputPanel.render(cont, g);
				
		g.setColor(Color.white);
		
		if( this.state == NEW_RECORD )
			drawRecords(g, 450, 50);
		else
			drawRecords(g, 250, 50);
		
		backMainMenuButton.render(cont, g);
	}
	
	@Override
	public void update(GameContainer cont, StateBasedGame state, int delta)
			throws SlickException {
		
		Input input = cont.getInput();
		
		if( input.isKeyPressed(Input.KEY_ESCAPE) || enterBackMainMenu ) {			
			enterToMainMenu(state, input);
		}	
		
		if( isInputPanelEnable )
			inputPanel.update(cont, delta);
		
		backMainMenuButton.update(cont, delta);
	}

	/**
	 * Saves scores
	 */
	private void saveScore() {
		
		record.setName( inputPanel.getText() );
		records.add( record );		
		Collections.sort(records);
		
		ScoreRecorder recorder = new ScoreRecorder(recordFilePath);
		
		try {
			recorder.saveScores(records);
			Confiqurations.setProperty( Confiqurations.PLAYER_NAME, record.getName() );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Transfers game back to main menu and clears all key inputs
	 * 
	 * @param state
	 * @param input
	 */
	private void enterToMainMenu(StateBasedGame state, Input input) {
		input.clearControlPressedRecord();
		input.clearKeyPressedRecord();
		input.clearMousePressedRecord();
		
		state.enterState(Main.MAINMENU_STATE, new FadeOutTransition(Color.red), 
				  							  new FadeInTransition(Color.red));
	}		

	/**
	 * Draw records
	 * 
	 * @param g
	 * @param x Horizontal position
	 * @param y Vertical position
	 */
	private void drawRecords(Graphics g, float x, float y ) {
		
		g.drawString("Top Scores", x, y );
		
		float verticalStep = 35; 
		
		int index = 1;
		
		
		String line = String.format("%-10s %-10s %-10s", "Name", "Scores", "Time" ); 
		
		g.setColor(Color.black);
		y += verticalStep;
		
		g.drawString( "Name", x, y );
		g.drawString( "Scores", x + 250, y );
		g.drawString( "Time", x + 400, y );
		
		y += verticalStep;
		
		g.setColor(Color.white);
		
		for( Record record : records ) {
			
			line = String.format("%-8s %-8d %-5.1f s", record.getName(), 
														record.getScore(), 
														record.getSurvivingTime());
			
			g.drawString( record.getName(), x, y );
			g.drawString( String.valueOf( record.getScore()), x + 250, y );
			g.drawString( String.valueOf(record.getSurvivingTime()), x + 400, y );
			y += verticalStep;
			index++;
		}
	}

	@Override
	public void inputPanelTextChanged(InputPanelEvent event) {
		
	}

	@Override
	public void inputPanelTextEntered(InputPanelEvent event) {
		saveScore();
		state = SHOW_RECORS;
	}

	@Override
	public void buttonPressed(ButtonEvent event) {
		enterBackMainMenu = true;
	}
}
