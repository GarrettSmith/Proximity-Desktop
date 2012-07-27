/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class ZoomInAction extends Action {

  public ZoomInAction() {
    super(ProximityDesktop.getBundle().getString("Actions.ZoomIn.text"));
    setAccelerator(0 | '=');
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_ADD_NAME));
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().getCanvas().zoomIn();
  }
}
