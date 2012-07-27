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
public class ZoomTo1Action extends Action {

  public ZoomTo1Action() {
    super(MainWindow.getBundle().getString("MainWindow.actnZoom1to1.text"));
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_1_NAME));
  }
  
  @Override
  public void run() {
    MainWindow.getApp().getCanvas().zoomTo1();
  }
}
