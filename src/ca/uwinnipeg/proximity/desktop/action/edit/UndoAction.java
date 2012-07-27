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
public class UndoAction extends Action {
  
  public UndoAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Undo.text"));
  }

  @Override
  public void run() {
    ProximityController controller = ProximityDesktop.getController();
    ProximityDesktop app = ProximityDesktop.getApp();
    if (controller.getUndo()) {
      controller.undo();
    }
    app.getCanvas().redraw();
    setEnabled(controller.getUndo());
    app.updateHistoryActions();
  }
}
