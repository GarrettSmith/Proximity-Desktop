/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.history;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import ca.uwinnipeg.proximity.desktop.Region;

/**
 * A history action for modifying regions. This isn't used currently.
 * @author Garrett Smith
 *
 */
public class ModifyRegionAction implements HistoryAction {
  
  public enum Action {
    RESIZE,
    MOVE
  }
  
  private Action mAction;
  
  private Region mRegion;
  
  private Region.Shape mOldShape;
  private Region.Shape mNewShape;
  
  private Point[] mOldPoints;
  private Point[] mNewPoints;

  /**
   * 
   */
  public ModifyRegionAction(
      Region region, 
      Action action, 
      Region.Shape newShape, 
      Point[] newPoints) {
    mRegion = region;
    mAction = action;
    
    mOldShape = mRegion.getShape();
    mOldPoints = mRegion.getPoints();
    
    mNewShape = newShape;
    mNewPoints = newPoints;
  }

  public void apply() {
    modifyRegion(mNewShape, mNewPoints);
  }

  public void unapply() {
    modifyRegion(mOldShape, mOldPoints);
  }
  
  protected void modifyRegion(Region.Shape shape, Point[] points) {
    mRegion.setShape(shape);
    if (shape == Region.Shape.POLYGON) {
      for (Point p : points) {
        mRegion.addPoint(p.x, p.y);
      }
    }
    else {
      Point p1 = points[0];
      Point p2 = points[1];
      mRegion.setBounds(
          new Rectangle(
            Math.min(p1.x, p2.x),
            Math.min(p1.y, p2.y),
            Math.abs(p1.x - p2.x),
            Math.abs(p1.y - p2.y)));
    }
  }

  public String getName() {
    String msg = "";
    switch(mAction) {
      case RESIZE:
        msg = "Resize Region";
        break;
      case MOVE:
        msg = "Move Region";
        break;
    }
    return msg;
  }

}
