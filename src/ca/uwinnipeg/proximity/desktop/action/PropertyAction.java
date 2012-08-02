/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ImageCanvas;
import ca.uwinnipeg.proximity.desktop.PropertyController;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * Sets the property displayed by the window's {@link ImageCanvas}.
 * @author garrett
 *
 */
public class PropertyAction extends Action {
  
  private Class<? extends PropertyController> mKey;
  
  public PropertyAction(String textKey, Class<? extends PropertyController> key) {
    super(ProximityDesktop.getBundle().getString(textKey), Action.AS_RADIO_BUTTON);
    mKey = key;
  }
  
  @Override
  public void run() {
<<<<<<< HEAD
    ProximityDesktop.getApp().setProperty(mKey);
  }   
}
