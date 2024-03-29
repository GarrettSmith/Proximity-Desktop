package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * A polygon class used by neighbourhoods.
 * <p>
 * A polygon is a list of points with some helper methods.
 * @author garrett
 *
 */
public class Polygon {

  private final ArrayList<Point> mPoints = new ArrayList<Point>();

  /**
   * Creates a new empty polygon.
   */
  public Polygon() {}
  
  /**
   * Creates a new polygon that is a copy of the given polygon.
   * @param orig the original polygon to copy
   */
  public Polygon(Polygon orig) {
    set(orig);
  }
  
  /**
   * Creates a new polygon from the given array of integers.
   * @param points an array where every two integers represent a point
   */
  public Polygon(int[] points) {
    set(points);
  }
  
  /**
   * Creates a new polygon from the given list of points.
   * @param points
   */
  public Polygon(List<Point> points) {
    set(points);
  }
  
  /**
   * Returns the number of points in the polygon.
   * @return
   */
  public int size() {
    return mPoints.size();
  }
  
  /**
   * Returns true if the polygon has no points.
   * @return
   */
  public boolean isEmpty() {
    return mPoints.size() == 0;
  }

  /**
   * Returns the point at the given index.
   * @param index
   * @return
   */
  public Point getPoint(int index) {
    return mPoints.get(index);
  }

  /**
   * Returns an array of all the points.
   * @return
   */
  public Point[] getPoints() {
    Point[] points = new Point[mPoints.size()];
    mPoints.toArray(points);
    return points;
  }
  
  /**
   * Converts the points of the polygon to an array of ints.
   * @return
   */
  public int[] toArray() {
    final int size = mPoints.size();
    int[] ps = new int[size * 2];
    for (int i = 0; i < size; i++) {
      Point p = mPoints.get(i);
      ps[i * 2]     = p.x;
      ps[i * 2 + 1] = p.y;
    }
    return ps;
  }
  
  /**
   * Converts the points of the polygon to an array of floats.
   * @return
   */
  public float[] toFloatArray() {
    final int size = mPoints.size();
    float[] fs = new float[size * 2];
    for (int i = 0; i < size; i++) {
      Point p = mPoints.get(i);
      fs[i * 2]     = p.x;
      fs[i * 2 + 1] = p.y;
    }
    return fs;
  }
  
  /**
   * Returns the index within the points list of the given point.
   * @param p
   * @return
   */
  public int indexOf(Point p) {
    return mPoints.indexOf(p);
  }

  /**
   * Makes this polygon a copy of the given polygon.
   * @param orig
   */
  public void set(Polygon orig) {
    mPoints.clear();
    for (Point p : orig.mPoints) {
      addPoint(p);
    }
  }

  /**
   * Makes this polygon a copy of the given array representing a polygon.
   * @param orig
   */
  public void set(int[] points) {
    mPoints.clear();
    for (int i = 0; i < points.length; i += 2) {
      addPoint(points[i], points[i+1]);
    }
  }
  
  /**
   * Sets the points of this polygon to be the given list of points.
   * @param points
   */
  public void set(List<Point> points) {
    mPoints.clear();
    for (Point p: points) {
      addPoint(p);
    }
  }
  
  /**
   * Clears all points from the polygon.
   */
  public void reset() {
    mPoints.clear();
  }

  /**
   * Adds a point to the polygon and returns the added point.
   * @param p
   * @return
   */
  public Point addPoint(Point p) {
    Point point = new Point(p.x, p.y);
    mPoints.add(point);
    return point;
  }

  /**
   * Adds a point to the polygon and returns the added point.
   * @param x
   * @param y
   * @return
   */
  public Point addPoint(int x, int y) {
    return addPoint(new Point(x, y));
  }
  
  /**
   * Adds a point at the given index.
   * @param index
   * @param p
   * @return
   */
  public Point addPoint(int index, Point p) {
    Point point = new Point(p.x, p.y);
    mPoints.add(index, point);
    return p;
  }
  
