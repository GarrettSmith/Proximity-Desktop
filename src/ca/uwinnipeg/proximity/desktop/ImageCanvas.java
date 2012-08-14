/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * The canvas used to draw the currently selected image and the property we are currently 
 * interested in.
 * @author Garrett Smith
 *
 */
public class ImageCanvas extends Canvas {
  
  protected static final int PADDING = 10;
  protected static final int PIVOT_SIZE = 16;
  
  protected float mMinScale;
  
  private Display mDisplay;
  
  private Image mImage;
  
  private Transform mTransform;
  
  private boolean mFitToImage = false;
  
  private Rectangle mOldBounds;
  
  private Map<Class<? extends PropertyController>, Image> mPropertyImages = 
      new HashMap<Class<? extends PropertyController>, Image>();
  
  private Class<? extends PropertyController> mPropertyKey;
  
  private boolean mShowPivots = true;
  
  /**
   * Create a canvas with the given image.
   * @param parent
   * @param style
   * @param image
   */
  public ImageCanvas(Composite parent, int style, Image image) {
    super(parent, style);
    
    mDisplay = Display.getCurrent();
    
    mTransform = new Transform(mDisplay);
    
    setImage(image);

    // register to handle painting
    addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        onPaint(e.gc);
      }        
    });
  }
  
  /**
   * Sets the image and resets the transform to fit the image.
   * @param image
   */
  public void setImage(Image image) {
    mImage = image;
    updateMinScale();
    
    // reset transform
    fitToImage();    
    
    redraw();
  }
  
  /**
   * Sets the property to be displayed to the given string.
   * @param key
   */
  public void setProperty(Class<? extends PropertyController> key) {
    boolean changed = mPropertyKey != key;
    mPropertyKey = key;
    if (changed) redraw();
  }
  
  /**
   * Update the image associated with the given property key.
   * @param key
   * @param points
   */
  public void updateProperty(Class<? extends PropertyController> key, int[] points) {
    if (mImage != null) {
      ImageData baseData = mImage.getImageData();
      Image img = new Image(mDisplay, mImage.getBounds());
      ImageData data = img.getImageData();

      // fill with transparent
      data.alphaData = new byte[data.data.length];

      for (int i = 0; i < points.length; i += 2) {
        int x = points[i];
        int y = points[i + 1];
        int pixel = baseData.getPixel(x , y);
        pixel = ~pixel; // invert colour
        data.setPixel(x, y, pixel);
        data.setAlpha(x, y, 255);
      }

      Image previousImage = mPropertyImages.put(key, new Image(mDisplay, data));
      
      // dispose the previous image
      if (previousImage != null) {
        previousImage.dispose();
      }

      //redraw if the current key was updated
      if (mPropertyKey != null && key != null && mPropertyKey.equals(key)) {
        redraw();
      }
    }
  }

  /**
   * Sets whether the pivot pixels should be drawn or not
   * @param show
   */
  public void showPivots(boolean show) {
    boolean changed = show != mShowPivots;
    mShowPivots = show;
    if (changed) redraw();
  }

  /**
   * Get an image that represents the current view of the image canvas.
   * This is used to generate the image saved as a snapshot.
   * @return
   */
  public Image getImage() {
    Rectangle bounds = mImage.getBounds();
    Image img = new Image(mDisplay, bounds.width, bounds.height);
    // draw the image
    if (mImage != null) {
      GC gc = new GC(img);
      gc.drawImage(mImage, 0, 0);

      // draw the property
      if (mPropertyKey != null) {
        Image propImg = mPropertyImages.get(mPropertyKey);
        if (propImg != null) {
          gc.drawImage(propImg, 0, 0);
        }
      }

      // draw regions
      for (Region r : ProximityDesktop.getController().getRegions()) {
        drawRegion(gc, r, true, mShowPivots, false);
      }
      gc.dispose();
    }
    return img;
  }

  /**
   * Paints the image with the current transform over a background.
   * @param gc
   */
  protected void onPaint(GC gc) {   
    Rectangle currentBounds = getBounds();    
    
    gc.setInterpolation(SWT.HIGH);
    gc.setAntialias(SWT.ON);
    
    
    //draw background
    // fill with black
    gc.setBackground(mDisplay.getSystemColor(SWT.COLOR_BLACK));
    gc.fillRectangle(0, 0, currentBounds.width, currentBounds.height);
    // draw gradient on bottom
    gc.setForeground(mDisplay.getSystemColor(SWT.COLOR_BLACK));
    gc.setBackground(new Color(mDisplay, 60, 60, 60));
    gc.fillGradientRectangle(
        0, 
        currentBounds.height/2, 
        currentBounds.width, 
        currentBounds.height/2, 
        true);

    // draw the image
    if (mImage != null) {
      updateBounds(currentBounds, mOldBounds);
      gc.setTransform(mTransform);
      gc.drawImage(mImage, 0, 0);
      gc.setTransform(null);
      
      // draw the property
      if (mPropertyKey != null) {
        Image propImg = mPropertyImages.get(mPropertyKey);
        if (propImg != null) {
          gc.setTransform(mTransform);
          gc.drawImage(propImg, 0, 0);
          gc.setTransform(null);
        }
      }

      // draw regions
      ProximityController controller = ProximityDesktop.getController();
      List<Region> selectedRegions = controller.getSelectedRegions();
      for (Region r : controller.getRegions()) {
        drawRegion(gc, r, selectedRegions.contains(r), mShowPivots, true);
      }
      
      // draw the selection bounds
      if (!selectedRegions.isEmpty()) {
        gc.setLineStyle(SWT.LINE_DASH);
        gc.setLineWidth(1);
        Rectangle bounds = controller.getSelectionBounds();
        bounds.x -= 6;
        bounds.y -= 6;
        bounds.width += 10;
        bounds.height += 10;
        bounds = toScreenSpace(bounds);
        gc.drawRectangle(bounds);
        gc.setLineStyle(SWT.LINE_SOLID);
      }

    }

    // store the new bounds
    mOldBounds = currentBounds;
  }
  
  /**
   * Draws the given region on the canvas.
   * @param gc the GC being used
   * @param region the region to be drawn
   * @param selected whether the region should be drawn as selected
   * @param drawCenter whether the center pixel of the region should be blown up and drawn
   * @param scale whether the region should be scaled to the screen
   */
  public void drawRegion(GC gc, Region region, boolean selected, boolean drawCenter, boolean scale) {
    drawRegion(gc, region.getShape(), region.toArray(), selected, drawCenter, scale);
  }
  
  /**
   * Draws a region using the given shape and points.
   * @param gc the GC being used
   * @param shape the shape to be drawn
   * @param points the points used to define the shape
   * @param selected whether the region should be drawn as selected
   * @param drawCenter whether the center pixel of the region should be blown up and drawn
   * @param scale whether the region should be scaled to the screen
   */
  public void drawRegion(
      GC gc, 
      Region.Shape shape, 
      int[] points, 
      boolean selected, 
      boolean drawCenter,
      boolean scale) {
    
    gc.setLineWidth(2);
    
    gc.setForeground(mDisplay.getSystemColor(SWT.COLOR_WHITE));
    gc.setXORMode(true);
    
    // translate to screen space
    int[] usedPoints;
    
    // check if we should scale to screen space
    if (scale) {
      usedPoints = toScreenSpace(points);
    }
    else {
      usedPoints = points;
    }
    
    // fade out if not selected
    if (selected) {
      gc.setAlpha(255);
    }
    else {
      gc.setAlpha(150);
    }
    
    // draw the shape border
    switch(shape) {
      case RECTANGLE:
        gc.drawRectangle(
            usedPoints[0],
            usedPoints[1],
            usedPoints[2] - usedPoints[0], 
            usedPoints[3] - usedPoints[1]);
        break;
      case OVAL:
        gc.drawOval(
            usedPoints[0], 
            usedPoints[1], 
            usedPoints[2] - usedPoints[0], 
            usedPoints[3] - usedPoints[1]);
        break;
      case POLYGON:
        gc.drawPolygon(usedPoints);
        break;
    }
    
    // draw the center pixel blown up
    if (drawCenter) {
      
      Rectangle imageBounds = new Rectangle(0, 0, 0, 0);
      if (points.length > 0) {

        // create an initial bounds from the first point
        imageBounds.x = points[0];
        imageBounds.y = points[1];

        Rectangle tmp = new Rectangle(0, 0, 0, 0);
        // Get the union of each point to get the final bounds
        for (int i = 2; i < points.length; i += 2) {
          tmp.x = points[i];
          tmp.y = points[i + 1];
          imageBounds = imageBounds.union(tmp);
        }
      }
      
      // draw pivot
      gc.setAlpha(255);
      
      int imageCenterx = imageBounds.x + (imageBounds.width / 2);
      int imageCentery = imageBounds.y + (imageBounds.height / 2);

      Point center = new Point(imageCenterx, imageCentery);
      
      if (scale) {
        center = toScreenSpace(center);
      }
      
      int pxl = mImage.getImageData().getPixel(imageCenterx, imageCentery);
      Color color = 
          new Color(Display.getCurrent(), (pxl >> 16) & 0xFF, (pxl >> 8) & 0xFF, pxl & 0xFF);
      
      gc.setBackground(color);
      gc.setXORMode(false);
      gc.fillRectangle(
          center.x - (PIVOT_SIZE / 2), 
          center.y - (PIVOT_SIZE / 2), 
          PIVOT_SIZE, 
          PIVOT_SIZE);
      
      // draw pivot outline
      gc.setXORMode(true);
      gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
      gc.drawRectangle(
          center.x - (PIVOT_SIZE / 2) - 1, 
          center.y - (PIVOT_SIZE / 2) - 1, 
          PIVOT_SIZE + 2, 
          PIVOT_SIZE + 2);
      
      color.dispose();
    }
    
  }

  /**
   * Find the scale that either makes the image fill the canvas or 1 if the image is smaller than 
   * the canvas.
   */
  private void updateMinScale() {
    if (mImage != null) {
      Rectangle canvasBounds = getBounds();
      Rectangle imageBounds = mImage.getBounds();

      float width = canvasBounds.width - PADDING * 2;
      float height = canvasBounds.height - PADDING * 2;

      float scaleX = width / imageBounds.width;
      float scaleY = height / imageBounds.height;
      float scale = Math.min(scaleX, scaleY);
      mMinScale = Math.min(scale, 1);      
    }
    else {
      mMinScale = 1;
    }
  }

  /**
   * Update the transform to keep the centre of the current transform in the centre of the canvas.
   * @param current
   * @param old
   */
  private void updateBounds(Rectangle current, Rectangle old) {
    if (!current.equals(old)) {
      // update the minimum scale
      updateMinScale();
      if (mFitToImage) {
        fitToImage();
      }
      else {
        float dx = (float)(current.width - old.width) / 2;
        float dy = (float)(current.height - old.height) / 2;
        mTransform.translate(dx, dy);
      }
    }
  }  
  
  /**
   * Gets values from the image transformation.
   * @return
   */
  private float[] getValues() {
    float[] values = new float[6];
    mTransform.getElements(values);
    return values;
  }
  
  /**
   * Get the current scale.
   * @return
   */
  public float getScale() {
    return getValues()[0];
  }
  
  /**
   * Get the translation in the x axis.
   * @return
   */
  public float getTranslateX() {
    return getValues()[4];
  }
  
  /**
   * Get the translation in the y axis.
   * @return
   */
  public float getTranslateY() {
    return getValues()[5];
  }
  
  /**
   * Sets the transform to contain and center the image in the canvas.
   */
  public void fitToImage() {    
    // set that we want to stay fit to the image
    mFitToImage = true;
    
    if (mImage != null) {      
//      mScale = mMinScale;
      float dScale = mMinScale / getScale();
      mTransform.scale(dScale, dScale);

      center();
    }
  }
  
  /**
   * Sets the scale to 1 so the image is 1 to 1 on the screen.
   */
  public void zoomTo1() {
    zoomTo(1);
    mFitToImage = false;
  }
  
  /**
   * Zoom to the given scale from the center of the image.
   * @param scale
   */
  public void zoomTo(float scale) {
    float dScale = scale / getScale();
    zoomBy(dScale);
  }
  
  /**
   * Zooms to the given scale at the given point.
   * @param startPoint
   * @param endPoint
   */
  // TODO: get this working!
  public void zoomTo(Point startPoint, Point endPoint) {
    
    Rectangle canvasBounds = getBounds();
    
    int top = Math.min(startPoint.x, endPoint.x);
    int left = Math.min(startPoint.y, endPoint.y);
    
    Rectangle zoomBounds = new Rectangle(
        top, 
        left, 
        Math.abs(endPoint.x - startPoint.x), 
        Math.abs(endPoint.y - startPoint.y));    

    float width = canvasBounds.width;
    float height = canvasBounds.height;

    float scaleX = width / zoomBounds.width;
    float scaleY = height / zoomBounds.height;
    float scale = Math.max(scaleX, scaleY);
    //scale = Math.min(scale, 1); 
    
    float dScale = Math.max(scaleX, scaleY) / getScale();
    
    mTransform.scale(dScale, dScale);
    
    System.out.println("scale x: " + scaleX + ", scale y: " + scaleY + ", scale: " + scale + ", scale delta: " + dScale);
    
    // find the transform to center
    scale = getScale();
    float dx = ((float)canvasBounds.width / 2) - ((float)zoomBounds.width * scale / 2) ;
    float dy = ((float)canvasBounds.height / 2) - ((float)zoomBounds.height * scale / 2);
    mTransform.translate((dx - getTranslateX()) / scale, (dy - getTranslateY()) / scale);
    
    System.out.println("dx: " + dx + ", dy: " + dy);
    System.out.println("translate x: " + getTranslateX() + ", translate y: " + getTranslateY());

    redraw();
  }

  /**
   * Zooms using the given delta in scale at the center of the image.
   * @param dScale
   */
  public void zoomBy(float dScale) {    
    Rectangle imageBounds = mImage.getBounds();
    float scale = getScale();
    
    float oldWidth = imageBounds.width * scale;
    float oldHeight = imageBounds.height * scale;
    
    float newWidth = oldWidth * dScale;
    float newHeight = oldHeight * dScale;
    
    float dx = (oldWidth - newWidth) / 2;
    float dy = (oldHeight - newHeight) / 2;
    
    mTransform.scale(dScale, dScale);
    
    scale = getScale();
    
    mTransform.translate(dx / scale, dy / scale);
    redraw();
//    float scale = getScale();
//    Rectangle bounds = mImage.getBounds();
//    zoomBy(dScale, bounds.width / 2, bounds.height / 2);
  }
  
  /**
   * Zooms using the given delta in scale at the given point.
   * @param dScale
   * @param x
   * @param y
   */
  public void zoomBy(float dScale, float x, float y) {
    Rectangle imageBounds = mImage.getBounds();
    float scale = getScale();
    
    float oldWidth = imageBounds.width * scale;
    float oldHeight = imageBounds.height * scale;
    
    float newWidth = oldWidth * dScale;
    float newHeight = oldHeight * dScale;
    
    float dx = -x - (newWidth / 2);
    float dy = -y - (newHeight / 2);
    
    mTransform.scale(dScale, dScale);
    
    scale = getScale();
    
    mTransform.translate(dx / scale, dy / scale);
    redraw();
  }

  /**
   * Zoom in to the image at a default value.
   */
  public void zoomIn() {
    zoomBy(1.2f);
    mFitToImage = false;
  }
  
  /**
   * Zoom out of the image at a default value.
   */
  public void zoomOut() {
//    zoomTo(Math.max(mScale * 0.9f, mMinScale));
    zoomTo(Math.max(getScale() * 0.8f, mMinScale));
    mFitToImage = false;
  }
  
  /**
   * Sets the current pan to the current value.
   * @param x
   * @param y
   */
  public void panTo(float x, float y) {
    float dx = getTranslateX() - x;
    float dy = getTranslateY() - y;
    panBy(dx, dy);
  }
  
  /**
   * Pans the image by the given deltas.
   * @param dx
   * @param dy
   */
  public void panBy(float dx, float dy) {
    float scale = getScale();
    mTransform.translate(dx / scale, dy / scale);
    redraw();
  }
  
  /**
   * Called by outside objects to pan the image.
   * @param dx
   * @param dy
   */
  public void pan(float dx, float dy) {
    panBy(dx, dy);
    mFitToImage = false;
  }
  
  /**
   * Centers the image in the screen.
   */
  public void center() {
    Rectangle canvasBounds = getBounds();
    Rectangle imageBounds = mImage.getBounds();
    
    // find the transform to center
    float scale = getScale();
    float dx = ((float)canvasBounds.width / 2) - ((float)imageBounds.width * scale / 2) ;
    float dy = ((float)canvasBounds.height / 2) - ((float)imageBounds.height * scale / 2);
    mTransform.translate((dx - getTranslateX()) / scale, (dy - getTranslateY()) / scale);
    
    redraw();
  }
  
  /**
   * Converts a point from screen space to image space.
   * @param p
   * @return
   */
  public Point toImageSpace(Point p) {
    Point rtn = new Point(p.x, p.y);
    
    // un-shift translation
    rtn.x -= getTranslateX();
    rtn.y -= getTranslateY();
    
    // un-scale
    float scale = getScale();
    rtn.x /= scale;
    rtn.y /= scale;
    
    return rtn;
  }
  
  /**
   * Converts a point from image space to screen space.
   * @param p
   * @return
   */
  public Point toScreenSpace(Point p) {
    Point rtn = new Point(p.x, p.y);
    
    // un-scale
    float scale = getScale();
    rtn.x *= scale;
    rtn.y *= scale;
    
    // un-shift translation
    rtn.x += getTranslateX();
    rtn.y += getTranslateY();
    
    return rtn;
  }
  
  /**
   * Converts an array of integers representing points alternating the x and y value from image
   * space to screen space.
   * @param points
   * @return
   */
  public int[] toScreenSpace(int[] points) {
    int[] rtn = new int[points.length];
    
    float scale = getScale();
    
    for (int i = 0; i < points.length; i++) {
      rtn[i] = (int) (points[i] * scale);
      if (i % 2 == 0) {
        // x point
        rtn[i] += getTranslateX();
      }
      else {
        // y point
        rtn[i] += getTranslateY();
      }
    }
    
    return rtn;
  }
  
  /**
   * Converts a rectangle from image space to screen space.
   * @param r
   * @return
   */
  public Rectangle toScreenSpace(Rectangle r) {
    
    // un-scale
    float scale = getScale();
    float x = r.x * scale;
    float y = r.y * scale;
    float width = r.width * scale;
    float height = r.height * scale;
    
    // un-shift translation
    x += getTranslateX();
    y += getTranslateY();
    
    return new Rectangle(
        Math.round(x), 
        Math.round(y), 
        Math.round(width),
        Math.round(height));
  }
  
  public boolean contains(Point p) {
    int width = mImage.getBounds().width;
    int height = mImage.getBounds().height;
    
    return ( (0 <= p.x && p.x <= width) && (0 <= p.y && p.y <= height));
  }
}
