/**
 * 
 */
package ca.uwinnipeg.proxmity.desktop;

import java.util.ResourceBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;

/**
 * The main window of the application.
 * @author Garrett Smith
 *
 */
public class MainWindow extends ApplicationWindow {
  private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("ca.uwinnipeg.proxmity.desktop.messages"); //$NON-NLS-1$
  private Action actnOpen;
  private Action actnSnapshot;
  private Action actnExit;
  private Action actnAbout;
  
  private Action actnAddFeatures;
  
  private Action actnRegions;
  private Action actnNeighbourhoods;
  private Action actnIntersection;
  private Action actnCompliment;
  private Action actnDifference;
  
  private Canvas canvas;
  
  private Image mImage;
  
  private MainController mController;
  
  // frames
  private Composite frameStack;
  private StackLayout stackLayout;
  
  private Composite buttonFrame;
  private Composite canvasFrame;

  /**
   * Create the application window.
   */
  public MainWindow() {
    super(null);
    createActions();
    addToolBar(SWT.FLAT | SWT.WRAP);
    addMenuBar();
    //addStatusLine();
  }

  /**
   * Create contents of the application window.
   * @param parent
   */
  // TODO: break down createContents
  @Override
  protected Control createContents(Composite parent) {
    Composite container = new Composite(parent, SWT.NONE);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    
    SashForm sashForm = new SashForm(container, SWT.NONE);
    
    Composite composite = new Composite(sashForm, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));
    
    Tree tree = new Tree(composite, SWT.BORDER | SWT.CHECK | SWT.MULTI);
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    
    ActionContributionItem add = new ActionContributionItem(actnAddFeatures);
    add.fill(composite);
    Button btnAdd = (Button) add.getWidget();
    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    btnAdd.setText("Add Features");
    
    frameStack = new Composite(sashForm, SWT.NONE);
    stackLayout = new StackLayout();
    frameStack.setLayout(stackLayout);
    
    canvasFrame = new Composite(frameStack, SWT.NONE);
    canvasFrame.setLayout(new GridLayout(1, false));
    {
      ToolBar propertyBar = new ToolBar(canvasFrame, SWT.FLAT);
      propertyBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
      ToolBarManager propertyBarManager = new ToolBarManager(propertyBar);
      propertyBarManager.add(actnRegions);
      propertyBarManager.add(actnNeighbourhoods);
      propertyBarManager.add(actnIntersection);
      propertyBarManager.add(actnCompliment);
      propertyBarManager.add(actnDifference);
      propertyBarManager.update(false);
      
      // select the first action
      actnRegions.setChecked(true);
    }
    {
      canvas = new Canvas(canvasFrame, SWT.BORDER | SWT.DOUBLE_BUFFERED);
      canvas.addPaintListener(new PaintListener() {
        
        public void paintControl(PaintEvent e) {
          if (mImage != null) {
            Rectangle canvasBounds = canvas.getBounds();
            Rectangle imageBounds = mImage.getBounds();
            
            // find the scale to make the image fill the screen
            float scaleX = (float) canvasBounds.width / imageBounds.width;
            float scaleY = (float) canvasBounds.height / imageBounds.height;
            float scale = Math.min(scaleX, scaleY);
            scale = Math.min(scale, 1);
            
            // find the transform to center
            float translateX = (float) (canvasBounds.width - imageBounds.width * scale) / 2; 
            float translateY = (float) (canvasBounds.height - imageBounds.height * scale) / 2; 
            
            Transform transform = new Transform(Display.getCurrent());
            transform.translate(translateX, translateY);
            transform.scale(scale, scale);
            e.gc.setTransform(transform);
            e.gc.drawImage(mImage, 0, 0);
          }
        }
      });
      canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    }
    
    buttonFrame = new Composite(frameStack, SWT.NONE);
    buttonFrame.setLayout(new GridLayout(1, false));
    
