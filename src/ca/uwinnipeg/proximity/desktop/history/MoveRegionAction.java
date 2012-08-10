/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Rectangle;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.Region;

/**
 * @author Garrett Smith
 *
 */
public class MoveRegionAction implements HistoryAction {
  
  private List<Region> mRegions;
  
  private Map<Region, Rectangle> mOldBounds;
  private Map<Region, Rectangle> mNewBounds;
  
  private ProximityController mController;
  
  /**
   * 
   */
  public MoveRegionAction(
      List<Region> regions, 
      Map<Region, Rectangle> oldBounds, 
      Map<Region, Rectangle> newBounds, 
      ProximityController controller) {
    mRegions = new ArrayList<Region>(regions);
    mController = controller;
    mOldBounds = new HashMap<Region, Rectangle>(oldBounds);
    mNewBounds = new HashMap<Region, Rectangle>(newBounds);
  }
  
  /**
   * 
   */
  public MoveRegionAction(
      Region region, 
      Rectangle oldBounds, 
      Rectangle newBounds, 
      ProximityController controller) {
    mRegions = new ArrayList<Region>();
    mRegions.add(region);
    mController = controller;
    mOldBounds = new HashMap<Region, Rectangle>();
    mOldBounds.put(region, oldBounds);
    mNewBounds = new HashMap<Region, Rectangle>();
    mNewBounds.put(region, newBounds);
  }

  public void apply() {
    for (Region reg : mRegions) {
      reg.setBounds(mNewBounds.get(reg));
    }
    mController.regionsModified(mRegions);
  }

  public void unapply() {
    for (Region reg : mRegions) {
      reg.setBounds(mOldBounds.get(reg));
    }
    mController.regionsModified(mRegions);
  }

  public String getName() {
    if (mRegions.size() == 1) {
      return "Move Region";
    }
    else {
      return "Move Regions";
    }
  }


}
