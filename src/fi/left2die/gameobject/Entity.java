package fi.left2die.gameobject;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.geom.Shape;

import fi.left2die.library.UniqueID;

/**
 * Base class for game world object. 
 * Basically almost every thing in game world is entity world, enemies, player.
 * 
 * @author TeeMuki
 */
public class Entity implements Comparable<Object> {

	/**
	 * Variables has public modifier for performance reasons
	 */	
	private String id;
	
	/**
	 * Objects parent
	 */
	public Entity parent;
	
	/**
	 * Boundaries of object	
	 */
	public Shape shape;
	
	/**
	 * Angle of rotation on degrees
	 */
	public double rotation = 0;
	
	/**
	 * Rotation speed 
	 */
	public double rotationSpeed = 0;
	
	/**
	 * Sprite size multiplier
	 */
	public float scale = 1;
	
	/**
	 * Tells if entity is on use in the game
	 */
	public boolean isInUse = false;
	
	/**
	 * Top layer 0 and as number grows we are going lower layers 
	 */
	public int layer;

	/**
	 * Children entities
	 */
	private ArrayList<Entity> childrens = new ArrayList<Entity>();
	
	/**
	 * Entity class default constructor 
	 */
	public Entity()
	{	
		this.id = "entity" + UniqueID.get();
	}
	

	/**
	 * Entity class constructor
	 * 
	 * @param id Entity identification string
	 * @param parent Parent entity
	 * @param shape Entity boundaries / shape / position
	 * @param rotation Angle of entity
	 * @param scale Size of entity
	 */
	public Entity(String id, Entity parent, Shape shape,
				  float rotation, float scale ) {
	
		this();
		this.parent = parent;
		this.shape = shape;
		this.rotation = rotation;
		this.scale = scale;
	}

	/**
	 * Entity class constructor
	 * 
	 * @param id Entity identification string
	 */
	public Entity(String id) {
		this.id = id + UniqueID.get();
	}
	
	/**
	 * Method for checking if entity's shape overlaps this.shape
	 * @param entity
	 */
	public boolean intersects( Entity entity ) {
		return this.shape.intersects(entity.shape);
	}
	
	/**
	 * clears children list
	 */
	public void clearChildrens() {
		childrens.clear();
	}
	
	/**
	 * add child entity to list
	 * @param entity
	 */
	public void addChildren( Entity entity ) {
		childrens.add(entity);
	}
	
	/**
	 * removes one child entity
	 * @param entityID id for child
	 */
	public void removeChildren( String entityID ) {
		
		Entity entityToRemove = null;
		
		for( Entity e : childrens ) {
			if( e.id.equals(entityID) )
				entityToRemove = e;
		}
		
		if( entityToRemove != null )
			childrens.remove(entityToRemove);
	}	
	
	/**
	 * Returns the children array
	 * @return list to be returned
	 */
	public List<Entity> getChildrens() {
		return childrens;
	}
	
	/**
	 * @return Returns identification string
	 */
	public String getId() {
		return id;
	}

	/**
	 * Compares entities to each other with comparing entities layer values.
	 */
	@Override
	public int compareTo(Object entity) {
		
		if(((Entity)entity).layer == this.layer )
			return 0;
		else if(((Entity)entity).layer > this.layer )
			return 1;
		else
			return -1;
	}
}
