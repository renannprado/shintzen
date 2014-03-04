package fi.left2die.gameobject;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.io.Resources;
import fi.left2die.library.UniqueID;

/**
 *  
 * 
 * @author TeeMuki
 */
public class Explosion extends MovableEntity  {

	public final static int LIFE_TIME = 1800;//ms
	
	public long creationTime;//system time when created
	public float damage;//damage caused by explosion
	public float currentRadius;//radius of shockwave
	public Vector2f position;//position of explosions center
	public Animation animation;//explosion animation
	
	/**
	 * constructor
	 * @param parent
	 */
	public Explosion( Entity parent ) {
		super("id" + UniqueID.get(), parent );
		this.layer = 3;
		this.creationTime = System.currentTimeMillis();
	}
	
	/**
	 * constructor
	 * @param parent
	 * @param position
	 * @param damage
	 */
	public Explosion( Entity parent, Vector2f position, float damage){
		
		this(parent);//Calls Explosion( Entity parent )	
		this.shape = new Circle(position.x, position.y, 0);
		this.damage = damage;
		this.position = position;
		this.isInUse = false;//Indicates is explosion put to action in game arena.
	}
	
	/**
	 * reset explosion
	 */
	public void reset() {
		creationTime = System.currentTimeMillis(); 
		((Circle)shape).setRadius(0);//shockwave radius
		shape.setCenterX(position.x);
		shape.setCenterY(position.y);
		this.isInUse = false;
		this.animation.restart();
		this.animation.stop();		
		this.animation = null;
	}
	
	/**
	 * use as a new explosion
	 * @param position center of explosion
	 */
	public void putToUse( Vector2f position ) {
		animation = Resources.getNextNonPlayingAnimation("explosionanimation");
		animation.start();
		creationTime = System.currentTimeMillis();
		this.position = position;
		shape.setCenterX(position.x);
		shape.setCenterY(position.y);
		this.isInUse = true;
	}
	
	/**
	 * draw animations
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {
		if( animation != null) {
			animation.draw(position.x-animation.getWidth()/2,
						  position.y-animation.getHeight()/2);
		}
	}

	/**
	 * update animation stats
	 */
	@Override
	public void update(GameContainer cont, int delta) {		
		if( isInUse ) {			
			currentRadius = ((Circle)shape).radius;
			currentRadius += 0.05f * delta;//increase shockwave
			((Circle)shape).setCenterX(position.x);
			((Circle)shape).setCenterY(position.y);
			((Circle)shape).setRadius(currentRadius);
		}
	}
}
