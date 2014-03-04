package fi.left2die.gameobject.enviroment;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.MovableEntity;
import fi.left2die.io.Resources;
import fi.left2die.library.UniqueID;
/**
 * Healthpacks that are randomly inserted into the map to give player more hitpoints
 * @author Jaska
 *
 */
public class HealthPack extends AbstractItem {
	
	/**
	 * Randomizer for healthpacks
	 */
	private static Random random = new Random();
	
	/**
	 * How much health point pack is containing
	 */
	public float healingPoints = 30;
	
	/**
	 * Last time when health pack was placed on map
	 */
	private long lastInUse = 0; 
		
	/**
	 * Used make health pack transparent when pack enters to map
	 */
	private Color filterColor;
	
	/**
	 * Constructor of Healthpack class
	 * 
	 * @param parent Parent entity
	 * @param position Heatlh pack location
	 */
	public HealthPack( Entity parent, Vector2f position ) {
		super( "healtpack", parent, position );
		
		
		this.rotationSpeed = 0.01f;
		this.texture = Resources.getImage("healtpack");
		this.scale = 0.5f;
		
		this.texture.setCenterOfRotation( texture.getWidth()/2*scale, 
									 texture.getHeight()/2*scale);
		
		//Hitbox
		this.shape = new Circle( position.x + texture.getWidth()/2*scale, 
							position.y + texture.getHeight()/2*scale, 
							texture.getHeight()/2*scale);
		
		//Lifetime of object in milliseconds
		this.lifeTime = 30000;
		
		this.layer = 6;
		
		//Health pack is made transparent with this
		this.filterColor = new Color(1f,1f,1f,0f);
	}
	
	/**
	 * Call this method to indicate that health pack is in use on map. 
	 * 
	 * @param isOnUse Give true if is in use or false if pack is going to stored. 
	 */
	public void setOnUse(boolean isOnUse) {
		this.isInUse = isOnUse;
		this.lastInUse = System.currentTimeMillis();
	}
	
	/**
	 * @return Returns true if pack is in map, else return false
	 */
	public boolean isOnUse() {
		return isInUse;
	}
	
	/**
	 * Set health pack position and reset filterColor opacity
	 * 
	 * @param position Health pack location
	 */
	public void setPosition( Vector2f position ) {
		this.position = position;
		this.shape.setCenterX(position.x + texture.getWidth()/2 * scale);
		this.shape.setCenterY(position.y + texture.getHeight()/2 * scale);
		this.filterColor.a = 0;
	}
	
	/**
	 * Renders health pack
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {
		texture.draw(position.x, position.y, scale, filterColor );
	}

	/**
	 * Updates health pack
	 */
	@Override
	public void update(GameContainer cont, int delta) {
		rotation += rotationSpeed * delta;
		texture.setRotation( (float)rotation );

		//Make health pack come to map slowly fading in. :)
		if( filterColor.a < 1 )
			filterColor.a += 0.001 * delta;
		
		//Life time is used
		if( lastInUse + lifeTime < System.currentTimeMillis() ) {
			this.isInUse = false;
		}
	}
}
