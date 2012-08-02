/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;

/**
 * @author Garrett Smith
 *
 */
public abstract class LinearPropertyController extends PropertyController {

  // The indices of the pixels selected by the property
  protected List<Integer> mValue = new ArrayList<Integer>();
  
  // The list of operations that will bring us to our desired result 
  protected LinearRunnable mCurrentRunnable = null;
  
  // The list of regions to be used by intersect runnables
  protected List<Region> mQueue = new ArrayList<Region>(); 
  
  @Override
  protected void onRegionAdded(Region region) {
    super.onRegionAdded(region);
    addRunnable(region);
  }
  
  @Override
  protected void onRegionRemoved(Region region) {
    // if the region has yet to be processed remove it from the queue
    if (mQueue.contains(region)) {
      mQueue.remove(region);
    }
    // else recalculate the intersection
    else {
      invalidate();
    }
  }
  
  /**
   * Returns the current list of indices calculated to form the property.
   * @return
   */
  protected List<Integer> getValue() {
    return mValue;
  }
  
  /**
   * Sets and broadcasts the current general value calculated for the property.
   * @param indices
   */
  protected void setValue(List<Integer> indices) {
    // save the new intersection
    mValue.clear();
    if (indices != null) mValue.addAll(indices);
    
    // broadcast the change if we are finished calculating
    if (mQueue.isEmpty()) {
      broadcastValueChanged(mValue);
    }
  }

  @Override
  public int getProgress() {
    int runnables = mQueue.size();
    
    // add the current runnable to the count if it is running
    if (mCurrentRunnable != null && mCurrentRunnable.isRunning()) {
      runnables++;
    }
    
    if (runnables > 0) {
      return (super.getProgress() / runnables);
    }
    else {
      return MAX_PROGRESS;
    }
  }

  /**
   * Recalculates the property starting from the first region added.
   */
  protected void invalidate() {
    // stop all runnables
    // cancel running runnable
    if (mCurrentRunnable != null && mCurrentRunnable.isRunning()) mCurrentRunnable.cancel();
    // clear upcoming runnables
    mQueue.clear();
    // clear calculated value
    mValue.clear();
    
    // add all regions to the queue to be recalculated
    for (Region r : mRegions) {
      mQueue.add(r);
    }    
    // start running runnables
    runNextRunnable();
    
    // broadcast the clear
    //broadcastValueChanged(null);
  }
  

  /**
   * Adds a runnable to the queue for the given region.
   * @param region
   */
  protected void addRunnable(Region region) {
    // add the region to the queue
    mQueue.add(region);
    
    // run if this is the only region in the queue
    if (mQueue.size() == 1) {
      runNextRunnable();
    }
      
  }

  /**
   * Runs the next runnable in the queue if it is non empty.
   */
  protected void runNextRunnable() {
    // remove the reference to the previous runnable
    mCurrentRunnable = null;
    // only run if there are regions in the queue
    if (!mQueue.isEmpty()) {
      
      // run the runnable on the next region
      Region region = mQueue.remove(0);
      mCurrentRunnable = new LinearRunnable(region);

      // start the runnable
      Display.getCurrent().asyncExec(mCurrentRunnable);
    }
  }

  /**
   * A runnable that calculates a value and runs the next runnable in the queue.
   * @author Garrett Smith
   *
   */
  protected class LinearRunnable extends PropertyRunnable {

    public LinearRunnable(Region reg) {
      super(reg);
    }

    @Override
    protected List<Integer> calculateProperty(Region region) {
      return getProperty(region, this);
    }

    @Override
    protected void onPostRun(List<Integer> result, Region region) {   
      setResult(result, region);
      // run the next runnable if there is one
      runNextRunnable();
    }
  }
  
  /**
   * This method should be overridden by subclasses to calculate their specific property.
   * @param region
   * @param sub
   * @return
   */
  protected abstract List<Integer> getProperty(Region region, PerceptualSystemSubscriber sub);

  /**
   * Sets the result of calculating using the given region to the given value.
   * @param result
   * @param region
   */
  protected void setResult(List<Integer> result, Region region) {    
    // store result as the new value
    setValue(result);
  }

}
