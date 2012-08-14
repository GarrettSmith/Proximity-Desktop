/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * An empty action that does diddly.
 * @author garrett
 *
 */
public class EmptyAction extends Action {
  
  public EmptyAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Empty.text"));
    setEnabled(false);
  }

}
