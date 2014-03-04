package fi.left2die.gameobject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;
import java.util.Random;

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
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import fi.left2die.gameobject.enemies.BasicZombie;
import fi.left2die.gameobject.enemies.Enemy;
import fi.left2die.gameobject.enviroment.HealthPack;
import fi.left2die.gameobject.enviroment.Map;
import fi.left2die.io.Confiqurations;
import fi.left2die.io.Resources;
import fi.left2die.library.EntityManager;
import fi.left2die.library.TextRenderer;
import fi.left2die.library.TimeCounter;
import fi.left2die.library.TextRenderer.TextBehavior;
import fi.left2die.model.KeyboardConfig;
import fi.left2die.model.PadConfig;
import fi.left2die.model.PerkConsimingChoise;
import fi.left2die.ui.PerkButton;
import fi.left2die.ui.RadioButtonBox;
import fi.left2die.ui.Slider;
import fi.left2die.ui.Button.ButtonEvent;
import fi.left2die.ui.Button.ButtonListener;
import fi.left2die.ui.RadioButtonBox.RadioButtonBoxListener;
import fi.left2die.ui.RadioButtonBox.RbuttonEvent;
import fi.left2die.ui.Slider.SliderFormat;

/**
 * This is class with holds most of left2die game play logic. It is entity like player, enemies and items. 
 * 
 * @author TeeMuki
 */
public class GameWorld extends Entity implements IGameObject, RadioButtonBoxListener, ButtonListener {


	/**
	 * Storage for all movableEntites that are currently on the map/world. 
	 */
	private ArrayList<MovableEntity>gameObjects = new ArrayList<MovableEntity>();
	
	/**
	 * Storage for all projectiles that are currently on the map/world. The projectiles
	 * are stored also to gameObjects list.
	 */
	private ArrayList<Projectile>projectiles = new ArrayList<Projectile>();
	
	/**
	 * Storage for all enemies that are currently on the map/world. The enemies
	 * are stored also to gameObject list.
	 */
	private ArrayList<Enemy>enemies = new ArrayList<Enemy>();
	
	/**
	 * Storage for reusable entities 
	 */
	private EntityManager entityManager = new EntityManager();
	
	/**
	 * Character that player can control
	 */
	private Player playerOne;
	
	/**
	 * Basically this effects only how many enemies are created each wave
	 */
	private int difficultyMultiplier;
	
	/**
	 * Camera area 
	 */
	private Rectangle cameraArea;
	
	/**
	 * Left upper corner of camera
	 */
	private Vector2f cameraPosition;
	
	/**
	 * Area where new enemies are created
	 */
	private Rectangle spawArea;

	/**
	 * Color for health, point, and playing time text.
	 */
	private Color statusTextColor;
	
	/**
	 * Starting time of game
	 */
	private long startTime;
	
	
	private UnicodeFont font;
	
	/**
	 * Keyboard configurations
	 */
	private KeyboardConfig keyConfigurations;
	
	/**
	 * Game controller configurations
	 */
	private final PadConfig padConf = new PadConfig(1, 1, 0, 3, 2, 0.3f);
	
	/**
	 * World wall images
	 */
	private HashMap<String, Image> walls = new HashMap<String,Image>();
	
	/**
	 * Camera angle
	 */
	private float cameraAngle = 0;

	/**
	 * Shake the screen control flag
	 */
	private boolean shake = false;

	private Random random = new Random( System.currentTimeMillis() );
	
	/**
	 * This keep track for us all the emitters on the world
	 */
	private ParticleSystem particleSystem; 
	
	/**
	 * Storage list for emitters
	 */
	private ArrayList<ConfigurableEmitter> emitters = new ArrayList<ConfigurableEmitter>();
	
	/**
	 * Next emitter to select from emitter list
	 */
	private int emitterIndex = 0;
	
	/**
	 * Radio button box for selecting and consuming perks that player get killing enemies.
	 */
	private RadioButtonBox radioBtnBox;
	
