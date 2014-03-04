package fi.left2die.gameobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.enviroment.Map;
import fi.left2die.io.*;
import fi.left2die.library.StopWatch;
import fi.left2die.library.UniqueID;
import fi.left2die.model.KeyboardConfig;
import fi.left2die.model.PadConfig;

/**
 * Player class hold character that a player can control in game.
 * 
 * @author TeeMuki
 */
public class Player extends MovableEntity implements IGameObject {

	/**
	 * Vector show point where player is looking.
	 */
	private Vector2f lookingDirection = new Vector2f(0,0);
			
	/**
	 * Starting position where player is placed when game start
	 */
	private Vector2f initialPosition;
	
	/**
	 * Image of upper body
	 */
	private Image upperBody;
	
	/**
	 * Player shadow
	 */
	private Image shadow;

	/**
	 * Player experiences are kept track on here
	 */
	public Expirience expirience = new Expirience();
	
	/**
	 * Scores
	 */
	public long score = 0;
	
	/**
	 * Vitality is same as player maximum health
	 */
	public int strength,vitality = 100,defense;
	
	/**
	 * Player current health
	 */
	public float health = 0;
	
	/**
	 * Currently selected weapon
	 */
	private int currentWeapon;
		
	/**
	 * How fast player accelerates
	 */
	private float acceration = 0.00025f;//How fast player accelerates on using keyboard
	
	/**
	 * Class randomizer
	 */
	private static Random random;	
	
	/**
	 * Joystick key bindings
	 */
	private PadConfig padConfig;
	
	/**
	 * Keyboard key bindings
	 */
	private KeyboardConfig keyConfigs;
	
	/**
	 * Control flag that indicates that joystick controller is in use
	 */
	private boolean isPadInUse = false;//Is a game controller in use
	
	/**
	 * Weapon numbers
	 */
	public final static int PISTOL = 0,
							SHOTGUN = 1,
							AK = 2,
							BAZOOKA = 3,
							MINIGUN = 4;			
		
	/**
	 * Weapons in use
	 */
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>(); 
	
	/**
	 * Weapon image
	 */
	private ArrayList<Image> weaponImage = new ArrayList<Image>();
	
	/**
	 * Weapon shooting intervals in milliseconds
	 */
	private int PISTOL_SHOOT_SPEED_MS = 400, 
				AK_SHOOT_SPEED_MS = 150,	
				SHOTGUN_SHOOT_SPEED_MS = 500,
				BAZOOKA_SHOOT_SPEED_MS = 500, 
				MINIGUN_SHOOT_SPEED_MS = 50;
	
	private StopWatch pistolTimer = new StopWatch();
	private StopWatch akTimer = new StopWatch();
	private StopWatch shotgunTimer = new StopWatch();
	private StopWatch bazookaTimer = new StopWatch();
	private StopWatch minigunTimer = new StopWatch();
	
	/**
	 * How long muzzle flash is shown.
	 */
	private int muzzlePlashPlayTime = 50;//ms
	
	/**
	 * Timer for muzzle flash
	 */
	private StopWatch muzzleFlashTimer = new StopWatch();
	
	/**
	 * Muzzle flash animation
	 */
	private Animation muzzleFlashAnimation;
	
	/**
	 * Center of rotation for player texture
	 */
	private Vector2f centerOfRotation = new Vector2f( 0,0);	
	
