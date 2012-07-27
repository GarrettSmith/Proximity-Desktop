/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class DeleteAction extends Action {
  
  public DeleteAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Delete.text"));
  }
  
  @Override
  public void run() {
    ProximityController controller = ProximityDesktop.getController();
    controller.removeRegions(controller.getSelectedRegions());
  }

}
