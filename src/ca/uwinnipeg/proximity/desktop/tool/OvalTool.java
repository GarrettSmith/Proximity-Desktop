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
public class OvalTool extends SimpleRegionTool {

  public OvalTool() {
    super(Region.Shape.OVAL);
  }
  
  @Override
  public void paintProgress(
      Event event, 
      Point imageStart, 
      Point imageEnd,
      Point screenStart, 
      Point screenEnd) {
    int[] points = {imageStart.x, imageStart.y, imageEnd.x, imageEnd.y};
    getCanvas().drawRegion(event.gc, Region.Shape.OVAL, points, true, true);
  }
  
  public static class Action extends ToolAction {

    public Action() {
      super(
          "Actions.OvalTool.text",
          "oval.png",
          new OvalTool());
    }
    
  }

}
