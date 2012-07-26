/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import java.io.File;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.MainWindow;

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
    MainWindow.getApp().openFile(mPath);
  }
}
