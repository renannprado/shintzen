package fi.left2die.gamestates;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.text.Position;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import fi.left2die.gameobject.IGameObject;
import fi.left2die.gameobject.MovableEntity;
import fi.left2die.gameobject.enviroment.Sparkle;
import fi.left2die.io.Confiqurations;
import fi.left2die.io.Resources;

public class MainMenuState extends BasicGameState{

	private Image title;
	
	private int id;
	
	private Vector2f screenCenterPos = new Vector2f(0, 0);
	
	private int selectedItem = 2; 
	
	private HashMap<String, Rectangle>worldBoundaries = new HashMap<String, Rectangle>();
	private ArrayList<MovableEntity>items = new ArrayList<MovableEntity>();	
		
	private final float MENU_ITEM_MAX_SCALE = 0.8f;
	private final float MENU_ITEM_MIN_SCALE = 0.6f;
	
	private boolean isControllerUsed = false;
	
	private Vector2f centerPos;
	
	private Shape mousePos = new Circle(0,0,1);
	
	private Image backgroundImg;
	
	public MainMenuState( int id ) {
		this.id = id;
	}
	

	@Override
	public int getID() {	
		return id;
	}

	@Override
	public void init(GameContainer cont, StateBasedGame state ) throws SlickException {
		screenCenterPos = new Vector2f( cont.getWidth()/2, cont.getHeight()/2 );
		
		int height = 110;
		
		Image img = Resources.getImage("menuitems play");
		Vector2f position = new Vector2f((cont.getWidth() - img.getWidth()) / 2, height );
		items.add( new MenuItem("play", 2, img , position ));
		
		img =  Resources.getImage("menuitems highscores");
		position = new Vector2f((cont.getWidth() - img.getWidth()) / 2, height * 2 );
		items.add( new MenuItem("highscrores", 3, img, position ));
		
		img = Resources.getImage("menuitems options");
		position = new Vector2f((cont.getWidth() - img.getWidth()) / 2, height * 3 );
		items.add( new MenuItem("options", 4, img, position ));
		
		img = Resources.getImage("menuitems about");
		position = new Vector2f((cont.getWidth() - img.getWidth()) / 2, height * 4 );
		items.add( new MenuItem("about", 5, img, position ));
		
		img = Resources.getImage("menuitems exit");
		position = new Vector2f((cont.getWidth() - img.getWidth()) / 2, height * 5 );
		items.add( new MenuItem("exit", 6, img, position ));		
		
		//Boundaries of the virtual world		
		worldBoundaries.put( "top", new Rectangle(0, 0, cont.getWidth(), 1));
		worldBoundaries.put( "bottom", new Rectangle(0, cont.getHeight(), cont.getWidth(), 1));
		worldBoundaries.put( "left", new Rectangle(1, 0, 1, cont.getHeight()));
		worldBoundaries.put( "right", new Rectangle( cont.getWidth(), 0, 1, cont.getHeight() ));
		
		this.title = Resources.getImage("menu title");
		
		createSparkles( cont.getWidth(), cont.getHeight());
		
		centerPos = new Vector2f( cont.getWidth()/2, cont.getHeight()/2);
		
		backgroundImg = Resources.getImage("mainmenubg");
	}
	
	@Override
	public void render(GameContainer cont, StateBasedGame state, Graphics g) throws SlickException {
		
		backgroundImg.draw(0, 0, cont.getWidth(), cont.getHeight());
		
		title.draw( (cont.getWidth() - title.getWidth())/2, 50 );
		
		for( MovableEntity item : items ) {
			item.render(cont, g);
		}
		
		g.setColor(Color.white);
		g.drawString( "Version: Prototype v 3 alpha delta", 20, cont.getHeight() - 40 );
	}

	@Override
	public void update(GameContainer cont, StateBasedGame state, int delta)throws SlickException {
		
		if(Resources.getMusic("hellmarch").playing()){
			Resources.getMusic("hellmarch").stop();
			Resources.getSound("mainmenu_loaded").play();
		}
		
		Input input = cont.getInput();
		
		
		mousePos.setLocation(input.getMouseX(),input.getMouseY());
		
		if( input.isKeyPressed(Input.KEY_UP ))
			 setSelectedItem( selectedItem - 1) ;
		
		if( input.isKeyPressed(Input.KEY_DOWN ))
			 setSelectedItem( selectedItem + 1) ;

		if( input.isKeyPressed(Input.KEY_F10))
			state.enterState(Main.OPTION_STATE);
		
		
		//Here we check if user is press button1 or enter key to enter to new game state.
		if(input.isButton1Pressed(Input.ANY_CONTROLLER))
			switchGameState(cont, state);
		
		if( input.isKeyPressed(Input.KEY_ENTER) || input.isMousePressed(Input.MOUSE_LEFT_BUTTON) ){
			Resources.getSound("menu_click").play();
			switchGameState(cont, state);
		}
		
		//Menu item scaling
		for( MovableEntity entity : items ) {
			if( entity instanceof MenuItem ) {
				MenuItem item = (MenuItem) entity;
				
				if(mousePos.intersects(item.shape))
					selectedItem = item.index;
				
				if( item.index == selectedItem && item.scale < MENU_ITEM_MAX_SCALE ) {
					item.scale += 0.008;
				}
				else if(item.scale > MENU_ITEM_MIN_SCALE) {
					item.scale -=  0.008;
				}
				
			}
			
			
			if( entity instanceof Sparkle ) {
				String boundary = isHittingBoundaries( entity.shape );
					
				if( boundary != null ) {
					onHittingBoundary( entity, boundary );			
				}			
			}	
			
			entity.update(cont, delta);
		}
	}
		
