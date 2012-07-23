/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;

/**
 * An action that can be undone and redone.
 * @author Garrett Smith
 *
 */
public interface HistoryAction {
  
  /**
   * Applies the action. This is what should be called when applying the action for the first time 
   * or redoing the action.
   */
  public void apply();
  
  /**
   * Undoes the action.
   */
  public void unapply();
  
  /**
   * Returns the name of the action to be displayed in the menu.
   * @return
   */
  public String getName();

}
