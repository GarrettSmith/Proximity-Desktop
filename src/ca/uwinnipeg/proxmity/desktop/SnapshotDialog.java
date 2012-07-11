package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SnapshotDialog extends Dialog {
  private Text text;
  private Canvas canvas;
  
  private Image mImage;

  /**
   * Create the dialog.
   * @param parentShell
   */
  public SnapshotDialog(Shell parentShell) {
    super(parentShell);
  }

  /**
   * Create contents of the dialog.
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new GridLayout(4, false));
    
    canvas = new Canvas(container, SWT.BORDER);
    canvas.addPaintListener(new PaintListener() {
      
      public void paintControl(PaintEvent e) {
        if (mImage != null) {
          e.gc.drawImage(mImage, 0, 0);
        }
      }
    });
    canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
    
    Label lblName = new Label(container, SWT.NONE);
    lblName.setText("Name: ");
    new Label(container, SWT.NONE);
    
    text = new Text(container, SWT.BORDER);
    text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
    
    Label lblFolder = new Label(container, SWT.NONE);
    lblFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblFolder.setText("Save in folder: ");
    new Label(container, SWT.NONE);
    
    Combo combo = new Combo(container, SWT.NONE);
    combo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
    
    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));

    return container;
  }

  /**
   * Create contents of the button bar.
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, "Save", true);
    Button button = createButton(parent, 0, "Copy to Clipborad", false);
    button.setEnabled(false);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(600, 300);
  }
  
  public void setImage(Image image) {
    mImage = image;
    if (canvas != null) canvas.redraw();
  }
}  

