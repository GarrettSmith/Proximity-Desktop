/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action.file;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * @author garrett
 *
 */
public class OpenAction extends Action {
  
  public OpenAction() {
    super(MainWindow.getBundle().getString("MainWindow.actnOpen.text"));
  }
  
  @Override
  public void run() {
    // create the dialog to select an image file
    FileDialog dialog = new FileDialog(MainWindow.getApp().getShell(), SWT.OPEN);
    dialog.setText("Select an image file");
    dialog.setFilterExtensions(new String[]{"*.jpg;*.png;*.gif;*.bmp"});
    
    // get a file path
    String path = dialog.open();

    // open the file with the application
    if (path != null) {     
      MainWindow.getApp().openFile(path);
    }
  }

}
