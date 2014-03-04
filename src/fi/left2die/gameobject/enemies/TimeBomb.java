package fi.left2die.gameobject.enemies;

import org.newdawn.slick.Color;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.Entity;
import fi.left2die.io.Resources;

public class TimeBomb extends Enemy {

	private static int staticId = 0;
	
	private long creationTime;
	private long lifeTimeMS;
	
	private float effectRadius;
	
	private boolean isExpoled = false;
	
	private Color color = Color.red;
	
	private Animation explosionAnimation;
	
	public TimeBomb( Entity parent, String id, Vector2f position, long lifeTimeMS ) {
		
		super(id + staticId++,  parent, 0 );
	
		this.position = position;
		this.creationTime = System.currentTimeMillis();
		this.lifeTimeMS = lifeTimeMS;
		this.layer = 6;
		
	}
	
	@Override
	public void onHit() {
			
	}

	@Override
	public void render(GameContainer cont, Graphics g) {
		
	}

	@Override
	public void update(GameContainer cont, int delta) {		
		if( System.currentTimeMillis() > this.creationTime + this.lifeTimeMS ) {
			isExpoled = true;
		}	
		
		
	}	
}
