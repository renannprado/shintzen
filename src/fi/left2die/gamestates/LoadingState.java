package fi.left2die.gamestates;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

import fi.left2die.gameobject.Explosion;
import fi.left2die.io.Confiqurations;
import fi.left2die.io.Resources;
import fi.left2die.ui.Slider;
import fi.left2die.ui.Slider.SliderFormat;

public class LoadingState extends BasicGameState {

	private int id;
	
    private DeferredResource nextResource; 
    
    private boolean started; 
	
    private Slider slider;
    
    private boolean isSomeBtnPressed = false;
    
	public LoadingState ( int id ) throws SlickException{
		this.id = id;		
		
	}
	
	@Override
	public int getID() {
		return id;
	}

	@Override
	public void init(GameContainer cont, StateBasedGame state)throws SlickException {
		
		cont.setSoundOn( Confiqurations.getBoolean( Confiqurations.SOUNDS ));
		cont.setMusicOn( Confiqurations.getBoolean( Confiqurations.MUSIC ));
		
		UnicodeFont sliderFont = new UnicodeFont("gamedata/fonts/STEVE.TTF",13,true, false);
		Image sliderImage = new Image("gamedata/images/loadingbar/loadingbar.png");
		
		//Loading bar have to be loaded before loading process can be started.
		this.slider = new Slider(null, new Vector2f(0,0), 
								SliderFormat.PERCENT,
								sliderImage,
								sliderFont);
		
		this.slider.position.y = (cont.getHeight() - slider.getHeight())/2;
		this.slider.position.x = (cont.getWidth() - slider.getWidth())/2;
		
		LoadingList.setDeferredLoading(true);         
		//Resources.addResource("explosionImage", new Image("data/images/explosion.png"));
		
		Resources.addResource("slider", new Image("gamedata/images/loadingbar/loadingbar.png") );
		
		Resources.addResource("sparkle", new Image("gamedata/images/bullets/40px-electric.png"));
        Resources.addResource("hellmarch",new Music("gamedata/sounds/Hellmarch.ogg"));
        Resources.addResource("menu_click",new Sound("gamedata/sounds/menu_click.wav"));

        //Fonts
        Resources.addResource("slider font", sliderFont );
        Resources.addResource("textfield font", new UnicodeFont("gamedata/fonts/STEVE.TTF",20,true, false));
        Resources.addResource("sketch font", new UnicodeFont("gamedata/fonts/attic.ttf",40,true, false));
        Resources.addResource("sketch font small", new UnicodeFont("gamedata/fonts/attic.ttf",18,true, false));
        
        //Optionstate
        Resources.addResource("checkbox active checked", new Image("gamedata/images/checkbox/activececkedbox.png"));
		Resources.addResource("checkbox active unchecked", new Image("gamedata/images/checkbox/activeuncekcedbox.png"));
		Resources.addResource("checkbox checked", new Image("gamedata/images/checkbox/checkedbox.png"));
		Resources.addResource("checkbox unchecked", new Image("gamedata/images/checkbox/uncheckedbox.png"));
		Resources.addResource("big gear", new Image("gamedata/images/biggear.png"));
		Resources.addResource("small gear", new Image("gamedata/images/smallgear.png"));
		
		
        //Mainmenu
        Resources.addResource( "mainmenubg", new Image("gamedata/images/menu/abstractbh.jpg"));
		Resources.addResource("menu title", new Image("gamedata/images/menu/title.png"));			
		Resources.addResource("menuitems exit", new Image("gamedata/images/menu/exit.png"));
		Resources.addResource("menuitems play", new Image("gamedata/images/menu/play.png"));
		Resources.addResource("menuitems options", new Image("gamedata/images/menu/options.png"));
		Resources.addResource("menuitems about", new Image("gamedata/images/menu/about.png"));		
		Resources.addResource("menuitems highscores", new Image("gamedata/images/menu/topscores.png"));
		Resources.addResource("mainmenu_loaded",new Sound("gamedata/sounds/mainmenu_loaded.wav"));
		
        //HighScoreState
        Resources.addResource("hi button", new Image("gamedata/images/highscore/button.png"));
        Resources.addResource("hi background", new Image("gamedata/images/highscore/background.jpg"));
        
        //Playing state
        Resources.addResource("light", new Image("gamedata/images/light.png"));
        Resources.addResource("healtpack", new Image("gamedata/images/healtpack.png"));
        Resources.addResource("zombie", new Image("gamedata/images/zombie.png"));
        Resources.addResource("gameover", new Image("gamedata/images/gameover.png"));
        Resources.addResource( "paused", new Image("gamedata/images/paused.png"));
        Resources.addResource( "brick", new Image("gamedata/images/brick.jpg"));
        Resources.addResource( "shadow", new Image("gamedata/images/shadow.png"));
        Resources.addResource( "tile", new Image("gamedata/images/tile.png"));
        Resources.addResource( "bullet blue comet", new Image("gamedata/images/bullets/12px-blue-comet.png"));
        Resources.addResource( "bullet red comet", new Image("gamedata/images/bullets/12px-red-comet.png") );
        Resources.addResource( "bullet purple comet", new Image("gamedata/images/bullets/12px-purple-comet.png") );
        Resources.addResource( "bullet greed seed", new Image("gamedata/images/bullets/12px-green-seed.png") );
        Resources.addResource( "bullet long blue", new Image("gamedata/images/bullets/12px-long-blue.png") );
        Resources.addResource( "bullet red arrow", new Image("gamedata/images/bullets/12px-red-arrow.png") );
        Resources.addResource("12px-blue-arrow",new Image("gamedata/images/bullets/12px-blue-arrow.png"));
        Resources.addResource("sahko2",new Image("gamedata/images/bullets/40px-electric-2.png"));
        Resources.addResource("ak",new Sound("gamedata/sounds/colt45.wav"));
        Resources.addResource("pistol",new Sound("gamedata/sounds/Boom.wav"));
        Resources.addResource("shotgun",new Sound("gamedata/sounds/shotgun_shoot.wav"));        
        Resources.addResource("bazooka",new Sound("gamedata/sounds/Rocket.wav"));
        Resources.addResource("explosion",new Sound("gamedata/sounds/barrel_explode.wav"));              
        Resources.addResource("upperbody",new Image("gamedata/images/tatuukko.png"));   
        Resources.addResource("perknotification",new Image("gamedata/images/perk.png"));   
        Resources.addResource("pickup", new Sound("gamedata/sounds/weapon_pickup.wav"));
        Resources.addResource("scream", new Sound("gamedata/sounds/scream.wav"));
        Resources.addResource("levelup", new Sound("gamedata/sounds/levelup.wav"));
        
        //Sounds and Dreams
        Resources.addResource("zombie scream1", new Sound("gamedata/sounds/kipu1.wav"));
        Resources.addResource("zombie scream2", new Sound("gamedata/sounds/kipu2.wav"));
        Resources.addResource("zombie scream3", new Sound("gamedata/sounds/kipu3.wav"));
        Resources.addResource("zombie scream4", new Sound("gamedata/sounds/kipu4.wav"));
        Resources.addResource("zombie scream5", new Sound("gamedata/sounds/kipu5.wav"));
        Resources.addResource("zombie scream6", new Sound("gamedata/sounds/kipu6.wav"));
        
        //About state
        Resources.addResource( "about top gradient", new Image("gamedata/images/aboutstate/abouTopGradiant.png"));
        Resources.addResource( "about bottom gradient", new Image("gamedata/images/aboutstate/abouBottomGradiant.png"));
        
        //Weapons
        Resources.addResource("weapon m4a1", new Image("gamedata/images/weapons/m4a1.png"));
        Resources.addResource("weapon mp5", new Image("gamedata/images/weapons/mp5.png"));
        Resources.addResource("weapon shotgun", new Image("gamedata/images/weapons/shotgun.png"));
        Resources.addResource("weapon sniper", new Image("gamedata/images/weapons/sniper.png"));
        Resources.addResource("weapon deserteagle", new Image("gamedata/images/weapons/deserteagle.png"));      
        
         //Emitters = particle effects
        Resources.addResource("particle", new Image("gamedata/images/particle.tga"));
        loadEmitters();
        
        loadMuzzleFlashAnimation();      
        //loadWalls();
        loadExplosion();
        	
        SoundStore.get().setMaxSources(32);  
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
	

	}
	
