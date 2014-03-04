package fi.left2die.ui.inputpanel;

/**
 * Input panel event listener have to implement this interface
 * 
 * @author TeeMuki
 */
public interface InputPanelListener {
	
	/**
	 * Event is fired when text inputing ends
	 * 
	 * @param event
	 */
	public void inputPanelTextEntered( InputPanelEvent event );
	
	/**
	 * Event is launched always when text chance
	 * 
	 * @param event
	 */
	public void inputPanelTextChanged( InputPanelEvent event );	
}