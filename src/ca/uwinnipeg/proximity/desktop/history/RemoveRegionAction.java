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
public class RemoveRegionAction implements HistoryAction {

  private List<Region> mRegions;
  
  private Region mRegion;

  /**
   * 
   */
  public RemoveRegionAction(List<Region> regions, Region region) {
    mRegions = regions;
    mRegion = region;
  }

  public void apply() {
    mRegions.remove(mRegion);
  }

  public void unapply() {
    mRegions.add(mRegion);
  }

  public String getName() {
    return "Remove Region";
  }

}
