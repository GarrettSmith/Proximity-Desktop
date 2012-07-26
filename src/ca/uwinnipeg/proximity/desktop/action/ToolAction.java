/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.MainWindow;
import ca.uwinnipeg.proximity.desktop.tool.Tool;

/**
 * @author garrett
 *
 */
/**
 * An action used to activate a tool.
 * @author Garrett Smith
 *
 */
public class ToolAction extends Action {

  public static final String ICON_PATH = "/ca/uwinnipeg/proximity/desktop/icons/";
  
  private Tool tool;

  public ToolAction(String label, String icon, Tool tool) {
    super(MainWindow.getBundle().getString(label), Action.AS_RADIO_BUTTON);
    setImageDescriptor(
        ResourceManager.getImageDescriptor(MainWindow.class, ICON_PATH + icon));
    this.tool = tool;
  }

  @Override
  public void run() {
    if (tool.isRegistered() != isChecked()) {
      // register and unregister from events
      if (isChecked()) {
        tool.addListeners();
      }
      else {
        tool.removeListeners();
      }
    }
  }
}
