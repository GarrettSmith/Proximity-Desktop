/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.AddFeaturesDialog;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class EditFeaturesAction extends Action {
  
  public EditFeaturesAction() {
    super(ProximityDesktop.getBundle().getString("Actions.EditFeatures.text"));
  }

  @Override
  public void run() {
    AddFeaturesDialog dialog = new AddFeaturesDialog(ProximityDesktop.getApp().getShell());
    dialog.open();
  }
}
