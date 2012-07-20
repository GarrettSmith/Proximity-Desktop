/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Garrett Smith
 *
 */
public class ZoomTool extends Tool {

  /**
   * @param host
   * @param label
   * @param icon
   */
  public ZoomTool(ToolHost host) {
    super(host, "MainWindow.actnZoom.text", "zoom.png");
  }

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    ZoomListener listener = new ZoomListener();
    listener.register(map);
    return map;
  }
  
  class ZoomListener extends DragToolListener {

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
    
  }

}
