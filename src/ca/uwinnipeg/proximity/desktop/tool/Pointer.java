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
public class Pointer extends Tool {

  public Pointer(ToolHost host) {
    super(host, "MainWindow.action.text", "pointer.png");
  }

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    Listener listener = new PointerListener();
    map.put(SWT.MouseMove, listener);
    map.put(SWT.MouseDown, listener);
    map.put(SWT.MouseUp, listener);
    map.put(SWT.Paint, listener);
    return map;
  }
  
  class PointerListener extends DragToolListener {

    @Override
    public void onClick(Event event, Point image, Point screen) {
      System.out.println("Click");
    }

    @Override
    public void onDrag(Event event, Point imageStart, Point imageEnd,
        Point screenStart, Point screenEnd) {
      System.out.println("Drag");
    }
    
  }

}
