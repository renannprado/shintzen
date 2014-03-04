package fi.left2die.ui.inputpanel;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.IGameObject;
import fi.left2die.io.Resources;
import fi.left2die.ui.Button;
import fi.left2die.ui.Button.FilterColor;

/**
 * Very simple virtual keyboard with keyboard, joystick and mouse support.
 * 
 * @author TeeMuki
 */
public class InputPanel implements IGameObject {
	
	/**
	 * Maximum length of text.	
	 */
	private int textMaxLenght = 7;
	
	/**
	 * Currently selected letter, used with keyboard and joystick controllers.
	 */
	private String selectedLetter = "A";
		
	/**
	 * Keep track witch letter is highlighted and selected 
	 */
	private int selectionX = 0, selectionY = 0;
	
	/**
	 * Input panel letters
	 */
	private String[][] letters = {{"A","B","C","E","F"}, 
								  {"G","H","I","J","K" }, 
								  {"L","M","N","O","P" }, 
								  {"Q","R","S","T","U" }, 
								  {"V","Z","X","Ö","Ä" }};
	
	/**
	 * Input panel buttons. Each letter is a button.
	 */
	private ArrayList<InputPanelButton>buttons = new ArrayList<InputPanelButton>();	
	
	/**
	 * Component position
	 */
	private Vector2f position;
	
	/**
	 * When user brings mouse over the object this filter is used to indicate it.
	 */
	private Color filterColor;
	
	/**
	 * Component title text
	 */
	private String text = "";
	
	/**
	 * Button background image
	 */
	private Image button;
	
	/**
	 * Input panel event listeners
	 */
	private ArrayList<InputPanelListener>listeners = new ArrayList<InputPanelListener>();
	
	/**
	 * InputPanel constructor
	 * 
	 * @param position Component position
	 * @throws SlickException 
	 */
	public InputPanel( Vector2f position ) throws SlickException {
		this.position = position;
		filterColor = new Color(1,1,1,1);
		
		//Setting images
		this.button = Resources.getImage("hi button");	
		
		//This creates buttons for each letter
		for( int x=0; x<5; x++ ) {
			for( int y=0; y<5; y++ ) {
				
				Rectangle bounds = new Rectangle( position.x + x * 60, 
													position.y + y * 60, 
													50, 50);
				
				buttons.add( new InputPanelButton(x,y, button, bounds	, 
											letters[y][x] ));
			}
		}
		
		//Remove / Erase button
		Rectangle bounds = new Rectangle( position.x, 
				   						  position.y + 5 * 60, 
				   						  120, 50);
		
		buttons.add(new InputPanelButton(0,5,button, bounds, "Back"));
		
		bounds = new Rectangle( position.x + 130, 
					  position.y + 5 * 60, 
					  100, 50);
		
		//Ending button
		buttons.add(new InputPanelButton( 1,5,button, bounds, "End"));
	}

	/**
	 * Set text written on input panel
	 * 
	 * @param text Title text
	 */
	public void setText(String text) {
		if( text.length() <= textMaxLenght ) {
			this.text = text;
		}
	}
	
	/**
	 * @return Returns text written on input panel
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Renders component
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {
		
		float x = this.position.x;
		float y = this.position.y;
		
		for( InputPanelButton b : buttons ) {
			
			Color filterColor = b.getFilterColor();
			
			if( !b.equals( getSelectenButton() )) 	
				filterColor.a = 0.75f;
			else
				filterColor.a = 1f;	
			
			b.setFilterColor(FilterColor.GREEN);
			b.render(cont, g);
		}
			
		g.drawString(text, 100, 500);
	}

	/**
	 * Set input panel
	 * 
	 * @param textMaxLenght
	 */
	public void setTextMaxLenght(int textMaxLenght) {
		this.textMaxLenght = textMaxLenght;
	}
	
	public int getTextMaxLenght() {
		return textMaxLenght;
	}
	
	/**
	 * Update buttons
	 */
	@Override
	public void update(GameContainer cont, int delta) {
		Input input = cont.getInput();
		
		if( input.isKeyPressed(Input.KEY_UP) )
			moveSelectionUp();
		
		if(  input.isKeyPressed(Input.KEY_DOWN))
			moveSelectionDown();
		
		if(  input.isKeyPressed(Input.KEY_LEFT))
			moveSelectionLeft();
		
		if(  input.isKeyPressed(Input.KEY_RIGHT))
			moveSelectionRight();
		
		if( input.isKeyPressed(Input.KEY_ENTER)) {
			InputPanelButton btn = getSelectenButton();
			
			if( btn != null )
				handeAction(btn.getButtonText());
		}
		
		for( InputPanelButton b : buttons ) {
			b.update(cont, delta);
			
			if( b.isMouseOver( input.getMouseX(), input.getMouseY())
					&& input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				handeAction(b.getButtonText());
			}
		}
					
	}		
	
