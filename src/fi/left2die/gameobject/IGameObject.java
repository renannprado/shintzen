package fi.left2die.gameobject;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Simple interface with defines a game object. 
 * Game object is class with has render and update method.
 * 
 * @author TeeMuki
 */
public interface IGameObject {
	
	/**
	 * Updates game object
	 *  
	 * @param cont
	 * @param delta
	 */
	public void update(GameContainer cont, int delta);
	
	/**
	 * Renders game object
	 * 
	 * @param cont
	 * @param g
	 */
	public void render(GameContainer cont, Graphics g);
}
