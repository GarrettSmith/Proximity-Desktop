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
public class DifferenceController extends LinearPropertyController {

  @Override
  protected List<Integer> getProperty(Region region, PerceptualSystemSubscriber sub) {
    List<Integer> indices = new ArrayList<Integer>();

    // check if we should stop because the task was cancelled
    if (sub.isCancelled()) {
      indices = null;
    }
    else {
      // take the initial compliment
      long startTime = System.currentTimeMillis();
      if (mRegions.get(0) == region) {
        indices = region.getIndicesList();
      }
      // take the difference of with the next object
      else {  
        indices = mImage.hybridDifference(
            mValue, 
            region.getIndicesList(), 
            getEpsilon(), 
      }
      System.out.println("Difference took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
    }

    return indices;
  }

}
