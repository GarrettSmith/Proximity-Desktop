package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SnapshotDialog extends Dialog {
  private Text text;
  private Canvas canvas;
  private Button btnFolder;
  
  private Image mImage;
  
  private String mPath;

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
    
    text = new Text(container, SWT.BORDER);
    GridData gd_text = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
    gd_text.widthHint = 160;
    text.setLayoutData(gd_text);
    new Label(container, SWT.NONE);
    
    Label lblFolder = new Label(container, SWT.NONE);
    lblFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblFolder.setText("Save in folder: ");
    
    btnFolder = new Button(container, SWT.NONE);
    btnFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    btnFolder.addSelectionListener(new SelectionListener() {
      
      public void widgetSelected(SelectionEvent e) {
        doBrowse();
      }
      
      public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
      }
    });
    btnFolder.setText("Browse");
    new Label(container, SWT.NONE);
    
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
  
  @Override
  protected void okPressed() {
    // TODO: save image
    String fileName = mPath + '/' + text.getText();
    System.out.println(fileName);
    super.okPressed();
  }
  
  /**
   * Set the image to be saved.
   * @param image
   */
  public void setImage(Image image) {
    mImage = image;
    if (canvas != null) canvas.redraw();
  }

  /**
   * Browse for the directory to save into.
   */
  public void doBrowse() {
    DirectoryDialog dialog = new DirectoryDialog(getShell());
    String path = dialog.open();
    
    if (path != null) {
      btnFolder.setText(path.substring(path.lastIndexOf('/')+1));
      mPath = path;
    }
  }
}  