	/**
	 * By pressing this button user can consume his perks
	 */
	private PerkButton perkBtn;
		
	/**
	 * All different choices where you can use your perks :)
	 */
	private ArrayList<PerkConsimingChoise> perkConsumingChoises = new ArrayList<PerkConsimingChoise>();
			
	/**
	 * Map number
	 */
	private int mapNumber = 1;
	
	/**
	 * Current amount of maps created for the game
	 * Used for testing purposes
	 */
	private int mapCount = 3;
	
	/**
	 * Map
	 */
	public Map map;
	
	/**
	 * Variable for informing loading breaks between levels
	 */
	public boolean levelChanging;
	
	/**
	 * Health bar slider
	 */
	private Slider healthBar;
	
	private final TimeCounter counter;
	
	/**
	 * Used to render text with many different effects
	 */
	private TextRenderer textRendered;
		
	
	private Image flashlight;
	
	/**
	 * GameWorld constructor
	 * 	
	 * @param cont GameContainer for screen width and height
	 * @throws SlickException
	 */
	public GameWorld( GameContainer cont, final TimeCounter timeCounter ) throws SlickException {
			
		this.counter = timeCounter;		
 
		font = Resources.getFont("sketch font small");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
			
		cameraPosition = new Vector2f(0,0);
		
		loadKeyboarConfigurations();
		
		playerOne = new Player(this, 
					   new Vector2f(100,100), 
					   new Vector2f(0,0), padConf, keyConfigurations );
				
		loadMap(mapNumber);
						
		difficultyMultiplier = 10;
			
		statusTextColor = new Color(255,255,255);
		
		startTime = System.currentTimeMillis();
		
		cameraArea = new Rectangle( 10, 10, cont.getWidth()-20, cont.getHeight()-20);

		//Here we create health packs to memory 
		List<Entity>healthpacks = new ArrayList<Entity>();

		for( int i=0; i<10; i++ ) {
			healthpacks.add( new HealthPack(this, new Vector2f(0,0)) );
		}
		
		entityManager.add("healthpacks", healthpacks );

		//Here we create explosions to memory
		List<Entity>explosions = new ArrayList<Entity>();
				
		for( int i=0; i<100; i++) {
			explosions.add(new Explosion( this, new Vector2f(0,0), 1f));		
		}
		
		entityManager.add("explosions", explosions );
		
		Collections.sort( gameObjects );
		createRadioBtnBox();
				
		radioBtnBox.centerize( cont.getWidth(), cont.getHeight() );
		radioBtnBox.addListener(this);		
		
		perkBtn = new PerkButton( new Vector2f( cont.getWidth() - 50, 40 ));
		perkBtn.addListener(this);
		cont.getInput().addListener(perkBtn);		
		
		healthBar = new Slider(this, new Vector2f( 10, cont.getHeight() - 30 ), SliderFormat.PERCENT );
		healthBar.setMin(0);
		healthBar.setMax( playerOne.vitality );
		healthBar.setValue( healthBar.getMax() );
		
		textRendered = new TextRenderer(new Integer(Confiqurations.getProperty(Confiqurations.FPS)));
		
		particleSystem = new ParticleSystem( Resources.getImage("sparkle"));
		
		emitters = Resources.getEmitters("sand emitters");
		
		flashlight = Resources.getImage("light");
		flashlight.setCenterOfRotation( flashlight.getWidth()/2, flashlight.getHeight()/2 );
		flashlight.setAlpha(0.8f);
	}
	