	/**
	 * Player class constructor
	 * 
	 * @param parent Parent entity
	 * @param position Starting position
	 * @param velocity Velocity
	 * @param config Joystick configurations
	 * @param keyConfigs Keyboard configurations
	 * @throws SlickException
	 */
	public Player(Entity parent, Vector2f position, Vector2f velocity, 
				  PadConfig config, KeyboardConfig keyConfigs ) throws SlickException {
		
		super("Player " + UniqueID.get(), parent);
		
		this.initialPosition = new Vector2f(position);
		
		this.position = position;
		this.velocity = velocity;
		
		this.keyConfigs = keyConfigs;
		
		resetPlayer();

		upperBody = Resources.getImage("upperbody");
		shadow = Resources.getImage("shadow");
		
		this.shape = new Circle( position.x + upperBody.getWidth()/2*scale, 
								position.y + upperBody.getHeight() /2*scale, 
								upperBody.getHeight() / 2 * scale );
		
		upperBody.setCenterOfRotation( upperBody.getWidth()/2 * scale, 
									  upperBody.getHeight()/2 * scale);
		
		shadow.setCenterOfRotation( shadow.getWidth()/2*scale, shadow.getWidth()/2*scale);
				
		//Game controller configurations
		this.padConfig = config;		
		
		pistolTimer.start();
		akTimer.start();
		shotgunTimer.start();
		bazookaTimer.start();
		minigunTimer.start();
		muzzleFlashTimer.stop();
		
		muzzleFlashAnimation = Resources.getAnimations("muzzleflash").get(0);	
		
		addWeapon(Player.PISTOL);
	}
	
	/**
	 * @return Returns keyboard configurations
	 */
	public KeyboardConfig getKeyConfigs() {
		return keyConfigs;
	}

	/**
	 * Set keyboard configurations
	 * 
	 * @param keyConfigs
	 */
	public void setKeyConfigs(KeyboardConfig keyConfigs) {
		this.keyConfigs = keyConfigs;
	}

	/**
	 * Chances current weapon
	 *  
	 * @param weaponNo Give weapon number like Player.Pistol
	 */
	public void changeCurrentWeapon( int weaponNo ) {
		
		boolean isCurrentWeaponLegal = false;
		
		for( int i=0; i<weapons.size(); i++ ) {
			Weapon weapon = weapons.get(i);
			
			if( weapon.weaponID == weaponNo ) { 			
				weapon.enabled = true;
				currentWeapon = weaponNo;
				isCurrentWeaponLegal = true;
			}
			else
				weapon.enabled = false;
		}
		
		if( !isCurrentWeaponLegal )
			currentWeapon = Player.PISTOL;
	}
	
	
	/**
	 * Enables of disables weapon depending its state.
	 * 
	 * @param weaponNo Give weapon number like Player.Shotgun
	 */
	public void toggleWeapon( int weaponNo ) {
		if( weaponNo >= 0 && weaponNo < weapons.size() ) {
			
			//XOR logic 1 ^ 1 == 0 
			//XOR logic 0 ^ 1 == 0		
			Weapon weapon = weapons.get( weaponNo );;
			weapon.enabled ^= true;
		}
	}
			
	/**
	 * Reset player position, velocity, and other stats.
	 */
	public void resetPlayer() {
		this.rotation = 0;
		this.friction = 0.98f;
		this.maxSpeed = 0.45f;		
		this.strength=0;
		this.defense=0;
		this.vitality=100;
		this.health=this.vitality;
		this.scale = 0.5f;
		this.random = new Random();
		this.currentWeapon = 1;	
		this.position.x = this.initialPosition.x;
		this.position.y = this.initialPosition.y;		
		this.velocity.scale(0);
		this.score = 0;
		this.expirience.reset();
	}
	
	/**
	 * Returns game controller configurations
	 * @return Returns game controller configurations
	 */
	public PadConfig getPadConfig() {
		return padConfig;
	}

	/**
	 * Enables weapon use
	 * 
	 * @param weaponNo
	 */
	public void addWeapon( int weaponNo ) {
		switch( weaponNo ) {
			case Player.PISTOL :
				weapons.add( new Weapon(0, Player.PISTOL, Resources.getImage("weapon deserteagle"), true, keyConfigs.selectWeapon1 ));
				break;
			case Player.SHOTGUN :
				weapons.add( new Weapon(1, Player.SHOTGUN, Resources.getImage("weapon shotgun"), true, keyConfigs.selectWeapon2 ));
				break;
			case Player.AK :
				weapons.add( new Weapon(2, Player.AK, Resources.getImage("weapon m4a1"), true, keyConfigs.selectWeapon3 ));	
				break;
			case Player.MINIGUN :
				weapons.add( new Weapon(3, Player.MINIGUN, Resources.getImage("weapon sniper"), true, keyConfigs.selectWeapon4 ));
				break;
			case Player.BAZOOKA :
				weapons.add( new Weapon(4, Player.BAZOOKA,Resources.getImage("weapon sniper"), true, keyConfigs.selectWeapon5 ));
				break;		
		}
		
		//Sort weapons list
		Collections.sort(weapons);
		
		//Change current weapon to new weapon
		//weapons.get(currentWeapon).enabled = false;
		currentWeapon = weaponNo;
	}
	
