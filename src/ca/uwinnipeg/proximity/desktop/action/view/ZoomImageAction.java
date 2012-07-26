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
public class ZoomImageAction extends Action {
  
  public ZoomImageAction() {
    super("MainWindow.actnZoomImage.text");
    //TODO: setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_3_NAME));
  }
  
  @Override
  public void run() {
    MainWindow.getApp().getCanvas().fitToImage();
  }

}
