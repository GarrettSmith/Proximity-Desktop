/**
 * 
 */
package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
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
  
  private Display mDisplay;
  
  private Image mImage;
  
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
    
    mOldBounds = getBounds();
    
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

    if (mImage != null) {
      updateBounds(currentBounds, mOldBounds);
      gc.setTransform(mTransform);
      gc.drawImage(mImage, 0, 0);
    }
    
    mOldBounds = currentBounds;
  }
  
  /**
   * Update the transform to keep the centre of the current transform in the centre of the canvas.
   * @param current
   * @param old
   */
  private void updateBounds(Rectangle current, Rectangle old) {
    float dx = (float)(current.width - old.width)/2;
    float dy = (float)(current.height - old.height)/2;
    mTransform.translate(dx, dy);
  }
  
  /**
   * Sets the transform to contain and center the image in the canvas.
   */
  public void fitToImage() {
    // reset transform
    mTransform.identity();  
    
    // set that we want to stay fit to the image
    mFitToImage = true;
    
    if (mImage != null) {
      Rectangle canvasBounds = getBounds();
      Rectangle imageBounds = mImage.getBounds();

      float scaleX = (float) canvasBounds.width / imageBounds.width;
      float scaleY = (float) canvasBounds.height / imageBounds.height;
      float scale = Math.min(scaleX, scaleY);
      scale = Math.min(scale, 1);

      // find the transform to center
      float translateX = (float) (canvasBounds.width - imageBounds.width * scale) / 2; 
      float translateY = (float) (canvasBounds.height - imageBounds.height * scale) / 2; 

      // update transform  
      mTransform.translate(translateX, translateY);
      mTransform.scale(scale, scale);

      // redraw
      redraw();
    }
  }
  
  public void zoomTo(float scale) {
    
  }
  
  public void zoomBy(float scale) {
    
  }
  
  public void zoomIn() {
    
  }
  
  public void zoomOut() {
    
  }
  
  public void panTo(float x, float y) {
    
  }
  
  public void panBy(float dx, float dy) {
    
  }

}
