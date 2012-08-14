/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * An action that deletes the selected regions and adds them to the clip board. This currently does
 * nothing.
 * @author garrett
 *
 */
// TODO: implement cut action
public class CutAction extends Action {
  
  public CutAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Cut.text"));
  }

}
