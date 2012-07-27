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
public class EmptyAction extends Action {
  
  public EmptyAction() {
    super(MainWindow.getBundle().getString("MainWindow.actionEmpty.text"));
    setEnabled(false);
  }

}
