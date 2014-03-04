package fi.left2die.ui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.InputAdapter;

import fi.left2die.gameobject.IGameObject;
import fi.left2die.io.Resources;

/**
 * Simple button is a user interface component. Usable only with mouse...
 * 
 * @author TeeMuki
 */
public class Button extends InputAdapter implements IGameObject {

	/**
	 * Used to identificates button
	 */
	protected String buttonName;
		
	/**
	 * Texture of button
	 */
	protected Image buttonImg;
	
	/**
	 * Boundaries of button
	 */
	protected Shape shape;
	
	/**
	 * Text offset
	 */
	protected float textOffsetX = 0, textOffsetY = 0;
	
	/**
	 * Filter color is used to indicate that mouse is over the button
	 */
	protected Color filterColor;
	
	/**
	 * Button text
	 */
	protected String buttonText;
	
	/**
	 * Mouse position
	 */
	protected float mouseX = 0, mouseY = 0;
		
	/**
	 * Event listeners of button
	 */
	private ArrayList<ButtonListener>listeners = new ArrayList<ButtonListener>();	
	
	/**
	 * Component font
	 */
	private UnicodeFont font;
	
	private FilterColor filterColorInUse;
	
	public enum FilterColor { RED, GREEN };
	
	public Button( String buttonName, String text, float x, float y ) throws SlickException {
		
		super();
		
		this.buttonName = buttonName;		
		float padding = 5;		
		this.buttonImg = Resources.getImage("hi button");
		
		this.shape = new Rectangle( x, y, 
									buttonImg.getWidth() + padding *2 , 
									buttonImg.getHeight() + padding *2);
		
		this.buttonText = text;		
		this.filterColor = new Color(1f, 1f, 1f, 1f);
		filterColorInUse = FilterColor.GREEN;		
		
		font = Resources.getFont("textfield font");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
		
		centerizeText();
	}
	
	/**
	 * Constructor of Button class.
	 * 
	 * @param buttonName Identification name.
	 * @param text Button text.
	 * @param buttonImg Button texture.
	 * @param rectangle Position and size of button.
	 * @throws SlickException 
	 */
	public Button( String buttonName, String text,
					Image buttonImg, Shape rectangle ) throws SlickException {
		
		super();
		
		this.buttonName = buttonName;
		this.shape = rectangle;
		this.buttonText = text;
		this.buttonImg = buttonImg;
		this.filterColor = new Color(1f, 1f, 1f, 1f);
				
		font = Resources.getFont("textfield font");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
		
		centerizeText();
	}
	
	/**
	 * Default constructor that don't initialize anything!
	 */
	public Button() {
		this.buttonText = "";
		this.filterColor = new Color(1f, 1f, 1f, 1f);
	}
	
	/**
	 * Constructor of Button class.
	 * 
	 * @param buttonName Identification name.
	 * @param text Button text.
	 * @param buttonImg Button texture.
	 * @param rectangle Position and size of button.
	 * @param textOffsetX Horizontal offset  of button text.
	 * @param textOffsetY Vertical offset of button text.
	 * @throws SlickException 
	 */
	public Button( String buttonName, String text,
			Image buttonImg, Shape rectangle, 
			float textOffsetX, float textOffsetY ) throws SlickException {
		
		this(buttonName, text, buttonImg, rectangle);
		this.textOffsetX = textOffsetX;
		this.textOffsetY = textOffsetY;
		
	}
		
	/**
	 * 
	 * @return Returns button width.
	 */
	public float getWidth() {
		return shape.getWidth();
	}
	
	public String getButtonText() {
		return buttonText;
	}
	
	public void setFilterColor( FilterColor color ) {
		filterColorInUse = color;
	}
	
	public Color getFilterColor() {
		return filterColor;
	}
	
	/**
	 * Set horizontal position of button
	 * 
	 * @param x Horizontal position
	 */
	public void setX( float x ) {
		this.shape.setX(x);
	}
	
	/**
	 * Set vertical position of button
	 * 
	 * @param y Vertical position
	 */
	public void setY( float y ) {
		this.shape.setY(y);
	}
	
	/**
	 * @return Returns height of button
	 */
	public float getHeight() {
		return shape.getHeight();
	}
	
	/**
	 * Set position of button
	 * 
	 * @param x Horizontal position
	 * @param y Vertical position
	 */
	public void setPosition( float x, float y ) {
		this.shape.setX(x);
		this.shape.setY(y);
		centerizeText();
	}
	
	/**
	 * Set text horizontal offset
	 * 
	 * @param textOffsetX Text horizontal offset
	 */
	public void setTextOffsetX(float textOffsetX) {
		this.textOffsetX = textOffsetX;
	}
	
	/**
	 * Set text vertical offset
	 * 
	 * @param textOffsetY Text vertical offset
	 */
	public void setTextOffsetY(float textOffsetY) {
		this.textOffsetY = textOffsetY;
	}
	
