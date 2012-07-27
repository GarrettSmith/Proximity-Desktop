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
public class CopyAction extends Action {

  public CopyAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Cut.text"));
  }
}
