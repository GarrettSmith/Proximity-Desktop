package ca.uwinnipeg.proximity.desktop.action.view;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

public class TogglePivotAction extends Action {

  public TogglePivotAction() {
    super(
        ProximityDesktop.getBundle().getString("Actions.TogglePivots.text"), 
        Action.AS_CHECK_BOX);
    setChecked(true);
  }
  
  @Override
  public void runWithEvent(Event event) {
    MenuItem item = (MenuItem) event.widget;
    ProximityDesktop.getApp().getCanvas().showPivots(item.getSelection());
  }

}
