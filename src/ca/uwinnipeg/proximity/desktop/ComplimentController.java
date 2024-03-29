/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;

/**
 * A {@link PropertyController} used to calculate the compliment of the image with the selected 
 * regions.
 * @author Garrett Smith
 *
 */
public class ComplimentController extends LinearPropertyController<List<Integer>> {

	@Override
	protected List<Integer> initialValue() {
		return new ArrayList<Integer>();
	}

  @Override
  protected void broadcastValueChanged(List<Integer> indices) {
	 ImageCanvas canvas = ProximityDesktop.getApp().getCanvas();
	 canvas.updateProperty(getClass(), indicesToPoints(indices));
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
      if (mRegions.get(0) == region) {
        indices = mImage.hybridCompliment(
            getIndices(region), 
            getEpsilon(), 
            sub);
      }
      // take the difference of with the next object
      else {  
        indices = mImage.hybridDifference(
            mValue, 
            getIndices(region), 
            getEpsilon(), 
            sub);
      }
      System.out.println("Compliment took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
    }

    return indices;
  }

}
