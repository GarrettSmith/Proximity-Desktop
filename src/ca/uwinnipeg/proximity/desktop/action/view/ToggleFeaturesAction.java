/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class ToggleFeaturesAction extends Action {
  
  public ToggleFeaturesAction() {
    super(
        MainWindow.getBundle().getString("MainWindow.actnFeatures.text"), 
        Action.AS_CHECK_BOX);
    setChecked(true);
  }
  
  @Override
  public void runWithEvent(Event event) {
    MenuItem item = (MenuItem) event.widget;
    MainWindow.getApp().toggleFeatures(item.getSelection());
  }

}
