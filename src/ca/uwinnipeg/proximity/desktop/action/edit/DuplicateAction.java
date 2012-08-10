/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;
import ca.uwinnipeg.proximity.desktop.Region;
import ca.uwinnipeg.proximity.desktop.history.AddRegionAction;
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;

/**
 * @author garrett
 *
 */
public class DuplicateAction extends Action {
  
  public DuplicateAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Duplicate.text"));
  }
  
  @Override
  public void run() {
    ProximityController controller = ProximityDesktop.getController();
    List<Region> newRegions = new ArrayList<Region>();
    for (Region r : controller.getSelectedRegions()) {
      newRegions.add(new Region(r));
    }
    HistoryAction action = new AddRegionAction(newRegions, controller);
    controller.performAction(action);
  }

}
