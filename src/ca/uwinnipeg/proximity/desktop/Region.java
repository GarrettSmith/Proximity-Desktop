package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import ca.uwinnipeg.proximity.image.Image;

/**
 * A region of interest in the {@link Image}.
 * <p>
 * A region has a shape, rectangle, oval, or polygon, and a bounds.
 * @author Garrett Smith
 *
 */
public class Region {
  /**
   * The possible region shapes.
   * @author Garrett Smith
   *
   */
  public enum Shape { RECTANGLE, OVAL, POLYGON }

  /**
   * Represents the edges of a region being selected.
   * @author Garrett Smith
   *
   */
  public enum Edge { NONE, TL, T, TR, R, BR, B, BL, L }
  
  // The default ratio of padding when resetting the region size
  public static final float PADDING_RATIO = 1/8f;

  // The bounds of the neighbourhood IN IMAGE SPACE
  protected Rectangle mBounds = new Rectangle(0, 0, 0, 0);

  // current shape of the region
  protected Shape mShape = Shape.RECTANGLE;

  // The list of points that make up the polygon
  protected Polygon mPoly = new Polygon();
  
  // The image this region belongs to
  protected Image mImage;  
  
  /**
   * Creates a new region within the given image.
   * @param image
   */
  public Region(Image image) {
    mImage = image;
  }
  
  /**
   * Creates a new region that is a copy of the given region.
   * @param source
   */
  public Region(Region source) {
    if (source != null) {
      mShape = source.mShape;
      mBounds = 
          new Rectangle(
              source.mBounds.x, 
              source.mBounds.y, 
              source.mBounds.width, 
              source.mBounds.height);
      mPoly = new Polygon(source.mPoly);
      mImage = source.mImage;
    }
  }  
  
  /**
   * Sets the {@link Image} this region is a part of.
   * @param image
   */
  public void setImage(Image image) {
    mImage = image;
  }

  /**
   * Returns the bounds of the region.
   * @return
   */
  public Rectangle getBounds() {
    Rectangle bounds;
    // Calculate the bounds of the polygon
    if (mShape == Shape.POLYGON) {
      bounds = mPoly.getBounds();
    }
    else {
      bounds = new Rectangle(mBounds.x, mBounds.y, mBounds.width, mBounds.height);
    }
    return bounds;
  }

  /**
   * Sets bounds of region.
   * @param r
   * @return the dirty rectangle in image space, you can use this to invalidate nicely 
   */
  public Rectangle setBounds(Rectangle r) {
    Rectangle bounds = getBounds();
    Rectangle dirty = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    mPoly.setBounds(r);
    RectangleUtil.copy(r, mBounds);
    dirty.union(getBounds());
    return dirty;
  }

  /**
   * Sets the bounds to a default value.
   * @return the dirty rectangle in image space, you can use this to invalidate nicely 
   */
  public Rectangle resetBounds() {
    int w = mImage.getWidth();
    int h = mImage.getHeight();
    // Use the smaller side to determine the padding
    // This makes it feel more uniform
    int padding = (int)(Math.min(w, h) * PADDING_RATIO);
    return setBounds(new Rectangle(padding, padding, w-padding, h-padding));
  }
  
  /**
   * Update the bounds of the region.
   */
  public void updateBounds() {
    if (mShape == Shape.POLYGON) {
      RectangleUtil.copy(getBounds(), mBounds);
    }
  }
  
  /**
   * Returns the shape of the region.
   * @return
   */
  public Shape getShape() {
    return mShape;
  }

  /**
   * Sets the shape of the region.
   * @param s
   */
  public void setShape(Shape s) {
    mShape = s;
    // update the bounds if the poly has one point or less
    if (mShape == Shape.POLYGON && mPoly.size() < 2) updateBounds();
  }

  /**
   * Returns the region's polygon.
   * @return
   */
  public Polygon getPolygon() {
    return mPoly;
  }

