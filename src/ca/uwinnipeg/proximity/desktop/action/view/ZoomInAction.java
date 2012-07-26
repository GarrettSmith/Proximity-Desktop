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
public class ZoomInAction extends Action {

  public ZoomInAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnZoomIn.text"));
    setAccelerator(0 | '=');
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_ADD_NAME));
  }
  
  @Override
  public void run() {
    MainWindow.getApp().getCanvas().zoomIn();
  }
}
