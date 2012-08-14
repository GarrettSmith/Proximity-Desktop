/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;
import ca.uwinnipeg.proximity.desktop.history.RemoveRegionAction;

/**
 * An action that deletes the selected regions.
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
    HistoryAction action = new RemoveRegionAction(controller.getSelectedRegions(), controller);
    controller.performAction(action);
  }

}
