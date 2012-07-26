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
public class DuplicateAction extends Action {
  
  public DuplicateAction() {
    super(MainWindow.getBundle().getString("Actions.Duplicate.text"));
  }

}
