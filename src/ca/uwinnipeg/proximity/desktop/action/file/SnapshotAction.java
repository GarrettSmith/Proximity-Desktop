/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;
import ca.uwinnipeg.proximity.desktop.SnapshotDialog;

/**
 * An action that creates a snapshot dialog to save the current image.
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
    ProximityDesktop app = ProximityDesktop.getApp();
    SnapshotDialog dialog = 
        new SnapshotDialog(app.getShell(), app.getCanvas().getImage(), app.getImageName());
    dialog.open();
  }
}
