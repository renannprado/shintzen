package fi.left2die.gameobject.enviroment;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.tiled.TiledMap;

import fi.left2die.gameobject.Entity;


public class Map extends Entity{

	private TiledMap tmap;
	
	public static int mapWidth;
	public static int mapHeight;
	
	//layer of the tilemap file which is read
	private static final int layer = 0;
	
	//how big one "square" is
	public static final int TILESIZE = 48;
	
	//map of tiles; blocked or not
	private boolean[][] blocked;
		
	private Rectangle[][] blockedTiles;
	
	private ArrayList<Rectangle>shapesThatAreIntersecting = new ArrayList<Rectangle>(9);
	
	/**
	 * constructor for map
	 * @param ref location of map
	 * @throws SlickException
	 */
	public Map(String ref) throws SlickException{
		
		tmap = new TiledMap(ref, "gamedata/maps/tiles");//loads map
		mapWidth = tmap.getWidth() * TILESIZE;//sets map width in pixels
		mapHeight = tmap.getHeight() * TILESIZE;//sets map height in pixels
		blocked = new boolean[tmap.getWidth()][tmap.getHeight()];		
		blockedTiles = new Rectangle[tmap.getWidth()][tmap.getHeight()];
		
		
		for (int x = 0; x < tmap.getWidth(); x++) {
			for (int y = 0; y < tmap.getHeight(); y++) {
				int tileID = tmap.getTileId(x, y, layer);
				if (tileID == 1) {// first tile in tilemap is blocked
					blockedTiles[x][y] = new Rectangle(x * TILESIZE, y * TILESIZE, TILESIZE, TILESIZE);					
					blocked[x][y]=true;
				}
			}
		}
	}
	
	public void render( int x, int y ) {
		tmap.render(x, y);
		
		
	}
	
	public void renderBlockedTiles( Graphics g ) {
		for (int x = 0; x < tmap.getWidth(); x++) {
			for (int y = 0; y < tmap.getHeight(); y++) {
				if( blockedTiles[x][y] != null ) {
					g.draw(blockedTiles[x][y]);
				}					
			}
		}
	}
	
	public ArrayList<Rectangle> intersect( Shape shape ) {
		
		shapesThatAreIntersecting.clear();
		
		for (int x = 0; x < tmap.getWidth(); x++) {
			for (int y = 0; y < tmap.getHeight(); y++) {
			
				if( blockedTiles[x][y] != null 
					&& blockedTiles[x][y].intersects(shape)) {
					
					shapesThatAreIntersecting.add( blockedTiles[x][y] );
				}					
			}
		}
		
		return shapesThatAreIntersecting;
	}
	
	public Rectangle getBlockedTileRectangle( int x, int y ) {
		return blockedTiles[x][y];
	}
	
	/**
	 * checks if the tile is blocked
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return
	 */
	public boolean blocked(float x, float y){		
		return blocked[(int) x][(int) y];
	}
}
