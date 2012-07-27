/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;


import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.desktop.Region;

/**
 * @author Garrett Smith
 *
 */
public class AddRegionAction implements HistoryAction {
  
  private List<Region> mRegions;

  private List<Region> mDest;

  /**
   * 
   */
  public AddRegionAction(List<Region> dest, List<Region> regions) {
    this.mDest = dest;
    this.mRegions = regions;
  }
  
  /**
   * 
   */
  public AddRegionAction(List<Region> dest, Region region) {
    this.mDest = dest;
    this.mRegions = new ArrayList<Region>(1);
    mRegions.add(region);
  }

  public void apply() {
    mDest.addAll(mRegions);
  }

  public void unapply() {
    mDest.removeAll(mRegions);
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
