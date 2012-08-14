/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

import ca.uwinnipeg.proximity.desktop.action.file.EmptyAction;
import ca.uwinnipeg.proximity.desktop.action.file.RecentlyOpenedAction;

/**
 * Populates the recently opened files menu.
 * @author Garrett Smith
 *
 */
public class RecentMenuListener implements IMenuListener {
  
  private Preferences mRecentPrefs;

  /**
   * 
   */
  public RecentMenuListener(Preferences prefs) {
    mRecentPrefs = prefs;
  }

  /**
   * Populates the menu when it is about to be shown.
   */
  public void menuAboutToShow(IMenuManager manager) {
    // add all the recent items to the recent menu
    try {
      for (String key : mRecentPrefs.keys()) {
        String path = mRecentPrefs.get(key, null);
        if (path != null && new File(path).exists()) {
          manager.add(new RecentlyOpenedAction(path));
        }
      }
    } catch (BackingStoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // add the empty action if it is empty
    if (manager.isEmpty()) {
      manager.add(new EmptyAction());
    }
  }

}
