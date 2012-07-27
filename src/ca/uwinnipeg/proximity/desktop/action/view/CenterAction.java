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
public class CenterAction extends Action {
  
  public CenterAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnCenter.text"));
  }
  
  @Override
  public void run() {
    MainWindow.getApp().getCanvas().center();
  }

}
