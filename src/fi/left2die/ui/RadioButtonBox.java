package fi.left2die.ui;

import java.util.ArrayList;
import java.util.Vector;

import javax.crypto.spec.PSource;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.IGameObject;
import fi.left2die.io.Resources;
import fi.left2die.library.UniqueID;

import fi.left2die.ui.Button.ButtonEvent;
import fi.left2die.ui.Button.ButtonListener;

/**
 * UI-compent dialog with radio buttons and accept button.
 *  
 * User interface component used with Slick2d library.
 * 
 * @author TeeMuki
 */
public class RadioButtonBox extends Entity implements IGameObject, ButtonListener {

	/**
	 * Horizontal padding off choice 
	 */
	private final float choiceMarginX = 20;
	
	/**
	 * Vertical padding off choice
	 */
	private final float choiceMarginY = 20;
	
	/**
	 * Text line height
	 */
	private final float lineHeight = 30;
	
	/**
	 * RadiobuttonBox font
	 */
	private UnicodeFont font;
	
	/**
	 * Background color
	 */
	private Color backgroundColor;
	
	/**
	 * Indicates is component rendered and updated
	 */
	private boolean enabled = false;
	
	/**
	 * Accept button
	 */
	private Button accectBtn;
	
	/**
	 * Listeners of RadioButtonBox choice
	 */
	private ArrayList<RadioButtonBoxListener>listeners = new ArrayList<RadioButtonBoxListener>();
	
	/**
	 * RadioButtonBox choices or items
	 */
	private Vector<RadioButtonBoxItem> choices = new Vector<RadioButtonBoxItem>();
	
	/**
	 * Component position
	 */
	private Vector2f position;
	
	/**
	 * Indicates witch item is selected, if there is no selection it is null.
	 */
	private RadioButtonBoxItem selectedItem;
	
	/**
	 * RadioButtonBox constructor
	 *  
	 * @param position Component position
	 * @throws SlickException
	 */
	public RadioButtonBox( Vector2f position, UnicodeFont font ) throws SlickException {
		
		this.position = position;				

		this.font = font;
		//Load and set fonts effects		
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
		enabled = false;
		
		this.shape = new Rectangle( position.x, position.y, 400, 75 );
		
		this.backgroundColor = new Color(255, 100, 0, 220 );
		
		float buttonWidth = 150;
		
		Rectangle buttonRect = new Rectangle( position.x + (shape.getWidth()- buttonWidth)/2 , 
											position.y + shape.getHeight() - 10,
											buttonWidth, 30 );
		
		this.accectBtn = new Button( "accept", "Done", 
								  	Resources.getImage("hi button"), 
								  	buttonRect);

		accectBtn.setTextOffsetX( ( accectBtn.getWidth() - font.getWidth(accectBtn.getButtonText())) / 2 );
		accectBtn.setTextOffsetY( ( accectBtn.getHeight() - font.getHeight(accectBtn.getButtonText())) / 2);
		
		accectBtn.addListener(this);			
	}
	
	/**
	 * Set component position
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition( float x, float y ) {
		
		this.position.x = x;
		this.position.y = y;
		
		this.shape.setLocation( x, y);
		
		ArrayList<String>texts = new ArrayList<String>();
		
		for( int i=0; i<choices.size(); i++ ) {
			RadioButtonBoxItem item = choices.get(i);
			
			item.setPosition( choiceMarginX + x, 
							  choiceMarginY + y + i * lineHeight);
		}
		
		accectBtn.setPosition(x + (shape.getWidth()- accectBtn.getWidth())/2,
				  y + shape.getHeight() - accectBtn.getHeight() - 10);
	}
	
	/**
	 * Centers component to middle off screen.
	 * 
	 * @param screenwidth
	 * @param screenHeight
	 */
	public void centerize( float screenwidth, float screenHeight ) {
		setPosition( (screenwidth - shape.getWidth())/2, 
					 (screenHeight - shape.getHeight())/2 );
	}
	
	/**
	 * @return Returns component width.
	 */
	public float getWidth() {
		return shape.getWidth();
	}
	
	/**
	 * @return Returns component height.
	 */
	public float getHeight() {
		return shape.getHeight();
	}
	