	/**
	 * @return Returns currently selected button
	 */
	public InputPanelButton getSelectenButton() {
		for( InputPanelButton button : buttons ) {
			if( button.getIndexX() == selectionX 
				&& button.getIndexY() == selectionY ) {
				return button;
			}
		}
		
		return null;
	}
	
	/**
	 * @return Returns currently selected letter
	 */
	public String getCurrentLetter() {
		InputPanelButton btn = getSelectenButton();
		
		if( btn != null )
			return btn.getButtonText();
		else
			return "";
	}
	
	/**
	 * Moves selection position left
	 */
	private void moveSelectionLeft() {
		if( selectionX - 1 < 0 )
			selectionX = letters[0].length - 1 ;
		else			
			selectionX--;
		
		selectedLetter = getCurrentLetter();
	}
	
	/**
	 * Moves selection position right
	 */
	private void moveSelectionRight() {
		if( selectionX + 1 > letters[0].length - 1 )
			selectionX = 0;
		else
			selectionX++;	
		
		selectedLetter = getCurrentLetter();
	}
	
	/**
	 * Moves selection position up
	 */
	private void moveSelectionUp() {
		if( selectionY - 1 < 0 )
			selectionY = letters.length;
		else
			selectionY--;
		
		selectedLetter = getCurrentLetter();
	}
	
	/**
	 * Moves selection down
	 */
	private void moveSelectionDown() {	
		if( selectionY + 1 > letters.length  )
			selectionY = 0;
		else
			selectionY++;	
		
		selectedLetter = getCurrentLetter();
	}
	
	/**
	 * Handle input witch can be tricked with mouse click or enter key.
	 * 
	 * @param input Letter or button name
	 */
	private void handeAction(String input) {
		
		if( input.contains("Back") && text.length() > 0) {
			text = text.substring( 0, text.length() - 1);
			fireTextChanged();
		}
		
		else if( input.equals("End") ) {
			fireTextEntered();
		}
		
		else if( !input.equals("Back") ) {
			
			if( text.length() + input.length() <= textMaxLenght )
				text += input;
			
			fireTextChanged();
		}
		
		selectedLetter = input;
	}
		
	/**
	 * Add input panel event listener to list
	 * 
	 * @param listener Input panel event listener
	 */
	public void addInputPanelListener( InputPanelListener listener ) {
		listeners.add( listener );
	}
	
	/**
	 * Removes input panel event listener from list
	 * 	
	 * @param listener Input panel event listener
	 */
	public void removeInputPanelListener( InputPanelListener listener ) {
		listeners.remove( listener );
	}
	
	/**
	 * Method is fired when input panel text is chanced
	 */
	private void fireTextChanged() {
		for( InputPanelListener l : listeners ) {
			l.inputPanelTextChanged(new InputPanelEvent(text));
		}
	}
	
	/**
	 * Method is fired when input panel text editing is ended
	 */
	private void fireTextEntered() {
		for( InputPanelListener l : listeners ) {
			l.inputPanelTextEntered(new InputPanelEvent(text));
		}
	}
	
	/**
	 * InputPanelButton is indexed button so it can be used with keyboard and joystick.
	 * 
	 * @author TeeMuki
	 */
	private class InputPanelButton extends Button {

		/**
		 * Button index
		 */
		private int indexX, indexY;						
				
		public InputPanelButton( int indexX, int indexY, 
					   Image buttonImg, Rectangle bounds, 
					   String text ) throws SlickException {
			
			super("", text, buttonImg, bounds);
			
			this.indexX = indexX;
			this.indexY = indexY;
		}
		
		/**
		 * Is mouse over of component
		 * 
		 * @param x Horizontal position
		 * @param y Vertical position
		 * @return If mouse is over button true is returned otherwise false is returned.
		 */
		public boolean isMouseOver( float x, float y ) {
			return shape.contains(x,y);
		}
		
		/**
		 * @return Returns button horizontal index
		 */
		public int getIndexX() {
			return indexX;
		}
		
		/**
		 * @return Returns button vertical index
		 */
		public int getIndexY() {
			return indexY;
		}				
		
		@Override
		public boolean equals(Object obj) {
		
			if( obj instanceof InputPanelButton ) {
				InputPanelButton btn = (InputPanelButton)obj;
			
				if( btn.indexX == indexX && btn.indexY == indexY )
					return true;
			}
			return false;
		}
	}
}

