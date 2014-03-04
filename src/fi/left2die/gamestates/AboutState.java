package fi.left2die.gamestates;

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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import fi.left2die.io.Confiqurations;
import fi.left2die.io.Resources;
import fi.left2die.library.TextRenderer;
import fi.left2die.library.TimeCounter;
import fi.left2die.library.WrapText;
import fi.left2die.library.TextRenderer.TextBehavior;
import fi.left2die.ui.RadioButtonBox;

public class AboutState extends BasicGameState {

	private int id;
	
	//images used in about screen
	private Image topGradient;
	private Image bottomGradient;
	private Image left2DeadLogo;
	
	//image scale
	private float scale = 0.75f;
	private Vector2f logoPos;
	
	//about menu text
	private String text = "The Left2dead game is easy to master. To move your character around, use A, W, D and S keys. " +
							"To shoot enemies move your mouse pointer over enemy and then click a left mouse button. " +
							"Now you can see how enemy's health bar starts to go lower and lower as you shoot it. " +
							"In this stage of development we only have to offer you five different types of weapons. " +
							"First one is a pistol, second is shotgun, third is a AK not the AK-47, but AK. " +
							"Last but not least is the minigun."+
							"To change weapon use numbers one to five. And remember this is only a prototype version! ;) " +
							"Big thanks and respect for Tatu Puukko from the player and enemies textures artwork! ";
	
	private String[] wrappedText;
	
	private UnicodeFont font;
	
	private TextRenderer textRenderer;
			
	/**
	 * constructor
	 * @param id
	 */
	public AboutState(int id) {
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void init(GameContainer cont, StateBasedGame state)
			throws SlickException {
		
		//loads the images
		topGradient = Resources.getImage("about top gradient");
		bottomGradient = Resources.getImage("about bottom gradient");
		left2DeadLogo = Resources.getImage("menu title");
		
		scale = 0.65f;
		logoPos = new Vector2f((cont.getWidth() - left2DeadLogo.getWidth() * scale)/2, 10 );
		
		//sets the font options
		font = Resources.getFont("sketch font");
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		font.loadGlyphs(); 
		
		Rectangle textArea = new Rectangle(0, 0, cont.getWidth() - 100, cont.getHeight());
		
		wrappedText = drawWrapText(text, 40 ); 	
		
		textRenderer = new TextRenderer( new Integer( Confiqurations.getProperty(Confiqurations.FPS )));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {

		container.getInput().removeAllListeners();
	}
	
	@Override
	public void render(GameContainer cont, StateBasedGame state, Graphics g) throws SlickException {		
		//text starting position
		int textX = 20;
		int textY = 80;
		
		g.setColor(Color.red);
		g.setFont(font);
		
		/*
		for( String line : wrappedText ) {
			g.drawString(line, textX, textY);
			textY += 37;
		}*/
		
		
		for( int x = 0; x < cont.getWidth(); x += topGradient.getWidth() ) {
			topGradient.draw( x, 0 );
		}
		
		for( int x = 0; x < cont.getWidth(); x += bottomGradient.getWidth() ) {
			bottomGradient.draw( x, cont.getHeight() - bottomGradient.getHeight() );
		}	
		
		
		left2DeadLogo.draw( logoPos.x, logoPos.y, scale );
		
		
		
		textRenderer.render(cont, g);
	}

	@Override
	public void update(GameContainer cont, StateBasedGame state, int delta)
			throws SlickException {

		
		
		Input input = cont.getInput();
		
		if( input.isKeyPressed(Input.KEY_ESCAPE))
			state.enterState(Main.MAINMENU_STATE,
							  new FadeOutTransition(Color.white), 
							  new FadeInTransition(Color.white));		
		
		if( input.isKeyPressed(Input.KEY_SPACE))
			textRenderer.addText("123432423", 
								new Vector2f( cont.getWidth()/2, cont.getHeight()/2), 
								TextBehavior.SLIDING_UP_SWINNING, 
								new Color(Color.white), 10000);
		
		textRenderer.update(cont, delta);
	}
	/**
	 * uses WrapText class to wrap the text
	 * @param text text to wrap
	 * @param lenght line length
	 * @return
	 */
	private String[] drawWrapText( String text, int  lenght ) {
		return WrapText.wrapText(text, lenght ); 
	}
}
