/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * An action that zooms to fit the selected regions to the canvas. This currently does nothing.
 * @author garrett
 *
 */
// TODO: Implement zooming to selection
public class ZoomSelectionAction extends Action {
  
  public ZoomSelectionAction() {
    super(ProximityDesktop.getBundle().getString("Actions.ZoomSelection.text"));
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_2_NAME));
  }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }

}
