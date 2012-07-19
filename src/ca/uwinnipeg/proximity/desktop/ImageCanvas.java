/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Garrett Smith
 *
 */
public class ImageCanvas extends Canvas {
  
  protected final static int PADDING = 10;
  
  protected float mMinScale;
  
  private Display mDisplay;
  
  private Image mImage;
  
//  private float mTranslateX = 0;
//  private float mTranslateY = 0;
//  private float mScale = 1;
  
  private Transform mTransform;
  
  private boolean mFitToImage = false;
  
  private Rectangle mOldBounds;
  
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
   * Create a new ImageCanvas without an image.
   * @param parent
   * @param style
   */
  public ImageCanvas(Composite parent, int style) { 
    this(parent, style, null);
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
   * Paints the image with the current transform over a background.
   * @param gc
   */
  protected void onPaint(GC gc) {   
    Rectangle currentBounds = getBounds();    
    
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
//      Rectangle imageBounds = mImage.getBounds();
//      gc.drawImage(
//          mImage, 
//          0, 0, mImage.getBounds().width, mImage.getBounds().height, 
//          Math.round(mTranslateX), 
//          Math.round(mTranslateY), 
//          Math.round(mScale * imageBounds.width), 
//          Math.round(mScale * imageBounds.height));
      gc.setTransform(mTransform);
      gc.drawImage(mImage, 0, 0);
    }
    
    mOldBounds = currentBounds;
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
//        mTranslateX += dx;
//        mTranslateY += dy;
        mTransform.translate(dx, dy);
      }
    }
  }  
  
  private float[] getValues() {
    float[] values = new float[6];
    mTransform.getElements(values);
    return values;
  }
  
  public float getScale() {
//    return mScale;
    return getValues()[0];
  }
  
  public float getTranslateX() {
//    return mTranslateX;
    return getValues()[4];
  }
  
  public float getTranslateY() {
//    return mTranslateY;
    return getValues()[5];
  }
  
  /**
   * Sets the transform to contain and center the image in the canvas.
   */
  public void fitToImage() {    
    // set that we want to stay fit to the image
    mFitToImage = true;
    
    if (mImage != null) {
      Rectangle canvasBounds = getBounds();
      Rectangle imageBounds = mImage.getBounds();
      
//      mScale = mMinScale;
      float dScale = mMinScale / getScale();
      mTransform.scale(dScale, dScale);

      center();
    }
  }
  
  public void zoomTo1() {
    zoomTo(1);
    mFitToImage = false;
  }
  
  public void zoomTo(float scale) {
//    Rectangle imageBounds = mImage.getBounds();
//    
//    //  float oldScale = mScale;    
//    mScale = scale;    
//
//    float oldWidth = imageBounds.width * oldScale;
//    float oldHeight = imageBounds.height * oldScale;    
//    
//    float newWidth = imageBounds.width * mScale;
//    float newHeight = imageBounds.height * mScale;
//    
//    mTranslateX += (oldWidth - newWidth)/2;
//    mTranslateY += (oldHeight - newHeight)/2;
//
//    redraw();
    float dScale = scale / getScale();
    zoomBy(dScale);
  }
  
  public void zoomTo(Point startPoint, Point endPoint) {
//    int top = Math.min(startPoint.x, endPoint.x);
//    int left = Math.min(startPoint.y, endPoint.y);
//    
//    Rectangle bounds = new Rectangle(
//        top, 
//        left, 
//        Math.abs(endPoint.x - startPoint.x), 
//        Math.abs(endPoint.y - startPoint.y));    
//    
//    Rectangle imageBounds = mImage.getBounds();
//    
//    float scaleX = (float)imageBounds.width / bounds.width;
//    float scaleY = (float)imageBounds.height / bounds.height;
//    
//    mScale = Math.max(scaleX, scaleY);
//
//    mTranslateX = -top * mScale;
//    mTranslateY = -left * mScale;
//    
//    redraw();
  }

  public void zoomBy(float dScale) {
//    float dScale = mScale * scale;
//    zoomTo(dScale);
    
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
  }

  public void zoomIn() {
    zoomBy(1.1f);
    mFitToImage = false;
  }
  
  public void zoomOut() {
//    zoomTo(Math.max(mScale * 0.9f, mMinScale));
    zoomTo(Math.max(getScale() * 0.9f, mMinScale));
    mFitToImage = false;
  }
  
  public void panTo(float x, float y) {
    float dx = getTranslateX() - x;
    float dy = getTranslateY() - y;
    panBy(dx, dy);
//    mTranslateX = x;
//    mTranslateY = y;
//    redraw();
  }
  
  public void panBy(float dx, float dy) {
//    mTranslateX += dx;
//    mTranslateY += dy;
    float scale = getScale();
    mTransform.translate(dx / scale, dy / scale);
    redraw();
  }
  
  public void pan(float dx, float dy) {
    panBy(dx, dy);
    mFitToImage = false;
  }
  
  public void center() {
    Rectangle canvasBounds = getBounds();
    Rectangle imageBounds = mImage.getBounds();
    
    // find the transform to center
    float scale = getScale();
    float dx = ((float)canvasBounds.width / 2) - ((float)imageBounds.width * scale / 2) ;
    float dy = ((float)canvasBounds.height / 2) - ((float)imageBounds.height * scale / 2);
    mTransform.translate((dx - getTranslateX()) / scale, (dy - getTranslateY()) / scale);
    System.out.println("center: {" + dx + ", " + dy + "}");
    System.out.println("translation: {" + getTranslateX()/scale + ", " + getTranslateY()/scale + "}");
//    mTranslateX = (float) (canvasBounds.width - imageBounds.width * mScale) / 2; 
//    mTranslateY = (float) (canvasBounds.height - imageBounds.height * mScale) / 2; 
    
    redraw();
  }
  
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
  
  public boolean contains(Point p) {
    int width = mImage.getBounds().width;
    int height = mImage.getBounds().height;
    
    return ( (0 <= p.x && p.x <= width) && (0 <= p.y && p.y <= height));
  }

}
