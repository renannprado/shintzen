package fi.left2die.gameobject.enemies;

import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.Entity;
import fi.left2die.io.Resources;

public class DummyEnemy extends Enemy {

	private Image sprite;
	
	private Color healtColor;
	
	public DummyEnemy( String id, Entity parent, Vector2f position, float damage ) {
		super( "dummyEnemy",parent, damage);

		this.armor = 1;
		this.sprite = Resources.getImage("sahko2");
		this.position = position;
		this.velocity = new Vector2f(0,0);
		this.layer = 5;
		this.scoresFromKilling = 10;
		this.maxHealt = 100;
		this.currentHealt = this.maxHealt;		
		
		this.healtColor = new Color( 1, 1, 1, 
									 1/ this.maxHealt * this.currentHealt  );
		
		this.shape = new Circle( this.sprite.getCenterOfRotationX(),
								 this.sprite.getCenterOfRotationY(), 
								 this.sprite.getWidth()/4 );	
	}

	@Override
	public void render(GameContainer cont, Graphics g) {
		sprite.draw(position.x, position.y, healtColor );
	}

	/**
	 * Updates enemy's opacity. Enemys opacity is relative to it's healt.
	 */
	private void updateOpacity() {
		healtColor.a = 1 / this.maxHealt * currentHealt; 
	}
	
	@Override
	public void onHit() {
		updateOpacity();
	}
	
	@Override
	public void update(GameContainer cont, int delta) {
		position.x += velocity.x;
		position.y += velocity.y;
		shape.setLocation(position.x + this.sprite.getWidth()/4, position.y + this.sprite.getHeight()/4);		
	}
}
