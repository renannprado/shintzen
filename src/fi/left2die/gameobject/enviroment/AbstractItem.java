package fi.left2die.gameobject.enviroment;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.Explosion;
import fi.left2die.gameobject.MovableEntity;
import fi.left2die.gameobject.enemies.Enemy;
import fi.left2die.library.UniqueID;

/**
 * Abstract base class for all game items int left2die game
 * 
 * @author TeeMuki
 */
public abstract class AbstractItem extends MovableEntity {

	/**
	 * Texture of item
	 */
	protected Image texture;
		
	/**
	 * Last time when was placed on map.
	 */
	protected long lastTimePlacedOnMap=0;
	
	/**
	 * How long item is going to stay on map in milliseconds
	 */
	protected long lifeTime=0;
	
	/**
	 * AbstractItem constructor
	 * 
	 * @param id Identification string
	 * @param parent Parent entity
	 * @param position Object position
	 */
	public AbstractItem( String id, Entity parent, Vector2f position ) {
		super( id + UniqueID.get(), parent );
		this.position = position;
	}
}
