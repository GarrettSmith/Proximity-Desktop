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
public class SelectAllAction extends Action {
  
  public SelectAllAction() {
    super(MainWindow.getBundle().getString("Actions.SelectAll.text"));
  }

}
