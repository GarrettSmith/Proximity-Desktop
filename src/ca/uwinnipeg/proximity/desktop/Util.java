/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author Garrett Smith
 *
 */
public abstract class Util {

  public static Rectangle createRectangle(Point p1, Point p2) {
    return new Rectangle(
        Math.min(p1.x, p2.x),
        Math.min(p1.y, p2.y),
        Math.abs(p1.x - p2.x),
        Math.abs(p1.y - p2.y));
  }
  
  public static boolean rectangleContains(Rectangle outer, Rectangle inner) {
    return (outer.contains(inner.x, inner.y ) && 
        outer.contains(inner.x + inner.width, inner.y + inner.height));
  }
}
