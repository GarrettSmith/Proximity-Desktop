/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;

/**
 * Listens to the middle mouse button being held while the mouse is dragged and pans the canvas 
 * accordingly.
 * @author Garrett Smith
 *
 */
public class PanListener implements MouseMoveListener {
  
  private ImageCanvas mCanvas;

  private int mPrevX = -1;
  private int mPrevY = -1;

  /**
   * Creates a new pan listener to operate on the given canvas.
   */
  public PanListener(ImageCanvas canvas) {
    mCanvas = canvas;
  }
  
  /**
   * Respons to the mouse being moved and pan if the correct condition are met.
   */
  public void mouseMove(MouseEvent e) {
    // check if the middle mouse button is pressed
    if ((e.stateMask & SWT.BUTTON2) != 0) {
      // make if this is the first valid event
      if (mPrevX != -1) {
        int dx = e.x - mPrevX;
        int dy = e.y - mPrevY;              
        mCanvas.pan(dx, dy);
      }
      // record the previous point
      mPrevX = e.x;
      mPrevY = e.y;
    }
    else {
      // record we have stopped
      mPrevX = -1;
    }
  }

}
