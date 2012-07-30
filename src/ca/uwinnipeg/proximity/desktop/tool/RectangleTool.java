/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import ca.uwinnipeg.proximity.desktop.Region;
import ca.uwinnipeg.proximity.desktop.action.ToolAction;

/**
 * @author Garrett Smith
 *
 */
// TODO: add shift and ctrl functionality
public class RectangleTool extends SimpleRegionTool {
  
  public static final Color RECT_COLOR = new Color(Display.getCurrent(), 0, 255, 255);

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
    GC gc = event.gc;
    gc.setForeground(RECT_COLOR);
    int width = screenEnd.x - screenStart.x;
    int height = screenEnd.y - screenStart.y;
    gc.drawRectangle(
        screenStart.x, 
        screenStart.y, 
        width, 
        height);
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
