/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;


import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.Region;

/**
 * @author Garrett Smith
 *
 */
public class AddRegionAction implements HistoryAction {
  
  private List<Region> mRegions;
  
  private ProximityController mController;
  
  /**
   * 
   */
  public AddRegionAction(List<Region> regions, ProximityController controller) {
    mRegions = regions;
    mController = controller;
  }
  
  /**
   * 
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
