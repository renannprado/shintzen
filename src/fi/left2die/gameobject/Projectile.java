package fi.left2die.gameobject;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.enemies.Enemy;
import fi.left2die.io.Resources;
import fi.left2die.library.UniqueID;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author TeeMuki
 */
public class Projectile extends MovableEntity  {
    
    /**
     * HashMap for fast way to accessing textures
     */
    private static HashMap<Integer,Image>staticTexture;
    
    private ArrayList<Enemy>affectedEnemies=new ArrayList<Enemy>();
    
    //projectile graphics
    private Image texture;
    
    //names for projectile textures
    public static final int TEXTURE_BLUECOMET = 0;
    public static final int TEXTURE_RED_ARROW = 1;
    public static final int TEXTURE_RED_COMET = 2;
    public static final int TEXTURE_LONG_BLUE = 3;
    public static final int TEXTURE_GREEN_SEED = 4;
    public static final int TEXTURE_PURPLE_COMET = 5;
    
    /**
     * How well bullet going trough enemies and armors.
     */
    public short piercing = 0;
    
    /**
     * How much damage it made on impact 
     */
    public float damage = 32;
    
    /**
     * Is projectile an explosive
     */
    public boolean explosive = false;
    
    /**
     * constructor
     * @param parent
     * @param position starting position
     * @param velocity speed
     * @param textureNo graphics
     * @param damage
     */
    public Projectile( Entity parent, Vector2f position, 
		Vector2f velocity, int textureNo, float damage) {

		this( parent, position, velocity, textureNo );
		this.damage = damage;
		this.scale = 1.5f;//texture scale
    }
    
    /**
     * constuctor
     * @param parent
     * @param position starting position
     * @param velocity speed
     * @param textureNo graphics
     * @param damage
     * @param explosive does the projectile explode on contact
     */
    public Projectile( Entity parent, Vector2f position, 
    				   Vector2f velocity, int textureNo, float damage, boolean explosive) {
    	
    	this( parent, position, velocity, textureNo );
    	this.damage = damage;
    	this.explosive = explosive;
    }
    
    /**
     * constructor
     * @param parent
     * @param position starting position
     * @param velocity speed
     * @param textureNo graphics
     * @param damage
     * @param explosive does the projectile explode on contact
     * @param piercingForce how well does the projectile pierce enemies
     */
    public Projectile( Entity parent, Vector2f position, 
			   Vector2f velocity, int textureNo, float damage, boolean explosive, short piercingForce ) {

		this( parent, position, velocity, textureNo, damage, explosive );
		this.damage = damage;
		this.explosive = explosive;
		this.piercing = piercingForce;
	}

    /**
     * constructor
     * @param parent
     * @param position starting position
     * @param velocity speed
     * @param textureNo graphics
     */
    public Projectile( Entity parent, Vector2f position, Vector2f velocity, int textureNo ) {

        super("projectile" + UniqueID.get(), parent);

        this.position = position;
        this.velocity = velocity;

        if( staticTexture == null ) {
            staticTexture = new HashMap<Integer,Image>();
            staticTexture.put( TEXTURE_BLUECOMET, Resources.getImage("bullet blue comet") );
            staticTexture.put( TEXTURE_RED_COMET, Resources.getImage("bullet red comet"));
            staticTexture.put( TEXTURE_GREEN_SEED,Resources.getImage("bullet greed seed") );
            staticTexture.put( TEXTURE_LONG_BLUE, Resources.getImage("bullet long blue") );
            staticTexture.put( TEXTURE_RED_ARROW, Resources.getImage("bullet red arrow") );
            staticTexture.put( TEXTURE_PURPLE_COMET, Resources.getImage("bullet purple comet") );
        }

        this.texture = staticTexture.get( textureNo ).copy();
        
        //Center position
        this.position.x -= texture.getWidth()/2;
        this.position.y -= texture.getHeight()/2;

        //Shape or hitbox
        float radius = ( texture.getWidth() + texture.getHeight())/4;
        this.shape = new Circle( position.x + radius, position.y + radius, radius );
    }

    /**
     * moves the shape
     */
    private void updateShapePosition() {
        this.shape.setLocation( position.x, position.y );
    }

	@Override
	public void render(GameContainer cont, Graphics g) {
        texture.draw( position.x, position.y, (float)scale );
	}
	
	/**
	 * pierced enemies
	 * @param enemy
	 */
	public void addAffectedEnemy( final Enemy enemy ) {
		affectedEnemies.add( enemy );
	}
	
	/**
	 * checks if damage is already made to enemy
	 * @param enemy
	 * @return
	 */
	public boolean isDamageMade( final Enemy enemy ) {
		
		for( int i=0; i<affectedEnemies.size(); i++ ) {
			if( enemy.equals(affectedEnemies.get(i)))
				return true;
		}
		
		return false;
	}

	@Override
	public void update(GameContainer cont, int delta) {
		
    	position.x += velocity.x*delta;//moves the image
        position.y += velocity.y*delta;
        rotation = (float) velocity.getTheta();//gets rotation in degrees
        texture.setRotation((float)rotation);//rotates the image
        
        updateShapePosition();
	}
}
