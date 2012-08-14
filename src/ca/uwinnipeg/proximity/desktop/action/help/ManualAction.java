/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.help;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * Show a dialog with the usage manual. This does nothing right now.
 * @author garrett
 *
 */
public class ManualAction extends Action {

  public ManualAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Manual.text"));
  }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }
}