	public Button getAcceptButton() {
		return accectBtn;
	}

	/**
	 * Set component enabled or disabled
	 * 
	 * @param isEnabled
	 */
	public void setEnabled( boolean isEnabled ) {
		this.enabled = isEnabled;
	}
	
	/**
	 * @return Tells is component enabled or disabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Add choosable item to radio button list
	 * 
	 * @param text Item text
	 * @return Returns identification number of added choice.
	 */
	public long addChoice( String text ) {		
	
		float choiceWidth = 100 + font.getWidth(text);
		
		RadioButtonBoxItem item = new RadioButtonBoxItem( position.x + choiceMarginX, 
											position.y + choiceMarginY + lineHeight  * choices.size() + 10, 
											choiceWidth, lineHeight, text);
		choices.add(item);			
		
		resize();
		
		return item.getId();
	}
	
	/**
	 * Resizes object
	 */
	public void resize() {
		
		final int textMargin = 20;
		
		float x = this.shape.getX() + textMargin;
		float y = this.shape.getY() + textMargin;
		
		float itemMaxWidth = 0;
		
		for( RadioButtonBoxItem item : choices ) {
			item.shape.setLocation(x, y);
			y += lineHeight;
			
			if( itemMaxWidth < item.shape.getWidth() )
				itemMaxWidth = item.shape.getWidth();
		}
		
		Rectangle rectangle = (Rectangle)shape;
				
		rectangle.setWidth(itemMaxWidth + textMargin );
		rectangle.setHeight( textMargin * 2 + lineHeight * choices.size() + accectBtn.getHeight() );
		accectBtn.setY( shape.getY() + shape.getHeight() - accectBtn.getHeight() - 10 );	
	}	
	
	/**
	 * Renders component
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {		
		if( enabled ) {
			final float lineWidth = 3.5f;
			
			//Fills background
			g.setColor(backgroundColor);
			g.setAntiAlias(true);
			g.fill(shape);
			
			//Draws borders 
			g.setColor(Color.yellow);
			g.setLineWidth(lineWidth);
			g.draw(shape);
				
			//Draws choises
			g.setFont(font);
		
			for( int i=0; i<choices.size(); i++) {
				choices.get(i).render(cont, g);
			}
			
			//Renders accept buttons
			g.setColor(Color.black);
			accectBtn.render(cont, g);
			
			g.setAntiAlias(false);
		}
	}

	/**
	 * Updates component
	 */
	@Override
	public void update(GameContainer cont, int delta) {
		
		if( enabled ) {		
			for( int i=0; i<choices.size(); i++) {
				
				if( choices.get(i).equals(selectedItem))
					choices.get(i).ovalColor = Color.orange;
				else
					choices.get(i).ovalColor = Color.black;
					
				choices.get(i).update(cont, delta);
			}
			
			accectBtn.update(cont, delta);
		}
	}

	/**
	 * Removes all choices
	 */
	public void clearChoises() {
		choices.clear();
	}
	
	/**
	 * Set selected item
	 * 
	 * @param item Selected item
	 */
	private void setSelectedItem( RadioButtonBoxItem item ) {
		selectedItem = item;
	}
	
	/**
	 * Remove choice from list with given id
	 */
	public void removeChoice( long id ) {
		for( int i=0; i<choices.size(); i++ ) {
			if( choices.get(i).id == id ) {
				choices.remove(i);
				break;
			}
		}
		
		resize();
	}
	
	/**
	 * 
	 * @author TeeMuki
	 *
	 * Inner class RadioButtonBoxItem is choocable item. 
	 */
	public class RadioButtonBoxItem implements IGameObject {
		
		/**
		 * Identification number
		 */
		private long id;
		
		/**
		 * Component shape
		 */
		private Rectangle shape;
		
		/**
		 * Component text
		 */
		private String text;
		
		/**
		 * Oval color that indicates is item selected
		 */
		private Color ovalColor;
		
		/**
		 * Text color when item is selected
		 */
		private Color activeColor; 
		
		/**
		 * Text default color when item is't selected
		 */
		private Color defaultColor;
		
		/**
		 * Tells are mouse over this component
		 */
		private boolean isMouseOver = false;
		
