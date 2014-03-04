package fi.left2die.gamestates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import fi.left2die.gameobject.GameWorld;
import fi.left2die.gameobject.Player;
import fi.left2die.io.Resources;
import fi.left2die.library.TimeCounter;

import org.newdawn.slick.imageout.*;

public class PlayingState extends BasicGameState{

	/**
	 * Game world is a world of gaming
	 */
	private GameWorld world;
	
	/**
	 * Game state id;
	 */
	private int id;
	
	/**
	 * 
	 */
	private HighscoreState highscoreState;
	
	private Image paused;
	private Image gameover;
	
	private boolean isScreamOfDeathPlayed = false;
	
	/**
	 * Count playing time
	 */
	private TimeCounter timeCounter = new TimeCounter();
	
	private boolean continueToHighscoreState = false;
	
	public PlayingState( int id, HighscoreState state ) {
		this.id = id;
		this.highscoreState = state;
	}
	
	@Override
	public int getID() {
		return id;
	}

	@Override
	public void init(GameContainer cont, StateBasedGame state)
			throws SlickException {
		
		world = new GameWorld(cont, timeCounter);
		paused = Resources.getImage("paused");
		paused.setAlpha(0);
		gameover = Resources.getImage("gameover");
		gameover.setAlpha(0);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		world.leave(container);
		
		container.setPaused(false);
		timeCounter.stop();
		
		container.setMouseGrabbed(false);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame state) throws SlickException {
		super.enter(container, state);
		
		world.enter(container);
				
		//Reloads keyboard configurations
		world.loadKeyboarConfigurations();
		
		Resources.getMusic("hellmarch").loop(1, 0.1f);
		
		isScreamOfDeathPlayed = false;
		
		timeCounter.start();
		
		container.setMouseGrabbed(true);
	}
	
	@Override
	public void update(GameContainer cont, StateBasedGame state, int delta)throws SlickException {
				
		Input input = cont.getInput();
			
		//If escape is pressed we are entering back main menu
		if( world.isGameEnded() == false && input.isKeyPressed(Input.KEY_ESCAPE)) {
			timeCounter.stop();
			
			state.enterState( Main.MAINMENU_STATE, 
							 new FadeOutTransition(Color.blue), 
							 new FadeInTransition(Color.white));
		}
				
		if( world.isGameEnded() ) {
			timeCounter.stop();
			
			if( isScreamOfDeathPlayed == false ) {
				Resources.getSound("scream").play();
				isScreamOfDeathPlayed = true;
			}
			
			if( input.isKeyPressed(Input.KEY_ENTER) )
				continueToHighscoreState = true;
			
  
			if( continueToHighscoreState ) {
								
				
				
				Player player = world.getPlayerOne();

				//This gives player scores and playing time for the highscore record
				highscoreState.newRecord( player.score, timeCounter.timeElapsedInSeconds() );
				
				//Reset game
				world.resetGame();
				
				//Reset timer
				timeCounter.reset();
				
				//Moves player to highscore state after				
				state.enterState(Main.HIGHSCORE_STATE,
							     new FadeOutTransition(Color.red), 
								 new FadeInTransition(Color.white));
				
				
			}
			
			gameover.setAlpha( gameover.getAlpha() + 0.001f * delta);
		}		
		
		//If game is not ended we update the game
		else 		
			world.update(cont, delta);
		
		if( input.isKeyPressed(Input.KEY_PAUSE)	|| input.isKeyPressed(Input.KEY_P)) {
			
			if( cont.isPaused() ) {
				cont.setPaused(false);
				timeCounter.start();
			}
			else {
				cont.setPaused( true );
				timeCounter.stop();
			}
		}
		
		//Nice looking fading effect
		if( cont.isPaused()) {
			paused.setAlpha( paused.getAlpha() + 0.01f * delta);
		}
		else {
			paused.setAlpha(0);
		}
		
		//Screen Capture
		if (cont.getInput().isKeyPressed( Input.KEY_F10)) {
			Image target = new Image(cont.getWidth(), cont.getHeight());
			cont.getGraphics().copyArea(target, 0, 0);
			ImageOut.write( target.getFlippedCopy(false, true), "screenshot.png", false);
			target.destroy();
		}

	}

	
	@Override
	public void render(GameContainer cont, StateBasedGame state, Graphics g)throws SlickException {
		
		world.render(cont, g);	
		
		if( cont.isPaused() && !world.isGameEnded() ) {		
			paused.draw( (cont.getWidth() - paused.getWidth())/2,
						  cont.getHeight()/2 - paused.getHeight() );
			
		}
					
		if( world.isGameEnded() && !continueToHighscoreState) {		
			gameover.draw((cont.getWidth() - gameover.getWidth())/2,
						  cont.getHeight()/2 - gameover.getHeight()/2 );
		}		
		
		Input input = cont.getInput();
		
		final float radius = 10;
		
		g.setLineWidth(2.3f);
		g.setColor(Color.white);
		g.fillOval( input.getMouseX() - radius, input.getMouseY() - radius, radius * 2 , radius * 2 );
		g.setColor(Color.black);
		g.drawOval( input.getMouseX() - radius, input.getMouseY() - radius, radius * 2 , radius * 2 );
		
	}	
}