    Label lblOpenButton = new Label(buttonFrame, SWT.NONE);
    lblOpenButton.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, true, 1, 1));
    lblOpenButton.setBounds(0, 0, 569, 582);
    lblOpenButton.setText(BUNDLE.getString("MainWindow.lblOpenButton.text")); //$NON-NLS-1$
    
    ActionContributionItem open = new ActionContributionItem(actnOpen);
    open.fill(buttonFrame);
    Button btnNewButton = (Button) open.getWidget();
    btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));
    btnNewButton.setBounds(0, 0, 81, 27);
    btnNewButton.setText("Select Image");
    
    stackLayout.topControl = buttonFrame;
    frameStack.layout();
    
    sashForm.setWeights(new int[] {160, 398});

    return container;
  }
  
  /**
   * Create the actions.
   */
  private void createActions() {
    // Create the actions
    {
      actnOpen = new Action(BUNDLE.getString("MainWindow.actnOpen.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          doOpen();
        }
      };
    }
    {
      actnSnapshot = new Action(BUNDLE.getString("MainWindow.actnSnapshot.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          doSnapshot();
        }
      };
      actnSnapshot.setEnabled(false);
    }
    {
      actnExit = new Action(BUNDLE.getString("MainWindow.actnExit.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          doExit();
        }
      };
    }
    {
      actnAbout = new Action(BUNDLE.getString("MainWindow.actnAbout.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          doAbout();
        }
      };
    }  
    
    // features
    {
      actnAddFeatures = new Action("Add Features") {
        @Override
        public void run() {
          doAddFeatures();
        }
      };
    }
    
    // properties
    // TODO: Switch between properties
    {
      actnRegions = new Action(BUNDLE.getString("MainWindow.actnRegions.text"), Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
        @Override
        public void run() {
          if (isChecked()) {
            System.out.println("Regions checked!");
          }
        }        
      };
    }
    {
      actnNeighbourhoods = new Action(BUNDLE.getString("MainWindow.actnNeighbourhoods.text"), Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
        @Override
        public void run() {

        }
      };
    }
    {
      actnIntersection = new Action(BUNDLE.getString("MainWindow.actnIntersection.text"), Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
        @Override
        public void run() {

        }
      };
    }
    {
      actnCompliment = new Action(BUNDLE.getString("MainWindow.actnCompliment.text"), Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
        @Override
        public void run() {

        }
      };
    }
    {
      actnDifference = new Action(BUNDLE.getString("MainWindow.actnDifference.text"), Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
        @Override
        public void run() {

        }
      };
    }
  }

  /**
   * Create the menu manager.
   * @return the menu manager
   */
  @Override
  protected MenuManager createMenuManager() {
    MenuManager menuManager = new MenuManager("menu");
    MenuManager menuFile = new MenuManager("&File", null);
    menuManager.add(menuFile);
    menuFile.add(actnOpen);
    menuFile.add(new Separator());
    menuFile.add(actnExit);
    
    MenuManager menuHelp = new MenuManager("&Help");
    menuManager.add(menuHelp);
    menuHelp.add(actnAbout);
    return menuManager;
  }

  /**
   * Create the toolbar manager.
   * @return the toolbar manager
   */
  @Override
  protected ToolBarManager createToolBarManager(int style) {
    ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP);
    toolBarManager.add(actnSnapshot);
    return toolBarManager;
  }

  /**
   * Create the status line manager.
   * @return the status line manager
   */
  @Override
  protected StatusLineManager createStatusLineManager() {
    StatusLineManager statusLineManager = new StatusLineManager();
    return statusLineManager;
  }

  /**
   * Launch the application.
   * @param args
   */
  public static void main(String args[]) {
    try {
      MainWindow window = new MainWindow();
      MainController controller = new MainController();
      window.setController(controller);
      window.setBlockOnOpen(true);
      window.open();
      Display.getCurrent().dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Configure the shell.
   * @param newShell
   */
  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(BUNDLE.getString("MainWindow.newShell.text")); //$NON-NLS-1$
  }

  /**
   * Return the initial size of the window.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }
  
  /**
   * Sets the controller to send and receive data from.
   * @param controller
   */
  public void setController(MainController controller) {
    mController = controller;
  }

  /**
   * Swaps the currently displayed frame to the given composite.
   * @param frame
   */
  public void swapFrame(Composite frame) {
    stackLayout.topControl = frame;
    frameStack.layout();
  }

  /**
   * Respond to the open item being selected.
   */
  public void doOpen() {
    // create the dialog to select an image file
    FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
    dialog.setText("Select an image file");
    dialog.setFilterExtensions(new String[]{"*.jpg;*.png;*.gif"});
    
    // get a file path
    String path = dialog.open();
    
    // draw the image
    if (path != null) { 
      
      // swap frames if this is the first image selected
      if (stackLayout.topControl == buttonFrame) {
        stackLayout.topControl = canvasFrame;
        frameStack.layout();
      }
      
      // load the image
      ImageData data = new ImageData(path);
      mImage = new Image(Display.getCurrent(), data);

      // request a redraw
      canvas.redraw();

      // enable disabled buttons
      actnSnapshot.setEnabled(true);
    }
  }

  /**
   * Show the about dialog.
   */
  public void doAbout() {
    AboutDialog dialog = new AboutDialog(getShell());
    dialog.open();
  }
  
  /**
   * Save a snapshot of the current view.
   */
  public void doSnapshot() {
    SnapshotDialog dialog = new SnapshotDialog(getShell());
    dialog.setImage(mImage);
    dialog.open();
  }

  /**
   * Exit the program.
   */
  public void doExit() {    
    close();
  }
  
  /**
   * Opens dialog to load new features to be used.
   */
  public void doAddFeatures() {
    AddFeaturesDialog dialog = new AddFeaturesDialog(getShell());
    dialog.open();
  }
  
  @Override
  public boolean close() {
    // tell the controller we are closing
    mController.onClose();
    return super.close();
  }
}