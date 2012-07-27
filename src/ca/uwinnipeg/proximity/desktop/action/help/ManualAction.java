/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.help;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
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