	/**
	 * Creates radio button box
	 * @throws SlickException
	 */
	private void createRadioBtnBox() throws SlickException {

		//Creates radiobuttonbox dialog
		radioBtnBox = new RadioButtonBox( new Vector2f(0,0), Resources.getFont("textfield font") );
		
		//Choice 'heal me'
		String text = "Heal me! (1 perk)";
		
		long id = radioBtnBox.addChoice(text);
		
		//Radiobuttonbox idex is given to perkConsumingchoice
		PerkConsimingChoise choise = new PerkConsimingChoise( 2, "Heal me!", 1, id ) {
			@Override
			public void consumePerk(Player player) {
				player.health = player.vitality;
			}
		};
		
		perkConsumingChoises.add(choise);
		
		//Shotgun
		text = "New Weapon Shogun (3 perks)";
		
		id = radioBtnBox.addChoice(text);
		
		choise = new PerkConsimingChoise(3, text, 1, id ) {
			
			@Override
			public void consumePerk(Player player) {
				player.addWeapon( Player.SHOTGUN );
			}
		};
		
		perkConsumingChoises.add(choise);
		
		//Machigun or ak
		text = "New Weapon Machingun (4 perks)";
		id = radioBtnBox.addChoice(text);
		
		choise = new PerkConsimingChoise(4,text,2, id) {
			
			@Override
			public void consumePerk(Player player) {
				player.addWeapon( Player.AK );
			}
		};
		
		perkConsumingChoises.add(choise);
				
		//Machigun or ak
		text = "New Weapon Minigun (5 perks)";
		id = radioBtnBox.addChoice(text);
		
		choise = new PerkConsimingChoise(5,text,1, id) {
			
			@Override
			public void consumePerk(Player player) {
				player.addWeapon( Player.MINIGUN );
			}
		};
		
		perkConsumingChoises.add(choise);
		
		text = "New Weapon Bazooka (10 perks)";
		id = radioBtnBox.addChoice(text);
		
		choise = new PerkConsimingChoise(10,text,1, id) {
			
			@Override
			public void consumePerk(Player player) {
				player.addWeapon( Player.BAZOOKA );
			}
		};
		
		perkConsumingChoises.add(choise);
	}
	
	/**
	 * This is method is called when game enters to PlayingState.
	 * 
	 * @param cont
	 */
	public void enter( GameContainer cont ) {
		cont.getInput().addListener( radioBtnBox.getAcceptButton() );
	}
	
	/**
	 * This method is called when game leaves from PlayingState 
	 * 
	 * @param cont
	 */
	public void leave( GameContainer cont ) {
		cont.getInput().removeAllListeners();
	}
	
