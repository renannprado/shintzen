package fi.left2die.gameobject.enemies;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.GameWorld;
import fi.left2die.gameobject.MovableEntity;
import fi.left2die.gameobject.Projectile;
import fi.left2die.gameobject.enviroment.Map;
import fi.left2die.io.Resources;

public class BasicZombie extends Enemy{

	/**
	 * Zombies texture
	 */
	private Image sprite;
	
	/**
	 * Healthbar color
	 */
	private Color healthColor;
	
	/**
	 * Attack target
	 */
	private Vector2f target;
	
	/**
	 * Image center
	 */
	private Vector2f centerImg;
	
	/**
	 * Determinate how zombie is currently behaving
	 */
	private Behavior behaviorMode;
	
	/**
	 * Helpping variable to make zombie walk away from player when it it's wounded.
	 */
	private float directionCorrection;
	
	public enum Behavior {
		AGRESSIVE,
		DEFENCISE
	}
	
	public BasicZombie(String id, Entity parent, Vector2f position, float damage, final MovableEntity target) {
		super( "BasicZombie",parent, damage);
		
		this.armor = 1;
		this.target = target.position;
		this.sprite = Resources.getImage("zombie");
		this.scale = 0.2f;
		this.position = position;
		this.layer = 5;
		this.scoresFromKilling = 15;
		this.maxHealt = 100;
		this.expirenceFromKill = 3;
		this.currentHealt = this.maxHealt;	
		this.maxSpeed = 0.05f;
		this.velocity = new Vector2f((float)maxSpeed,(float)maxSpeed);
		this.healthColor = new Color( 255-(255*maxHealt/100), 255*maxHealt/100, 1 );
		this.friction = 0.3f;		
		
		this.sprite.setCenterOfRotation( sprite.getWidth()/2 * scale, 
										 sprite.getHeight()/2 * scale);
		
		this.shape = new Circle( this.sprite.getCenterOfRotationX() * scale,
								 this.sprite.getCenterOfRotationY() * scale, 
								 this.sprite.getWidth()/4 * scale );
		
		centerImg = new Vector2f(this.shape.getWidth() * scale / 2, 
								 this.shape.getHeight() * scale / 2);
	}


	
	@Override
	public void onHit() {
		healthColor.r = 1-currentHealt/100;
		healthColor.g = currentHealt/100;
		healthColor.b = 0;		
	}	


	@Override
	public void render(GameContainer cont, Graphics g) {
		
		sprite.draw(position.x - centerImg.x,
					position.y - centerImg.y, scale );		
		
		
		g.setColor(healthColor);
		g.fillRect(position.x, position.y, sprite.getWidth()*scale*currentHealt/100, 5);		
	}

	@Override
	public void update(GameContainer cont, int delta) {
		
		velocity.x = (float)maxSpeed;
		velocity.y = (float)maxSpeed;		
		
		for( Vector2f force : forces ) {
			
			force.x *= friction;
			force.y *= friction;
						
			move( this, force, delta );
		}
		
		//If enemy is hurt it goes to defensife mode
		if( currentHealt / maxHealt < 0.20 ) {
			behaviorMode = Behavior.DEFENCISE;
		}
		
		//Player is on this direction
		float targetAngle = (float)Math.toDegrees(Math.atan2(target.y-position.y,target.x-position.x));
		
		//This make enemy to walk away from player when it it's wounded
		if( behaviorMode == Behavior.DEFENCISE ) {
			if(directionCorrection < 180)
				directionCorrection += 0.1 * delta;
			
			targetAngle += directionCorrection;
		}		
				
		velocity.setTheta(targetAngle);
		
		if(!touchesPlayer){
			
			move( this, this.velocity, delta );			
			
			shape.setLocation(position.x + sprite.getCenterOfRotationX()/2, 
							  position.y + sprite.getCenterOfRotationY()/2);
		}
		
		sprite.setRotation(targetAngle-180);
	}
	/*
	private void move( MovableEntity entity, Vector2f force, int delta){
		
		float newX = entity.position.x + force.x*delta;
		float newY = entity.position.y + force.y*delta;
		
		GameWorld gameWorld = (GameWorld)this.parent;
		
		float shapeWidth = shape.getWidth();
		float shapeHeight = shape.getHeight();
		
		//get monsters new locationtile and check if it's blocked
		if (gameWorld.map.blocked((newX+shapeWidth)/Map.TILESIZE,
								  (newY+shapeHeight)/Map.TILESIZE)) {
			
			if (gameWorld.map.blocked((newX+shapeWidth)/Map.TILESIZE, 
									  (position.y+shapeHeight)/Map.TILESIZE)) {
				
				if (gameWorld.map.blocked((position.x+shapeWidth)/Map.TILESIZE, 
										  (newY+shapeHeight)/Map.TILESIZE)) {

					//cannot move that way
					force.x = 0;
					force.y = 0;
					
				} else {//move y coordinates only
					entity.position.y = newY;
					force.x = 0;
				}
				
			} else {//move x coordinates only
				entity.position.x = newX;
				force.y = 0;
			}
			
		} else {//yay, success!
			entity.position.x = newX;
			entity.position.y = newY;
		}

	}*/
	
	private void move( MovableEntity entity, int delta){
		
		float newX = entity.position.x + entity.velocity.x*delta;
		float newY = entity.position.y + entity.velocity.y*delta;
		
		GameWorld gameWorld = (GameWorld)this.parent;
		
		float shapeWidth = shape.getWidth();
		float shapeHeight = shape.getHeight();
		
		//get monsters new locationtile and check if it's blocked
		if (gameWorld.map.blocked((newX+shapeWidth)/Map.TILESIZE,
								  (newY+shapeHeight)/Map.TILESIZE)) {
			
			if (gameWorld.map.blocked((newX+shapeWidth)/Map.TILESIZE, 
									  (position.y+shapeHeight)/Map.TILESIZE)) {
				
				if (gameWorld.map.blocked((position.x+shapeWidth)/Map.TILESIZE, 
										  (newY+shapeHeight)/Map.TILESIZE)) {

					//cannot move that way
					entity.velocity.x = 0;
					entity.velocity.y = 0;
					
				} else {//move y coordinates only
					entity.position.y = newY;
					entity.velocity.x = 0;
				}
				
			} else {//move x coordinates only
				entity.position.x = newX;
				entity.velocity.y = 0;
			}
			
		} else {//yay, success!
			entity.position.x = newX;
			entity.position.y = newY;
		}

	}
}