	private void loadEmitters() {
		
		ArrayList<ConfigurableEmitter> emitters = new ArrayList<ConfigurableEmitter>();
		
		for( int i=0; i<100; i++ ) {
			ConfigurableEmitter emitter = null;
		
	        try {
	        	
				InputStream xml = getClass().getClassLoader().getResourceAsStream("gamedata/effects/explode.xml");
				
				if( xml != null )
					emitter = ParticleIO.loadEmitter(xml);
				
				if( xml == null )
					emitter = ParticleIO.loadEmitter( new File("gamedata/effects/blood.xml"));
				
			} catch (Exception e) {
				e.printStackTrace();			
			}
			
			emitters.add(emitter);			
		}
		
		Resources.addResource("sand emitters", emitters );	
	}

	private void loadWalls() throws SlickException {
		Image image = new Image("gamedata/images/walltiles.png");
		
		//Tile height and width
		int tileWidth = 98;
		int tileHeight = 98;	
		
		Resources.addResource("wall lefttop",  image.getSubImage(0, 0, tileWidth, tileHeight));
		Resources.addResource("wall top",  image.getSubImage( tileWidth, 0, tileWidth, tileHeight));
		Resources.addResource("wall righttop",  image.getSubImage( tileWidth*2, 0, tileHeight, tileHeight));
		Resources.addResource("wall left",  image.getSubImage(0, tileHeight, tileWidth, tileHeight));
		Resources.addResource("wall right",  image.getSubImage( tileWidth * 2, tileHeight, tileWidth, tileHeight));
		Resources.addResource("wall leftbottom",  image.getSubImage(0, tileHeight*2, tileWidth, tileHeight));
		Resources.addResource("wall bottom",  image.getSubImage( tileWidth,  tileHeight*2, tileWidth, tileHeight));
		Resources.addResource("wall rightbottom",  image.getSubImage( tileWidth*2,  tileHeight*2, tileWidth, tileHeight));
	}
	
