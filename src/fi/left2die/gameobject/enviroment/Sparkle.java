package fi.left2die.gameobject.enviroment;


import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.IGameObject;
import fi.left2die.gameobject.MovableEntity;

/**
 * The Sparkle class is used on mainmenu, those flying blue thinghs there... :)
 * 
 * @author TeeMuki
 */
public class Sparkle extends MovableEntity implements IGameObject{
		
	/**
	 * Texture
	 */
	private Image sprite;
	
	/**
	 * Randomizer for Sparkle class
	 */
	private static Random random = new Random();
	
	/**
	 * Opacity filter
	 */
	private Color filter;
	
	/**
	 * Indicates is rotated to moving direction
	 */
	private boolean rotateToMovingDirection = false;
	
	public Sparkle( Image sprite, Vector2f position, boolean rotateToMovingDirection ) {
		
		super( "bonus", null );
		
		this.sprite = sprite;
		
		velocity.x = (float) random.nextDouble() * 0.1f - 0.05f;
		velocity.y = (float) random.nextDouble() * 0.1f - 0.05f;
		
		this.position = position;
		
		rotationSpeed = (float)random.nextDouble() * 0.8f - 0.4f;;
		
		scale = (float)(random.nextFloat() * 2 - 0.8);
		
		shape = new Ellipse( position.x + sprite.getWidth()/2, 
							position.y +sprite.getWidth()/2, 
						   (float)(20 * scale), 
						   (float)(20 * scale));
	
		layer = 2;
		filter = new Color(255, 255, 255, random.nextInt(255));		
		
		this.rotateToMovingDirection = rotateToMovingDirection;
	}

	/**
	 * Renders sparkle
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {
		sprite.draw(position.x, position.y , (float)scale, filter );		
	}

	/**
	 * Updates sparkle
	 */
	@Override
	public void update(GameContainer cont, int delta) {
		position.x += velocity.x * delta;
		position.y += velocity.y * delta;
	
		shape.setLocation(position.x + 30*scale, position.y+30*scale);
		
		if( rotateToMovingDirection )
			rotation = (float)velocity.getTheta();
		else 
			rotation += rotationSpeed * delta;
		
		sprite.setCenterOfRotation(50*scale, 50*scale);
		sprite.setRotation((float)rotation);
	}

}
