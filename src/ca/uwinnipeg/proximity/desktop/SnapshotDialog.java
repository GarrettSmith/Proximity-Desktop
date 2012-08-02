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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//TODO: DOCUMENT!
public class SnapshotDialog extends Dialog {
  
  public class ThumbnailPaintListener implements PaintListener {
    
    // create thumbnail image
    // minus 2's are to ensure a border
    public void paintControl(PaintEvent e) {    
      Rectangle labelBounds = ((Control) e.getSource()).getBounds();
      
      float scale = Math.min(1, ((float)labelBounds.height / mImage.getBounds().height));
      scale = Math.min(scale, ((float)labelBounds.width / mImage.getBounds().width));
      
      Rectangle bounds = mImage.getBounds();
      int scaledWidth = Math.round(bounds.width * scale) - 2;
      int scaledHeight = Math.round(bounds.height * scale) - 2;
      
      int offsetX = Math.round((float)(labelBounds.width - scaledWidth) / 2);
      int offsetY = Math.round((float)(labelBounds.height - scaledHeight) / 2);
      
      // draw black background
      e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      e.gc.fillRectangle(0, 0, labelBounds.width, labelBounds.height);
      
      // draw image
      e.gc.setAntialias(SWT.ON);
      e.gc.setInterpolation(SWT.HIGH);
      e.gc.drawImage(
          mImage, 
          0, 
          0, 
          bounds.width, 
          bounds.height, 
          offsetX, 
          offsetY, 
          scaledWidth, 
          scaledHeight);
    }
    
  }
  
  public final int HEIGHT = 300;
  public final int WIDTH = 600;
  
  private Text text;
  private Button btnFolder;
  
  private Image mImage;
  
  private String mPath;
  
  private String mFileName;
  
  private Preferences mPrefs = Preferences.userRoot().node("proximity-system");
  
  public static final String PATH = "snapshot path";
  private Composite container;

  /**
   * Create the dialog.
   * @param parentShell
   */
  public SnapshotDialog(Shell parentShell, Image image, String fileName) {
    super(parentShell);
    mFileName = fileName;
    mImage = image;    
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
    
    createImage(container);
    createOptions(container);
    
        Label lblFolder = new Label(container, SWT.NONE);
        lblFolder.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        lblFolder.setText("Save in folder: ");
    
    btnFolder = new Button(container, SWT.NONE);
    btnFolder.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
    btnFolder.addSelectionListener(new SelectionListener() {
      
      public void widgetSelected(SelectionEvent e) {
        doBrowse();
      }
      
      public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
      }
    });
    btnFolder.setText(mPath.substring(mPath.lastIndexOf(File.separatorChar)+1));

    return container;
  }
  
  private void createImage(Composite container) {    
    Label lblThumb = new Label(container, SWT.BORDER);
    lblThumb.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
    lblThumb.addPaintListener(new ThumbnailPaintListener());
  }
  
  private void createOptions(Composite container) {
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
//    mScale = Math.min(1, ((float)220 / mImage.getBounds().height));
//    
//    return new Point((int) (285 + (mImage.getBounds().width * mScale)), HEIGHT);
    return new Point(WIDTH, HEIGHT);
  }
  
  @Override
  protected void okPressed() {
    if (doSave()) {
      System.out.println("Image saved");
      super.okPressed();
    }
    else {
      // TODO: alert of error
    }
  }
  
  public boolean doSave() {
    String fileName = mPath + '/' + text.getText();
    System.out.println("Saving " + fileName);
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

