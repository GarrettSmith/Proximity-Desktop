/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * An action that adds the selected regions to the clip board. This currently does nothing.
 * @author garrett
 *
 */
// TODO: Implement copy action
public class CopyAction extends Action {

  public CopyAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Cut.text"));
  }
}
