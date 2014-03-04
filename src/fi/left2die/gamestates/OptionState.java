package fi.left2die.gamestates;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import fi.left2die.gameobject.enviroment.GearPair;
import fi.left2die.io.Confiqurations;
import fi.left2die.io.Resources;
import fi.left2die.model.KeyboardConfig;
import fi.left2die.ui.Button;
import fi.left2die.ui.CheckBox;
import fi.left2die.ui.KeySelector;
import fi.left2die.ui.Slider;
import fi.left2die.ui.Button.ButtonEvent;
import fi.left2die.ui.Button.ButtonListener;
import fi.left2die.ui.Button.FilterColor;
import fi.left2die.ui.Slider.SliderEvent;
import fi.left2die.ui.Slider.SliderFormat;
import fi.left2die.ui.Slider.SliderListener;

/**
 * OptionState is a game state where user can change setting like key setting, and sound music volume.
 * 
 * @author TeeMuki
 */
public class OptionState extends BasicGameState implements ButtonListener, SliderListener {

	private int stateID;
		
	/**
	 * Hard coded default configuration values
	 */
	private KeyboardConfig defaultConfigs = new KeyboardConfig( Input.KEY_D, Input.KEY_A, 
																Input.KEY_W, Input.KEY_S, Input.KEY_SPACE, 
																Input.KEY_1, Input.KEY_2, Input.KEY_3,
																Input.KEY_4, Input.KEY_5, Input.KEY_6,
																Input.KEY_Q, Input.KEY_R );
	
	/**
	 * The KeyboarConfig class instance is serialized to this file. 
	 */
	private String keyConfigsFileName = Confiqurations.getProperty(Confiqurations.KEYBOARD_CONF_FILE);
		
	/**
	 * 	Boxes where you can bind you keyboard configurations
	 */
	private ArrayList<KeySelector>selectors = new ArrayList<KeySelector>();
	
	/**
	 * The reset button load default configurations when it is pressed .
	 */
	private Button resetBtn;
	
	/**
	 * The save button saves user configurations and returns to main menu after it is pressed.
	 */
	private Button saveBtn;
	
	/**
	 * Font for the texts
	 */
	private UnicodeFont font;
		

	/**
	 * Slider ui-component to adjust sound volume
	 */
	private Slider soundSlider;
	

	/**
	 * Slider ui-component to adjust music volume
	 */
	private Slider musicSlider;
	
	/**
	 * 
	 */
	private SliderEvent lastEvent;
	
	/**
	 * Checkbox ui-component for setting music on or off
	 */
	private CheckBox musicChx;
	
	/**
	 * Checkbox ui-component for setting fullscreen mode on or off
	 */
	private CheckBox fullscreenCbx;

	/**
	 * Checkbox ui-component for setting sounds on & off
	 */
	private CheckBox soundChx;
	
	private ArrayList<GearPair> gearPairs = new ArrayList<GearPair>();
	
	/**
	 * Control flag witch indicate that configurations is saved on next update
	 */
	private boolean saveSettingOnNextUpdate = false;
	
	private Button returnBtn;
	
	/**
	 * Control flag witch indicate program is returning to main menu
	 */
	private boolean returningToMainMenu = false;
		
	/**
	 * Constructor for OptionsState
	 * 
	 * @param id
	 */
	public OptionState( int id ) {
		this.stateID = id;
	}
	
