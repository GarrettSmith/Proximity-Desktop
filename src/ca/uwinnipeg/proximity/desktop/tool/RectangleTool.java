/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import org.eclipse.swt.SWT;
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
    GC gc = event.gc;
    gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
    gc.setXORMode(true);
    int width = screenEnd.x - screenStart.x;
    int height = screenEnd.y - screenStart.y;
    
    // draw border
    gc.drawRectangle(
        screenStart.x, 
        screenStart.y, 
        width, 
        height);
    
    // draw pivot
    int centerx = screenStart.x + (width / 2);
    int centery = screenStart.y + (height / 2);
    
    int imageCenterx = imageStart.x + (imageEnd.x - imageStart.x) / 2;
    int imageCentery = imageStart.y + (imageEnd.y - imageStart.y) / 2;
    
    int pxl = getImage().getImageData().getPixel(imageCenterx, imageCentery);
    Color color = new Color(Display.getCurrent(), (pxl >> 16) & 0xFF, (pxl >> 8) & 0xFF, pxl & 0xFF);
    
    gc.setBackground(color);
    gc.setXORMode(false);
    gc.fillRectangle(
        centerx - (PIVOT_SIZE / 2), 
        centery - (PIVOT_SIZE / 2), 
        PIVOT_SIZE, 
        PIVOT_SIZE);
    
    // draw pivot outline
    gc.setXORMode(true);
    gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));    
    gc.setLineWidth(1);
    gc.drawRectangle(
        centerx - (PIVOT_SIZE / 2), 
        centery - (PIVOT_SIZE / 2), 
        PIVOT_SIZE + 1, 
        PIVOT_SIZE + 1);
    
    color.dispose();
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
