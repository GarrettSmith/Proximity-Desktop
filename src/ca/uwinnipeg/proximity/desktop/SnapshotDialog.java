package ca.uwinnipeg.proximity.desktop;

import java.io.File;
import java.util.prefs.Preferences;

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
//TODO: DOCUMENT!
public class SnapshotDialog extends Dialog {
  
  public final int HEIGHT = 300;
  
  private Text text;
  private Canvas canvas;
  private Button btnFolder;
  
  private Image mImage;
  
  private float mScale;
  
  private String mPath;
  
  private String mFileName;
  
  private Preferences mPrefs = Preferences.userRoot().node(this.getClass().getName());
  
  public static final String PATH = "Path";
  private Composite container;

  /**
   * Create the dialog.
   * @param parentShell
   */
  public SnapshotDialog(Shell parentShell, Image image, String fileName) {
    super(parentShell);
    mImage = image;
    mFileName = fileName;
    mScale = Math.min(1, ((float)HEIGHT / mImage.getBounds().height));
    doSetup();
  }

  /**
   * Create contents of the dialog.
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    container = (Composite) super.createDialogArea(parent);
    container.setLayout(new GridLayout(3, false));
    
    createCanvas(container);
    createFileNameEntry(container);
    createDirectoryEntry(container);

    return container;
  }
  
  protected void draw(GC gc) {
    Transform transform = new Transform(Display.getCurrent());
    transform.scale(mScale, mScale);
        
    gc.setTransform(transform);
    gc.drawImage(mImage, 0, 0);
  }
  
  private void createCanvas(Composite container) {
    canvas = new Canvas(container, SWT.BORDER);
    GridData gd_canvas = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5);
    gd_canvas.widthHint = 0;
    canvas.setLayoutData(gd_canvas);
    canvas.addPaintListener(new PaintListener() {
      
      public void paintControl(PaintEvent e) {
        draw(e.gc);
      }
    });
  }
  
  private void createFileNameEntry(Composite container) {
    Label lblName = new Label(container, SWT.NONE);
    lblName.setText("Name: ");
    
    text = new Text(container, SWT.BORDER);
    GridData gd_text = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
    gd_text.widthHint = 170;
    text.setLayoutData(gd_text);
    // set the default file name
    text.setText(getDefaultFileName());
    text.setFocus();
    // select everything but the extension
    text.setSelection(0, text.getText().length() - 4);
  }

  public String getDefaultFileName() {
    int i = 0;
    String origName = mFileName.substring(0, mFileName.lastIndexOf('.'));
    File f = null;
    while (f == null || f.exists()) {
      f = new File(mPath, origName + "-snap-" + i + ".png");
      i++;      
    }
    return f.getName();
  }

  private void createDirectoryEntry(Composite container) {

    Label lblFolder = new Label(container, SWT.NONE);
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
    btnFolder.setText(mPath.substring(mPath.lastIndexOf(File.separatorChar)+1));
  }
  
  /**
   * Create contents of the button bar.
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, "Save", true);
    Button button = createButton(parent, 0, "Copy to Clipboard", false);
    button.setEnabled(false);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  // TODO: deal with magic numbers
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
    String fileName = mPath + '/' + text.getText();
    System.out.println(fileName);
    ImageLoader imgLoader = new ImageLoader();
    imgLoader.data = new ImageData[] {mImage.getImageData()};
    imgLoader.save(fileName, SWT.IMAGE_PNG);
    // TODO: find if we saved the image
    return true;
  }
  
  /**
   * 
   */
  private void doSetup() {
    mPath = mPrefs.get(PATH, new File("").getAbsolutePath());
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
      mPrefs.put(PATH, mPath);
    }
  }
}  

