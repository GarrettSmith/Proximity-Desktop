package ca.uwinnipeg.proximity.desktop.tool;

import java.util.HashMap;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.RectangleUtil;
import ca.uwinnipeg.proximity.desktop.Region;
import ca.uwinnipeg.proximity.desktop.history.AddRegionAction;
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;

/**
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.ArrayList;

/**
 * A Tool that adds a region by dragging a box that will become the bounds for the new region.
 * @author Garrett Smith
 *
 */
public abstract class SimpleRegionTool extends Tool {
  
  private Region.Shape mShape;

  public SimpleRegionTool(Region.Shape shape) {
    super();
    mShape = shape;
  }

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    SimpleRegionListener listener = new SimpleRegionListener(this);
    listener.register(map);
    return map;
  }

  public abstract void paintProgress(
      Event event, 
      Point imageStart, 
      Point imageEnd,
      Point screenStart, 
      Point screenEnd);
  
  class SimpleRegionListener extends DragToolListener {

    public SimpleRegionListener(Tool tool) {
      super(tool);
    }

    @Override
    public void onDrag(
        Event event, 
        Point imageStart, 
        Point imageEnd,
        Point screenStart, 
        Point screenEnd) {
      // add a region
      ProximityController controller = getController();
      Region region = new Region(controller.getImage());
      region.setShape(mShape);
      Rectangle bounds = RectangleUtil.create(imageStart, imageEnd);
      region.setBounds(bounds);
      HistoryAction action = new AddRegionAction(region, controller);
      controller.performAction(action);
    }
    
    @Override
    public void onPaint(
        Event event, 
        Point imageStart, 
        Point imageEnd,
        Point screenStart, 
        Point screenEnd) {
      paintProgress(event, imageStart, imageEnd, screenStart, screenEnd);   
    }
    
  }

}
