/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.prefs.Preferences;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

/**
 * @author Garrett Smith
 *
 */
public class NeighbourhoodSelectionListener implements SelectionListener {

  private Preferences mPref  = Preferences.userRoot().node("proximity-system").node("use-neighbourhoods");
  private Class<? extends PropertyController> mKey = null;
  
  public void setProperty(Class<? extends PropertyController> key) {
    mKey = key;
  }

  public void widgetSelected(SelectionEvent e) {
    widgetDefaultSelected(e);
  }

  public void widgetDefaultSelected(SelectionEvent e) {
    Button btn = (Button) e.getSource();
    boolean checked = btn.getSelection();
    ProximityDesktop.getController().setUseNeighbourhoods(mKey, checked);
  }

}
