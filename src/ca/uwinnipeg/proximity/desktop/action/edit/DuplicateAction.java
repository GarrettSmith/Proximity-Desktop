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
public class DuplicateAction extends Action {
  
  public DuplicateAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Duplicate.text"));
  }

}
