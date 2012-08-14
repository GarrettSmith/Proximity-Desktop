/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;


import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.Region;

/**
 * An action that adds one for many regions to the controller.
 * @author Garrett Smith
 *
 */
public class AddRegionAction implements HistoryAction {
  
  private List<Region> mRegions;
  
  private ProximityController mController;
  
  /**
   * Creates a new {@link HistoryAction} that adds the list of regions to the given controller.
   */
  public AddRegionAction(List<Region> regions, ProximityController controller) {
    mRegions = regions;
    mController = controller;
  }
  
  /**
   * Creates a new {@link HistoryAction} that adds a region to the given controller.
   */
  public AddRegionAction(Region region, ProximityController controller) {
    mRegions = new ArrayList<Region>(1);
    mRegions.add(region);
    mController = controller;
  }

  public void apply() {
    for (Region reg : mRegions) {
      mController.addRegion(reg);
    }
  }

  public void unapply() {
    for (Region reg : mRegions) {
      mController.removeRegion(reg);
    }
  }

  public String getName() {
    if (mRegions.size() == 1) {
      return "Add Region";
    }
    else {
      return "Add Regions";
    }
  }

}
