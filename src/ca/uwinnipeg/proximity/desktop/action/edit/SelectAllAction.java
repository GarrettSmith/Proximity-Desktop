/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * Selects all the regions added to the image.
 * @author garrett
 *
 */
public class SelectAllAction extends Action {
  
  public SelectAllAction() {
    super(ProximityDesktop.getBundle().getString("Actions.SelectAll.text"));
    setAccelerator(SWT.MOD1 | 'a');
  }
  
  @Override
  public void run() {
    ProximityController controller = ProximityDesktop.getController();
    controller.setSelected(controller.getRegions());
  }

}
