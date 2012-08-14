/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;

import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.Region;

/**
 * A {@link HistoryAction} that removes one or more regions from the controller.
 * @author Garrett Smith
 *
 */
public class RemoveRegionAction implements HistoryAction {

  private List<Region> mRegions;
  
  private ProximityController mController;
  
  /**
   * 
   */
  public RemoveRegionAction(List<Region> regions, ProximityController controller) {
    mRegions = regions;
    mController = controller;
  }
  
  /**
   * 
   */
  public RemoveRegionAction(Region region, ProximityController controller) {
    mRegions = new ArrayList<Region>(1);
    mRegions.add(region);
    mController = controller;
  }

  public void apply() {
    for (Region reg : mRegions) {
      mController.removeRegion(reg);
    }
  }

  public void unapply() {
    for (Region reg : mRegions) {
      mController.addRegion(reg);
    }
  }

  public String getName() {
    if (mRegions.size() == 1) {
      return "Remove Region";
    }
    else {
      return "Remove Regions";
    }
  }

}
