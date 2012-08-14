/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.help;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.AboutDialog;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * Creates the {@link AboutDialog}.
 * @author garrett
 *
 */
public class AboutAction extends Action {
  
  public AboutAction() {
    super(ProximityDesktop.getBundle().getString("Actions.About.text"));
  }

  @Override
  public void run() {
    AboutDialog dialog = new AboutDialog(ProximityDesktop.getApp().getShell());
    dialog.open();
  }
}
