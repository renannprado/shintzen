package fi.left2die.library;

/**
 * Used to calculate all game play time. 
 * Single TimeRecord holds starting and ending time of playing event.
 * 
 * @author TeeMuki
 */
public class TimeCounter {

	/**
	 * Hold elapsed time as in milliseconds
	 */
	private long timeElapsed;
	
	/**
	 * Last time when start method was called
	 */
	private long lastTimeStarted;
	
	/**
	 * Is timer active
	 */
	private boolean active = false;
	
	/**
	 * Start the timer
	 */
	public void start() {	
		update();	
		lastTimeStarted = System.currentTimeMillis();
		active = true;		
	}
	
	private void update() {
		if( lastTimeStarted > 0 ) {
			timeElapsed += System.currentTimeMillis() - lastTimeStarted;
		}	
	}
	
	/**
	 * @return If timer is active it returns true and false if it's stopped.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Stop the timer
	 */
	public void stop() {
		update();
		lastTimeStarted = 0;
		active = false;				
	}	
	
	/**
	 * Reset timer
	 */
	public void reset() {
		timeElapsed = 0;
		lastTimeStarted = 0;
		active = false;		
	}
	
	/**
	 * @return Returns elapsed time in milliseconds
	 */	
	public long timeElabsedInMilliseconds() {
		if( active )
			return (timeElapsed + System.currentTimeMillis() - lastTimeStarted); 
		else
			return timeElapsed;
	}
	
	/**
	 * @return Returns elabsed time in seconds 
	 */
	public float timeElapsedInSeconds() {
		return timeElabsedInMilliseconds() / 1000f;
	}	
}
