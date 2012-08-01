/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ImageCanvas;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * Sets the property displayed by the window's {@link ImageCanvas}.
 * @author garrett
 *
 */
public class PropertyAction extends Action {
  
  private String mKey;
  
  public PropertyAction(String textKey, String propertyKey) {
    super(ProximityDesktop.getBundle().getString(textKey), Action.AS_RADIO_BUTTON);
    mKey = propertyKey;
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().getCanvas().setProperty(mKey);
  }   
}
