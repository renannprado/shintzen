package fi.left2die.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.IGameObject;
import fi.left2die.model.KeyboardConfig;

/**
 * Component is used to customize key bindings of the left2die game.
 * User interface component that is used with the slick2d library.
 * 
 * @author TeeMuki
 */
public class KeySelector implements IGameObject{

	/**
	 * Component position
	 */
	private Vector2f position;
	
	/**
	 * Component text
	 */
	private String text;
	
	/**
	 * Corresponding binding to Input.KEY_... value
	 * 
	 * Binding should be something like Input.KEY_LEFT or Input.KEY_ENTER... 
	 */
	private int binding = -1;
	
	/**
	 * Item shape or boundaries
	 */
	private Rectangle keySelectionArea;

	/**
	 * Control flag that indicates is component selected
	 */
	private boolean isActive;
	
	private Color textColor = Color.black;
	private Color textBoxColor = Color.black;
	
	private Color backgroundColor = new Color( 100,100,100, 50);
	
	/**
	 * KeySelector constructor
	 * 
	 * @param position Component position
	 * @param text Component text
	 */
	public KeySelector( Vector2f position, String text ) {
		this.position = position;
		this.text = text;
		this.keySelectionArea = new Rectangle(position.x + 210, 
											  position.y - 5, 
											  200, 25 );
		isActive = false;
	}
	
	/**
	 * Set item key binding.
	 * 
	 * @param binding Binding should be something like Input.KEY_LEFT.
	 */
	public void setBinding(int binding) {	
		this.binding = binding;
		this.isActive = false;
	}
	
	/**
	 * @return Returns item key binding.
	 */
	public int getBinding() {
		return binding;
	}
	
	/**
	 * Set component text
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * @return Returns component text
	 */
	public String getText() {
		return text;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	
	/**
	 * Renders component
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {
						
		g.setLineWidth(2.5f);
		g.draw(keySelectionArea);
		
		g.setColor(backgroundColor);		
		g.fill(keySelectionArea);
		
		float margin = 5;			
		
		g.setColor( Color.black );
		g.drawString( text, position.x, position.y );				
		
		if( isActive ) {
			g.drawString( "Press someting", 
					  		keySelectionArea.getX() + margin,
					  		keySelectionArea.getY() + margin);
		}	
			
		
		if( binding > 0 && isActive == false ) {
			
			String text = Input.getKeyName(binding);
			
			g.drawString( text, keySelectionArea.getX() +margin ,
			  		keySelectionArea.getY() +margin);
		}
	}

	/**
	 * @return Returns true if component is active otherwise false is returned.
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Set component active
	 * 
	 * @param isActive
	 */
	public void setActive( boolean isActive ) {
		this.isActive = isActive;
	}
	
	/**
	 * Updates component
	 */
	@Override
	public void update(GameContainer cont, int delta) {
		Input input = cont.getInput();
		
		if( keySelectionArea.contains( input.getMouseX(), input.getMouseY() ) 
							&& input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			isActive = true;
		}		
	}	
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof KeySelector ) {
			if( ((KeySelector)obj).text.equals( this.text ) )
				return true;
		}
		
		return false;
	}
}