package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;

public class UpperApprox extends LinearPropertyController<List<List<Integer>>> {

	@Override
	protected List<List<Integer>> initialValue() {
		return new ArrayList<List<Integer>>();
	}

	@Override
	protected List<List<Integer>> getProperty(Region region, PerceptualSystemSubscriber sub) {	  
	  List<List<Integer>> results;

    // check if we should stop because the task was cancelled
    if (sub.isCancelled()) {
      results = null;
    }
    // take the difference of with the next object
    else { 
      long startTime = System.currentTimeMillis();

      results = new ArrayList<List<Integer>>();
      results.addAll(mImage.equivalenceClasses(getIndices(region), sub));
      
      if (mValue != null) {
        results.addAll(mValue);
      }

      System.out.println("Upper Approximation took " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
    }

    return results;
	}
	
  protected List<int[]> indicesListToPoints(List<List<Integer>> indices) {    
    List<int[]> results = new ArrayList<int[]>();
    if (indices != null) {
      for (int i = 0; i < indices.size(); i++) {
        results.add(indicesToPoints(indices.get(i)));
      }
    }
    return results;
  }

	@Override
	protected void broadcastValueChanged(List<List<Integer>> indices) {
	   ImageCanvas canvas = ProximityDesktop.getApp().getCanvas();
	   canvas.updateProperty(getClass(), indicesListToPoints(indices));		
	}

}