  /**
   * Sets the polygon of this region to be a copy of the given polygon.
   * @param poly
   * @return the dirty rectangle in image space, you can use this to invalidate nicely 
   */
  public Rectangle setPolygon(Polygon poly) {
    Rectangle dirty = new Rectangle(0, 0, 0, 0);
    RectangleUtil.copy(getBounds(), dirty);
    mPoly.set(poly);
    updateBounds();
    dirty.union(getBounds());
    return dirty;
  }
  
  /**
   * Reset the polygon's points.
   * @return
   */
  public Rectangle resetPolygon() {
    Rectangle dirty = new Rectangle(0, 0, 0, 0);
    RectangleUtil.copy(getBounds(), dirty);
    mPoly.reset();
    updateBounds();
    return dirty;
  }
  
  /**
   * Returns the points of the region.
   * @return
   */
  public Point[] getPoints() {
    if (mShape == Shape.POLYGON) {
      return mPoly.getPoints();
    }
    else {
      Rectangle bounds = getBounds();
      Point[] points = new Point[2];
      points[0] = new Point(bounds.x, bounds.y);
      points[1] = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
      return points;
    }
  }
  
  /**
   * Returns the points of the region in an int array.
   * @return
   */
  public int[] toArray() {
    Point[] points = getPoints();
    int[] rtn = new int[points.length * 2];
    for (int i = 0; i < points.length; i++) {
      Point p = points[i];
      rtn[i * 2] = p.x;
      rtn[i * 2 + 1] = p.y;
    }
    return rtn;
  }
  
  /**
   * Returns whether the given point is contained by the region.
   * @param point
   * @return
   */
  public boolean contains(Point point) {
    Rectangle bounds = getBounds();
    boolean contains = false;
    // first check if it is within the rectangular bounds
    if (bounds.contains(point)) {
      switch(mShape) {
        case POLYGON:
          contains = mPoly.contains(point.x, point.y);
          break;
        case OVAL:
          int cx = RectangleUtil.centerX(bounds);
          int cy = RectangleUtil.centerY(bounds);
          int rx2 = RectangleUtil.right(bounds) - cx;
          rx2 *= rx2; // square
          int ry2 = RectangleUtil.bottom(bounds) - cy;
          ry2 *= ry2; // square
          float dx = (float)(point.x - cx);
          dx *= dx;
          dx /= rx2;
          float dy = (float)(point.y - cy);
          dy *= dy;
          dy /= ry2;
          contains = (dx + dy <= 1);
          break;
        default:
          contains = true;
      }
    }
    else {
      contains = false;
    }
    return contains;
  }
  
  /**
   * Gets all the indices of pixels within the {@link Image} contained by this region.
   * @param img
   * @return
   */  
  public int[] getIndices() {
    Rectangle bounds = getBounds();
    int[] indices;
    
    switch (mShape) {
      case POLYGON:

        // find all the points within the poly
        int[] tmp = new int[bounds.width * bounds.height];
        int i = -1;
        for (int y = bounds.y; y < RectangleUtil.bottom(bounds); y++) {
          for (int x = bounds.x; x < RectangleUtil.right(bounds); x++) {
            if (mPoly.contains(x, y)) {
              tmp[++i] = mImage.getIndex(x, y);
            }
          }
        }
        // trim out the empty spots
        indices = Arrays.copyOf(tmp, i);
        break;
        
      case OVAL:
        // find all the points within the oval
        int[] tmp2 = new int[bounds.width * bounds.height];
        int j = -1;

        int cx = RectangleUtil.centerX(bounds);
        int cy = RectangleUtil.centerY(bounds);
        int rx2 = RectangleUtil.right(bounds) - cx;
        rx2 *= rx2; // square
        int ry2 = RectangleUtil.bottom(bounds) - cy;
        ry2 *= ry2; // square
        
        for (int y = bounds.y; y < RectangleUtil.bottom(bounds); y++) {
          for (int x = bounds.x; x < RectangleUtil.right(bounds); x++) {
            
            float dx = (float)(x - cx);
            dx *= dx;
            dx /= rx2;
            float dy = (float)(y - cy);
            dy *= dy;
            dy /= ry2;
            
            // if the point is within the oval
            if (dx + dy <= 1) {
              tmp2[++j] = mImage.getIndex(x, y);
            }
          }
        }
        // trim out the empty spots
        indices = Arrays.copyOf(tmp2, j);
        break;
        
      default: // RECTANGLE
        indices = mImage.getIndices(
            bounds.x, 
            bounds.y, 
            RectangleUtil.right(bounds), 
            RectangleUtil.bottom(bounds));
        break;
    }
    return indices;
  }
  
