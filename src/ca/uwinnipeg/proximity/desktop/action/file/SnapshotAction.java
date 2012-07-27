/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author garrett
 *
 */
public class SnapshotAction extends Action {

  public SnapshotAction() {
    super(ProximityDesktop.getBundle().getString("Actions.Snapshot.text"));
    setImageDescriptor(
        ResourceManager.getImageDescriptor(
            ProximityDesktop.class, 
            "/ca/uwinnipeg/proximity/desktop/icons/snap.png"));
  }
  
  @Override
  public void run() {
    ProximityDesktop window = ProximityDesktop.getApp();
    //TODO:SnapshotDialog dialog = new SnapshotDialog(window.getShell(), mImage, mImageName);
    //dialog.open();
  }
}
