/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class RedoAction extends Action {
  
  public RedoAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Redo.text"));
  }
  @Override
  public void run() {
    ProximityController controller = ProximityDesktop.getController();
    ProximityDesktop app = ProximityDesktop.getApp();
    if (controller.getRedo()) {
      controller.redo();
    }
    app.getCanvas().redraw();
    setEnabled(controller.getRedo());
    app.updateHistoryActions();
  }
}
