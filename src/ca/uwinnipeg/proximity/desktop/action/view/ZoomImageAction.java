/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * An action that sets the zoom to fit the image to the canvas.
 * @author garrett
 *
 */
public class ZoomImageAction extends Action {
  
  public ZoomImageAction() {
    super(ProximityDesktop.getBundle().getString("Actions.ZoomImage.text"));
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_3_NAME));
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().getCanvas().fitToImage();
  }

}
