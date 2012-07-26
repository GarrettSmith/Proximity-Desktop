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
public class CutAction extends Action {
  
  public CutAction() {
    super(MainWindow.getBundle().getString("Actions.Copy.text"));
  }

}
