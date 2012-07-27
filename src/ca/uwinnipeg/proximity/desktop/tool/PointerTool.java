/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.action.ToolAction;

/**
 * @author Garrett Smith
 *
 */
public class PointerTool extends Tool {

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    PointerListener listener = new PointerListener(this);
    listener.register(map);
    return map;
  }
  
  class PointerListener extends DragToolListener {

    public PointerListener(Tool tool) {
      super(tool);
    }

    @Override
    public void onClick(Event event, Point image, Point screen) {
      // TODO: select the region under the cursor
    }

    @Override
    public void onDrag(
        Event event, 
        Point imageStart, 
        Point imageEnd, 
        Point screenStart, 
        Point screenEnd) {
      // TODO: select regions within the drag
    }
    
    @Override
    public void paint(
        Event event, 
        Point imageStart, 
        Point imageEnd, 
        Point screenStart,
        Point screenEnd) {
      if (screenStart != null) {
        GC gc = event.gc;
        
        int width = screenEnd.x - screenStart.x;
        int height = screenEnd.y - screenStart.y;
        
        gc.setForeground(DRAG_COLOR);
        gc.setLineStyle(SWT.LINE_DOT);
        
        gc.drawRectangle(screenStart.x, screenStart.y, width, height);        
      }
    }
    
  }
  
  public static class Action extends ToolAction {

    public Action() {
      super(
          "Actions.PointerTool.text",
          "pointer.png",
          new PointerTool());
    }
    
  }

}