	@Override
	public void init(GameContainer cont, StateBasedGame state) throws SlickException {
		
		Image buttonBg = Resources.getImage("hi button");
		
		saveBtn = new Button( "save", "Save", cont.getWidth() - 250 ,
											  cont.getHeight() - 80 );
		saveBtn.addListener(this);
		saveBtn.setFilterColor( FilterColor.GREEN );
		
		resetBtn = new Button( "reset", "Reset Defaults", 
								cont.getWidth() - 250 , 
								cont.getHeight() - 150);
		
		resetBtn.setFilterColor(FilterColor.RED);
		
		resetBtn.addListener(this);
		
		returnBtn = new Button("return", "Main menu", cont.getWidth() - 250, cont.getHeight()-250);
		returnBtn.addListener(this);
		returnBtn.setFilterColor( FilterColor.GREEN );
		
		selectors.add( new KeySelector( new Vector2f(50,50), "Move Left") );
		selectors.add( new KeySelector( new Vector2f(50,100), "Move Right") );
		selectors.add( new KeySelector( new Vector2f(50,150), "Move Up") );
		selectors.add( new KeySelector( new Vector2f(50,200), "Move Down") );
		selectors.add( new KeySelector( new Vector2f(50,250), "Shoot") );
		
		selectors.add( new KeySelector( new Vector2f(480,50), "Weapon 1") );		
		selectors.add( new KeySelector( new Vector2f(480,100), "Weapon 2") );		
		selectors.add( new KeySelector( new Vector2f(480,150), "Weapon 3") );		
		selectors.add( new KeySelector( new Vector2f(480,200), "Weapon 4") );		
		selectors.add( new KeySelector( new Vector2f(480,250), "Weapon 5") );		
		selectors.add( new KeySelector( new Vector2f(480,300), "Weapon 6") );
		
		selectors.add( new KeySelector( new Vector2f(480,350), "Next Weapon") );		
		selectors.add( new KeySelector( new Vector2f(480,400), "Previous Weapon") );
		
		font = Resources.getFont("textfield font");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
		
		Vector2f soundPos = new Vector2f(50, 500);
		soundSlider = new Slider( null, soundPos, SliderFormat.PERCENT );
		soundSlider.addListener(this);
		soundSlider.setValue(cont.getSoundVolume());
		
		Vector2f mucisPos =  new Vector2f(50, 570);
		musicSlider = new Slider( null, mucisPos, SliderFormat.PERCENT );
		musicSlider.setValue(cont.getMusicVolume());
		musicSlider.addListener(this);
		
		musicChx = new CheckBox(50, 600, 
									"Music", 
									Color.black);
		
		soundChx = new CheckBox( 50, 650, 
								"Sounds", 
								Color.black);
		
		fullscreenCbx = new CheckBox( 50, 700, 
									"Fullscreen", 
									Color.black);		

		
		musicChx.setChecked( Confiqurations.getBoolean(Confiqurations.MUSIC));
		soundChx.setChecked( Confiqurations.getBoolean(Confiqurations.SOUNDS));
		
		loadConfigs();
		
		gearPairs.add( new GearPair(null, 30f, new Vector2f(100,100), 0.5f));
		gearPairs.add( new GearPair(null, 10f, new Vector2f(50,50), 0.3f));
		gearPairs.add( new GearPair(null, 15f, new Vector2f(400,350), 0.1f));
		gearPairs.add( new GearPair(null, 20f, new Vector2f(380,200), 0.6f));
		gearPairs.add( new GearPair(null, 12f, new Vector2f(380,500), 1f));
		gearPairs.add( new GearPair(null, 5f, new Vector2f(500,600), 0.7f));
		gearPairs.add( new GearPair(null, 14f, new Vector2f(780,100), 0.3f));
		gearPairs.add( new GearPair(null, 25f, new Vector2f(680,200), 0.2f));			
	}		

	/**
	 * Game state id
	 */
	public int getID() {
		return stateID;
	};
	
	/**
	 * This is called when this game state are entered
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		
		loadConfigs();
		
		Input input = container.getInput();
		
		input.addKeyListener(this);		
		input.addListener(saveBtn);
		input.addListener(resetBtn);
		input.addListener(returnBtn);
		
		input.clearMousePressedRecord();
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {

		container.getInput().removeListener(saveBtn);
		container.getInput().removeListener(resetBtn);
		
		Input input = container.getInput();
		
		input.clearMousePressedRecord();
		input.clearKeyPressedRecord();
		input.clearControlPressedRecord();
	}
	
	@Override
	public void render(GameContainer cont, StateBasedGame state, Graphics g) throws SlickException {
		
		g.setBackground(Color.white);	
		
		for( GearPair pair : gearPairs ) {
			pair.render(cont, g);
		}
		
		final Color bgColor = new Color( 100, 100, 100, 100 );
		
		g.setColor(bgColor);
		g.fillRect( 40, 30, 425, 250 );
		
		g.fillRect( 470, 30, 430, 400 );
		
		g.setFont(font);			
		
		for( KeySelector selector : selectors )  {
			selector.render(cont, g);
		}				
		
		g.setColor(Color.black );
		g.drawString("Sound volume", soundSlider.position.x, soundSlider.position.y - 25);
		g.drawString("Music volume", musicSlider.position.x, musicSlider.position.y - 25);
		soundSlider.render(cont, g);
		musicSlider.render(cont, g);		
		
		g.setColor(Color.black);
		resetBtn.render(cont, g);
		saveBtn.render(cont, g);
		returnBtn.render(cont, g);
		
		soundChx.render(cont, g);
		fullscreenCbx.render(cont, g);
		musicChx.render(cont, g);
	}
	
	/**
	 * Event occurs when some mouse button is pressed. 
	 * It binds mouse button to the active selectors.
	 */
	@Override
	public void mousePressed(int button, int x, int y) {
		for( int i=0; i<selectors.size(); i++ ) {
			KeySelector selector = selectors.get(i);
		}
	}
	
