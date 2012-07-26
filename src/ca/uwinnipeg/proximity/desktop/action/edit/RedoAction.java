/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class RedoAction extends Action {
  
  public RedoAction() {
    super(MainWindow.getBundle().getString("Actions.Redo.text"));
  }
  @Override
  public void run() {
    if (mController.getRedo()) {
      mController.redo();
    }
    canvas.redraw();
    setEnabled(mController.getRedo());
    actnUndo.setEnabled(true);
  }
}
