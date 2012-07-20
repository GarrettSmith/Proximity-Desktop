/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.HashMap;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Garrett Smith
 *
 */
public class PointerTool extends Tool {

  public PointerTool(ToolHost host) {
    super(host, "MainWindow.action.text", "pointer.png");
  }

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    PointerListener listener = new PointerListener();
    listener.register(map);
    return map;
  }
  
  class PointerListener extends DragToolListener {

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
    
  }

}
