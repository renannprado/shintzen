package fi.left2die.ui;



import java.text.DecimalFormat;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Rectangle;

import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.MovableEntity;
import fi.left2die.io.Resources;
import fi.left2die.library.UniqueID;

/**
 * Slider class is simple slider user interface component to used with Slick2d library.
 * 
 * @author TeeMuki
 */
public class Slider extends MovableEntity {

	/**
	 * Slider minimum value
	 */
	private float min;
	
	/**
	 * Slider maximum value
	 */
	private float max;
	
	/**
	 * Slider current value
	 */
	private float value;

	/**
	 * How much slider is moved on every update when slider is used with keyboard.
	 */
	private float stepValue;
	
	/**
	 * Control flag that indicates can user edit slider by mouse or keyboard.
	 */
	private boolean isEditable;
	
	/**
	 * 	Slider bar font
	 */
	private UnicodeFont font;
	
	/**
	 * Indicates witch way slider value is printed.
	 */
	private SliderFormat format;
	
	/**
	 * Slider bar inner height in px
	 */
	public static int SLIDER_INNER_HEIGHT = 13;
	
	/**
	 * Slider bar inner width in px
	 */
	public static int SLIDER_INNER_WIDTH = 454;
	
	public enum SliderFormat {
		PERCENT,
		VALUE
	}	
	
	/**
	 * Slider event listeners
	 */
	private ArrayList<SliderListener> listeners = new ArrayList<SliderListener>();
	
	private Image texture;
	
	public Slider( Entity parent, Vector2f pos, SliderFormat format ) throws SlickException {
		this(parent, pos, format, 
			Resources.getImage("slider"),
			Resources.getFont("slider font"));
	}

	public Slider( Entity parent, Vector2f pos, SliderFormat format, Image sliderImg, UnicodeFont font ) throws SlickException {
		super( "slider" + UniqueID.get(), parent );
		
		this.position = pos;
		
		this.texture = sliderImg;
		
		this.shape = new Rectangle( position.x, position.y, texture.getWidth(), texture.getHeight() );
		this.format = format;
		
		this.min = 0;
		this.max = 1;		
		this.value = 0.5f;
		this.stepValue = 0.001f;
		
		this.font = font;		
		this.font.addAsciiGlyphs();
		this.font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		this.font.loadGlyphs(); 
	}
	
	/**
	 * 
	 * @return Returns slider height
	 */
	public int getHeight() {
		return texture.getHeight();
	}
	
	/**
	 * 
	 * @return Returns slider width
	 */
	public int getWidth() {
		return texture.getWidth();
	}
	
	/**
	 * Add slider event listener
	 * 
	 * @param listener
	 */
	public void addListener( SliderListener listener ) {
		listeners.add(listener);
	}
	
	/**
	 * Removes slider event listener
	 * 
	 * @param listener
	 */
	public void removeListener( SliderListener listener ) {
		listeners.remove(listener);
	}
	
	/**
	 * @return Returns slider's current maximum value.
	 */
	public float getMin() {
		return min;
	}
	
	/**
	 * @return Returns slider's current maximum value.
	 */
	public float getMax() {
		return max;
	}
	
	/**
	 * Returns slider's current value. Value is between minimum and maximum values;
	 *  
	 * @return Returns slider's current value.
	 */
	public float getValue() {
		return value;
	}
	
	/**
	 * Set value of slider. 
	 * If value is greater than max value, value is set to max value or 
	 * if value is less than min value is set.
	 * 
	 * @param value
	 */
	public void setValue( float value ) {
		
		if( value > max )
			this.value = max;
		
		else if( value < min )
			this.value = min;
		
		else
			this.value = value;
		
		fireSliderValueChanged();
	}	
	
	/**
	 * Set slider's value with given percent. 
	 * Given value have to between 0 - 100.
	 * 
	 * @param percent Given value have to between 0 - 100.
	 */
	public void setPercent( float percent ) {
		if( percent >= 0 && percent <= 100 ) {
			setValue( (max-min)/100*percent );
		}
		
		fireSliderValueChanged();
	}
	
	/**
	 * Returns slider's value in percent format.
	 * 
	 * @return Returns slider's value in percent format. Value is between 0 - 100;
	 */
	public float getPercent() {
		return value / max * 100;
	}
	
