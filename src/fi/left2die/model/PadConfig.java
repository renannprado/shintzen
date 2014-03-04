package fi.left2die.model;

/**
 * This class holds a game controller configurations. 
 * Classes properties are public for performance issues, each one of the properties are 
 * accessed more than 60 time in seconds. The accessing times totally depends on current FPS.
 * 
 * @author TeeMuki
 */
public class PadConfig {
	/**
	 * Witch one game controller is on use
	 */
	public int gameContIndex;
	
	/**
	 * Index of horizontal movement axis
	 */
	public int padHorizontalMovement;
	
	/**
	 * Index of vertical movement axis
	 */
	public int padVerticalMovement;
	
	/**
	 * Index of horizontal looking axis
	 */	
	public int padHorizontalLooking;
	
	/**
	 * Index of vertical looking axis
	 */
	public int padVerticalLooking;
	
	/**
	 * Game controller input values that are smoler than deadzone are not used any way.
	 */
	public float deadZone;

	/**
	 * Constructor for PadConfig class 
	 * 
	 * @param gameControllerIndex Witch one game controller is on use
	 * @param padHorizontalMovement Index of horizontal movement axis
	 * @param padVerticalMovement Index of vertical movement axis
	 * @param padHorizontalLooking Index of horizontal looking axis
	 * @param padVerticalLooking Index of vertical looking axis
	 */
	public PadConfig(int gameContIndex, int padHorizontalMovement,
			int padVerticalMovement, int padHorizontalLooking,
			int padVerticalLooking, float deadZone ) {
		
		this.gameContIndex = gameContIndex;
		this.padHorizontalMovement = padHorizontalMovement;
		this.padVerticalMovement = padVerticalMovement;
		this.padHorizontalLooking = padHorizontalLooking;
		this.padVerticalLooking = padVerticalLooking;
		this.deadZone = deadZone;
	}	
	
	public float getDeadZone() {
		return deadZone;
	}

	public void setDeadZone(float deadZone) {
		this.deadZone = deadZone;
	}



	public int getGameContIndex() {
		return gameContIndex;
	}

	public void setGameContIndex(int gameContIndex) {
		this.gameContIndex = gameContIndex;
	}

	public int getPadHorizontalMovement() {
		return padHorizontalMovement;
	}

	public void setPadHorizontalMovement(int padHorizontalMovement) {
		this.padHorizontalMovement = padHorizontalMovement;
	}

	public int getPadVerticalMovement() {
		return padVerticalMovement;
	}

	public void setPadVerticalMovement(int padVerticalMovement) {
		this.padVerticalMovement = padVerticalMovement;
	}

	public int getPadHorizontalLooking() {
		return padHorizontalLooking;
	}

	public void setPadHorizontalLooking(int padHorizontalLooking) {
		this.padHorizontalLooking = padHorizontalLooking;
	}

	public int getPadVerticalLooking() {
		return padVerticalLooking;
	}

	public void setPadVerticalLooking(int padVerticalLooking) {
		this.padVerticalLooking = padVerticalLooking;
	}
}
