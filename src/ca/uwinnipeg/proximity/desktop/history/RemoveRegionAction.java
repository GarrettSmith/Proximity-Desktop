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
public class RemoveRegionAction implements HistoryAction {

  private List<Region> mRegions;

  private List<Region> mDest;

  /**
   * 
   */
  public RemoveRegionAction(List<Region> dest, List<Region> regions) {
    this.mDest = dest;
    this.mRegions = regions;
  }
  
  /**
   * 
   */
  public RemoveRegionAction(List<Region> dest, Region region) {
    this.mDest = dest;
    this.mRegions = new ArrayList<Region>(1);
    mRegions.add(region);
  }

  public void apply() {
    mDest.removeAll(mRegions);
  }

  public void unapply() {
    mDest.addAll(mRegions);
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
