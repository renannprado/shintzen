package fi.left2die.gameobject.enviroment;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.MovableEntity;
import fi.left2die.io.Resources;

public class GearPair extends MovableEntity {

	private Gear smallGear, bigGear;
	
	public GearPair(Entity parent, float gearsRotationSpeed, Vector2f position, float scale ) {
		super("GearPair", parent);
		
		this.position = position;
		
		
		
		Random random = new Random();
		
		this.rotationSpeed = (random.nextDouble()-0.5) / 100;
		
		int color = random.nextInt(255);
		
		Color filterColor = new Color( color, color, color, random.nextInt(200) );
		
		Image smallGearImage =  Resources.getImage("small gear");
		smallGearImage.setCenterOfRotation( smallGearImage.getWidth()/2*scale, smallGearImage.getHeight()/2*scale);
		Vector2f smallPos = new Vector2f( position.x, position.y );
		smallGear = new Gear( this, smallPos, gearsRotationSpeed, smallGearImage, 291.2f/2, filterColor );
		smallGear.scale = scale;
				
		Image bigGearImage = Resources.getImage("big gear");
		bigGearImage.setCenterOfRotation( bigGearImage.getWidth()/2*scale, bigGearImage.getHeight()/2*scale);
		Vector2f bigPos = new Vector2f( position.x + smallGearImage.getWidth()*scale - 30*scale,
										position.y - (bigGearImage.getHeight() - smallGearImage.getHeight())/2*scale);
		
		bigGear = new Gear( this, bigPos, -gearsRotationSpeed, bigGearImage, 312/2, filterColor );
		bigGear.rotation = 12;
		bigGear.scale = scale;
		
		
		
		
	}

	@Override
	public void render(GameContainer cont, Graphics g) {
		
		g.rotate( position.x, position.y, (float) rotation );
		smallGear.render(cont, g);
		bigGear.render(cont, g);
		g.resetTransform();
	}

	@Override
	public void update(GameContainer cont, int delta) {
		smallGear.update(cont, delta);
		bigGear.update(cont, delta);
		
		rotation += rotationSpeed * delta;
	}
	
	public enum GearType {
		BIG_GEAR, SMALL_GEAR;
	}
	
	public class Gear extends MovableEntity {

		private Image image;
		private Color filterColor;
		
		public Gear( Entity parent, Vector2f position, float rotationSpeed, Image image, float radius, Color filterColor ) {
			super("Gear", parent);
			
			this.position = position;
			this.image = image;
			this.rotationSpeed = rotationSpeed/(2* Math.PI *radius);
			
			this.filterColor = filterColor;
		}

		@Override
		public void render(GameContainer cont, Graphics g) {
			image.draw( position.x, position.y, scale, filterColor);
		}

		@Override
		public void update(GameContainer cont, int delta) {
			rotation += rotationSpeed*delta;
			image.setRotation( (float) rotation );	
		}
	}
}