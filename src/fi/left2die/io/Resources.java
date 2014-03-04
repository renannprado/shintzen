package fi.left2die.io;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Resource class to storage any image or sound resource used in game.
 * By using this class to storage data it prevents images and sound to 
 * be loaded from hard disk multiple times. In different places in source code.
 * 
 * 
 * To add a new resource use addResource method call:
 *
 * Resource.addResource( "helikopterinlapa", new Image("data/images/lapa.png") );
 * 
 * To get a image resource, it gives you a new instance of image resources object:
 * 
 * Resources.getImage( "helikopterinlapa" ):
 * 
 * 
 * @author TeeMuki
 *
 */
public class Resources {

	private static HashMap<String, Object>resources = new HashMap<String, Object>();
	
	private static HashMap<String, ArrayList<Animation>>animations = new HashMap<String,  ArrayList<Animation>>();
	
	public Resources() {}

	public static void addResource( String key, Object image ) {
		resources.put(key, image);
	}
	
	public static void addAnimation( String animationID, 
									 Image[] frames, 
									 int frameDuration,
								     int instanceCount ) {
		
		ArrayList<Animation>anima = new ArrayList<Animation>();
		
		for( int i=0; i<instanceCount; i++ ) {
			
			Animation mation = new Animation(frames, frameDuration);
			mation.stop();
			anima.add(mation);
		}		
		
		animations.put(animationID, anima);
	}
		
	public static Image getImage( String imageID ) {
		return ((Image)resources.get(imageID)).copy();
	}
	
	public static Sound getSound( String soundID ) {
		return (Sound)resources.get(soundID);
	}
	
	public static Music getMusic( String musicID ) {
		return (Music)resources.get(musicID);
	}
	
	public static TiledMap getMap( String mapID) {
		return (TiledMap)resources.get(mapID);
	}
	
	public static UnicodeFont getFont( String fontID ) {
		return (UnicodeFont) resources.get( fontID );
	}

	public static ConfigurableEmitter getEmitter( String emitterID ) {
		return (ConfigurableEmitter) resources.get( emitterID );
	}
	
	public static Object get( String resourceID ) {
		return resources.get(resourceID);
	}
	
	public static void addAnimation( String animationID, Animation mation ) {
		ArrayList<Animation>mations = new ArrayList<Animation>();
		mations.add(mation);
		animations.put(animationID,mations);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<ConfigurableEmitter> getEmitters( String emittersID ) {
		return (ArrayList<ConfigurableEmitter>) resources.get(emittersID);
	}
	
	public static Animation getNextNonPlayingAnimation( String animationID ) {
		ArrayList<Animation>mations = animations.get(animationID);	
		
		for( Animation ani : mations ) {
			if( ani.isStopped() )
				return ani;
		}
		
		return mations.get(mations.size()-1);
	}
	
	public static ArrayList<Animation> getAnimations( String animationID ) {
		return animations.get(animationID);
	}
	
	public static int getSize(){
		return resources.size();
	}

	/**
	 * Removes a resource with given resourceID.
	 * If resource is removed it returns true, 
	 * other cases return value is false.
	 *  
	 * @param resourceID
	 * @return If resource is removed it returns true, 
	 * 		   other cases return value is false.
	 */
	public static boolean remove( String resourceID ) {
		if( resources.containsValue(resourceID) ) {
			resources.remove(resourceID);
			
			return true;
		}
		
		return false;
	}
}
