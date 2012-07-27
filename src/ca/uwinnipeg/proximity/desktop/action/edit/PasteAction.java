/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class PasteAction extends Action {
  
  public PasteAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Paste.text"));
  }

}
