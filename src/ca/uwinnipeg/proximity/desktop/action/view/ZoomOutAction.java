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
public class ZoomOutAction extends Action {
  
  public ZoomOutAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnZoomOut.text"));
    setAccelerator(0 | '-');
    //TODO:actnZoomOut.setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_SUBTRACT_NAME));
  }
  
  @Override
  public void run() {
    MainWindow.getApp().getCanvas().zoomOut();
  }
}
