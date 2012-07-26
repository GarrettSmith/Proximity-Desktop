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
public class PasteAction extends Action {
  
  public PasteAction() {
    super(MainWindow.getBundle().getString("Actions.Paste.paste"));
  }

}
