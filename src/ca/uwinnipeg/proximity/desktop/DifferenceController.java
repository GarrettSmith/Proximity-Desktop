/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.BitSet;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;

/**
 * @author Garrett Smith
 *
 */
public class DifferenceController extends LinearPropertyController {
  
  public static final String KEY = "Difference";

  /**
   * @param key
   */
  public DifferenceController() {
    super(KEY);
  }

  @Override
  protected BitSet getProperty(Region region, PerceptualSystemSubscriber sub) {
    BitSet indices = new BitSet();

    // check if we should stop because the task was cancelled
    if (sub.isCancelled()) {
      indices = null;
    }
    else {
      // take the initial compliment
      long startTime = System.currentTimeMillis();
      if (mRegions.get(0) == region) {
        indices = region.getMask();
      }
      // take the difference of with the next object
      else {  
//        indices = mImage.hybridDifference(
//            mValue, 
//            region.getIndicesList(), 
//            0.2, //TODO: getEpsilon(), 
//            sub);
      }
      System.out.println("Difference took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
    }

    return indices;
  }

}