	/**
	 * Method is used to load and reload keyboard configurations from file. Method is called from PlayingState class
	 * when player re-enters to game from the main menu.
	 */
	public void loadKeyboarConfigurations() {
		
		/**
		 * If keyConfigurations are all ready initialized we can load a new configurations from file
		 * without losing references to old keyConfiguration object, just bypassing this initialization.
		 */
		if( keyConfigurations == null )
			keyConfigurations = new KeyboardConfig();
		
		try {		
			keyConfigurations.load( new File( Confiqurations.getProperty(Confiqurations.KEYBOARD_CONF_FILE) ) );			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load map to memory 
	 *  
	 * @param container
	 * @throws SlickException
	 */
	private void loadMap( int mapNumber ) throws SlickException{
		enemies.clear();
		projectiles.clear();
		gameObjects.clear();
		playerOne.position.x=100;//back to starting point
		playerOne.position.y=100;
		gameObjects.add(playerOne);
		
		map = new Map("gamedata/maps/map"+mapNumber+".tmx");
		shape = new Rectangle( 0, 0, Map.mapWidth, Map.mapHeight );

		spawArea =  new Rectangle(50, 50, shape.getWidth() - 100, shape.getHeight() - 100);
		
	}
	

	/**
	 * This is used to display an emitter effect.
	 * 
	 * @param position Position on world
	 * @param velocity Not implemented
	 */
	private void addEmitter( Vector2f position, Vector2f velocity ) {	
		
		if( emitters.size() - 1 <= emitterIndex ) {
			emitterIndex = 0;
			particleSystem.removeAllEmitters();	
		}
				
		ConfigurableEmitter emitter = emitters.get(emitterIndex++);
		
		emitter.resetState();
		emitter.setPosition(position.x, position.y);
		emitter.speed.setMin(30);
		emitter.speed.setMax(90);		   
		
		particleSystem.addEmitter(emitter);
	}
	
	private void playRandomScream() {
		final int max = 5;		
		Resources.getSound("zombie scream" + (1+random.nextInt(max))).play();
	}
	
	/**
	 * Reset game back to starting state
	 * @throws SlickException
	 */
	public void resetGame() throws SlickException {
		
		enemies.clear();
		projectiles.clear();
		gameObjects.clear();
		
		cameraPosition = new Vector2f(0,0);
		
		playerOne.resetPlayer();	
		
		gameObjects.add( playerOne );
		
		difficultyMultiplier = 10;
		
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * This method is called when player shoots from Player object.
	 * 
	 * @param proj The projectile to be added gameObject and projectile lists.
	 */
	public void addProjectile( Projectile proj ) {
		gameObjects.add(proj);
		projectiles.add(proj);
	}
	
	/**
	 * Methods draw game world walls
	 */
	private void drawWalls() {
		
		float tileWidth = 98;
		float tileHeight = 98;
				
		int verticalTilesCount = (int) ( shape.getWidth() / tileWidth);
		int horizontalTilesCount = (int) (shape.getHeight() / tileHeight);
		
		//Top left corner
		walls.get("lefttop").draw(0, 0);
		
		//Top section
		for( int x=1; x<verticalTilesCount; x++ )
			walls.get("top").draw(98*x, 0);
		
		//Top right
		walls.get("righttop").draw( (verticalTilesCount ) * tileWidth, 0);
		
		//Left section
		for( int y=1; y<verticalTilesCount; y++ )
			walls.get("left").draw(0,y*tileHeight);
		
		//Right section
		for( int y=1; y<verticalTilesCount; y++ )
			walls.get("right").draw( (horizontalTilesCount) * tileWidth,y*tileHeight);
		
		//Left bottom
		walls.get("leftbottom").draw(0,tileHeight*verticalTilesCount);
		
		//Bottom
		for( int x=1; x<horizontalTilesCount; x++ )
			walls.get("bottom").draw(x*tileWidth, verticalTilesCount * tileHeight );
		
		//Right bottom
		walls.get("rightbottom").draw(tileWidth * horizontalTilesCount,tileHeight * horizontalTilesCount);
	}
	
	/**
	 * Returns next free explosion from arraylist and marks it reserved. 
	 * 
	 * @param position
	 * @return Returns next free explosion from arraylist, 
	 * 		   if there is no free explosion null is returned.
	 */
	private Explosion getNextFreeExplosion( Vector2f position ) {
	
		Resources.getSound("explosion").play();
		
		List<Entity>explosions = entityManager.getEntities("explosions");
		
		Explosion exp = null;
		
		for( Entity e : explosions ) {
						
			if( e.isInUse == false ) {
				exp = (Explosion)e;
				exp.putToUse( position );
				break;
			}
		}
				
		return exp;
	}
	
	/**
	 * Check if player is trying to go outside of map
	 */
	private void checkBoundaries( MovableEntity entity ) {	
		
		final float amount = 0.007f;
		final Vector2f centerPos = new Vector2f( shape.getWidth()/2, shape.getHeight()/2 );	
		
		if( 0 > (entity.shape.getCenterX() - entity.shape.getWidth()/2) ) {						
			entity.velocity.x += amount;			
		}
		
		if( 0 > (entity.shape.getCenterY() - entity.shape.getHeight()/2) ) {
			entity.velocity.y += amount;
		}
		
		if( shape.getWidth() < (entity.shape.getCenterX() + entity.shape.getWidth()/2) ) {						
			entity.velocity.x -= amount;		
		}
		
		if( shape.getHeight() < (entity.shape.getCenterY() + entity.shape.getHeight()/2) ) {
			entity.velocity.y -= amount;
		}
	}
	
	/**
	 * Check projectile collisions and looks for are pickuping healthpacks and other items. 
	 */
	private void checkCollisions() {
		
		
		for( int i=0; i<projectiles.size(); i++) {
			
			Projectile proj = projectiles.get(i);
			
			//Projectile is removed from game, if this is true
			boolean removeProjectile = false;
			
			//Checks are projectile inside the gameworld
			if( intersects(proj) == false )
				removeProjectile = true;						
						
			//projectile might already be outside the map area		
			if( !removeProjectile && map.blocked(proj.position.x/ Map.TILESIZE, proj.position.y/Map.TILESIZE))
				removeProjectile = true;
						
			//Check damage all enemies that projectile is hitting
			for( int j=0; j<enemies.size(); j++) {
							
				Enemy enemy = enemies.get(j);
				
				if( !proj.isDamageMade(enemy) && enemy.shape.intersects(proj.shape)) {					
					
					enemy.currentHealt -= proj.damage;
					enemy.onHit();//Updates enemy's status
					
					//This make enemy to stop when a projectile hit to them
					enemy.forces.add( new Vector2f( proj.velocity) );
					
					textRendered.addText( new Integer((int)proj.damage).toString(), 
										 new Vector2f( enemy.position ), 
										 TextBehavior.SLIDING_UP_SWINNING, 
										 new Color(Color.red), 
										 random.nextInt(3000) + 1000  );
					
					proj.piercing -= enemy.armor;
					proj.damage *= 0.75;//Projectiles damage is reduced 25% on hitting enemy
					
					//If enemy has less than zero health, enemy = dead :]
					if( enemy.currentHealt < 0 ) {
						playerOne.score+=enemy.scoresFromKilling;
						playerOne.expirience.addExpirience(enemy.expirenceFromKill);
						placeHealtpackOnMap(new Vector2f(enemy.position));
						
						//This renders scores from kill to screen 
						textRendered.addText( new Integer( (int)enemy.scoresFromKilling).toString(), 
											 new Vector2f( enemy.position), 
											 TextBehavior.SLIDING_UP_SWINNING, 
											 new Color(Color.blue), 3000 );
						
						enemies.remove(enemy);
						gameObjects.remove(enemy);
						j--;
						
						//Enemies make some noise when a projectile hit to them
						playRandomScream();											
					}
					
					addEmitter( new Vector2f(proj.position),  new Vector2f(proj.velocity));
					proj.addAffectedEnemy(enemy);
				}
			}
			
			//Projectile is lost its penetration power.
			if( proj.piercing <= 0 )
				removeProjectile = true;
			
			if( removeProjectile ) {

				//If projectile is an explosive we are placing ten explosions to map
				if(proj.explosive) {
					
					for( int e=0; e<10; e++ ) {					
						
						float x = random.nextFloat() * 200 - 100;
						float y = random.nextFloat() * 200 - 100;
						
						Vector2f position = new Vector2f( proj.position.x + x,
														proj.position.y + y );
						
						//Get next free explosion from stack
						Explosion exp = getNextFreeExplosion(position);
						
						gameObjects.add(exp);
					}
				}
				
				//Play the blood effect
				if( proj.explosive == false )
					addEmitter( new Vector2f(proj.position ), proj.velocity);
								
				projectiles.remove(i);
				gameObjects.remove(proj);
				
				i--;		
			}
		}
		
		List<Entity>healthpacks = entityManager.getEntities("healthpacks");
		
		//Check if player is hitting to a health pack		
		for( int i=0; i< healthpacks.size(); i++ ) {
			
			HealthPack pack = (HealthPack)healthpacks.get(i);
			
			//If health pack life time is up
			if( pack.isOnUse() == false ) {
				pack.setOnUse(false);
				gameObjects.remove(pack);
			}
						
			//If player hit to the health pack player gets the healingPoints from the pack.
			if( pack.isOnUse() && pack.shape.intersects(playerOne.shape) ) {
				
				if( playerOne.health + pack.healingPoints > playerOne.vitality )
					playerOne.health = playerOne.vitality;
				else
					playerOne.health += pack.healingPoints;
				
				pack.setOnUse(false);
				
				//Play pickup sound
				Resources.getSound("pickup").play(1, 0.4f);
				
				//We are only removing health pack from gameobject list, not from the game.
				gameObjects.remove(pack);
			}				
		}
	}
	

	/**
	 * Places a healthpack to map with 5% probability after enemy dies.
	 * 
	 * @param position Place where the health pack should appear.
	 */
	private void placeHealtpackOnMap( Vector2f position ) {
		
		final float probabilityPercent = 0.05f;	 
		
		if( random.nextFloat() < probabilityPercent ) {
		
			HealthPack pack = null;
			
			List<Entity>healthPacks = entityManager.getEntities("healthpacks");
			
			//Search next free health pack
			for( Entity ent : healthPacks ) {
				
				HealthPack pack2 = (HealthPack)ent;
				
				if( pack2.isOnUse() == false ) {
					pack = pack2;
					pack.setPosition(position);
					break;
				}
			}
			
			//If there is free health pack it's placed to map
			if( pack != null ) { 
				pack.setOnUse(true);
				gameObjects.add(pack);
			}
			
			//Short game layers
			Collections.sort(gameObjects);
		}
	}
		
	/**
	 * Iterates trough enemies and look are enemies touching a player.
	 * Iterates trough explosions and look are enemies under explosion effect area.
	 */
	private void checkDamage(){
		
		//Looks if enemy is touching a player
		for( int j=0; j<enemies.size(); j++) {			
			Enemy enemy = enemies.get(j);
			
			if(enemy.shape.intersects(playerOne.shape)){
				playerOne.health-=enemy.damage;
				enemy.touchesPlayer=true;
			}
			else
				enemy.touchesPlayer=false;
		}
		
		List<Entity>explosions = entityManager.getEntities("explosions");
		
		//Here we are looking explosion damage to enemies
		for( int i=0; i<explosions.size();i++){
			
			Explosion explosion = (Explosion)explosions.get(i);
			
			if( explosion.isInUse ) {
				
				for(int j=0;j<enemies.size();j++){
				
					Enemy enemy = enemies.get(j);
					
					if(explosion.shape.intersects(enemy.shape)) {
						enemy.currentHealt -= explosion.damage;
	
						
						playRandomScream();
					}
					
					//Enemies are removed from game if enemy is dead
					//Player get killing scores :)
					if( enemy.currentHealt <= 0 ){
												
						playerOne.score += enemy.scoresFromKilling;
						
						textRendered.addText( new Integer( (int)enemy.scoresFromKilling).toString(), 
											 new Vector2f( enemy.position), 
											 TextBehavior.SLIDING_UP_SWINNING, 
											 new Color(Color.green), 3000 );
						
						gameObjects.remove(enemy);
						enemies.remove(enemy);
						

						
						j--;
					}
				}
				
				//If explosion has achieved it animation duration it will be removed from gameObject
				//and will be marked for reuse.
				if((System.currentTimeMillis()-explosion.creationTime)>Explosion.LIFE_TIME){
					gameObjects.remove(explosion);
					explosion.reset();
				}
			}
		}		
	}
	
	public Player getPlayerOne() {
		return playerOne;
	}
	
	/**
	 * Tells you is player dead or alive
	 * 
	 * @return If player is dead returns true, if is alive false is returned.
	 */
	public boolean isGameEnded() {
		if( playerOne.health < 0 ) {
			return true;
		}
		else
			return false;		
	}
	
	/**
	 * Creates enemies
	 * 
	 * @param count How many enemies are created
	 */
	private void createEnemies( int count ) {
			
		Random random = new Random();	
		
		for( int i=0; i<count; i++ ) {			

			//Margin make sure that enemy is not spawned outside off the map
			float margin = 50;

			//Position is some where in map are
			Vector2f position = new Vector2f( random.nextFloat() * spawArea.getWidth(), 
											  random.nextFloat() * spawArea.getHeight());

			//This make sure that enemy don't spawn to close player
			if(cameraArea.contains(position.x, position.y) || map.blocked(position.x/Map.TILESIZE, position.y/Map.TILESIZE))
				count++;		
			else{				
				BasicZombie enemy = new BasicZombie("enemy", this, 
													position, 0.2f, 
													playerOne );
				
			
				enemies.add(enemy);
				gameObjects.add(enemy);
			}
		}
		
		//Sort the gameObjects by layer
		Collections.sort(gameObjects);
	}	
	
	@Override
	public void render(GameContainer cont, Graphics g) {
		
		map.render((int)(-playerOne.shape.getMinX() + cont.getWidth()/2), 
				   (int)(-playerOne.shape.getMinY() + cont.getHeight()/2));
		
			
		
		g.setFont(font);
		
		g.rotate( cont.getWidth()/2, cont.getHeight()/2, cameraAngle);
		
		//Centers camera to player
		g.translate(cameraPosition.x, 
					cameraPosition.y);
		
		g.setColor(Color.red);
		map.renderBlockedTiles(g);
		
		g.setColor(Color.black);
				
		for( MovableEntity object : gameObjects ) {
			object.render(cont, g);
		}
		
		particleSystem.render();
		
		textRendered.render(cont, g);
		
		flashlight.draw( playerOne.position.x - flashlight.getWidth()/2 + playerOne.shape.getWidth()/2, 
						 playerOne.position.y - flashlight.getHeight()/2+ playerOne.shape.getHeight()/2 );
		
		//Here we reset coordinates transform
		g.resetTransform();			
		
		playerOne.drawTaskBar(20, cont.getHeight() - 75, g);
		
		drawStatusText(g);
		
		healthBar.render(cont, g);
		
		radioBtnBox.render(cont, g);
		
		if( playerOne.expirience.getPerksCount() > 0 ) {
			perkBtn.render(cont, g);
		}		
	}
		
	/**
	 * Draw status text
	 * 
	 * @param g
	 */
	private void drawStatusText( Graphics g ) {
		
		final NumberFormat formatter = new DecimalFormat("#0.0");
		final Color statusBg = new Color( 0,0,0, 120 ); 
		
		String statusText = "Score: "+playerOne.score
							+"\nTime: " + formatter.format(counter.timeElapsedInSeconds())+" s"
							+"\nExpirience " + playerOne.expirience.getExpirience() 
							+ "/" + playerOne.expirience.getExpPointsToNextLevel()
							+"\nPerks " + playerOne.expirience.getPerksCount();

		float textWidth = font.getWidth( statusText );
		float textHeight = font.getHeight( statusText );
		
		g.setColor( Color.white );
		g.setLineWidth( 2.5f );
		g.drawRoundRect( 10, 10,  textWidth + 120 , textHeight + 20, 15 );
		
		g.setColor( statusBg );		
		g.fillRoundRect( 10, 10,  textWidth + 120 , textHeight + 20, 15 );
		
		g.setColor( statusTextColor );
		g.drawString( statusText ,20, 20);
	}
	
	public float getPlayTime() {
		return (System.currentTimeMillis()-startTime) / 1000; 
	}
	
	/**
	 * Draws grid given area
	 * 
	 * @param g
	 * @param rec Area where grid will be drawn
	 * @param gridWidth How width is single cell
	 */
	private void drawGrid(Graphics g, Rectangle rec,  float gridWidth) {
		for( float x=rec.getX(); x<rec.getWidth(); x += gridWidth ) {
			g.drawLine(x, 0, x, rec.getHeight());
		}
		
		for( float y=rec.getY(); y<rec.getHeight(); y += gridWidth ) {
			g.drawLine(0, y, rec.getWidth(), y);
		}
	}
	
	/**
	 * Witch way camera is turning now
	 */
	private boolean turningCameraClockwise= false;

	/**
	 * Function can shake camera
	 * 
	 * @param delta
	 */
	private void shakeCamera( int delta ) {
		
		final float shakeSector = 5;
		final float shakeSpeed = 0.08f;
		
		if( cameraAngle < -shakeSector )
			turningCameraClockwise = true;
		
		if( cameraAngle > shakeSector )
			turningCameraClockwise = false;
					
		if( turningCameraClockwise ) {
			cameraAngle += shakeSpeed * delta;
		}
		else
			cameraAngle -= shakeSpeed * delta;
	}
	
	
	@Override
	public void update(GameContainer cont, int delta) {
		
		cameraArea.setCenterX(playerOne.position.x);
		cameraArea.setCenterY(playerOne.position.y);
		
		if(playerOne.expirience.getExpirience()>300*mapNumber && mapNumber<mapCount){
			mapNumber++;
			levelChanging=true;
			try {
				loadMap(mapNumber);
			} catch (SlickException e) {
				System.out.println("Failed to load map");
			}		
			finally{
				levelChanging=false;
			}
		}
				
		Input input = cont.getInput();
		
		if( !radioBtnBox.isEnabled() ) {
			
			particleSystem.update(delta);
			
			if(enemies.size()<2*difficultyMultiplier){
				createEnemies( difficultyMultiplier*3 );
				difficultyMultiplier++;
			}							
			
			if(input.isKeyDown( Input.KEY_T ))
				shakeCamera(delta);			
			
			if( input.isKeyPressed(Input.KEY_F12)) {
				try {
					resetGame();
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}					
						
			if( !cont.isPaused() ) {
			
				for( int i=0; i<gameObjects.size(); i++) {
					MovableEntity object = gameObjects.get(i);
					object.update(cont, delta);
				}
						
				cameraPosition.x = -playerOne.position.x + cont.getWidth()/2;
				cameraPosition.y = -playerOne.position.y + cont.getHeight()/2;
			
				playerOne.updateMouse( input.getMouseX() - cameraPosition.x, 
									   input.getMouseY() - cameraPosition.y, 
									   cameraAngle );
				
				checkDamage();
				checkCollisions();

				perkBtn.update(cont, delta);
						
			}					
		}
		
		if( input.isKeyPressed(Input.KEY_F11) && playerOne.expirience.getPerksCount() > 0 ) {
			radioBtnBox.setEnabled(true);
		}
		
		healthBar.setValue( playerOne.health );
		healthBar.update(cont, delta);
				
		radioBtnBox.update(cont, delta);
		
		textRendered.update(cont, delta);
		
		flashlight.setRotation( (float)playerOne.rotation );
	}

	/**
	 * This method is fired when radio button box dialog accept button is pressed. 
	 */
	@Override
	public void radioButtonBoxEvent(RbuttonEvent event) {
				
		//Where event was fired
		long id = event.getRadioBtnItemID();
				
		//Look witch way perk are consumed
		for( int i=0; i<perkConsumingChoises.size(); i++ ) {
				
			boolean removeChoiseFromList = false;
		
			PerkConsimingChoise choise = perkConsumingChoises.get(i); 
			
			if( choise.getRadioBtnBoxItemID() == id ) {
			
				//Uses perks
				if(choise.getPerkCost() <= playerOne.expirience.getPerksCount() ) {
				
					choise.consumePerk(playerOne);
					
					//Removes n count of perks from storage
					playerOne.expirience.removePerk( choise.getPerkCost() );
					
					removeChoiseFromList = true;
				}
			}
			
			if( removeChoiseFromList ) {
				radioBtnBox.removeChoice( choise.getRadioBtnBoxItemID() );
				perkConsumingChoises.remove(i);
				i--;
			}
		}						
		
		//Closes radio button dialog
		radioBtnBox.setEnabled(false);
	}

	/**
	 * Method is called when perk button is pressed
	 */
	@Override
	public void buttonPressed(ButtonEvent event) {
		radioBtnBox.setEnabled(true);
	}
}
