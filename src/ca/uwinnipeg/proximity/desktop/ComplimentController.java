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
public class ComplimentController extends LinearPropertyController {
  
  public static final String KEY = "Compliment";

  /**
   * @param key
   */
  public ComplimentController() {
    super(KEY);
  }

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
      if (mRegions.isEmpty()) {
        indices = mImage.hybridCompliment(
            getIndices(region), 
            0.2, //TODO: getEpsilon(), 
            sub);
      }
      // take the difference of with the next object
      else {  
        indices = mImage.hybridDifference(
            mValue, 
            getIndices(region), 
            0.2, //TODO: getEpsilon(), 
            sub);
      }
      System.out.println("Compliment took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
    }

    return indices;
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

}