	public void centerizeText() {
		textOffsetX = (getWidth() - font.getWidth(buttonText))/2; 
		textOffsetY = (getHeight() - font.getHeight(buttonText))/2;
		
		System.out.println( buttonText+" "+textOffsetX + font.getHeight(buttonText));
	}
	
	/**
	 * Returns text vertical offset
	 * 
	 * @return vertical offset of text
	 */
	public float getTextOffsetY() {
		return textOffsetY;
	}
	
	/**
	 * 
	 * @return Returns text horizontal offset
	 */
	public float getTextOffsetX() {
		return textOffsetX;
	}
	
	/**
	 * Tells is position over the button
	 * 
	 * @param x
	 * @param y
	 * @return Returns true if is over otherwise return false.
	 */
	private boolean isMouseOver( float x, float y) {
		return shape.contains(x, y);
	}
	
	/**
	 * Renders button
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {
		buttonImg.draw( shape.getX(), shape.getY(), 
						shape.getWidth(), shape.getHeight(),
						filterColor );
		
		g.setFont(font);
		
		g.drawString( buttonText, 
					  shape.getX() + textOffsetX, 
					  shape.getY() + textOffsetY);
	}

	/**
	 * Updates button
	 */
	@Override
	public void update(GameContainer cont, int delta) {
		
		Input input = cont.getInput();

		mouseX = input.getMouseX();
		mouseY = input.getMouseY();
		
		updateFilterColor(mouseX, mouseY, 
						  delta, filterColor);
	}
	
	private void updateFilterColor( float x, float y, 
									int delta, Color filterColor ) {
		
		float stepSize = 0.005f;
		
		if( filterColorInUse == FilterColor.RED ) {		
			if( isMouseOver( mouseX, mouseY ) ) {
				if( filterColor.b >= 0.1 ) {
					filterColor.b -=stepSize * delta;
					filterColor.g -=stepSize * delta;
				}
			}
			else if(filterColor.b < 1 ) {
				filterColor.b +=stepSize* delta;
				filterColor.g +=stepSize* delta;
			}
		}
		
		else if( filterColorInUse == FilterColor.GREEN ) {
			if( isMouseOver( mouseX, mouseY ) ) {
				if( filterColor.r >= 0.1 ) {
					filterColor.b -=stepSize * delta;
					filterColor.r -=stepSize * delta;
				}
			}
			else if(filterColor.r < 1 ) {
				filterColor.r +=stepSize* delta;
				filterColor.b +=stepSize* delta;
			}
		}
	}

	/**
	 * This method informs all listeners that button event is fired.
	 * 
	 * @param mousebutton Mouse button key
	 */
	protected void fireButtonPressed( int mousebutton ) {
		for( ButtonListener l : listeners ) {
			l.buttonPressed( new ButtonEvent(buttonName, mousebutton) );
		}
	}
	
	@Override
	public boolean equals(Object obj) {
	
		if( obj instanceof Button ) {
			Button btn = (Button)obj;
		
			if( btn.buttonName.equals( this.buttonName ) )
				return true;
		}
		return false;
	}

	/**
	 * Add button event listener
	 * 
	 * @param listener
	 */
	public void addListener( ButtonListener listener ) {
		listeners.add( listener );
	}
	
	/**
	 * Removes button event listener
	 * 
	 * @param listener
	 */
	public void removeListener( ButtonListener listener ) {
		listeners.remove( listener );
	}
	
	/**
	 * 
	 * @author TeeMuki
	 *
	 * Button listener have to implement this interface to listen button events.
	 */
	public interface ButtonListener {
		public void buttonPressed( ButtonEvent event );
	}
	
	/**
	 * 
	 * @author TeeMuki
	 *
	 * Button event
	 */
	public class ButtonEvent {
		
		/**
		 * Button name
		 */
		private String buttonName;
		
		/**
		 * Witch mouse button is pressed.
		 * Value is Input.MOUSE_KEY_LEFT, Input.MOUSE_KEY_RIGHT or Input.MOUSE_KEY_MIDDLE.
		 */
		private int mouseButton;
		
		public ButtonEvent( String buttonName, int mouseButton ) {
			this.buttonName = buttonName;
			this.mouseButton = mouseButton;
		}
		
		/**
		 * @return Returns mouse name
		 */
		public String getButtoName() {
			return buttonName;
		}
		
		/**
		 * @return Returns is Input.MOUSE_KEY_LEFT, Input.MOUSE_KEY_RIGHT or Input.MOUSE_KEY_MIDDLE.
		 */
		public int getMouseButton() {
			return mouseButton;
		}
	}

	/**
	 * Event happens when button is pressed.
	 */
	@Override
    public void mousePressed (int button, int x, int y) {   					
		if(isMouseOver( mouseX, mouseY)) {
			fireButtonPressed(button);
		}		        
    }
}