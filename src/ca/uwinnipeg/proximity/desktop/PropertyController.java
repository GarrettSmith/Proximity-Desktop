/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;
import ca.uwinnipeg.proximity.image.Image;

/**
 * An object used to calculate a propery of an image given the perceptual system, regions, and
 * other settings.
 * @author Garrett Smith
 *
 */
public abstract class PropertyController<T> {

  // the maximum progress value
  public static final int MAX_PROGRESS = 100;
  
  // the current progress of calculating
  protected int mProgress = MAX_PROGRESS;
  
  // The image system 
  protected Image mImage;
  
  // The regions of interest
  protected List<Region> mRegions = new ArrayList<Region>();
  
  // the neighbourhoods of the regions
  protected Map<Region, List<Integer>> mNeighbourhoods = new HashMap<Region, List<Integer>>();
  
  protected float mEpsilon;
  protected boolean mUseNeighbourhoods =
      Preferences.userRoot().node("proximity-system").node("use-neighbourhoods").getBoolean(getClass().toString(), false);
  
  /**
   * Sets the epsilon value to be used to calculate the property.
   * @param epsilon
   */
  public void setEpsilon(float epsilon) {
    boolean changed = epsilon != mEpsilon;
    mEpsilon = epsilon;
    if (changed) {
      invalidate();
    }
  }
  
  /**
   * Gets the epsilon value sued to calculate the property.
   * @return
   */
  public float getEpsilon() {
    return mEpsilon;
  }

  /**
   * Sets whether or not the neighbourhoods of regions should be used instead of the all the pixels
   * within the regions.
   * @param enabled
   */
  public void setUseNeighbourhoods(boolean enabled) {
    boolean changed = enabled != mUseNeighbourhoods;
    mUseNeighbourhoods = enabled;
    if (changed) {
      invalidate();
    }
  }
  
  /**
   * Gets whether the neighbourhoods are being used to calculate the property. 
   * @return
   */
  public boolean getUseNeighbourhoods() {
    return mUseNeighbourhoods;
  }
  
  /**
   * Sets the currently calculated neighbourhood for the given region.
   * @param region
   * @param neighbourhood
   */
  public void setNeighbourhood(Region region, List<Integer> neighbourhood) {
    mNeighbourhoods.put(region, neighbourhood);
    onNeighbourhoodSet(region, neighbourhood);
  }
  
  /**
   * Returns the currently calculated neighbourhood for the given region.
   * @param region
   * @return
   */
  public List<Integer> getNeighbourhood(Region region) {
    return mNeighbourhoods.get(region);
  }
  
  /**
   * Sets the given image we are calculating within.
   * @param image
   */
  public void setup(Image image) {
    mImage = image;
  }

  /**
   * Adds a region to the service.
   * @param region
   */
  public void addRegion(Region region) {
    mRegions.add(region);
    onRegionAdded(region);
  }
  
  /**
   * Removes a region from the service.
   * @param region
   */
  public void removeRegion(Region region) {
    mRegions.remove(region);
    mNeighbourhoods.remove(region);
    onRegionRemoved(region);
  }
  
  /**
   * Clears all the regions from the service.
   */
  public void clearRegions() {
    mRegions.clear();
    mNeighbourhoods.clear();
    onRegionsCleared();
  }
  
  public void regionsModified(List<Region> regions) {
    onRegionsModified(regions);
  }
  
  /**
   * Causes the property to be recalculated.
   */
  protected abstract void invalidate();

  // Callbacks
  
  /**
   * Called when a region is added.
   * @param region
   */
  protected void onRegionAdded(Region region) {}

  /**
   * Called when a region is removed.
   * @param region
   */
  protected void onRegionRemoved(Region region) {}

  /**
   * Called when the regions are cleared.
   */
  protected void onRegionsCleared() { 
    invalidate(); 
  }
  
  /**
   * Called when the neighbourhood of a region is updated.
   * @param region
   * @param neighbourhood
   */
  protected void onNeighbourhoodSet(Region region, List<Integer> neighbourhood) {}

  /**
   * Called when the enabled probe functions are changed.
   */
  protected void onProbeFuncsChanged() { 
    invalidate(); 
  }
  
  /**
   * Called when a region has been modified.
   * @param regions
   */
  protected void onRegionsModified(List<Region> regions) {
    if (!mUseNeighbourhoods) {
      invalidate();
    }
  }

  // broadcasting
  /**
   * Broadcasts the the value has been changed.
   * @param indices
   * @param region
   */
  protected abstract void broadcastValueChanged(T indices);

  /**
   * Converts the given list of indices into an array of points those indices are at.
   * @param indices
   * @return
   */
  protected int[] indicesToPoints(List<Integer> indices) {
    // handle nulls
    if (indices == null) return new int[0];

    int[] points = new int[indices.size() * 2];
    for (int i = 0; i < indices.size(); i++) {
      int index = indices.get(i);
      points[i*2] = mImage.getX(index);
      points[i*2 + 1] = mImage.getY(index);
    }
    return points;
  }

  /**
   * Returns the current progress of the calculation.
   * @return
   */
  public int getProgress() {
    return mProgress;
  }
  
  /**
   * Sets and broadcasts the progress of the calculation.
   * @param value
   */
  @SuppressWarnings("unchecked")
  public void setProgress(int value) {
    // store progress
    mProgress = value;

    // broadcast progress
    ProximityDesktop.getApp().setProgress((Class<? extends PropertyController<?>>) getClass(), mProgress);
  }
  
  /**
   * A runnable that calculates the property given a region.
   * @author Garrett Smith
   *
   */
  public abstract class PropertyRunnable implements Runnable, PerceptualSystemSubscriber {
    public static final float PROGRESS_THERSHOLD = 0.001f;
    private float mLastProgress = 0;
    
    private Region mRegion;
    
    private boolean mCancelled = false;
    private boolean mRunning = false;
    
    private T mResult;
    
    /**
     * Create a runnable to calculate the property using the given {@link Region}.
     * @param reg
     */
    public PropertyRunnable(Region reg) {
      mRegion = reg;
    }
    
    /**
     * Returns true if the runnable is currently running.
     * @return
     */
    public boolean isRunning() {
      return mRunning;
    }
    
    /**
     * Starts calculating the property for the given region and performs cleanup afterwards.
     */
    public void run() {
      mRunning = true;
      if (!mCancelled) {
        mResult = calculateProperty(mRegion);
      }
      postRun();
    }
    
    /**
     * Calculates the property.
     * @param region
     * @return
     */
    protected abstract T calculateProperty(Region region);
    
    protected void postRun() {
      mRunning = false;
      setProgress(0);
      onPostRun(mResult, mRegion);
    }
    
    /**
     * Called once the property has been calculated.
     * @param result
     * @param region
     */
    protected void onPostRun(T result, Region region) {}
    
    /**
     * Returns the result of running or null if the runnable has not finished yet.
     * @return
     */
    public T getResult() {
      return mResult;
    }

    /**
     * Update the progress.
     */
    public void onProgressSet(float progress) {
      if (progress - mLastProgress > PROGRESS_THERSHOLD) {
        mLastProgress = progress;
        setProgress(Integer.valueOf((int) (progress * MAX_PROGRESS)));
      }
    }

    /**
     * Return true if the runnable has been cancelled.
     */
    public boolean isCancelled() {
      return mCancelled;
    }
    
    /**
     * Cancels the runnable.
     */
    public void cancel() {
      mCancelled = true;
    }
  }

}
