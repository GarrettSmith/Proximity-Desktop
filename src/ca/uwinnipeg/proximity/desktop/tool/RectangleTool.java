/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

import ca.uwinnipeg.proximity.desktop.Region;
import ca.uwinnipeg.proximity.desktop.action.ToolAction;

/**
 * @author Garrett Smith
 *
 */
public class RectangleTool extends SimpleRegionTool {
  
  public static final int PIVOT_SIZE = 16;

  public RectangleTool() {
    super(Region.Shape.RECTANGLE);
  }
  
  @Override
  public void paintProgress(
      Event event, 
      Point imageStart, 
      Point imageEnd,
      Point screenStart, 
      Point screenEnd) {
    int[] points = {imageStart.x, imageStart.y, imageEnd.x, imageEnd.y};
    getCanvas().drawRegion(event.gc, Region.Shape.RECTANGLE, points, true, true, true);
  }
  
  public static class Action extends ToolAction {

    public Action() {
      super(
          "Actions.RectangleTool.text",
          "rect.png",
          new RectangleTool());
    }
    
  }
}
