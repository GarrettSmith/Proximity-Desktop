/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.prefs.Preferences;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Spinner;

/**
 * A listener for changes in the set epsilon value.
 * @author Garrett Smith
 *
 */
public class EpsilonSelectionListener implements SelectionListener {
  
  private Preferences mPref  = Preferences.userRoot().node("proximity-system").node("epsilon");
  private Class<? extends PropertyController> mKey = null;
  
  /**
   * Sets the key for the property we are currently working with.
   * @param key
   */
  public void setProperty(Class<? extends PropertyController> key) {
    mKey = key;
  }
  
  /**
   * Get the currently set epsilon from the preferences or 0 if we don't have a set property.
   * @return
   */
  public float getEpsilon() {
    if (mKey == null) {
      return 0;
    }
    else {
      return mPref.getFloat(mKey.toString(), 0);
    }
  }  

  // forward event
  public void widgetSelected(SelectionEvent e) {
    widgetDefaultSelected(e);
  }

  /**
   * React to changing the epsilon value and record and update the value.
   */
  public void widgetDefaultSelected(SelectionEvent e) {
    if (mKey != null) {
      Spinner spinner = (Spinner) e.getSource();
      int digits = spinner.getDigits();
      int selection = spinner.getSelection();
      float epsilon = (float) (selection / Math.pow(10, digits));

      // save the epsilon
      mPref.putFloat(mKey.toString(), epsilon);
      
      // set the system's epsilon
      ProximityDesktop.getController().setEpsilon(mKey, epsilon);
    }
  }

}
