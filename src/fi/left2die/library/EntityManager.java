package fi.left2die.library;

import java.util.HashMap;
import java.util.List;

import fi.left2die.gameobject.Entity;

/**
 * The Entity Manager holds reusable entities on storage.
 * 
 * @author TeeMuki
 */
public class EntityManager {
	
	/**
	 * Holds and stores reusable objects of gameWorld
	 */
	private HashMap<String,List<Entity>>entities= new HashMap<String, List<Entity>>();
	
	public EntityManager() {}
	
	/**
	 * Add entity group to manager
	 * 
	 * @param entitiesID
	 * @param list Entity group
	 */
	public void add( String entitiesID, List<Entity>list ) {
		entities.put(entitiesID,list);
	}
	
	/**
	 * Returns entity group from manager
	 * 
	 * @param entitiesID
	 * @return Entity group
	 */
	public List<Entity>getEntities(String entitiesID) {
		return entities.get(entitiesID);
	}	
}
