package fi.left2die.ui.inputpanel;

/**
 * InputPanelEvent carries input panel event source information
 * 
 * @author TeeMuki
 */
public class InputPanelEvent {
	
	private String text;
	
	/**
	 * InputPanelEvent constructor
	 *  
	 * @param text Text written with input panel
	 */
	public InputPanelEvent(String text) {
		this.text = text;
	}
	
	/**
	 * @return Returns text written with input panel
	 */
	public String getText() {
		return text;
	}	
}
