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

  // the degree of nearness
  private float mDegree = 1;
  
  // the number of pixels in the union
  private int mUnionSize = 0;
  
  // the number of pixels in the intersections
  private int mIntersectionSize = 0;

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
  protected void setDegree(float degree, int unionSize, int intSize) {
    mDegree = degree;
    mUnionSize = unionSize;
    mIntersectionSize = intSize;
    
    // broadcast the change if we are finished calculating
    if (mQueue.isEmpty()) {
      ProximityDesktop.getApp().setDegree(mDegree, unionSize, intSize);
    }
  }
  
  @Override
  protected void setResult(List<Integer> intersection, Region region) {    
    // size of the intersection
    int intSize = intersection.size();    
    // union of indices in the current intersection and in the added region
    List<Integer> union = new ArrayList<Integer>();
    for (Region r: mRegions) {
      union = MathUtil.union(union, getIndices(r));
    }
    int unionSize = union.size();
    float degree = 1 - ((float) intSize / unionSize);
    
    setDegree(degree, unionSize, intSize);    
    super.setResult(intersection, region);
  }

}
