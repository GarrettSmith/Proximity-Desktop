/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.action.ToolAction;

/**
 * A tool that zooms so the canvas is zoomed into the box dragged by the user. The tool also zooms
 * in and out by clicking (while holding a modifier to zoom out).
 * @author Garrett Smith
 *
 */
//TODO: implement zoom tool
public class ZoomTool extends Tool {
  
  public static final Color ZOOM_COLOR = new Color(Display.getCurrent(), 0, 0, 0);

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    ZoomListener listener = new ZoomListener(this);
    listener.register(map);
    return map;
  }
  
  class ZoomListener extends DragToolListener {

    public ZoomListener(Tool tool) {
      super(tool);
    }

    @Override
    public void onClick(Event event, Point image, Point screen) {
      if ((event.stateMask & SWT.CTRL) == 0) {
        getCanvas().zoomIn();
      }
      else {
        getCanvas().zoomOut();
      }
    }

    @Override
    public void onDrag(
        Event event, 
        Point imageStart, 
        Point imageEnd,
        Point screenStart,
        Point screenEnd) {
      getCanvas().zoomTo(imageStart, imageEnd);
    }
    
    @Override
    public void onPaint(
        Event event, 
        Point imageStart, 
        Point imageEnd,
        Point screenStart, 
        Point screenEnd) {
      GC gc = event.gc;
      gc.setForeground(ZOOM_COLOR);
      int width = screenEnd.x - screenStart.x;
      int height = screenEnd.y - screenStart.y;
      gc.drawRectangle(
          screenStart.x, 
          screenStart.y, 
          width, 
          height);
    }
    
  }
  
  public static class Action extends ToolAction {

    public Action() {
      super(
          "Actions.ZoomTool.text",
          "zoom.png",
          new ZoomTool());
    }
    
  }

}