	@Override
	public void controllerDownReleased(int controller) {
		super.controllerDownReleased(controller);
		
		setSelectedItem( selectedItem + 1 );	
	}
	
	@Override
	public void controllerUpReleased(int controller) {
		super.controllerDownReleased(controller);
		
		setSelectedItem( selectedItem - 1 );
	}
	
	private void switchGameState(  GameContainer cont, StateBasedGame state ) {
		switch (selectedItem) {
		case 2:
			Resources.getSound("mainmenu_loaded").stop();
			
			state.enterState(Main.PLAYING_STATE,new FadeOutTransition(Color.black),
												new FadeInTransition(Color.black)); 
			break;
		case 3:
			Resources.getSound("mainmenu_loaded").stop();
			state.enterState(Main.HIGHSCORE_STATE,new FadeOutTransition(Color.black), 
											  new FadeInTransition(Color.black)); 
			break;
			
		case 4:
			Resources.getSound("mainmenu_loaded").stop();
			state.enterState(Main.OPTION_STATE,new FadeOutTransition(Color.black), 
											  new FadeInTransition(Color.black)); 
			break;
		case 5:
			Resources.getSound("mainmenu_loaded").stop();
			state.enterState(Main.ABOUT_STATE,new FadeOutTransition(Color.black), 
											  new FadeInTransition(Color.black)); 
			break;
		case 6:
			Resources.getSound("mainmenu_loaded").stop();			 			
			try {
				Confiqurations.saveProperies();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cont.exit();
			break;	
		default:
			break;
		}
	}
	
	/**
	 * changes currently selected menuitem
	 * @param item new item
	 */
	private void setSelectedItem( int item ) {
		if( item > 6 )
			selectedItem = 2;
		else if( item < 2 )
			selectedItem = 6;
		else
			selectedItem = item;			
	}
	
	class MenuItem extends MovableEntity implements IGameObject{
		
		public int index;		
		private Image sprite;
		
		public MenuItem( String id, int index, Image sprite, Vector2f position ) {
			super( "Item" + id, null );
			this.index = index;			
			this.sprite = sprite;
			this.position = position;
			this.velocity = new Vector2f(0,0);
			this.scale = MENU_ITEM_MIN_SCALE;
			this.shape = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight() * MENU_ITEM_MIN_SCALE);
		}
		
		@Override
		public void render(GameContainer cont, Graphics g) {
			
			double moveToLeft = (sprite.getWidth() - sprite.getWidth() * scale)/2;
			double moveToUp = (sprite.getHeight() - sprite.getHeight() * scale)/2;
			sprite.draw(position.x + (float)moveToLeft, position.y + (float)moveToUp, (float)scale);
		}
		
		@Override
		public void update(GameContainer cont, int delta) {
			
		}	
	}
	
	private void onHittingBoundary( MovableEntity entity, String boundary ) {
		
		Vector2f vertical = new Vector2f(1,0);
		Vector2f horizontal = new Vector2f(0,1);
		
		
		if( boundary.equals("left"))		
			reflectHit(entity, vertical);
		
		if( boundary.equals("right"))
			reflectHit(entity, vertical);
		
		if( boundary.equals("top"))
			reflectHit(entity, horizontal);
		
		if( boundary.equals("bottom"))
			reflectHit(entity, horizontal);
	
	}
	
	public String isHittingBoundaries( Shape shape ) {	
		
		for (String boundary : worldBoundaries.keySet()) {
			if( worldBoundaries.get(boundary).intersects(shape))
				return boundary;
		}
		
		return null;
	}
	
	
	private void reflectHit( MovableEntity entity, Vector2f n ) {
		double dot = entity.velocity.dot(n) * 2;
		
		n.x *= dot;
		n.y *= dot;
		
		entity.velocity.sub(n);
		
		//Hope that this will prevent objects to stuck to boundaries.
		pushTowardsCenter( entity );
	}
	
	public void pushTowardsCenter( MovableEntity ent ) {
		
		if( ent.shape.getCenterX() < centerPos.x )
			ent.position.x += 2.5;
		else
			ent.position.x -= 2.5;
		
		if( ent.shape.getCenterY() < centerPos.y )
			ent.position.y += 2.5;
		else
			ent.position.y -= 2.5;
		 
	}
	
	private void createSparkles( int width, int height) {
		
		Random random = new Random();
		
		for( int i = 0; i<100; i++) {			
			
			MovableEntity entity = new Sparkle(Resources.getImage("sparkle"),  
											new Vector2f(random.nextFloat() * width, 
											random.nextFloat()*height), false );
			items.add(entity);
		}
		
		Collections.sort(items);
	}
	
}
