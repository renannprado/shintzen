package fi.left2die.ui;

import java.awt.geom.Point2D;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.IGameObject;
import fi.left2die.io.Resources;

public class CheckBox implements IGameObject{

	private Vector2f position;
	private Vector2f textPosition;
	private Rectangle shape;
	
	private float scale;
	
	private boolean isMouseOver;
	
	private Image inactiveUnchecked;
	private Image activeUnchecked;
	private Image inaciveChecked;
	private Image activeChecked;	
	
	private CheckBoxState state;
	
	private String text;
	
	private UnicodeFont font;
	
	private Color color;
	
	public enum CheckBoxState {
		INACTIVE_CHECKED, ACTIVE_CHECKED, ACTIVE_UNCHEKED , UNCHECKED; 
	}
	
	private boolean checked = false;
	
	public CheckBox( float x, float y, String text, Color color ) throws SlickException {
		
		position = new Vector2f(x, y);
		this.color = color; 
		
		inactiveUnchecked = Resources.getImage("checkbox unchecked");
		activeUnchecked = Resources.getImage("checkbox active unchecked");
		activeChecked = Resources.getImage("checkbox active checked");
		inaciveChecked = Resources.getImage("checkbox checked");
		
		scale = 0.5f;
		
		shape = new Rectangle(x, y, inaciveChecked.getWidth() * scale, 
									inaciveChecked.getHeight() * scale);
		
		font = Resources.getFont("textfield font");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
		
		
		float yoffset = (shape.getHeight() - font.getHeight("|"))/ 2;
		
		this.textPosition = new Vector2f( position.getX() + shape.getWidth(), 
										  shape.getY() + yoffset);
		
		this.text = text;
		

		state = CheckBoxState.UNCHECKED;
	}
	
	public boolean isMouseOver() {
		return isMouseOver;
	}
	
	@Override
	public void render(GameContainer cont, Graphics g) {
		switch( state ) {
			case INACTIVE_CHECKED :
				inaciveChecked.draw( position.x, position.y, scale);
				break;
			case ACTIVE_CHECKED :
				activeChecked.draw(position.x, position.y, scale);
				break;
			case ACTIVE_UNCHEKED :
				activeUnchecked.draw(position.x, position.y, scale);
				break;
			case UNCHECKED :
				inactiveUnchecked.draw(position.x, position.y, scale);
				break;			
		}
		
		g.setFont(font);
		g.setColor(color);
		g.drawString(text, textPosition.x, textPosition.y);
	}

	public void setChecked( boolean checked ) {
		
		this.checked = checked;
		
		if( checked )		
			state = CheckBoxState.INACTIVE_CHECKED;
		else
			state = CheckBoxState.UNCHECKED;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void update(GameContainer cont, int delta) {
		
		Input input = cont.getInput();		
		
		isMouseOver = shape.contains(input.getMouseX(), 
										input.getMouseY());
		
		
		if( isMouseOver ) {			
			if( input.isMousePressed(Input.MOUSE_LEFT_BUTTON) )
				checked ^= true;
			
			if( checked )
				state = CheckBoxState.ACTIVE_CHECKED;
			else
				state = CheckBoxState.ACTIVE_UNCHEKED;
		}
		
		else {
			if( checked )
				state = CheckBoxState.INACTIVE_CHECKED;
			else
				state = CheckBoxState.UNCHECKED;
		}
		
	}	
}
