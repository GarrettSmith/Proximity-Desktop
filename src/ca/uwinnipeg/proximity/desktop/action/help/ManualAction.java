/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.help;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class ManualAction extends Action {

  public ManualAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnManual.text"));
  }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }
}
