/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class ExitAction extends Action {
  
  public ExitAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnExit.text"));
  }
  
  @Override
  public void run() {
    MainWindow.getApp().close();
  }

}