  /**
   * Adds a point at the given index.
   * @param index
   * @param x
   * @param y
   * @return
   */
  public Point addPoint(int index, int x, int y) {
    return addPoint(index, new Point(x, y));
  }

  /**
   * Removes a point at the given index and returns the removed point.
   * @param index
   * @return
   */
  public Point removePoint(int index) {
    return mPoints.remove(index);
  }

  /**
   * Removes the given point.
   * @param p
   * @return true if the point was part of the polygon and removed
   */
  public boolean removePoint(Point p) {
    return mPoints.remove(p);
  }

  /**
   * Returns the bounds of this polygon. The bounds are empty if the polygon has less than 2 points.
   * @return
   */
  public Rectangle getBounds() {
    Rectangle bounds = new Rectangle(0, 0, 0, 0);
    if (mPoints.size() > 0) {
      Point p1 = mPoints.get(0);

      // create an initial bounds from the first point
      bounds.x = p1.x;
      bounds.y = p1.y;

      Rectangle tmp = new Rectangle(0, 0, 0, 0);
      // Get the union of each point to get the final bounds
      for (int i = 1; i < mPoints.size(); i++) {
        Point p = mPoints.get(i);
        tmp.x = p.x;
        tmp.y = p.y;
        bounds = bounds.union(tmp);
      }
    }
    return bounds;
  }
  
  /**
   * Sets the bounds of the polygon and stretches the current points to match the new bounds.
   * @param bounds
   */
  public void setBounds(Rectangle newBounds) {

    int newWidth = newBounds.width;
    int newHeight = newBounds.height;

    // Scale to fit in the new bounds if there is more than one point 
    // and the width and height of the new bounds are non zero.
    if (mPoints.size() > 1 && newWidth != 0 && newHeight != 0) {

      Rectangle oldBounds = getBounds();
      int oldWidth = oldBounds.width;
      int oldHeight = oldBounds.height;

      float widthRatio = newWidth / (float)oldWidth;
      float heightRatio = newHeight / (float)oldHeight;

      for (Point p : mPoints) {
        p.x *= widthRatio;
        p.y *= heightRatio;
      }

    }
    // Offset to the new bounds
    offsetTo(newBounds.x, newBounds.y);
  }

  /**
   * Moves the polygon by the given deltas.
   * @param dx
   * @param dy
   */
  public void offset(int dx, int dy) {
    for (Point p : mPoints) {
      p.x += dx;
      p.y += dy;
    }
  }
  
  /**
   * Moves the polygon so that the top left corner is located at the given point.
   * @param x
   * @param y
   */
  public void offsetTo(int x, int y) {
    Rectangle bounds = getBounds();    
    int dx = x - bounds.x;
    int dy = y - bounds.y;
    offset(dx, dy);
  }
  
  /**
   * Checks if the given point is within the polygon.
   * Adapted from http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
   * @param x
   * @param y
   * @return
   */
  public boolean contains(int x, int y) {
    boolean inside = false;
    for (int i = 0, j = (mPoints.size() - 1); i < mPoints.size(); j = i++) {
      Point pi = mPoints.get(i);
      Point pj = mPoints.get(j);
      if ( ((pi.y > y) != (pj.y > y)) && (x < (pj.x - pi.x) * (y - pi.y) / (pj.y - pi.y) + pi.x) )
        inside = !inside;
    }
    return inside;
  }

  /**
   * Returns the path representing this polygon transformed by the given matrix.
   * @param m
   * @return
   */
  public Path getPath() {

    Path path = new Path(Display.getCurrent());

    // we need at least 2 points to draw a poly
    if (mPoints.size() > 1) {
      int size = mPoints.size();
      
      Point first = mPoints.get(0);
      
      // Move to the first point
      path.moveTo(first.x, first.y); 
      
      // Connect all other points
      for (Point p : mPoints.subList(1, size)) {
        path.lineTo( p.x, p.y);
      }
      
      // close the path
      path.close();
    }

    return path;
  }

}
