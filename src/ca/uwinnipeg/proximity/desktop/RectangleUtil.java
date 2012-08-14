/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Helper class with various methods to alter rectangles.
 * @author Garrett Smith
 *
 */
public class RectangleUtil {
  
  /**
   * Gets the center of the rectangle in the x axis.
   * @param r
   * @return
   */
  public static int centerX(Rectangle r) {
    return (r.x + (r.width / 2));
  }

  /**
   * Gets the center of the rectangle in the y axis.
   * @param r
   * @return
   */
  public static int centerY(Rectangle r) {
    return (r.y + (r.height / 2));
  }
  
  /**
   * Offsets the given rectangle by the given deltas.
   * @param r
   * @param dx
   * @param dy
   */
  public static void offset(Rectangle r, int dx, int dy) {
    r.x += dx;
    r.y += dy;
  }
  
  /**
   * Offsets the given rectangle to the given x and y position.
   * @param r
   * @param x
   * @param y
   */
  public static void offsetTo(Rectangle r, int x, int y) {
    r.x = x;
    r.y = y;
  }
  
  /**
   * Gets the x position of the right edge of the rectangle.
   * @param r
   * @return
   */
  public static int right(Rectangle r) {
    return r.x + r.width;
  }
  
  /**
   * Gets the y position of the bottom edge of the rectangle.
   * @param r
   * @return
   */
  public static int bottom(Rectangle r) {
    return r.y + r.height;
  }
  
  /**
   * Sets the right edge of the given rectangle.
   * @param r
   * @param right
   */
  public static void setRight(Rectangle r, int right) {
    r.width = right - r.x;
  }

  /**
   * Sets the bottom edge of the given rectangle.
   * @param r
   * @param bottom
   */
  public static void setBottom(Rectangle r, int bottom) {
    r.height = bottom - r.y;
  }

  /**
   * Copies the source rectangle into the destination rectangle.
   * @param source
   * @param dest
   */
  static void copy(Rectangle source, Rectangle dest) {
    dest.x = source.x;
    dest.y = source.y;
    dest.width = source.width;
    dest.height = source.height;
  }

  /**
   * Returns whether the first given rectangle contains the second given rectangle.
   * @param outer
   * @param inner
   * @return
   */
  public static boolean contains(Rectangle outer, Rectangle inner) {
    return (outer.contains(inner.x, inner.y ) && 
        outer.contains(inner.x + inner.width, inner.y + inner.height));
  }

  /**
   * Creates a new rectangle from two points representing opposite corners of the rectangle.
   * @param p1
   * @param p2
   * @return
   */
  public static Rectangle create(Point p1, Point p2) {
    return new Rectangle(
        Math.min(p1.x, p2.x),
        Math.min(p1.y, p2.y),
        Math.abs(p1.x - p2.x),
        Math.abs(p1.y - p2.y));
  }

}
