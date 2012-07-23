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

/**
 * @author Garrett Smith
 *
 */
public class OvalTool extends SimpleRegionTool {
  
  public static final Color OVAL_COLOR = new Color(Display.getCurrent(), 0, 255, 255);

  public OvalTool(ToolHost host) {
    super(host, "MainWindow.action.text_1", "oval.png", Region.Shape.OVAL);
  }
  
  @Override
  public void paintProgress(
      Event event, 
      Point imageStart, 
      Point imageEnd,
      Point screenStart, 
      Point screenEnd) {
    GC gc = event.gc;
    gc.setForeground(OVAL_COLOR);
    int width = screenEnd.x - screenStart.x;
    int height = screenEnd.y - screenStart.y;
    gc.drawOval(
        screenStart.x, 
        screenStart.y, 
        width, 
        height);
  }

}
