/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * Sets the zoom level to 1. That is the image is displayed 1 to 1 on the screen.
 * @author garrett
 *
 */
public class ZoomTo1Action extends Action {

  public ZoomTo1Action() {
    super(ProximityDesktop.getBundle().getString("Actions.Zoom1to1.text"));
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_1_NAME));
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().getCanvas().zoomTo1();
  }
}
