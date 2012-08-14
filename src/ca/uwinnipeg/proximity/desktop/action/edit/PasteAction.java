/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * Adds the regions added to the clip board. Currently this does nothing.
 * @author garrett
 *
 */
// TODO: Implement paste action
public class PasteAction extends Action {
  
  public PasteAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Paste.text"));
  }

}
