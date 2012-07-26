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
public class DeleteAction extends Action {
  
  public DeleteAction() {
    super(MainWindow.getBundle().getString("Actions.Delete.text"));
  }

}
