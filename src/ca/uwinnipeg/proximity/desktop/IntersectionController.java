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

}
