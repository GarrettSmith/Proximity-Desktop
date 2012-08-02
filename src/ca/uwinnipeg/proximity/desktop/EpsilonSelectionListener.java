/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.prefs.Preferences;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Garrett Smith
 *
 */
public class EpsilonSelectionListener implements SelectionListener {
  
  private Preferences mPref  = Preferences.userRoot().node("proximity-system").node("epsilon");
  private Class<? extends PropertyController> mKey = null;
  
  public void setProperty(Class<? extends PropertyController> key) {
    mKey = key;
  }
  
  public float getEpsilon() {
    if (mKey == null) {
      return 0;
    }
    else {
      return mPref.getFloat(mKey.toString(), 0);
    }
  }  

  public void widgetSelected(SelectionEvent e) {
    widgetDefaultSelected(e);
  }

  public void widgetDefaultSelected(SelectionEvent e) {
    if (mKey != null) {
      Spinner spinner = (Spinner) e.getSource();
      int digits = spinner.getDigits();
      int selection = spinner.getSelection();
      float epsilon = (float) (selection / Math.pow(10, digits));
      mPref.putFloat(mKey.toString(), epsilon);
      ProximityDesktop.getController().setEpsilon(mKey, epsilon);
    }
  }

}
