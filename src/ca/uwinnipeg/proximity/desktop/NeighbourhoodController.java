/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

/**
 * @author Garrett Smith
 *
 */
public class NeighbourhoodController extends PropertyController {

  // The map of regions to the indices of the pixels in their neighbourhoods
  protected Map<Region, List<Integer>> mNeighbourhoods = new HashMap<Region, List<Integer>>();

  // Map each region to the runnable generating it's neighbourhood
  protected Map<Region, NeighbourhoodRunnable> mRunnables = new HashMap<Region, NeighbourhoodRunnable>();
  
  @Override
  protected void onRegionAdded(Region region) {
    super.onRegionAdded(region);
    invalidate(region);
  }
  
  @Override
  protected void onRegionRemoved(Region region) {
    super.onRegionRemoved(region);
    updateNeighbourhood(region, null);
  }
  
  /**
   * Returns a map of regions to the points within their neighbourhoods.
   * @return
   */
  public Map<Region, int[]> getNeighbourhoods() {
    Map<Region, int[]> nhs = new HashMap<Region, int[]>();
    for (Region reg : mNeighbourhoods.keySet()) {
      List<Integer> indices = mNeighbourhoods.get(reg);
      nhs.put(reg, indicesToPoints(indices));
    }
    return nhs;
  }
  
  /**
   * Sets the neighbourhood of the given region.
   * @param region
   * @param indices
   */
  protected void updateNeighbourhood(Region region, List<Integer> indices) {
    // save the change
    if (indices != null) {
      mNeighbourhoods.put(region, indices);
    }
    else {
      mNeighbourhoods.get(region).clear();
    }
    
    // broadcast
    indices = new ArrayList<Integer>();
    for (List<Integer> nbs : mNeighbourhoods.values()) {
      indices.addAll(nbs);
    }
    broadcastValueChanged(indices);    

    // set neighbourhoods of other controllers
    ProximityDesktop.getController().setNeighbourhood(region, indices);
  }

  // Runnableing 
  
  @Override
  public int getProgress() {
    // count the number of runnables running
    int runningRunnables = 0;
    for (NeighbourhoodRunnable runnable : mRunnables.values()) {
      if (runnable.isRunning()) {
        runningRunnables++;
      }
    }
    
    if (runningRunnables != 0) {
      return super.getProgress() / runningRunnables;
    }
    else {
      return MAX_PROGRESS;
    }
  }

  @Override
  protected void invalidate() {
    //clear all neighbourhoods
    mNeighbourhoods.clear();
    
    if (!mRegions.isEmpty()) {
      // update all neighbourhoods
      for (Region r : mRegions) {
        invalidate(r);
      } 
    }
    else {
      // broadcast that the neighbourhoods are empty
      broadcastValueChanged(null);
    }
  }
  
  /**
   * Invalidates the given region, causing its neighbourhood to be recalculated.
   * @param region
   */
  protected void invalidate(Region region) {
    // clear old points
    mNeighbourhoods.put(region, new ArrayList<Integer>());
    
    // Cancel running runnable
    NeighbourhoodRunnable runnable = mRunnables.get(region);
    if (runnable != null && runnable.isRunning()) {
      runnable.cancel();
    }
  
    // run the new runnable
    runnable = new NeighbourhoodRunnable(region);
    mRunnables.put(region, runnable);
    Display.getCurrent().asyncExec(runnable);
  }  

  /**
   * A runnable used to calculate the neighbourhood of a region.
   * @author Garrett Smith
   *
   */
  private class NeighbourhoodRunnable extends PropertyRunnable {
  
    public NeighbourhoodRunnable(Region reg) {
      super(reg);
    }

    @Override
    protected List<Integer> calculateProperty(Region region) {
  
      // check if we should stop because the runnable was cancelled
      if (isCancelled()) return null;
  
      int center = region.getCenterIndex();
      List<Integer> regionPixels = region.getIndicesList();
  
      long startTime = System.currentTimeMillis();
      List<Integer> rtn = mImage.hybridNeighbourhood(
          center, 
          regionPixels, 
          getEpsilon(),
          this);
      System.out.println("Neighbourhood took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
      
      return rtn;
    }
  
    @Override
    protected void onPostRun(List<Integer> result, Region region) {      
      // save the result
      updateNeighbourhood(region, result);
    }
  
  }

}