		/**
		 * RadioButtonBoxItem constructor
		 * 
		 * @param x Horizontal position
		 * @param y Vertical position
		 * @param width Component width
		 * @param height Component height
		 * @param text Component text
		 */
		public RadioButtonBoxItem( float x, float y, float width, float height,
								   String text ) {
			
			this.id = UniqueID.get();
			this.shape = new Rectangle(x, y, width, height);
			this.text = text;
			this.activeColor = Color.white;;
			this.defaultColor = Color.black;
			this.ovalColor = Color.black;
		}

		/**
		 * @return Returns identification number
		 */
		public long getId() {
			return id;
		}
		
		/**
		 * Set component position
		 * 
		 * @param x
		 * @param y
		 */
		public void setPosition( float x, float y ) {
			shape.setLocation(x, y);
		}
		
		/**
		 * Renders component
		 */
		@Override
		public void render(GameContainer cont, Graphics g) {				
				
			g.setColor(ovalColor);				
			g.fillOval( shape.getX(), shape.getY() - 5, 25, 25 );
			
			if( isMouseOver )			
				g.setColor(activeColor);
			else
				g.setColor(defaultColor);				
			
			g.drawString(text, shape.getX() + 40, shape.getY());
			
		}
		
		/**
		 * @return Returns component text
		 */
		public String getText() {
			return text;
		}
		
		/**
		 * Renders rectangle
		 * 
		 * @param g
		 */
		public void renderRectangle( Graphics g ) {
			g.setColor(Color.orange);
			g.setLineWidth(1.5f);
			g.draw(shape);
		}

		/**
		 * Updates component
		 */
		@Override
		public void update(GameContainer cont, int delta) {
			
			Input input = cont.getInput();
			
			isMouseOver = false;		
			
			if( shape.contains( input.getMouseX(), input.getMouseY()))	{
				
				isMouseOver = true;
				
				if( input.isMouseButtonDown( Input.MOUSE_LEFT_BUTTON ) ) {
					setSelectedItem(this);
				}
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if( obj != null &&  obj instanceof RadioButtonBoxItem) {
				if(((RadioButtonBoxItem)obj).id == this.id )
					return true;
			}
		
			return false;
		}
	}
	
	/**
	 * Add RadioButtonBoxEvent listener
	 * 
	 * @param listener
	 */
	public void addListener( RadioButtonBoxListener listener ) {
		listeners.add( listener );
	}
	
	/**
	 * Removes RadioButtonBoxEvent listener
	 * 
	 * @param listener
	 */
	public void removeListener( RadioButtonBoxListener listener ) {
		listeners.remove(listener);
	}
	
	/**
	 * RbuttonEvent carries selected button state to all event listeners.
	 * 
	 * @author TeeMuki
	 */
	public class RbuttonEvent {
		
		/**
		 * Tells witch item event was fired
		 */
		long radioBtnItemID;
		
		/**
		 * RadioButtonBoxItem text
		 */
		String text;
		
		public RbuttonEvent( long radioBtnItemID, String text ) {
			this.text = text;
			this.radioBtnItemID = radioBtnItemID;
		}		
		
		/**
		 * @return Returns identification number of event source witch is some RadioButtonBoxItem.
		 */
		public long getRadioBtnItemID() {
			return radioBtnItemID;
		}
				
		/**
		 * @return Returns RadioButtonBoxItem text
		 */
		public String getText() {
			return text;
		}
	}
	
	/**
	 * Method is fired when accept button is pressed.
	 * 
	 * @param value
	 */
	private void fireSelectionMadeEvent( long id,  String value ) {	
		for( RadioButtonBoxListener l : listeners ) {
			l.radioButtonBoxEvent( new RbuttonEvent( id, value));
		}
	}
	
	/**
	 * 
	 * @author TeeMuki
	 *
	 * If class is RadioButtonBoxListener it have to implement this interface.
	 */
	public interface RadioButtonBoxListener {
		public void radioButtonBoxEvent( RbuttonEvent event );			
	}

	/**
	 * When accept button is pressed this event is fired
	 */
	@Override
	public void buttonPressed(ButtonEvent event) {	
		if( selectedItem != null && isEnabled() ) {
			fireSelectionMadeEvent( selectedItem.getId(),
									selectedItem.getText() );
		}
	}	
}
