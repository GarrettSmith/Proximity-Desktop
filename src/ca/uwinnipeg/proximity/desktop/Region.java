package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

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

  // Flags if one time setup has been done
  private static boolean SETUP = false;
  
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
      mBounds = source.mBounds;
      mPoly = source.mPoly;
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

//  public RectF getBoundsF() {
//    return new RectF(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom);
//  }

  /**
   * Sets bounds of region.
   * @param r
   * @return the dirty rectangle in image space, you can use this to invalidate nicely 
   */
  public Rectangle setBounds(Rectangle r) {
    Rectangle bounds = getBounds();
    Rectangle dirty = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    mPoly.setBounds(r);
    copyRectangle(r, mBounds);
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
  
  public void updateBounds() {
    if (mShape == Shape.POLYGON) {
      copyRectangle(getBounds(), mBounds);
    }
  }
  
  private static void copyRectangle(Rectangle source, Rectangle dest) {
    dest.x = source.x;
    dest.y = source.y;
    dest.width = source.width;
    dest.height = source.height;
  }
  
  private static int centerX(Rectangle r) {
    return (r.x + (r.width / 2));
  }

  private static int centerY(Rectangle r) {
    return (r.y + (r.height / 2));
  }
  
  private static void offsetRectangle(Rectangle r, int dx, int dy) {
    r.x += dx;
    r.y += dy;
  }
  
  private static void offsetRectangleTo(Rectangle r, int x, int y) {
    int dx = x - r.x;
    int dy = y - r.y;
    offsetRectangle(r, dx, dy);
  }

  private Rectangle getImageBounds() {
    return new Rectangle(0, 0, mImage.getWidth(), mImage.getHeight());
  }
  
  private static int right(Rectangle r) {
    return r.x + r.width;
  }
  
  private static int bottom(Rectangle r) {
    return r.y + r.height;
  }
  
  private static void setRight(Rectangle r, int right) {
    r.width = right - r.x;
  }

  private static void setBottom(Rectangle r, int bottom) {
    r.height = bottom - r.y;
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
    copyRectangle(getBounds(), dirty);
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
    copyRectangle(getBounds(), dirty);
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
        for (int y = bounds.x; y < bottom(bounds); y++) {
          for (int x = bounds.x; x < right(bounds); x++) {
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

        int cx = centerX(bounds);
        int cy = centerY(bounds);
        int rx2 = right(bounds) - cx;
        rx2 *= rx2; // square
        int ry2 = bottom(bounds) - cy;
        ry2 *= ry2; // square
        
        for (int y = bounds.y; y < bottom(bounds); y++) {
          for (int x = bounds.x; x < right(bounds); x++) {
            
            float dx = (float)(x - cx);
            dx *= dx;
            dx /= rx2;
            float dy = (float)(y - cy);
            dy *= dy;
            dy /= ry2;
            
            // if the point is within the oval
            if ( dx + dy <= 1) {
              tmp2[++j] = mImage.getIndex(x, y);
            }
          }
        }
        // trim out the empty spots
        indices = Arrays.copyOf(tmp2, j);
        break;
        
      default: // RECTANGLE
        indices = mImage.getIndices(bounds.x, bounds.y, right(bounds), bottom(bounds));
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
    return mImage.getIndex(centerX(bounds), centerY(bounds));
  }
  
  /**
   * Moves the region by the given delta.
   * @param dx
   * @param dy
   */
  public Rectangle move(int dx, int dy) {
    Rectangle bounds = getBounds();
  
    // move
    offsetRectangle(bounds, dx, dy);
  
    // constrain top and left
    offsetRectangleTo(
        bounds, 
        Math.max(0, bounds.x),
        Math.max(0, bounds.y));
  
    // constrain bottom and right
    offsetRectangleTo(
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

  private void resize(int dx, int dy, Edge edg, Rectangle newBounds) {
    switch (edg) {
      case L: 
        // constrain to image area
        newBounds.x = Math.max(0, newBounds.x + dx); 
        // prevent flipping and keep min size
        newBounds.x = Math.min(newBounds.x, right(newBounds)); 
        break;
      case R: 
        setRight(newBounds, (Math.min(mImage.getWidth(), right(newBounds) + dx)));
        setRight(newBounds, Math.max(right(newBounds), newBounds.x));
        break;
      case T: 
        newBounds.y = Math.max(0, newBounds.y + dy);
        newBounds.y = Math.min(newBounds.y, bottom(newBounds));
        break;
      case B: 
        setBottom(newBounds, Math.min(mImage.getHeight(), bottom(newBounds) + dy));
        setBottom(newBounds, Math.max(bottom(newBounds), newBounds.y));
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

  public void addPoint(int x, int y) {
    mPoly.addPoint(x, y);
    updateBounds();
  }

  public Path getPath() {
    Path path = new Path(Display.getCurrent());
    path.addPath(getShapePath());

    // only add center point when the shape is a poly with atleast 3 points
    if (!(mShape == Shape.POLYGON && mPoly.size() < 3)) {
      path.addPath(getCenterPath());
    }
    
    return path;
  }

  /**
   * Returns the path representing this region in image space.
   * @return
   */
  // TODO: get the proper shape path
  public Path getShapePath() {
    Path shapePath = new Path(Display.getCurrent());
  
    switch (mShape) {
      case RECTANGLE:
        Rectangle bounds = getBounds();
        shapePath.addRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        break;
      case OVAL:
        //TODO:shapePath.addOval(getBoundsF(), Path.Direction.CW);
        break;
      case POLYGON:
        shapePath.addPath(mPoly.getPath());
        break;
    }
    
    return shapePath;
  }

  /**
   * Returns the path representing the center pixel of this region in image space.
   * @return
   */
  public Path getCenterPath() {
    Path centerPath = new Path(Display.getCurrent());
    
    Rectangle bounds = getBounds();
    int cx = centerX(bounds);
    int cy = centerY(bounds);
    
    int size = 10;
    
    centerPath.addRectangle(
        bounds.x - size/2, 
        bounds.y - size/2, 
        size, 
        size);
    
    return centerPath;
  }
  
//  /**
//   * Returns the paint that is the same colour as the center pixel of this region.
//   * @return
//   */
//  TODO: public Paint getCenterPaint() {
//    Rectangle bounds = getBounds();
//    Paint paint = new Paint(CENTER_BASE_PAINT);
//    int color = mImage.getPixel(bounds.centerX(), bounds.centerY());
//    paint.setColor(color);
//    return paint;
//  }

}
