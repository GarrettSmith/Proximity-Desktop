/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public abstract class PropertyAction extends Action {
  
  public PropertyAction(String textKey) {
    super(ProximityDesktop.getBundle().getString(textKey), Action.AS_RADIO_BUTTON);
  }
  
  @Override
  public void run() {
    if (isChecked()) {
      //TODO: display property?
    }
  }   
}
