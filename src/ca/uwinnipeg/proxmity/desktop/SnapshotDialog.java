package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//TODO: get rid of magic numbers
public class SnapshotDialog extends Dialog {
  
  public final int HEIGHT = 300;
  
  private Text text;
  private Canvas canvas;
  private Button btnFolder;
  
  private Image mImage;
  
  private float mScale;
  
  private String mPath;

  /**
   * Create the dialog.
   * @param parentShell
   */
  public SnapshotDialog(Shell parentShell, Image image) {
    super(parentShell);
    mImage = image;
    mScale = Math.min(1, ((float)HEIGHT / mImage.getBounds().height));
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
        draw(e.gc);
      }
    });
    
    
    GridData gd_canvas = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
    gd_canvas.widthHint = 0;
    canvas.setLayoutData(gd_canvas);
    
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
  
  protected void draw(GC gc) {
    Transform transform = new Transform(Display.getCurrent());
    transform.scale(mScale, mScale);
        
    gc.setTransform(transform);
    gc.drawImage(mImage, 0, 0);
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
    // find scale
    mScale = Math.min(1, ((float)220 / mImage.getBounds().height));
    
    return new Point((int) (285 + (mImage.getBounds().width * mScale)), HEIGHT);
  }
  
  @Override
  protected void okPressed() {
    if (doSave()) {
      super.okPressed();
    }
  }
  
  public boolean doSave() {
    // TODO Auto-generated method stub
    String fileName = mPath + '/' + text.getText();
    System.out.println(fileName);
    ImageLoader imgLoader = new ImageLoader();
    imgLoader.data = new ImageData[] {mImage.getImageData()};
    imgLoader.save(fileName, SWT.IMAGE_PNG);
    // TODO: find if we saved the image
    return true;
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

