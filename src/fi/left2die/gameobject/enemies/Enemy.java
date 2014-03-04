package fi.left2die.gameobject.enemies;

import fi.left2die.gameobject.Entity;
import fi.left2die.gameobject.MovableEntity;

public abstract class Enemy extends MovableEntity{
	
	public short armor = 1;
	public float maxHealt = 100;//Starting point, full health 
	public float currentHealt = 100;//Current health points
	public float damage;
	public boolean touchesPlayer = false;

	public int expirenceFromKill = 0;
	
	public float scoresFromKilling = 5;//How much points are achieved from killing an enemy	
	
	public Enemy( String id, Entity parent, float damage) {
		super(id, parent);
		this.damage = damage;
	}
	
	/**
	 * Event that should happen on a projectile hit.
	 */
	public abstract void onHit();
}
