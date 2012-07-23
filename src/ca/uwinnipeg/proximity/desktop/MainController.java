/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import ca.uwinnipeg.proximity.desktop.history.AddRegionAction;
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;
import ca.uwinnipeg.proximity.desktop.history.RemoveRegionAction;
import ca.uwinnipeg.proximity.image.Image;


/**
 * The main controller of the application.
 * @author Garrett Smith
 *
 */
public class MainController {
  
  private Image mImage = new Image();
  
  private List<Region> mRegions = new ArrayList<Region>();
  
  private Deque<HistoryAction> mUndoStack = new ArrayDeque<HistoryAction>();
  private Deque<HistoryAction> mRedoStack = new ArrayDeque<HistoryAction>();
  
  /**
   * Sets up the image data into the image.
   * @param data
   */
  public void onImageSelected(ImageData data) {
    int[] pixels = new int[data.width * data.height];
    data.getPixels(0, 0, data.width, pixels, 0);
    mImage.set(pixels, data.width, data.height);
  }
  
  /**
   * Returns all the regions added to the image.
   * @return
   */
  public List<Region> getRegions() {
    return new ArrayList<Region>(mRegions);
  }

  /**
   * Adds a region to the image.
   * @param shape
   * @param points
   */
  public void addRegion(Region.Shape shape, List<Point> points) {
    Region reg = new Region(mImage);
    reg.setShape(shape);
    
    //
    if (shape == Region.Shape.POLYGON) {
      for (Point p : points) {
        reg.addPoint(p.x, p.y);
      }
    }
    else {
      Point p1 = points.get(0);
      Point p2 = points.get(1);
      reg.setBounds(
          new Rectangle(
          Math.min(p1.x, p2.x),
          Math.min(p1.y, p2.y),
          Math.abs(p1.x - p2.x),
          Math.abs(p1.y - p2.y)));
    }
    
    performAction(new AddRegionAction(mRegions, reg));
  }
  
  /**
   * Removes a region from the image.
   * @param region
   */
  public void removeRegion(Region region) {
    performAction(new RemoveRegionAction(mRegions, region));
  }
  
  protected void performAction(HistoryAction action) {
    action.apply();
    mUndoStack.push(action);
    mRedoStack.clear();
  }
  
  public boolean hasUndo() {
    return !mUndoStack.isEmpty();
  }
  
  public boolean hasRedo() {
    return !mRedoStack.isEmpty();
  }
  
  public String getUndoString() {
    return mUndoStack.peek().getName();
  }
  
  public String getRedoString() {
    return mRedoStack.peek().getName();
  }
  
  public void undo() {
    HistoryAction action = mUndoStack.pop();
    action.unapply();
    mRedoStack.push(action);
  }
  
  public void redo() {
    HistoryAction action = mRedoStack.pop();
    action.apply();
    mUndoStack.push(action);
  }
  
  /**
   * Perform closing operations.
   */
  public void onClose() {
    
  }
  
}
