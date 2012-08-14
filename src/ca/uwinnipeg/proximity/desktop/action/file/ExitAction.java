/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * An action that exits the program.
 * @author garrett
 *
 */
public class ExitAction extends Action {
  
  public ExitAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Exit.text"));
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().close();
  }

}
