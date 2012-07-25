/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.regexp.internal.RE;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;
import ca.uwinnipeg.proximity.image.Image;

/**
 * @author Garrett Smith
 *
 */
public abstract class PropertyController {
  
  public static final String ACTION_PROGRESS_CHANGED = "action.PROGRESS_CHANGED";
  public static final String ACTION_VALUE_CHANGED = "action.VALUE_CHANGED";  

  public static final String PROGRESS = "Progress";
  public static final String POINTS = "points";
  public static final String INDICES = "indices";
  public static final String REGION = "Region";

  // the maximum progress value
  public static final int MAX_PROGRESS = 10000;
  
  // the current progress of calculating
  protected int mProgress = MAX_PROGRESS;
  
//  // used to broadcast and receive changes
//  protected LocalBroadcastManager mBroadcastManager;
  
  // The category used to send broadcasts
//  protected final String mCategory;
  
  // The image system 
  protected Image mImage;
  
  // The regions of interest
  protected List<Region> mRegions = new ArrayList<Region>();
  
//  public PropertyService(String category) {
//    mCategory = category;
//  }
  
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
    onRegionRemoved(region);
  }
  
  /**
   * Clears all the regions from the service.
   */
  public void clearRegions() {
    mRegions.clear();
    onRegionsCleared();
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
   * Called when the enabled probe functions are changed.
   */
  protected void onProbeFuncsChanged() { 
    invalidate(); 
  }

  // broadcasting
  /**
   * Broadcasts the the value associated with the given region has been changed.
   * @param indices
   * @param region
   */
  protected void broadcastValueChanged(List<Integer> indices, Region region) {
//    Intent intent = new Intent(ACTION_VALUE_CHANGED);
//    intent.addCategory(mCategory);
//    intent.putExtra(POINTS, indicesToPoints(indices));
//    intent.putIntegerArrayListExtra(INDICES, new ArrayList<Integer>(indices));
//    if (region != null) intent.putExtra(REGION, region);
//    mBroadcastManager.sendBroadcast(intent);
  }
  
  /**
   * Broadcasts that the general value has been changed.
   * @param indices
   */
  protected void broadcastValueChanged(List<Integer> indices) {
    broadcastValueChanged(indices, null);
  }

  /**
   * Converts the given list of indices into an array of points those indices are at.
   * @param indices
   * @return
   */
  // TODO: get this off the ui thread and make it cancellable
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
  public void setProgress(int value) {
    // store progress
    mProgress = value;

    // broadcast progress
//    Intent intent = new Intent(ACTION_PROGRESS_CHANGED);
//    intent.putExtra(PROGRESS, getProgress());
//    intent.addCategory(mCategory);
//    mBroadcastManager.sendBroadcast(intent);    
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
    
    private List<Integer> mResult;
    
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
    protected abstract List<Integer> calculateProperty(Region region);
    
    protected void postRun() {
      mRunning = false;
    }
    
    /**
     * Returns the result of running or null if the runnable has not finished yet.
     * @return
     */
    public List<Integer> getResult() {
      return mResult;
    }

    /**
     * Update the progress.
     */
    public void onProgressSet(float progress) {
      if (progress - mLastProgress > PROGRESS_THERSHOLD) {
        mLastProgress = progress;
        // TODO: updateProgress(Integer.valueOf((int) (progress * MAX_PROGRESS)));
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
