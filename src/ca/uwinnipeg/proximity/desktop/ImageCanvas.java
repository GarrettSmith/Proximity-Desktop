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
  
//  private Transform mTransform;  
  private float mTranslateX = 0;
  private float mTranslateY = 0;
  private float mScale = 1;
  
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
    
//    mTransform = new Transform(mDisplay);
    
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
    mOldBounds = getBounds();
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

    if (mImage != null) {
      updateBounds(currentBounds, mOldBounds);
      Rectangle imageBounds = mImage.getBounds();
      gc.drawImage(
          mImage, 
          0, 0, mImage.getBounds().width, mImage.getBounds().height, 
          Math.round(mTranslateX), 
          Math.round(mTranslateY), 
          Math.round(mScale * imageBounds.width), 
          Math.round(mScale * imageBounds.height));
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
        mTranslateX += dx;
        mTranslateY += dy;
      }
    }
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
      
      mScale = mMinScale;

      // find the transform to center
      mTranslateX = (float) (canvasBounds.width - imageBounds.width * mScale) / 2; 
      mTranslateY = (float) (canvasBounds.height - imageBounds.height * mScale) / 2; 

      // redraw
      redraw();
    }
  }
  
  public void zoomTo1() {
    zoomTo(1);
    mFitToImage = false;
  }
  
  public float getScale() {
    return mScale;
  }
  
  public float getTranslateX() {
    return mTranslateX;
  }
  
  public float getTranslateY() {
    return mTranslateY;
  }
  
  public void zoomTo(float scale) {
    Rectangle imageBounds = mImage.getBounds();
    
    float oldScale = mScale;    
    mScale = scale;    

    float oldWidth = imageBounds.width * oldScale;
    float oldHeight = imageBounds.height * oldScale;    
    
    float newWidth = imageBounds.width * mScale;
    float newHeight = imageBounds.height * mScale;
    
    mTranslateX += (oldWidth - newWidth)/2;
    mTranslateY += (oldHeight - newHeight)/2;
    
//    int oldW = mBounds.width;
//    int oldH = mBounds.height;  
//    
//    mBounds.width *= scale;
//    mBounds.height *= scale;
//    
//    // offset by half the change in width and height to keep centered
//    mBounds.x += (oldW - mBounds.width)/2;
//    mBounds.y += (oldH - mBounds.height)/2;    
    redraw();
  }

  public void zoomBy(float scale) {
    float dScale = mScale * scale;
    zoomTo(dScale);
  }

  public void zoomIn() {
    zoomBy(1.1f);
    mFitToImage = false;
  }
  
  public void zoomOut() {
    zoomTo(Math.max(mScale * 0.9f, mMinScale));
    mFitToImage = false;
  }
  
  public void panTo(float x, float y) {
    mTranslateX = x;
    mTranslateY = y;
    redraw();
  }
  
  public void panBy(float dx, float dy) {
    mTranslateX += dx;
    mTranslateY += dy;
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
    mTranslateX = (float) (canvasBounds.width - imageBounds.width * mScale) / 2; 
    mTranslateY = (float) (canvasBounds.height - imageBounds.height * mScale) / 2; 
    
    redraw();
  }
  
  public Point toImageSpace(Point p) {
    Point rtn = new Point(p.x, p.y);
    
    // un-shift translation
    rtn.x -= mTranslateX;
    rtn.y -= mTranslateY;
    
    // un-scale
    rtn.x /= mScale;
    rtn.y /= mScale;
    
    return rtn;
  }
  
  public boolean contains(Point p) {
    int width = mImage.getBounds().width;
    int height = mImage.getBounds().height;
    
    return ( (0 <= p.x && p.x <= width) && (0 <= p.y && p.y <= height));
  }

}
