package fi.left2die.gameobject.enviroment;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.Explosion;

public class OilBarrel extends AbstractItem {

	private int defusingTime;
	private int explosionCount;
	
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	
	public OilBarrel(String id, Entity parent, Vector2f position) {
		super(id, parent, position);

		Random random = new Random();
		
		this.defusingTime = random.nextInt() * 2000 + 2000;
		this.explosionCount = random.nextInt() * 5 + 2;
		
		for( int i=0; i<explosionCount; i++ ) {
			explosions.add( new Explosion( this, position, 1f ));
		}
	}		
	
	@Override
	public void render(GameContainer cont, Graphics g) {
		
	}

	@Override
	public void update(GameContainer cont, int delta) {
		
	}

}