  /**
   * Gets the indices of all pixels within this region in list form.
   * @return
   */
  public List<Integer> getIndicesList() {
    int[] indices = getIndices();
    List<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < indices.length; i++) {
      list.add(indices[i]);
    }
    return list;
  }
  
  /**
   * Returns the center pixel of this region.
   * @param img
   * @return
   */  
  public int getCenterIndex() {
    Rectangle bounds = getBounds();
    return mImage.getIndex(RectangleUtil.centerX(bounds), RectangleUtil.centerY(bounds));
  }
  
  /**
   * Moves the region by the given delta.
   * @param dx
   * @param dy
   */
  public Rectangle move(int dx, int dy) {
    Rectangle bounds = getBounds();
  
    // move
    RectangleUtil.offset(bounds, dx, dy);
  
    // constrain top and left
    RectangleUtil.offsetTo(
        bounds, 
        Math.max(0, bounds.x),
        Math.max(0, bounds.y));
  
    // constrain bottom and right
    RectangleUtil.offsetTo(
        bounds,
        Math.min(mImage.getWidth() - bounds.width, bounds.x),
        Math.min(mImage.getHeight() - bounds.height, bounds.y));
  
    return setBounds(bounds);
  }

  /**
   * Resizes the given edge by the given delta.
   * @param dx
   * @param dy
   * @param edg
   */
  public Rectangle resize(int dx, int dy, Edge edg) {
    Rectangle newBounds = getBounds();
    resize(dx, dy, edg, newBounds);
    return setBounds(newBounds);
  }

  /**
   * Resizes the given edge of the region by the given deltas.
   * @param dx
   * @param dy
   * @param edg
   * @param newBounds
   */
  private void resize(int dx, int dy, Edge edg, Rectangle newBounds) {
    switch (edg) {
      case L: 
        // constrain to image area
        newBounds.x = Math.max(0, newBounds.x + dx); 
        // prevent flipping and keep min size
        newBounds.x = Math.min(newBounds.x, RectangleUtil.right(newBounds)); 
        break;
      case R: 
        RectangleUtil.setRight(newBounds, (Math.min(mImage.getWidth(), RectangleUtil.right(newBounds) + dx)));
        RectangleUtil.setRight(newBounds, Math.max(RectangleUtil.right(newBounds), newBounds.x));
        break;
      case T: 
        newBounds.y = Math.max(0, newBounds.y + dy);
        newBounds.y = Math.min(newBounds.y, RectangleUtil.bottom(newBounds));
        break;
      case B: 
        RectangleUtil.setBottom(newBounds, Math.min(mImage.getHeight(), RectangleUtil.bottom(newBounds) + dy));
        RectangleUtil.setBottom(newBounds, Math.max(RectangleUtil.bottom(newBounds), newBounds.y));
        break;
      case TL:
        resize(dx, dy, Edge.T, newBounds);
        resize(dx, dy, Edge.L, newBounds);
        break;
      case TR:
        resize(dx, dy, Edge.T, newBounds);
        resize(dx, dy, Edge.R, newBounds);
        break;
      case BL:
        resize(dx, dy, Edge.B, newBounds);
        resize(dx, dy, Edge.L, newBounds);
        break;
      case BR:
        resize(dx, dy, Edge.B, newBounds);
        resize(dx, dy, Edge.R, newBounds);
        break;
      default:
    }
  }

  /**
   * Adds a point to the polygon of the region.
   * @param x
   * @param y
   */
  public void addPoint(int x, int y) {
    mPoly.addPoint(x, y);
    updateBounds();
  }

}
