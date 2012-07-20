package ca.uwinnipeg.proximity.desktop.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.Region;

/**
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.ArrayList;

/**
 * @author Garrett Smith
 *
 */
public abstract class SimpleRegionTool extends Tool {
  
  private Region.Shape mShape;

  public SimpleRegionTool(ToolHost host, String label, String icon, Region.Shape shape) {
    super(host, label, icon);
    mShape = shape;
  }

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    SimpleRegionListener listener = new SimpleRegionListener();
    listener.register(map);
    return map;
  }
  
  class SimpleRegionListener extends DragToolListener {

    @Override
    public void onClick(Event event, Point image, Point screen) {
      // Do nothing
    }

    @Override
    public void onDrag(
        Event event, 
        Point imageStart, 
        Point imageEnd,
        Point screenStart, 
        Point screenEnd) {
      // add a rectangular region
      List<Point> points = new ArrayList<Point>();
      points.add(imageStart);
      points.add(imageEnd);
      getController().addRegion(mShape, points);
    }
    
  }

}
