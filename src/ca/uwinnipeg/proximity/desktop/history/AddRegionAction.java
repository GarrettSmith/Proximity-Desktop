/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;


import java.util.List;

import ca.uwinnipeg.proximity.desktop.Region;

/**
 * @author Garrett Smith
 *
 */
public class AddRegionAction implements HistoryAction {
  
  private List<Region> mRegions;
  
  private Region mRegion;

  /**
   * 
   */
  public AddRegionAction(List<Region> regions, Region region) {
    mRegions = regions;
    mRegion = region;
  }

  public void apply() {
    mRegions.add(mRegion);
  }

  public void unapply() {
    mRegions.remove(mRegion);
  }

  public String getName() {
    return "Add Region";
  }

}