	/**
	 * Event occurs when key is pressed, it iterates trough each selector and look for active ones.
	 * If selector is active it binds pressed key to the selector.
	 */
	@Override
	public void keyPressed(int key, char c) {			
		for( int i=0; i<selectors.size(); i++ ) {
			KeySelector selector = selectors.get(i);
			
			if( selector.isActive() )
				selector.setBinding(key);
		}
	}
	
	@Override
	public void update(GameContainer cont, StateBasedGame state, int delta)	throws SlickException {
		
		Input input = cont.getInput();
		
		if( input.isKeyPressed(Input.KEY_ESCAPE))
			state.enterState(Main.MAINMENU_STATE);	
		
		for( int i=0; i<selectors.size(); i++) {
			KeySelector selector = selectors.get(i);			
			selector.update(cont, delta);
		}
		
		soundSlider.update(cont, delta);
		musicSlider.update(cont, delta);
		
		resetBtn.update(cont, delta);		
		saveBtn.update(cont, delta);	
		returnBtn.update(cont, delta);
		
		if( lastEvent != null ) {
			
			//Sound slider value changed
			if(lastEvent.getId().equals(soundSlider.getId()) ) {
				cont.setSoundVolume(lastEvent.getPercent()/100);				
			}
			
			//Music slider value changed
			if(lastEvent.getId().equals(musicSlider.getId())) {
				cont.setMusicVolume(lastEvent.getPercent()/100);				
			}
			
			lastEvent = null;
		}
		
		soundChx.update(cont, delta);
		fullscreenCbx.update(cont, delta);
		musicChx.update(cont, delta);
		
		if( saveSettingOnNextUpdate ) {
			saveConfigs(cont);
			saveSettingOnNextUpdate = false;
		}
		
		for( GearPair pair : gearPairs ) {
			pair.update(cont, delta);
		}
		
		if( returningToMainMenu ) {
			returningToMainMenu = false;
			state.enterState( Main.MAINMENU_STATE );
		}
	}

	/**
	 * Reset default key configurations
	 */
	private void resetDefaultConfigs() {
		
		for( int i=0; i<selectors.size(); i++ ) {
			KeySelector selector = selectors.get(i);
			selector.setActive(false);
		}		
					  
	    setBindingsToSelectors( defaultConfigs );
	}	
	
