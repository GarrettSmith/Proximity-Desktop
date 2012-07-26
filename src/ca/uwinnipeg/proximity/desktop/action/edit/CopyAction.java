/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.edit;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class CopyAction extends Action {

  public CopyAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnCut.text"));
  }
}
