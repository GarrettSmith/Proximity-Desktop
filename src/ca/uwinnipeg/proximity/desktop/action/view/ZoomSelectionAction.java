/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class ZoomSelectionAction extends Action {
  
  public ZoomSelectionAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnZoomSelection.text"));
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_2_NAME));
  }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }

}
