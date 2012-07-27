/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

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
    if (mController.getUndo()) {
      mController.undo();
    }
    canvas.redraw();
    setEnabled(mController.getUndo());
    actnRedo.setEnabled(true);
  }
}
