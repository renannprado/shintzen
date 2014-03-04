package fi.left2die.library;

import java.util.ArrayList;

import javax.crypto.spec.PSource;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.transition.RotateTransition;

import fi.left2die.gameobject.IGameObject;
import fi.left2die.gameobject.enemies.BasicZombie.Behavior;
import fi.left2die.io.Resources;

public class TextRenderer implements IGameObject{

	private ArrayList<Text> texts = new ArrayList<Text>();
		
	private UnicodeFont font;
	
	private int fps;
	
	public enum TextBehavior {
		SLIDING_LEFT, SLIDING_UP, SLIDING_DOWN, SLIDING_RIGHT, SLIDING_UP_SWINNING;
	}
	
	public TextRenderer( int fps ) throws SlickException {
		font = Resources.getFont("sketch font small");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs();
		this.fps = fps; 
		System.out.println("fps" + fps);
	}
	
	public void addText( String text,Vector2f position, TextBehavior behavior, Color textColor, int livingTime ) {		
		Text textEffect = new Text( text, position,livingTime, textColor, font, behavior );
		texts.add(textEffect);
	}
	
	@Override
	public void render(GameContainer cont, Graphics g) {
		for( int i=0; i<texts.size(); i++ ) {
			texts.get(i).render(cont, g);
		}
	}

	@Override
	public void update(GameContainer cont, int delta) {
		for( int i=0; i<texts.size(); i++ ) {
			Text text = texts.get(i);
			
			text.update(cont, delta);			
			
			if( text.isAlive() == false ) {				
				texts.remove(text);
				i--;				
			}
		}		
	}
	
	public class Text implements IGameObject {
		
		private long id;
		
		private Vector2f position;
		
		private Vector2f velocity;
		
		private float rotation;
		
		private String text;
		
		private StopWatch watch = new StopWatch();
		private int lifeTimeMS;
		
		private Color textColor;
		
		private UnicodeFont font;
		
		private TextBehavior behavior;
		
		private float colorFadingStepSize;
		
		private float swingStep;

		public Text(String text, Vector2f position, int lifeTimeMS, Color textColor,
				UnicodeFont font, TextBehavior behavior ) {
			
			this.id = UniqueID.get();
			this.text = text;
			this.lifeTimeMS = lifeTimeMS;
			this.textColor = textColor;
			this.font = font;
			this.behavior = behavior;
			this.rotation = 0;
			this.position = position;
			this.watch.start();
			this.colorFadingStepSize = fps*1f / lifeTimeMS ;
						
			if( TextBehavior.SLIDING_UP == behavior )
				this.velocity = new Vector2f(0f, -0.05f );
			
			if( TextBehavior.SLIDING_DOWN == behavior )
				this.velocity = new Vector2f(0f, 0.05f );
				
			if( TextBehavior.SLIDING_LEFT == behavior )
				this.velocity = new Vector2f(-0.05f, 0f );
					
			if( TextBehavior.SLIDING_RIGHT == behavior )
				this.velocity = new Vector2f(0.05f, 0f );
			
			if( TextBehavior.SLIDING_UP_SWINNING == behavior )
				this.velocity = new Vector2f(0, -0.5f );
		}		
		
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public long getLifeTimeMS() {
			return lifeTimeMS;
		}
		
		public boolean isAlive() {
			return watch.getElapsedTime() < lifeTimeMS; 
		}

		public void setLifeTimeMS(int lifeTimeMS) {
			this.lifeTimeMS = lifeTimeMS;
		}

		public Color getTextColor() {
			return textColor;
		}

		public void setTextColor(Color textColor) {
			this.textColor = textColor;
		}

		public UnicodeFont getFont() {
			return font;
		}

		public void setFont(UnicodeFont font) {
			this.font = font;
		}

		@Override
		public void render(GameContainer cont, Graphics g) {
			g.setColor(textColor);
			g.setFont(font);
			g.drawString(text, position.x, position.y);						
		}

		@Override
		public void update(GameContainer cont, int delta) {
			
			final float swingWidth = 3;
			
			switch( behavior ) {
			
				case SLIDING_UP_SWINNING :
					
				position.x += Math.sin(swingStep) * swingWidth;
				position.y += velocity.y;				
				swingStep += 0.01f * delta;
				
				break;
			
				default:
					position.x += velocity.x * delta;
					position.y += velocity.y * delta;
				break;
			}
			
			
			textColor.a -= colorFadingStepSize;
		}	
	}
}
