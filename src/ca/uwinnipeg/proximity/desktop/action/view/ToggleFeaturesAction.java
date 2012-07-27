/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class ToggleFeaturesAction extends Action {
  
  public ToggleFeaturesAction() {
    super(
        ProximityDesktop.getBundle().getString("Actions.ToggleFeatures.text"), 
        Action.AS_CHECK_BOX);
    setChecked(true);
  }
  
  @Override
  public void runWithEvent(Event event) {
    MenuItem item = (MenuItem) event.widget;
    ProximityDesktop.getApp().toggleFeatures(item.getSelection());
  }

}
