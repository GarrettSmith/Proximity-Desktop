/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;

/**
 * Listens from modifier and scrolling to zoom in and out of the canvas.
 * @author Garrett Smith
 *
 */
//TODO: zoom to cursor
public class ZoomListener implements MouseWheelListener {
  
  private ImageCanvas mCanvas;
  
  /**
   * Creates a new listener for the given canvas.
   * @param canvas
   */
  public ZoomListener(ImageCanvas canvas) {
    mCanvas = canvas;
  }

  /**
   * Checks if the correct event has occurred and zooms if it did.
   */
  public void mouseScrolled(MouseEvent e) {
    // check if ctrl is being held
    if ((e.stateMask & SWT.CTRL) != 0 ) {
      // check whether to zoom in or out
      if (e.count < 0) {
        mCanvas.zoomOut();
      }
      else {
        mCanvas.zoomIn();
      }
    }
  }

}
