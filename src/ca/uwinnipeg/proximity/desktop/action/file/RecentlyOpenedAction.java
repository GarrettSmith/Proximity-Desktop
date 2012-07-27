/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import java.io.File;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class RecentlyOpenedAction extends Action {
  
  public String mPath;
      
  public RecentlyOpenedAction(String path) {
    super(path.substring(path.lastIndexOf(File.separatorChar) + 1));
    mPath = path;
  }
  
  @Override
  public void run() {
    ProximityDesktop.getApp().openFile(mPath);
  }
}
