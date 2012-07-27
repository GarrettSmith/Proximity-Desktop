/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;

/**
 * @author Garrett Smith
 *
 */
//TODO: zoom to cursor
public class ZoomListener implements MouseWheelListener {
  
  private ImageCanvas mCanvas;
  
  public ZoomListener(ImageCanvas canvas) {
    mCanvas = canvas;
  }

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