	/**
	 * Load all configurations from disk
	 */
	private void loadConfigs() {
		File file = new File( keyConfigsFileName );
		
		KeyboardConfig conf = new KeyboardConfig();
		
		//Lets read sound and music volumes from configurations file
		try {
			float soundVolume = new Float(Confiqurations.getProperty(Confiqurations.SOUND_VOLUME));
			soundSlider.setPercent(soundVolume * 100);
			
			float musicVolume = new Float(Confiqurations.getProperty(Confiqurations.MUSIC_VOLUME));
			musicSlider.setPercent(musicVolume * 100);
			
			musicChx.setChecked( Confiqurations.getBoolean(Confiqurations.MUSIC));
			soundChx.setChecked( Confiqurations.getBoolean(Confiqurations.SOUNDS));
			fullscreenCbx.setChecked(Confiqurations.getBoolean(Confiqurations.FULLSCREEN));
		} 
		
		//No values found, lets kick default values in
		catch( Exception e ) {
			soundSlider.setPercent( soundSlider.getMax() / 2 );
			musicSlider.setPercent( musicSlider.getMax() / 2 );
		}
		
		try {
			conf.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		setBindingsToSelectors(conf);
	}
	
	/**
	 * Binds given keyboard configurations to selector boxes.
	 * 
	 * @param configs
	 */
	private void setBindingsToSelectors( KeyboardConfig configs ) {
		getSelectorByText("Move Left").setBinding( configs.moveLeft );
		getSelectorByText("Move Right").setBinding( configs.moveRight );
		getSelectorByText("Move Up").setBinding( configs.moveUp );
		getSelectorByText("Move Down").setBinding( configs.moveDown );
		
		getSelectorByText("Shoot").setBinding( configs.shoot );
		
		getSelectorByText("Weapon 1").setBinding( configs.selectWeapon1 );
		getSelectorByText("Weapon 2").setBinding( configs.selectWeapon2 );
		getSelectorByText("Weapon 3").setBinding( configs.selectWeapon3 );
		getSelectorByText("Weapon 4").setBinding( configs.selectWeapon4 );
		getSelectorByText("Weapon 5").setBinding( configs.selectWeapon5 );
		getSelectorByText("Weapon 6").setBinding( configs.selectWeapon6 );
		
		getSelectorByText("Next Weapon").setBinding( configs.nextWeapon );
		getSelectorByText("Previous Weapon").setBinding( configs.previousWeapon );
	}
	
	/**
	 * Read configurations form selectors and then saves them to file
	 * @throws SlickException 
	 */
	private void saveConfigs( GameContainer cont ) throws SlickException {
		
		int moveRight = getSelectorByText("Move Right").getBinding();
		int moveLeft = getSelectorByText("Move Left").getBinding();
		int moveUp = getSelectorByText("Move Up").getBinding();
		int moveDown = getSelectorByText("Move Down").getBinding();
		int shoot = getSelectorByText("Shoot").getBinding();
		
		int selectWeapon1 = getSelectorByText("Weapon 1").getBinding();
		int selectWeapon2 = getSelectorByText("Weapon 2").getBinding();
		int selectWeapon3 = getSelectorByText("Weapon 3").getBinding();
		int selectWeapon4 = getSelectorByText("Weapon 4").getBinding();
		int selectWeapon5 = getSelectorByText("Weapon 5").getBinding();
		int selectWeapon6 = getSelectorByText("Weapon 6").getBinding();
		
		int nextWeapon= getSelectorByText("Next Weapon").getBinding();
	    int previousWeapon = getSelectorByText("Previous Weapon").getBinding();
		
		KeyboardConfig configs = new KeyboardConfig(moveRight, moveLeft, moveUp, moveDown, 
													shoot, selectWeapon1, selectWeapon2, 
													selectWeapon3, selectWeapon4, selectWeapon5, 
													selectWeapon6, nextWeapon, previousWeapon);
		
		Confiqurations.setProperty( Confiqurations.SOUND_VOLUME, soundSlider.getPercent()/100);
		Confiqurations.setProperty( Confiqurations.MUSIC_VOLUME, musicSlider.getPercent()/100);
		
		Confiqurations.setProperty( Confiqurations.MUSIC, 
									new Boolean(musicChx.isChecked()).toString() );
		
		Confiqurations.setProperty( Confiqurations.SOUNDS, 
									new Boolean(soundChx.isChecked()).toString() );
		
		Confiqurations.setProperty( Confiqurations.FULLSCREEN,
									new Boolean(fullscreenCbx.isChecked()).toString());
		
		try {
			configs.save( new File( keyConfigsFileName ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Let's apply configurations
		cont.setSoundVolume(soundSlider.getPercent()/100);
		cont.setMusicVolume(musicSlider.getPercent()/100);
		cont.setSoundOn(soundChx.isChecked());
		cont.setMusicOn(musicChx.isChecked());
		cont.setFullscreen(fullscreenCbx.isChecked());
	}
	
	/**
	 * Returns selector form the list by given selector name.
	 * 
	 * @param selectorName A selector name that should be returned.
	 * @return Returns selector form the list by given selector name 
	 * 				or if there is no such selector null is returned.
	 */
	private KeySelector getSelectorByText( String selectorName ) {
		for( int i=0; i<selectors.size(); i++) {
			KeySelector selector = selectors.get(i);
			
			if( selector.getText().equals(selectorName)) {
				return selector;
			}
		}
		
		return null;
	}

	/**
	 * This event occurs when reset or save button is pressed. 
	 */
	@Override
	public void buttonPressed(ButtonEvent event) {	
		if( event.getButtoName().equals("reset"))
			resetDefaultConfigs();
		
		if( event.getButtoName().equals("save") ) {
			saveSettingOnNextUpdate = true;
		}
				
		if( event.getButtoName().equals("return")) {
			returningToMainMenu = true;
		}
	}

	@Override
	public void sliderValueChanged(SliderEvent event ) {		
		//Slider event is saved to instance variable to processed on next update on update method.
		this.lastEvent = event;			
	}
}
