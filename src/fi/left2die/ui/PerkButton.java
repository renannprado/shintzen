package fi.left2die.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import fi.left2die.io.Resources;

public class PerkButton extends Button {

	public PerkButton( Vector2f position ) {
		this.buttonName = "Perkbutton";
		this.buttonText = "";
		this.buttonImg = Resources.getImage("perknotification");
		this.shape = new Circle( position.x, position.y, buttonImg.getWidth()/2 );
		setAcceptingInput(true);
	}	
	
	@Override
	public void render(GameContainer cont, Graphics g) {				
		buttonImg.draw(shape.getX(), shape.getY(), filterColor);
	}
	
	@Override
	public void update(GameContainer cont, int delta) {
		super.update(cont, delta);
		
		buttonImg.rotate(0.1f * delta);
	
		Input input = cont.getInput();
		
		boolean isOver = shape.contains( input.getMouseX(), input.getMouseY() );
		
		if( isOver && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
			fireButtonPressed(Input.MOUSE_LEFT_BUTTON);
		}
	}
}
