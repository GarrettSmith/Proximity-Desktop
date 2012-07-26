/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.help;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.AboutDialog;
import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class AboutAction extends Action {
  
  public AboutAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnAbout.text"));
  }

  @Override
  public void run() {
    AboutDialog dialog = new AboutDialog(MainWindow.getApp().getShell());
    dialog.open();
  }
}
