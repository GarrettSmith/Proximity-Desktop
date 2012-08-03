/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import org.eclipse.swt.SWT;
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
    GC gc = event.gc;
    gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
    int width = screenEnd.x - screenStart.x;
    int height = screenEnd.y - screenStart.y;
    gc.drawOval(
        screenStart.x, 
        screenStart.y, 
        width, 
        height);
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
