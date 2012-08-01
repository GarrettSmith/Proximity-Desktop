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
  private String mKey = null;
  
  public void setProperty(String key) {
    mKey = key;
  }

  public void widgetSelected(SelectionEvent e) {
    widgetDefaultSelected(e);
  }

  public void widgetDefaultSelected(SelectionEvent e) {
    if (mKey != null) {
      Spinner spinner = (Spinner) e.getSource();
      int digits = spinner.getDigits();
      int selection = spinner.getSelection();
      mPref.putFloat(mKey, (float) (selection / Math.pow(10, digits)));
    }
  }

}