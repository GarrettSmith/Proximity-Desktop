/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * An action that zooms out of the canvas.
 * @author garrett
 *
 */
public class ZoomOutAction extends Action {
  
  public ZoomOutAction() {
    super(ProximityDesktop.getBundle().getString("Actions.ZoomOut.text"));
    setAccelerator(0 | '-');
    //TODO:actnZoomOut.setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_SUBTRACT_NAME));
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().getCanvas().zoomOut();
  }
}
