package fi.left2die.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Input;

/**
 * 
 * @author TeeMuki
 *
 * This KeyboardConfig class holds player keyboard configurations.
 */
public class KeyboardConfig implements Serializable{
		
	/**
	 * Randomly generated serialVersionUID
	 */
	private static final long serialVersionUID = -8721077199596197645L;

	public KeyboardConfig() {}
	
	public KeyboardConfig(int moveRight, int moveLeft, int moveUp, int moveDown,
			int shoot, int selectWeapon1, int selectWeapon2, int selectWeapon3,
			int selectWeapon4, int selectWeapon5, int selectWeapon6,
			int nextWeapon, int previousWeapon) {
		
		this.moveRight = moveRight;
		this.moveLeft = moveLeft;
		this.moveUp = moveUp;
		this.moveDown = moveDown;
		this.shoot = shoot;
		this.selectWeapon1 = selectWeapon1;
		this.selectWeapon2 = selectWeapon2;
		this.selectWeapon3 = selectWeapon3;
		this.selectWeapon4 = selectWeapon4;
		this.selectWeapon5 = selectWeapon5;
		this.selectWeapon6 = selectWeapon6;
		this.nextWeapon = nextWeapon;
		this.previousWeapon = previousWeapon;
	}

	public void setConfigs( KeyboardConfig config ) {	
		this.moveRight = config.moveRight;
		this.moveLeft = config.moveLeft;
		this.moveUp = config.moveUp;
		this.moveDown = config.moveDown;
		this.shoot = config.shoot;
		this.selectWeapon1 = config.selectWeapon1;
		this.selectWeapon2 = config.selectWeapon2;
		this.selectWeapon3 = config.selectWeapon3;
		this.selectWeapon4 = config.selectWeapon4;
		this.selectWeapon5 = config.selectWeapon5;
		this.selectWeapon6 = config.selectWeapon6;
		this.nextWeapon = config.nextWeapon;
		this.previousWeapon = config.previousWeapon;
	}
	
	public int moveRight;
	public int moveLeft;
	public int moveUp;
	public int moveDown;
	
	public int shoot;
	
	public int selectWeapon1;
	
	public int selectWeapon2;
	
	public int selectWeapon3;
	
	public int selectWeapon4;
	
	public int selectWeapon5;
	
	public int selectWeapon6;
	
	public int nextWeapon;
	public int previousWeapon;
	
	public static HashMap<Integer, String> getMouseKeys() {
		HashMap<Integer, String>keys = new HashMap<Integer, String>();
		
		keys.put( Input.MOUSE_LEFT_BUTTON, "Mouse Left Button");
		keys.put( Input.MOUSE_MIDDLE_BUTTON, "Mouse Middle Button");
		keys.put( Input.MOUSE_RIGHT_BUTTON, "Mouse Right Button");
		
		return keys;
	}
	
	public static HashMap<Integer,String> getKeyboardKeys() {
		
		HashMap<Integer, String>keys = new HashMap<Integer, String>();
		
		keys.put( Input.KEY_0, "0"); keys.put( Input.KEY_1, "1");
		keys.put( Input.KEY_2, "2"); keys.put( Input.KEY_3, "3");
		keys.put( Input.KEY_4, "4"); keys.put( Input.KEY_5, "5");
		keys.put( Input.KEY_6, "6"); keys.put( Input.KEY_7, "7");
		keys.put( Input.KEY_8, "8"); keys.put( Input.KEY_9, "9");
		
		keys.put( Input.KEY_A, "A");keys.put( Input.KEY_B, "B");
		keys.put( Input.KEY_C, "C");keys.put( Input.KEY_D, "D");		
		keys.put( Input.KEY_E, "E");keys.put( Input.KEY_F, "F");
		keys.put( Input.KEY_G, "G");keys.put( Input.KEY_H, "H");
		keys.put( Input.KEY_I, "I");keys.put( Input.KEY_J, "J");
		keys.put( Input.KEY_K, "K");keys.put( Input.KEY_L, "L");
		keys.put( Input.KEY_M, "M");keys.put( Input.KEY_N, "N");
		keys.put( Input.KEY_O, "O");keys.put( Input.KEY_P, "P");
		keys.put( Input.KEY_Q, "Q");keys.put( Input.KEY_R, "R");
		keys.put( Input.KEY_S, "S");keys.put( Input.KEY_T, "T");
		keys.put( Input.KEY_U, "U");keys.put( Input.KEY_Y, "Y");
		keys.put( Input.KEY_V, "V");keys.put( Input.KEY_W, "W");		
		keys.put( Input.KEY_X, "X");keys.put( Input.KEY_Z, "Z");		
		
		keys.put( Input.KEY_LEFT, "LEFT");
		keys.put( Input.KEY_RIGHT, "RIGHT");
		keys.put( Input.KEY_UP, "UP");
		keys.put( Input.KEY_DOWN, "DOWN");
				
		keys.put( Input.KEY_PAUSE, "Pause");
		keys.put( Input.KEY_SYSRQ, "Print Screen / SysRq");
		
		keys.put( Input.KEY_BACKSLASH, "Backslash");
		keys.put( Input.KEY_SPACE, "Space");
		keys.put( Input.KEY_TAB, "Tab");
		
		keys.put( Input.KEY_LCONTROL, "Left Control");
		keys.put( Input.KEY_RCONTROL, "Right Control");
		
		
		keys.put( Input.KEY_LALT, "Left Alt");
		keys.put( Input.KEY_RALT, "Right Alt");
		
		keys.put( Input.KEY_LSHIFT, "Left Shift");
		keys.put( Input.KEY_RSHIFT, "Right Shift");
		
		keys.put( Input.KEY_F1, "F1");keys.put( Input.KEY_F2, "F2");
		keys.put( Input.KEY_F3, "F3");keys.put( Input.KEY_F4, "F4");
		keys.put( Input.KEY_F5, "F5");keys.put( Input.KEY_F6, "F6");
		keys.put( Input.KEY_F7, "F7");keys.put( Input.KEY_F8, "F8");
		keys.put( Input.KEY_F9, "F9");keys.put( Input.KEY_F10, "F10");
		keys.put( Input.KEY_F11, "F11");keys.put( Input.KEY_F11, "F12");
		
		keys.put( Input.KEY_NUMPAD0, "Num 0");keys.put( Input.KEY_NUMPAD1, "Num 1");
		keys.put( Input.KEY_NUMPAD2, "Num 2");keys.put( Input.KEY_NUMPAD3, "Num 3");
		keys.put( Input.KEY_NUMPAD4, "Num 4");keys.put( Input.KEY_NUMPAD5, "Num 5");
		keys.put( Input.KEY_NUMPAD6, "Num 6");keys.put( Input.KEY_NUMPAD7, "Num 7");
		keys.put( Input.KEY_NUMPAD8, "Num 8");keys.put( Input.KEY_NUMPAD9, "Num 9");
		
		return keys;		
	}
	
	public void load( File file ) throws IOException, ClassNotFoundException {
	   if( file.exists() ) {
		   FileInputStream fileIn = new FileInputStream( file );
		   ObjectInputStream in = new ObjectInputStream(fileIn);
		   KeyboardConfig configs = (KeyboardConfig) in.readObject();
		   setConfigs(configs);
		   in.close();
		   fileIn.close();  
	   }	       
	}
	
	public void save( File file ) throws IOException {
				
		boolean isThereAfile = false;
		
		if( file.isFile() ) {
			file.delete();
		}
		
		isThereAfile = file.createNewFile();
				
		if( isThereAfile ) {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		}		
	}
}
