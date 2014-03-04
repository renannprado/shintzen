package fi.left2die.gameobject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;


/**
 * MovableEntity is subclass of Entity class. 
 * It is like name says class defines a movable entity.
 * 
 * @author TeeMuki
 */
public abstract class MovableEntity extends Entity implements IGameObject {
	
	/**
	 * Entity max speed
	 */
  	public double maxSpeed = 1;
  	
  	/**
  	 * 1 means no friction and 0 infinite friction, normal friction is something like 0.98
  	 */
  	public double friction = 1;
  		
  	/**
  	 * Item position
  	 */
	public Vector2f position;
	
	/**
	 * Item velocity
	 */
	public Vector2f velocity;//Items speed vector 	
 	
	/**
	 * Forces witch effect to entity
	 */
	public ArrayList<Vector2f>forces = new ArrayList<Vector2f>();
	
	/**
	 * Static help variable to store an entity future position.
	 */
	private static Circle temponaryCircle = new Circle(0, 0, 0);
	
	/**
	 * Movable entity constructor
	 * 
	 * @param id Indentification string
	 * @param parent Parent entity
	 */
 	public MovableEntity( String id, Entity parent )
 	{
 		super(id);
 		this.parent = parent;
 		this.position = new Vector2f(0,0);
 		this.velocity = new Vector2f(0,0);
 	}
 	
 	/**
 	 * Movable entity consturctor
 	 * 
 	 * @param id Identification string
 	 * @param parent Parent entity
 	 * @param rectangle Movable entity boundaries
 	 * @param rotation Entity rotation
 	 * @param scale Entity scale or size
 	 * @param position Entity position
 	 * @param velocity Entity velocity
 	 */
	public MovableEntity(String id, Entity parent, Rectangle rectangle,
						float rotation, float scale, 
						Vector2f position, Vector2f velocity) {
		
		super(id, parent, rectangle, rotation, scale);
		
		this.position = position;
		this.velocity = velocity;
	}	
	
	/**
	 * Checks is player hitting wall and other stuff
	 * 
	 * @param entity
	 * @param force
	 * @param delta
	 */
	protected void move( MovableEntity entity, Vector2f force, int delta){
			
		temponaryCircle.setRadius( ((Circle)shape).getRadius() ); 
		temponaryCircle.setCenterX( shape.getCenterX() + force.x * delta);
		temponaryCircle.setCenterY( shape.getCenterY() + force.y * delta);
		
		GameWorld gameWorld = (GameWorld)this.parent;
				
		ArrayList<Rectangle> blogginTiles = gameWorld.map.intersect(temponaryCircle);
		
		if( blogginTiles.size() > 0 ) {
			
			//Looking is there way to go up or down
			temponaryCircle.setCenterX( shape.getCenterX() - force.x * delta );
			
			if( howManyIntersects(temponaryCircle, blogginTiles ) == 0 ) {
				force.x = 0;
				entity.position.y += force.y * delta;			
			}
			
			//And here we look way to go left or right
			else {				
				temponaryCircle.setCenterX( shape.getCenterX() + force.x * delta);				
				temponaryCircle.setCenterY( shape.getCenterY() - force.y * delta);
				
				if(howManyIntersects(temponaryCircle, blogginTiles ) == 0) {
					force.y = 0;
					position.x += force.x * delta;					
				}						
			}			
		}
		else
		{
			entity.position.x += force.x * delta;
			entity.position.y += force.y * delta;
		}
	}
	
	private int howManyIntersects( Shape shape, List<Rectangle> blokkedTiles ) {
		int count = 0; 
		
		for( Rectangle tile : blokkedTiles ) {
			if( temponaryCircle.intersects(tile) )
				count++;
		}
		
		return count;
	}
}
