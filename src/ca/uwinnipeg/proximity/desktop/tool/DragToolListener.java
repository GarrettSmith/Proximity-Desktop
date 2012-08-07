/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.ImageCanvas;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;
import ca.uwinnipeg.proximity.desktop.Region;

/**
 * @author garrett
 *
 */
public abstract class DragToolListener implements Listener {
  
  private Tool tool;
  
  public DragToolListener(Tool tool) {
    this.tool = tool;
  }
  
  public void register(Map<Integer, Listener> map) {
    map.put(SWT.MouseMove, this);
    map.put(SWT.MouseDown, this);
    map.put(SWT.MouseUp, this);
    map.put(SWT.KeyDown, this);
    map.put(SWT.Paint, this);
  }

  // route events
  public void handleEvent(Event event) {
    switch(event.type) {
      case SWT.MouseDown:
        mouseDown(event);
        break;
      case SWT.MouseUp:
        mouseUp(event);
        break;
      case SWT.MouseMove:
        mouseMove(event);
        break;
      case SWT.KeyDown:
        keyDown(event);
        break;
      case SWT.Paint:
        if (mImageStartPoint != null) {
          paint(
              event, 
              mImageStartPoint, 
              mImageCurrentPoint, 
              mScreenStartPoint, 
              mScreenCurrentPoint,
              mClickedRegion);
        }
        break;
    }
  }
  
  private Point mImageStartPoint;
  private Point mImageCurrentPoint;

  private Point mScreenStartPoint;
  private Point mScreenCurrentPoint;
  
  private Region mClickedRegion = null;
  
  public void keyDown(Event event) {
    switch(event.keyCode) {
      // stop action on esc pressed
      case SWT.ESC: 
        mScreenStartPoint = mImageStartPoint = null;
        tool.getCanvas().redraw();
        break;
    }
  }
  
  public void mouseDown(Event event) {
    // if we are pressing the first button inside the image
    if (event.button == 1 && tool.getCanvas().contains(mImageCurrentPoint)) {
      mScreenStartPoint = mScreenCurrentPoint;
      mImageStartPoint = mImageCurrentPoint;
      
      for (Region r : ProximityDesktop.getController().getRegions()) {
        if (r.contains(mImageStartPoint)) {
          mClickedRegion = r;
        }
      }
    }
  }
  
  public void mouseUp(Event event) {    
    // if the cursor was dragged
    if (mImageStartPoint != null) {      
      
      if (mImageStartPoint.equals(mImageCurrentPoint)) {
        // perform the click action
        onClick(event, mImageCurrentPoint, mScreenCurrentPoint, mClickedRegion);
      }
      else {
        // perform the drag action
        onDrag(event, mImageStartPoint, mImageCurrentPoint, mImageStartPoint, mScreenCurrentPoint, mClickedRegion);
      }
      
      // clear the clicked region
      mClickedRegion = null;

      // redraw to clear box
      tool.getCanvas().redraw();
    }
    // clear the start point
    mImageStartPoint = mScreenStartPoint = null;
  }
  
  public void mouseMove(Event event) {
    ImageCanvas canvas = tool.getCanvas();
    Image image = tool.getImage();
    
    mScreenCurrentPoint = new Point(event.x, event.y);
    mImageCurrentPoint = canvas.toImageSpace(mScreenCurrentPoint);

    // check if the mouse has been clicked and we should draw something
    if (mImageStartPoint != null) {
      Rectangle imageBounds = image.getBounds();
      Rectangle imageScreenBounds = canvas.toScreenSpace(imageBounds);
      
      //limit points to be within image bounds
      mImageCurrentPoint.x = Math.max(0, mImageCurrentPoint.x);
      mImageCurrentPoint.x = Math.min(imageBounds.width, mImageCurrentPoint.x);        
      mImageCurrentPoint.y = Math.max(0, mImageCurrentPoint.y);
      mImageCurrentPoint.y = Math.min(imageBounds.height, mImageCurrentPoint.y);
      
      mScreenCurrentPoint.x = Math.max(imageScreenBounds.x, mScreenCurrentPoint.x);
      mScreenCurrentPoint.x = Math.min(imageScreenBounds.x + imageScreenBounds.width, mScreenCurrentPoint.x);        
      mScreenCurrentPoint.y = Math.max(imageScreenBounds.y, mScreenCurrentPoint.y);
      mScreenCurrentPoint.y = Math.min(imageScreenBounds.y + imageScreenBounds.height, mScreenCurrentPoint.y);

      canvas.redraw();
    }
  }
  
  public abstract void paint(
      Event event, 
      Point imageStart, 
      Point imageEnd,
      Point screenStart, 
      Point screenEnd, 
      Region region);
  
  public abstract void onClick(
      Event event, 
      Point image, 
      Point screen, 
      Region region);
  
  public abstract void onDrag(
      Event event, 
      Point imageStart, 
      Point imageEnd, 
      Point screenStart, 
      Point screenEnd, 
      Region region);
  
}
