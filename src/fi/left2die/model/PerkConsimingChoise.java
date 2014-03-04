package fi.left2die.model;

import fi.left2die.gameobject.Player;


public abstract class PerkConsimingChoise {
	
	private int perkCost;
	private String text;
	private int availableOnLevel;
	private long radioBtnBoxItemID;
	
	public PerkConsimingChoise( int perkCost, String text, int availableOnLevel ) {
		this.perkCost = perkCost;
		this.text = text;
		this.availableOnLevel = availableOnLevel;
	}
	
	public PerkConsimingChoise( int perkCost, String text, int availableOnLevel, long radioBtnBoxItemID ) {
		this.perkCost = perkCost;
		this.text = text;
		this.availableOnLevel = availableOnLevel;
		this.radioBtnBoxItemID  = radioBtnBoxItemID;
	}
	
	public abstract void consumePerk( Player player );		
	
	public void setAvailableOnLevel(int availableOnLevel) {
		this.availableOnLevel = availableOnLevel;
	}
	
	public void setPerkCost(int perkCost) {
		this.perkCost = perkCost;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public long getRadioBtnBoxItemID() {
		return radioBtnBoxItemID;
	}
	
	public int getAvailableOnLevel() {
		return availableOnLevel;
	}
	
	public int getPerkCost() {
		return perkCost;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isSameID( int id ) {
		return this.radioBtnBoxItemID == id;
	}
}	