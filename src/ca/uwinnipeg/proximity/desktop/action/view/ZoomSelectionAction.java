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
