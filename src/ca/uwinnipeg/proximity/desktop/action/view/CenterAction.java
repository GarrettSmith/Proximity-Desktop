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
public class CenterAction extends Action {
  
  public CenterAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Center.text"));
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().getCanvas().center();
  }

}