	private void loadMuzzleFlashAnimation() throws SlickException {
		
		int duration = 40;
		
		Animation flash = new Animation();
		flash.addFrame( new Image("gamedata/images/muzzleflash0.png"), duration );
		flash.addFrame( new Image("gamedata/images/muzzleflash2.png"), duration );
		flash.addFrame( new Image("gamedata/images/muzzleflash3.png"), duration );
		flash.setPingPong(true);
		
		Resources.addAnimation("muzzleflash", flash);		
	}
	
	private void loadFonts() {
		
	}
	
	private boolean isNotLoaded = false;
	
	private void loadExplosion() throws SlickException {
				
		//Tile height and width
		int tileWidth = 240;
		int tileHeight = 240;
		
		SpriteSheet image = new SpriteSheet("gamedata/images/explosion.png", 
											tileWidth, tileHeight );
		
		//How long each frame last
		int frameDurationMS = 50;
		
		int howManyAnimationsAreStored = 300;
		
		boolean isFirstRow = true;
		
		//List for frames
		ArrayList<Image>frames = new ArrayList<Image>();
		
		for( int y=0; y< image.getHeight(); y +=tileHeight ) {
			for( int x=0; x< image.getWidth(); x +=tileWidth ) {
				
				//This skip 3 tiles on the first row
				if( isFirstRow ) {
					x = tileWidth * 3;
					isFirstRow = false;
				}
				
				//Takes frame from large image
				frames.add(image.getSubImage(x, y, tileWidth, tileHeight ));
			}
		}
		
		//Back from list to array
		Image[] frameArray = frames.toArray( new Image[frames.size()]);
		
		//And here we store some animations to storage for the later use
		Resources.addAnimation("explosionanimation", frameArray,
								frameDurationMS, howManyAnimationsAreStored);
	}
	
	@Override
	public void render(GameContainer cont, StateBasedGame state, Graphics g)throws SlickException {
				         
        int total = LoadingList.get().getTotalResources(); 
        int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources(); 
         
        //Loading percents
		double bar = (double)loaded / (double)total; 
		         
		slider.setValue((float)bar);
        slider.render(cont, g);        
       
        g.setColor(Color.white);
        
        //Resource on loading process
		if (nextResource != null) { 
            g.drawString("Loading: "+nextResource.getDescription(), slider.position.x, slider.position.y + 50); 
        } 

        //Loading completed
        if (started) { 
            g.drawString("LOADING COMPLETED - PRESS KEY TO CONTINUE", slider.position.x, slider.position.y + 50);            
        }       		
	}

	@Override
	public void controllerButtonPressed(int controller, int button) {
		super.controllerButtonPressed(controller, button);
		
		isSomeBtnPressed = true;
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		
		isSomeBtnPressed = true;
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		isSomeBtnPressed = true;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta)throws SlickException {
		//move to main menu					
        if(isSomeBtnPressed && started) {            	
        	state.enterState(Main.MAINMENU_STATE,
        					 new FadeOutTransition(Color.black), 
        					 new FadeInTransition(Color.black));
        	
        	//Prevents game to enter the playing state, with key no second enter key pressing.
        	gc.getInput().clearKeyPressedRecord();    		
        }
		
		if (nextResource != null) {       	
            try { 
                nextResource.load();
            } catch (IOException e) { 
                throw new SlickException("Failed to load: "+nextResource.getDescription(), e); 
            }  
            nextResource = null; 
        } 

        if (LoadingList.get().getRemainingResources() > 0) { 
            nextResource = LoadingList.get().getNext(); 
        } else { 
            if (!started) { 
                started = true;
	            Resources.getMusic("hellmarch").loop(1, 0.1f);
                Resources.getSound("ak").play();
            } 
        }
	}

}
