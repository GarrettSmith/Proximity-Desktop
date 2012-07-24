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

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;
import ca.uwinnipeg.proximity.desktop.history.AddRegionAction;
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;
import ca.uwinnipeg.proximity.desktop.history.RemoveRegionAction;
import ca.uwinnipeg.proximity.image.BlueFunc;
import ca.uwinnipeg.proximity.image.GreenFunc;
import ca.uwinnipeg.proximity.image.Image;
import ca.uwinnipeg.proximity.image.RedFunc;


/**
 * The main controller of the application.
 * @author Garrett Smith
 *
 */
// TODO: update undo and redo to window
public class MainController {
  
  private Image mImage = new Image();
  
  private List<Region> mRegions = new ArrayList<Region>();
  
  private Deque<HistoryAction> mUndoStack = new ArrayDeque<HistoryAction>();
  private Deque<HistoryAction> mRedoStack = new ArrayDeque<HistoryAction>();
  
  public MainController() {
    mImage.addProbeFunc(new RedFunc());
    mImage.addProbeFunc(new BlueFunc());
    mImage.addProbeFunc(new GreenFunc());
  }
  
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
    
    if (shape == Region.Shape.POLYGON) {
      for (Point p : points) {
        reg.addPoint(p.x, p.y);
      }
    }
    // add non-polygon shape
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
  
  /**
   * Applies a {@link HistoryAction} adds it to the undo stack and clears the redo stack.
   * @param action
   */
  protected void performAction(HistoryAction action) {
    action.apply();
    mUndoStack.push(action);
    mRedoStack.clear();
  }
  
  /**
   * Check if there are actions that can be undone.
   * @return
   */
  public boolean hasUndo() {
    return !mUndoStack.isEmpty();
  }
  
  /**
   * Check if there are actions that can be redone.
   * @return
   */
  public boolean hasRedo() {
    return !mRedoStack.isEmpty();
  }
  
  /**
   * Get the name of the next action to be undone.
   * @return
   */
  public String getUndoString() {
    return mUndoStack.peek().getName();
  }
  
  /**
   * Get the name of the next action to be redone.
   * @return
   */
  public String getRedoString() {
    return mRedoStack.peek().getName();
  }
  
  /**
   * Unapply the last {@link HistoryAction}, remove it from the undo stack and adds the action to 
   * the redo stack.
   */
  public void undo() {
    HistoryAction action = mUndoStack.pop();
    action.unapply();
    mRedoStack.push(action);
  }
  
  /**
   * Reapply the next next {@link HistoryAction}, remove it from the redo stack and add it to the 
   * undo stack.
   */
  public void redo() {
    HistoryAction action = mRedoStack.pop();
    action.apply();
    mUndoStack.push(action);
  }
  
  public List<Integer> getNeighbourhood(Region reg) {
    PerceptualSystemSubscriber sub = new PerceptualSystemSubscriber() {

      public void onProgressSet(float progress) {
        // TODO Auto-generated method stub
        
      }

      public boolean isCancelled() {
        // TODO Auto-generated method stub
        return false;
      }
    };
    return mImage.neighbourhood(reg.getCenterIndex(), reg.getIndicesList(), sub);
  }
  
  /**
   * Perform closing operations.
   */
  public void onClose() {
    
  }
  
}
