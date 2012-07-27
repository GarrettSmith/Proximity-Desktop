/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.AddFeaturesDialog;
import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class EditFeaturesAction extends Action {
  
  public EditFeaturesAction() {
    super(MainWindow.getBundle().getString("Actions.EditFeatures.text"));
  }

  @Override
  public void run() {
    AddFeaturesDialog dialog = new AddFeaturesDialog(MainWindow.getApp().getShell());
    dialog.open();
  }
}
