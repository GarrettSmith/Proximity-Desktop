/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.prefs.Preferences;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

/**
 * Listener that responds to the "use neighbourhood" button being clicked. 
 * @author Garrett Smith
 *
 */
public class NeighbourhoodSelectionListener implements SelectionListener {

  private Class<? extends PropertyController<?>> mKey = null;
  
  /**
   * Sets the current property we are dealing with.
   * @param key
   */
  public void setProperty(Class<? extends PropertyController<?>> key) {
    mKey = key;
  }

  // forward event
  public void widgetSelected(SelectionEvent e) {
    widgetDefaultSelected(e);
  }

  /**
   * Saves the selection and forwards it to the correct controller.
   */
  public void widgetDefaultSelected(SelectionEvent e) {
    Button btn = (Button) e.getSource();
    boolean checked = btn.getSelection();
    ProximityDesktop.getController().setUseNeighbourhoods(mKey, checked);
    Preferences prefs = Preferences.userRoot().node("proximity-system").node("use-neighbourhoods");
    prefs.putBoolean(mKey.toString(), checked);
  }

}
