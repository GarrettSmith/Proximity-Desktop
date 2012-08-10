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
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;
import ca.uwinnipeg.proximity.desktop.history.MoveRegionAction;

/**
 * The tool used to select, move, and modify regions.
 * @author Garrett Smith
 *
 */
//TODO: stop from being able to drag region out of image
public class PointerTool extends Tool {

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    PointerListener listener = new PointerListener(this);
    listener.register(map);
    return map;
  }
  
  /**
   * Sets the given region as selected.
   * @param region
   * @param add whether the region should be added to or replace the current selection.
   */
  protected void select(Region region, boolean add) {
    List<Region> list = new ArrayList<Region>();
    list.add(region);
    select(list, add);
  }
  
  /**
   * Sets the given regions as selected.
   * @param regions
   * @param add whether the region should be added to or replace the current selection.
   */
  protected void select(List<Region> regions, boolean add) {
    ProximityController controller = getController();
    // if we are adding to the selection
    if (add) {
      regions.addAll(controller.getSelectedRegions());
    }
    // set the selection
    controller.setSelected(regions);
  }
  
  protected void toggleSelect(Region region, boolean add) {
    ProximityController controller = getController();
    List<Region> regions;
    // if we are adding to the selection
    if (add) {
      regions = controller.getSelectedRegions();
      // toggle whether the region is in the selection
      if (regions.contains(region)) {
        regions.remove(region);
      }
      else {
        regions.add(region);
      }
    }
    else {
      // set the selection to just be the region
      regions = new ArrayList<Region>();
      regions.add(region);
    }
    // set the selection
    controller.setSelected(regions);
  }

  class PointerListener extends DragToolListener {
    
    private Map<Region, Point> mOffset = new HashMap<Region, Point>();
    
    private Map<Region, Rectangle> mOldBounds = new HashMap<Region, Rectangle>();
    
    private Region mClickedRegion = null;

    public PointerListener(Tool tool) {
      super(tool);
    }
    
    @Override
    public void onMouseDown(Event event, Point imageStart, Point screenStart) {
      
      // find if we clicked a region
      for (Region r : getController().getRegions()) {
        if (r.contains(imageStart)) {
          mClickedRegion = r;
        }
      }
    }
    
    @Override
    public void onMouseUp(Event event, Point imageCurrent, Point screenCurrent) {
      // clear offset
      mOffset.clear();
      // clear clicked region
      mClickedRegion = null;
      // clear old bounds
      mOldBounds.clear();
    }

    @Override
    public void onClick(Event event, Point imagePoint, Point screenPoint) {
      // if shift or ctrl is held
      boolean add = (event.stateMask & SWT.SHIFT) != 0 || (event.stateMask & SWT.CTRL) != 0;
      toggleSelect(mClickedRegion, add);
      getCanvas().redraw();
    }
    
    @Override
    public void duringDrag(
        Event event, 
        Point imageStart, 
        Point imageCurrent,
        Point screenStart, 
        Point screenCurrent) {
      // if a region was clicked
      if (mClickedRegion != null) {
        
        // select the moving region and possibly the other selected regions
        if (!getController().getSelectedRegions().contains(mClickedRegion)) {
          // if shift or ctrl is held
          boolean add = (event.stateMask & SWT.SHIFT) != 0 || (event.stateMask & SWT.CTRL) != 0;
          select(mClickedRegion, add);   
        }
        
        // calculate offsets once
        if (mOffset.isEmpty()) {
          // calculate the offset for each region and record original bounds
          for (Region r: getController().getSelectedRegions()) {
            Rectangle bounds = r.getBounds();
            mOldBounds.put(r, bounds);
            mOffset.put(r, new Point(imageStart.x - bounds.x, imageStart.y - bounds.y));
          }
        }
        
        // move selected regions
        for (Region r: getController().getSelectedRegions()) {
          Rectangle bounds = r.getBounds();
          Point offset = mOffset.get(r);
          bounds.x = imageCurrent.x - offset.x;
          bounds.y = imageCurrent.y - offset.y;
          r.setBounds(bounds);
        }
      }
    }

    @Override
    public void onDrag(
        Event event, 
        Point imageStart, 
        Point imageEnd, 
        Point screenStart, 
        Point screenEnd) {
      ProximityController controller = getController();
      
      // if we clicked a region
      if (mClickedRegion != null) {
        List<Region> selected = controller.getSelectedRegions();        
        // record the new positions
        HashMap<Region, Rectangle> newBounds = new HashMap<Region, Rectangle>();
        for (Region r: selected) {
          newBounds.put(r, r.getBounds());
        }
        // add history action to controller
        HistoryAction action = 
            new MoveRegionAction(selected, mOldBounds, newBounds, controller);
        controller.performAction(action);
      }
      else {
        // select regions we dragged the box around
        List<Region> regions = controller.getRegions();
        List<Region> selection = new ArrayList<Region>();
        Rectangle drag = Util.createRectangle(imageStart, imageEnd);
        // get all the surrounded regions
        for (Region r : regions) {
          Rectangle bounds = r.getBounds();
          if (Util.rectangleContains(drag, bounds)) {
            selection.add(r);
          }
        }
        // if shift or ctrl is held
        boolean add = (event.keyCode & SWT.SHIFT) != 0 || (event.keyCode & SWT.CTRL) != 0;
        select(selection, add);
      }      
      // redraw the canvas
      getCanvas().redraw();
    }
    
    @Override
    public void onPaint(
        Event event, 
        Point imageStart, 
        Point imageEnd, 
        Point screenStart,
        Point screenEnd) {
      if (screenStart != null) {        
        // if we didn't click a region
        if (mClickedRegion == null) {
          // draw selection box
          GC gc = event.gc;

          int width = screenEnd.x - screenStart.x;
          int height = screenEnd.y - screenStart.y;

          gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
          gc.setLineStyle(SWT.LINE_DOT);

          gc.drawRectangle(screenStart.x, screenStart.y, width, height);      
        }
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