	/**
	 * Set game controller configurations
	 * @param padConfig Set game controller configurations
	 */
	public void setPadConfig(PadConfig padConfig) {
		this.padConfig = padConfig;
	}
		
	@Override
	public void update(GameContainer cont, int delta) {
		
		Input input = cont.getInput();
						
		if( input.isKeyDown( keyConfigs.selectWeapon1 ))
			currentWeapon = Player.PISTOL;
		
		if( input.isKeyDown( keyConfigs.selectWeapon2 ))
			currentWeapon = Player.SHOTGUN;
		
		if( input.isKeyDown( keyConfigs.selectWeapon3 ))
			currentWeapon = Player.AK;
		
		if( input.isKeyDown( keyConfigs.selectWeapon4 ))
			currentWeapon = Player.MINIGUN;
		
		if( input.isKeyDown( keyConfigs.selectWeapon5 ))
			currentWeapon = Player.BAZOOKA;
				
		changeCurrentWeapon(currentWeapon);
		
		if( input.isKeyDown( keyConfigs.moveLeft))
			velocity.x -= acceration * delta;
		
		if( input.isKeyDown( keyConfigs.moveRight ))
			velocity.x += acceration * delta;
		
		if( input.isKeyDown( keyConfigs.moveUp ))
			velocity.y -= acceration * delta;
		
		if( input.isKeyDown( keyConfigs.moveDown ))
			velocity.y += acceration * delta;		
				
		if( input.isButton3Pressed(Input.ANY_CONTROLLER))
			isPadInUse ^= true;
		
		//GameController handling
		if( isPadInUse ) {
			float padScaling = 0.004f;
		
			float x = input.getAxisValue( padConfig.gameContIndex, 
										  padConfig.padHorizontalMovement );
			
			float y = input.getAxisValue( padConfig.gameContIndex, 
										  padConfig.padVerticalMovement );
			
			if( Math.abs(x) > padConfig.deadZone ||  Math.abs(y) > padConfig.deadZone) {			
				velocity.x += x * padScaling;
				velocity.y += y * padScaling; 
			}
			
			x = input.getAxisValue( padConfig.gameContIndex, padConfig.padHorizontalLooking );
			y = input.getAxisValue( padConfig.gameContIndex, padConfig.padVerticalLooking );
						
			if( Math.abs(x) > padConfig.deadZone 
				||  Math.abs(y) > padConfig.deadZone) {
				
				lookingDirection.x = -x;
				lookingDirection.y = -y;
			}
			
			if( input.isButton1Pressed(Input.ANY_CONTROLLER))
				shootWeapon(input);
		}
		
		
		if( input.isMouseButtonDown( Input.MOUSE_LEFT_BUTTON ) || input.isMouseButtonDown( keyConfigs.shoot ) ){
			shootWeapon(input);
		}
		
		velocity.x *= friction;
		velocity.y *= friction;
		
		//check if players coordinates go outside the game area
		if(position.x>-shape.getWidth()/2 
			&& position.y>-shape.getHeight()/2 
			&& position.x<Map.mapWidth+shape.getWidth()/2 
			&& position.y<Map.mapHeight+shape.getHeight()/2) {
			
			move(this, velocity, delta);
		}
					
		rotation = lookingDirection.getTheta();
		upperBody.setRotation((float)rotation);
		shadow.setRotation((float)rotation);
		shape.setLocation( position.x, position.y );
	}
	
