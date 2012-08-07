/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.Region;
import ca.uwinnipeg.proximity.desktop.Util;
import ca.uwinnipeg.proximity.desktop.action.ToolAction;

/**
 * @author Garrett Smith
 *
 */
// TODO: add shift and ctrl
public class PointerTool extends Tool {

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    PointerListener listener = new PointerListener(this);
    listener.register(map);
    return map;
  }
  
  class PointerListener extends DragToolListener {
    
    private boolean mMultiSelect = false;

    public PointerListener(Tool tool) {
      super(tool);
    }
    
    @Override
    public void register(Map<Integer, Listener> map) {
      map.put(SWT.KeyUp, this);
      super.register(map);
    }
    
    @Override
    public void handleEvent(Event event) {
      switch (event.type) {
        case SWT.KeyUp:
          keyUp(event);
        default:
          super.handleEvent(event);
      }
    }
    
    @Override
    public void keyDown(Event event) {
      switch (event.keyCode) {
        case SWT.SHIFT:
        case SWT.CTRL:
          mMultiSelect = true;
          break;
        default:
          super.keyDown(event);
      }
    }
    
    public void keyUp(Event event) {
      switch (event.keyCode) {
        case SWT.SHIFT:
        case SWT.CTRL:
          mMultiSelect = false;
          break;
      }      
    }

    @Override
    public void onClick(Event event, Point imagePoint, Point screenPoint, Region region) {
      ProximityController controller = getController();
      if (mMultiSelect) {
        List<Region> regions = controller.getSelectedRegions();
        regions.add(region);
        controller.setSelected(regions);
      }
      else {
        controller.setSelected(region);
      }
      getCanvas().redraw();
    }

    @Override
    public void onDrag(
        Event event, 
        Point imageStart, 
        Point imageEnd, 
        Point screenStart, 
        Point screenEnd,
        Region region) {
      ProximityController controller = getController();
      List<Region> regions = controller.getRegions();
      List<Region> selection = new ArrayList<Region>();
      Rectangle drag = Util.createRectangle(imageStart, imageEnd);
      for (Region r : regions) {
        Rectangle bounds = r.getBounds();
        if (Util.rectangleContains(drag, bounds)) {
          selection.add(r);
        }
      }
      if (mMultiSelect) {
        regions = controller.getSelectedRegions();
        regions.addAll(selection);
        controller.setSelected(regions);
      }
      else {
        controller.setSelected(selection);
      }
      getCanvas().redraw();
    }
    
    @Override
    public void paint(
        Event event, 
        Point imageStart, 
        Point imageEnd, 
        Point screenStart,
        Point screenEnd, 
        Region region) {
      if (screenStart != null) {
        GC gc = event.gc;
        
        int width = screenEnd.x - screenStart.x;
        int height = screenEnd.y - screenStart.y;

        gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
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
