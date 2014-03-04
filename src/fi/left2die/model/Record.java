package fi.left2die.model;

import java.io.Serializable;

import fi.left2die.library.UniqueID;

/**
 *  A data class holding one game record. 
 * 
 * @author TeeMuki 
 */
public class Record implements Serializable, Comparable<Record> {
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identification number
	 */
	private long id = 0;
	
	/**
	 * Record creation time
	 */
	private long creationTime;
	
	/**
	 * Record holder name or nick
	 */
	private String name;
	
	/**
	 * Scores
	 */
	private long score;
	
	/**
	 * How long player game last
	 */
	private float survivingTime;
	
	/**
	 * Default constructor
	 */
	public Record() {
		this.id = UniqueID.get();
		this.creationTime = System.currentTimeMillis();
	}
	
	/**
	 * Create new record using this constructor
	 * 
	 * @param name Player name or nick
	 * @param score Player scores
	 * @param survivingTime How long game last in milliseconds
	 */
	public Record( String name, long score, float survivingTime ) {
		this();
		this.name = name;
		this.score = score;
		this.survivingTime = survivingTime;
	}
	
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
	/**
	 * @return Returns creationg time
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
	/**
	 * @return Returns record holder name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set record holder name
	 * 
	 * @param name Record holder name or nick
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns scores
	 */
	public long getScore() {
		return score;
	}
	
	/**
	 * Set scores
	 * 
	 * @param score
	 */
	public void setScore(long score) {
		this.score = score;
	}
	
	/**
	 * @return Returns surviving time on milliseconds
	 */
	public float getSurvivingTime() {
		return survivingTime;
	}
	
	/**
	 * Set surviving time
	 * 
	 * @param survivingTime Surviving time on milliseconds
	 */
	public void setSurvivingTime(float survivingTime) {
		this.survivingTime = survivingTime;
	}
	
	@Override
	public String toString() {
		return "Name: " + name + " Score: " + score;
	}

	/**
	 * Used to help	 sorting records
	 */
	@Override
	public int compareTo(Record record ) {
		if( record.score < this.score )
			return -1;
		else
			return 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if( obj instanceof Record ) {
			if( ((Record)obj).id == this.id )
				return true;
			else
				return false;
		}
		
		return false;
	}
}