	/**
	 * Draw weapon task bar
	 * 
	 * @param x
	 * @param y
	 */
	public void drawTaskBar( float x, float y, Graphics g ) {
		
		final float margin = 120;
		
		final Color rectangleColor = new Color( 1f, 1f, 1f, 0.75f );
		g.setColor(rectangleColor);
		g.fillRoundRect( x -10 , y -10, margin * weapons.size(), 45, 5 );
		
		for( int i=0; i< weapons.size(); i++ ) {
			
			if( i > 0 )
				x += margin;
			
			drawWeapon(x, y, weapons.get(i).enabled, weapons.get(i).picture);
			g.setColor(Color.red);
			g.drawString( Input.getKeyName( weapons.get(i).keyBind ) , x-5, y + 7);
		}			
	}
	
	/**
	 * Helper function drawing taskbar
	 * 
	 * @param x
	 * @param y
	 * @param isActive
	 * @param image
	 */
	private void drawWeapon( float x, float y, Boolean isActive, Image image ) {
		
		final Color active = new Color(1f,1f,1,1f);
		final Color nonActive = new Color(0f,0f,0f,1f);
		final float scale = 0.4f;
		
		if( isActive ) 
			image.draw(x, y, scale, active );
		else
			image.draw(x, y, scale, nonActive);
	}
		
