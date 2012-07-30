/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;

/**
 * @author Garrett Smith
 *
 */
public class IntersectionController extends LinearPropertyController {
  
  public static final String KEY = "Intersection";

  /**
   * @param key
   */
  public IntersectionController() {
    super(KEY);
  }
  
  /**
   * Returns the indices associated with the given region.
   * <p>
   * We use this so we can override it for the other intersection services.
   * @param region
   * @return
   */
  protected List<Integer> getIndices(Region region) {
    return region.getIndicesList();
  }


  @Override
  protected List<Integer> getProperty(Region region, PerceptualSystemSubscriber sub) {
    // check if we should stop because the task was cancelled
    if (sub.isCancelled()) return null;

    List<Integer> indices = new ArrayList<Integer>();

    // check if this is the only region
    if (mValue.isEmpty()) {
      indices = getIndices(region);
    }
    // else take the intersection of the region and the current intersection
    else {
      long startTime = System.currentTimeMillis();
      indices = mImage.hybridIntersection(
          mValue, 
          getIndices(region),
          0.2,//TODO: getEpsilon(), 
          sub);
      System.out.println("Intersection took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
    }

    return indices;
  }

}
