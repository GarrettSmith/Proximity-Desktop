/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;

/**
 * A {@link PropertyController} that calculates the intersection of the regions.
 * @author Garrett Smith
 *
 */
public class IntersectionController extends LinearPropertyController {

  private float mDegree;

  @Override
  protected List<Integer> getProperty(Region region, PerceptualSystemSubscriber sub) {
    // check if we should stop because the task was cancelled
    if (sub.isCancelled()) return null;

    List<Integer> indices = new ArrayList<Integer>();

    long startTime = System.currentTimeMillis();
    // check if this is the only region
    if (mRegions.get(0) == region) {
      indices = getIndices(region);
    }
    // else take the intersection of the region and the current intersection
    else {
      indices = mImage.hybridIntersection(
          mValue, 
          getIndices(region),
          getEpsilon(), 
          sub);
    }
    System.out.println("Intersection took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");

    return indices;
  }
  
  /**
   * Returns the degree of nearness of the current intersection.
   * @return
   */
  public float getDegree() {
    return mDegree;
  }
  
  /**
   * Updates and broadcasts the current degree of nearness.
   * @param degree
   */
  protected void setDegree(float degree) {
    mDegree = degree;
    
    // broadcast the change if we are finished calculating
    if (mQueue.isEmpty()) {
      ProximityDesktop.getApp().setDegree(mDegree);
    }
  }
  
  @Override
  protected void setResult(List<Integer> intersection, Region region) {    
    setDegree(calculateDegree(intersection, getIndices(region)));    
    setValue(intersection);
  }
  
  /**
   * Calculates the degree of nearness.
   * @param intersection
   * @param regionIndices
   * @return
   */
  protected float calculateDegree(List<Integer> intersection, List<Integer> regionIndices) {
    // size of the intersection
    float intSize = intersection.size();    
    // union of indices in the current intersection and in the added region
    float unionSize = MathUtil.union(mValue, regionIndices).size();
    float degree = 1 - (intSize / unionSize);
    return degree;
  }

}