	/**
	 * Shoot weapon currently selected weapon
	 * 
	 * @param input Input system
	 */
	private void shootWeapon( Input input ) {
		switch(currentWeapon){
			case PISTOL:							
				shootPistol();				
				break;
				
			case SHOTGUN:
				shootShotgun();
				break;
				
			case AK:
				shootAk();
				break;
				
			case BAZOOKA:
				shootBazooka();	
				break;
				
			case MINIGUN:
				shootMinigun();
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * Creates pistol projectiles
	 */
	private void shootPistol() {
		
		if( PISTOL_SHOOT_SPEED_MS < pistolTimer.getElapsedTime() ) {
			Sound pistol = Resources.getSound("pistol");	
			Resources.getSound("pistol").play();
			
			pistolTimer.reset();
			muzzleFlashTimer.start();
			
			final int bulletCount = 1;
			final float maxSpeed = 0.5f;
			final float minSpeed = 0.5f;		
			final float sector = 1;
			final float damage = 40;
			final short penetrationForce = 4;
			
			createProjectiles(bulletCount,maxSpeed,minSpeed,sector, 
							  Projectile.TEXTURE_LONG_BLUE, 
							  damage, penetrationForce );
		}
	}

	/**
	 * Creates ak projectiles
	 */
	private void shootAk() {
		
		long elapsedTime = akTimer.getElapsedTime();
		
		if(  AK_SHOOT_SPEED_MS < elapsedTime ) {
		
			akTimer.reset();
			
			muzzleFlashTimer.start();
			Resources.getSound("ak").play();
			
			final int bulletCount = 1;
			final float maxSpeed = 0.8f;
			final float minSpeed = 0.8f;		
			final float sector = 3;
			final float damage = 40;
			final short penetrationForce = 3;
			
			createProjectiles(bulletCount,maxSpeed,minSpeed,sector, 
							  Projectile.TEXTURE_RED_COMET, damage,
							  penetrationForce );
		}
		
	}	
	
	/**
	 * Creates minigun projectiles
	 */
	private void shootMinigun() {
		
		long elapsedTime = minigunTimer.getElapsedTime();
		
		if( MINIGUN_SHOOT_SPEED_MS < elapsedTime ) {
		
			minigunTimer.reset();
			
			muzzleFlashTimer.start();
			Resources.getSound("ak").play();
			
			final int bulletCount = 3;
			final float maxSpeed = 0.8f;
			final float minSpeed = 0.8f;		
			final float sector = 15;
			final float damage = 10;
			final short penetrationForce = 2;
			
			createProjectiles(bulletCount,maxSpeed,minSpeed,sector, 
							  Projectile.TEXTURE_PURPLE_COMET, damage,
							  penetrationForce );
		}
		
	}	
	
	/**
	 * Creates bazooka projectiles
	 */
	private void shootBazooka() {
		
		long elapsedTime = bazookaTimer.getElapsedTime();
		
		if(  BAZOOKA_SHOOT_SPEED_MS < elapsedTime ) {
			
			muzzleFlashTimer.start();
			bazookaTimer.reset();
			
			Resources.getSound("bazooka").play();
			
			final int bulletCount = 1;
			final float maxSpeed = 0.2f;
			final float minSpeed = 0.2f;		
			final float sector = 1;
			final float damage = 80;
			final short penetrationForce = 1;
			
			createProjectiles(bulletCount,maxSpeed,minSpeed,sector, 
							  Projectile.TEXTURE_GREEN_SEED, 
							  damage, penetrationForce );
		}
	}
	
	/**
	 * Creates shotgun projectiles
	 */
	private void shootShotgun()  {
		
		long elapsedTime = shotgunTimer.getElapsedTime();
		
		if( SHOTGUN_SHOOT_SPEED_MS < elapsedTime ) {
		
			muzzleFlashTimer.start();
			shotgunTimer.reset();
						
			Resources.getSound("shotgun").play();
		
			final int bulletCount = 20;
			final float maxSpeed = 0.8f;
			final float minSpeed = 0.4f;		
			final float sector = 30;
			final float damage = 10;
			final short penetrationForce = 2;
			
			createProjectiles(bulletCount,maxSpeed,minSpeed,sector, 
							  Projectile.TEXTURE_BLUECOMET, 
							  damage, penetrationForce );
		}
	}
	
	/**
	 * Creates flametrower projectiles Not implemented!
	 */
	private void shootFlameTrower() {
		
	}
	
	/**
	 * Creates projectiles
	 * 
	 * @param bulletCount How many Projectile is created same time
	 * @param maxSpeed Projectile max speed
	 * @param minSpeed Projectile min speed
	 * @param sector Dispersion of bullets
	 * @param bulletTexture Projectile texture 
	 * @param damage Single projectile damage
	 * @param penerationForce How many creature projectile penerates before it are removed from map
	 */
	private void createProjectiles(int bulletCount, float maxSpeed, 
								   float minSpeed, float sector, 
								   int bulletTexture, float damage,
								   short penerationForce ) {
				
		for( int i=0; i<bulletCount; i++ ) {
			
			double theta = lookingDirection.getTheta() - 180;
			theta += random.nextFloat() * sector - sector/2;
			
			float speed = random.nextFloat() * maxSpeed + minSpeed;
		
			Vector2f velocity = new Vector2f( speed, speed );
			velocity.setTheta( theta );
			
			double radius = 80;
			
			float x = (float)(Math.cos(Math.toRadians(theta)) * (radius));
			float y = (float)(Math.sin(Math.toRadians(theta)) * (radius));
			
			Vector2f pos = new Vector2f( position.x + upperBody.getWidth() / 2 * scale + x,
										 position.y + upperBody.getHeight() / 2 * scale + y);	
				
			boolean isExplosive = false;
			
			if(currentWeapon==BAZOOKA)
				isExplosive = true;			
			
			Projectile proj = new Projectile( this.parent, pos, velocity,
											  bulletTexture, damage, 
											  isExplosive, penerationForce);
			
			((GameWorld)this.parent).addProjectile(proj);			
		}
	}
	
	/**
	 * Renders weapon muzzle flash when weapon is fired
	 */
	private void renderMuzzleFlash() {		
		if( muzzleFlashTimer.getElapsedTime() < muzzlePlashPlayTime ) {		
			double theta = lookingDirection.getTheta() - 180;
			float radius = 150; 
			
			float x = (float)(Math.cos(Math.toRadians(theta)) * (radius)) + 14;
			float y = (float)(Math.sin(Math.toRadians(theta)) * (radius)) + 27;
			
			Image frame = muzzleFlashAnimation.getCurrentFrame();
			frame.setCenterOfRotation( frame.getWidth()/2,  frame.getHeight()/2);
			frame.setRotation((float)theta);		
					
			muzzleFlashAnimation.draw( position.x + x + centerOfRotation.x, position.y + y + centerOfRotation.y );
		}
		else
			muzzleFlashTimer.stop();
	}
		
	/**
	 * Mouse coordinates are relative to camera position.
	 * 
	 * @param x
	 * @param y
	 */
	public void updateMouse( float x, float y, float angle ) {
		if( isPadInUse == false ) {
			lookingDirection.x = position.x+upperBody.getWidth()*scale/2-x;
			lookingDirection.y = position.y+upperBody.getHeight()*scale/2-y;
			lookingDirection.setTheta( lookingDirection.getTheta() - angle );
		}
	}
	
	@Override
	public void render(GameContainer cont, Graphics g) {
		shadow.draw(position.x + 10, position.y +10, (float)scale);
		renderMuzzleFlash();
		upperBody.draw(position.x, position.y, (float)scale);
	
		g.setColor( Color.green );
		g.fillRect( position.x, position.y, 3, 3);
		g.draw(shape);
		
		g.setColor(Color.orange);			
	}

	/**
	 * Experience class keep track player experiences and perks.
	 * Persks are point that player can use to improve his/her character.
	 *  
	 * @author TeeMuki
	 */
	public class Expirience {
		
		/**
		 * Maximum level limit
		 */
		public final static int LEVEL_LIMIT = 100;
		
		/**
		 * Level and exprierence state where player get level
		 */
		private final HashMap<Integer, Integer> levels = new HashMap<Integer, Integer>();
		
		/**
		 * How many percks we have in store
		 */
		private int perksInStorage;
		
		/**
		 * Player current level
		 */
		private int currentLevel;
		
		/**
		 * Player experiences
		 */
		private int playerExpirience;
		
		public Expirience() {
			for( int i=0; i<LEVEL_LIMIT; i++ ) {
				levels.put( i, i * 300 );
			}
			
			reset();
		}
		
		/**
		 * Add more experience
		 * 
		 * @param expirience
		 */
		public void addExpirience( int expirience ) {
						
			if( playerExpirience + expirience > levels.get(currentLevel) ) {
				perksInStorage++;
				currentLevel++;
				
				//Level Up sound!
				Resources.getSound("levelup").play();
			}
			
			playerExpirience += expirience;
		}

		/**
		 * Reset experience system
		 */
		public void reset() {
			playerExpirience = 0;
			currentLevel = 1;
			perksInStorage = 10;
		}
		
		/**
		 * @return Return player perk count
		 */
		public int getPerksCount() {
			return perksInStorage;
		}
		
		/**
		 * @return Return player experiences
		 */
		public int getExpirience() {
			return playerExpirience;
		}
		
		/**
		 * Removes perks
		 */
		public void removePerk( int count ) {		
			perksInStorage -= count;
			
			if( perksInStorage < 0) {
				perksInStorage = 0;
			}
		}
		
		/**
		 * @return Return next level limit
		 */
		public long getExpPointsToNextLevel() {		
			if( currentLevel + 1 < LEVEL_LIMIT)
				return levels.get(currentLevel);
			else
				return -1;			
		}
	}
	
	/**
	 * Weapon class hold information of weapons that player has on use.
	 * @author TeeMuki
	 */
	public class Weapon implements Comparable<Weapon> {
		
		/**
		 * Index in list
		 */
		public int index;
		
		/**
		 * 
		 */
		public int weaponID;
		
		/**
		 * Image resenting weapon in taskbar
		 */
		public Image picture;
		
		/**
		 * Can player use weapon
		 */
		public boolean enabled;
		
		/**
		 * Key where player has bind weapon
		 */
		public int keyBind;
		
		public Weapon( int index, int weaponID, Image picture, boolean isEnabled, int keyBind ) {
			this.index = index;
			this.weaponID = weaponID;
			this.picture = picture;
			this.enabled = isEnabled;
			this.keyBind = keyBind;
		}

		@Override
		public int compareTo(Weapon weapon) {
			if( weapon.index == this.index )
				return 0;
			else if (weapon.index > this.index)
				return -1;
			else
				return 1;
		}
	}
}
