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

  private List<Region> regions;

  private List<Region> dest;

  /**
   * 
   */
  public RemoveRegionAction(List<Region> dest, List<Region> regions) {
    this.dest = dest;
    this.regions = regions;
  }
  
  /**
   * 
   */
  public RemoveRegionAction(List<Region> dest, Region region) {
    this.dest = dest;
    this.regions = new ArrayList<Region>(1);
    regions.add(region);
  }

  public void apply() {
    dest.removeAll(regions);
  }

  public void unapply() {
    dest.addAll(regions);
  }

  public String getName() {
    if (regions.size() == 1) {
      return "Remove Region";
    }
    else {
      return "Remove Regions";
    }
  }

}