	/**
	 * Set how slider value is presented.
	 * 
	 * @param format Set how slider value is presented.
	 */
	public void setSliderFormat( SliderFormat format ) {
		this.format = format;
	}
	
	/**
	 * Returns how slider value is presented.
	 * 
	 * @return Returns how slider value is presented.
	 */
	public SliderFormat getFormat() {
		return format;
	}
	
	public float getStepValue() {
		return stepValue;
	}
	
	/**
	 * Set minimum value of slider
	 * 
	 * @param min
	 */
	public void setMin( float min ) {
		this.min = min;
		
		fireSliderValueChanged();
	}
	
	/**
	 * Set maximum value of slider
	 * 
	 * @param max
	 */
	public void setMax( float max ) {
		this.max = max;
		
		fireSliderValueChanged();
	}
	
	/**
	 * Set location of slider
	 * 	
	 * @param x
	 * @param y
	 */
	public void setPosition( float x, float y ) {
		this.position.x = x;
		this.position.y = y;
	}
	
	/**
	 * Render slider
	 */
	@Override
	public void render(GameContainer cont, Graphics g) {
		         		                
		g.setFont(font);
		
		texture.draw(position.x, position.y);            
		
        //Gray rectangle to present the non-loaded percents
        float lenght = 454 - Math.abs( SLIDER_INNER_WIDTH / max * value);
        
        g.setColor( Color.gray );
        g.fillRect( position.x + 458, position.y + 4, -lenght ,14);
        
        //Loading percent with black color
        g.setColor(Color.black);
        
        if( format == SliderFormat.PERCENT ) {
			float percents = getPercent();
	        
	        g.drawString(new DecimalFormat("0").format(percents)+"%", 
			        			position.x + texture.getWidth()/2 ,
			        			position.y + 5 );
        }
        
        if( format == SliderFormat.VALUE ) {
        	
	        g.drawString(new DecimalFormat("###.##").format(value)+"%", 
        			position.x + texture.getWidth()/2 ,
        			position.y + 5 );
        }
	}

	/**
	 * Updates slider
	 */
	@Override
	public void update(GameContainer cont, int delta) {
		
		Input input = cont.getInput();
		
		if( input.isKeyDown(Input.KEY_LEFT))
			setValue( value - stepValue * delta );
		
		if( input.isKeyDown(Input.KEY_RIGHT))
			setValue( value + stepValue * delta );
		
		//This adjust slider value depending where mouse cursor is.
		if( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) 
			&& shape.contains( input.getMouseX(), input.getMouseY() )) {
			
			float percent = ( input.getMouseX() - position.x) / shape.getWidth(); 
			
			setPercent( percent * 100 );
		}		
	}
	
	/**
	 * Tells all slider listeners that slider value is changed
	 */
	private void fireSliderValueChanged() {
		
		SliderEvent event = new SliderEvent( this.getId(), min, max, value );
		
		for( SliderListener listener : listeners ) {
			listener.sliderValueChanged(event);
		}
	}
	
	/**
	 * 
	 * @author TeeMuki
	 *
	 * SliderEvent class carries slider current state.
	 */
	public class SliderEvent {
		
		/**
		 * Slider id
		 */
		private String id;
		
		/**
		 * Slider minimum and maximum value
		 */
		private float min, max;
		
		/**
		 * Slider value
		 */
		private float value;
		
		/**
		 * SliderEvent constructor
		 * 
		 * @param id Slider id
		 * @param min Slider minimum value
		 * @param max Slider maximum value
		 * @param value Slider current value
		 */
		public SliderEvent( String id,  float min, float max, float value ) {
			this.id = id;
			this.min = min;
			this.max = max;
			this.value = value;
		}
		
		/**
		 * @return Returns slider minimum value
		 */
		public float getMin() {
			return min;
		}
		
		/**
		 * @return Returns slider maximum value
		 */
		public float getMax() {
			return max;
		}
		
		/**
		 * @return Returns current slider value
		 */
		public float getValue() {
			return value;
		}
		
		/**
		 * @return Returns percent value 
		 */
		public float getPercent() {
			return value / max * 100;
		}
		
		/**
		 * @return Returns slider id
		 */
		public String getId() {
			return id;
		}
	}
	
	/**
	 * @author TeeMuki
	 *
	 * All slider listener classes have to implement SliderListener interface.
	 */
	public interface SliderListener {
		public void sliderValueChanged( SliderEvent event );
	}
}
