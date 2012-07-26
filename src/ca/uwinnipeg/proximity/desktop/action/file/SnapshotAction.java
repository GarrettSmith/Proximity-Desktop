/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class SnapshotAction extends Action {

  public SnapshotAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnSnapshot.text"));
    setImageDescriptor(
        ResourceManager.getImageDescriptor(
            MainWindow.class, 
            "/ca/uwinnipeg/proximity/desktop/icons/snap.png"));
  }
  
  @Override
  public void run() {
    MainWindow window = MainWindow.getApp();
    //TODO:SnapshotDialog dialog = new SnapshotDialog(window.getShell(), mImage, mImageName);
    //dialog.open();
  }
}
