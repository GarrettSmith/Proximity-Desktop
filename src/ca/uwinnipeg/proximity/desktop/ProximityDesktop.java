/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.action.EditFeaturesAction;
import ca.uwinnipeg.proximity.desktop.action.edit.CopyAction;
import ca.uwinnipeg.proximity.desktop.action.edit.CutAction;
import ca.uwinnipeg.proximity.desktop.action.edit.DeleteAction;
import ca.uwinnipeg.proximity.desktop.action.edit.DuplicateAction;
import ca.uwinnipeg.proximity.desktop.action.edit.PasteAction;
import ca.uwinnipeg.proximity.desktop.action.edit.RedoAction;
import ca.uwinnipeg.proximity.desktop.action.edit.SelectAllAction;
import ca.uwinnipeg.proximity.desktop.action.edit.UndoAction;
import ca.uwinnipeg.proximity.desktop.action.file.EmptyAction;
import ca.uwinnipeg.proximity.desktop.action.file.ExitAction;
import ca.uwinnipeg.proximity.desktop.action.file.OpenAction;
import ca.uwinnipeg.proximity.desktop.action.file.RecentlyOpenedAction;
import ca.uwinnipeg.proximity.desktop.action.file.SnapshotAction;
import ca.uwinnipeg.proximity.desktop.action.help.AboutAction;
import ca.uwinnipeg.proximity.desktop.action.help.ManualAction;
import ca.uwinnipeg.proximity.desktop.action.view.CenterAction;
import ca.uwinnipeg.proximity.desktop.action.view.ToggleFeaturesAction;
import ca.uwinnipeg.proximity.desktop.action.view.ZoomImageAction;
import ca.uwinnipeg.proximity.desktop.action.view.ZoomInAction;
import ca.uwinnipeg.proximity.desktop.action.view.ZoomOutAction;
import ca.uwinnipeg.proximity.desktop.action.view.ZoomSelectionAction;
import ca.uwinnipeg.proximity.desktop.action.view.ZoomTo1Action;
import ca.uwinnipeg.proximity.desktop.tool.OvalTool;
import ca.uwinnipeg.proximity.desktop.tool.PointerTool;
import ca.uwinnipeg.proximity.desktop.tool.PolygonTool;
import ca.uwinnipeg.proximity.desktop.tool.RectangleTool;
import ca.uwinnipeg.proximity.desktop.tool.ZoomTool;

/**
 * The main window of the application.
 * @author Garrett Smith
 *
 */
// TODO: split up
public class ProximityDesktop extends ApplicationWindow {
  private static final ResourceBundle BUNDLE = 
      ResourceBundle.getBundle("ca.uwinnipeg.proximity.desktop.strings.messages");
  
  private static ProximityDesktop APP;
  
  private static ProximityController CONTROLLER = new ProximityController();
    
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
  
  private Action actnPointer;
  private Action actnRectangle;
  private Action actnOval;
  private Action actnPolygon;
  private Action actnZoom;
  private Action actnUndo;
  private Action actnRedo;
  private Action actnCut;
  private Action actnCopy;
  private Action actnPaste;
  private Action actnDuplicate;
  private Action actnDelete;
  private Action actnSelectAll;
  private Action actnManual;
  private Action actnZoomIn;
  private Action actnZoomOut;
  private Action actnZoom1to1;
  private Action actnZoomSelection;
  private Action actnZoomImage;
  private Action actnFeatures;
  private Action actnEmpty;  
  private Action actnCenter;

  IKeyLookup mKeyLookup;
  
  private Action[] mImageDependantActions;
  private Action[] mSelectionDependantActions;
  
  private ImageCanvas canvas;
  
  private String mImageName;
  
  private Image mImage;
  
  private Preferences mPrefs = Preferences.userRoot().node("proximity-system");  
  private Preferences mRecentPrefs = mPrefs.node("recent");
  
  private static final int RECENT_DOCUMENTS_LIMIT = 10;
  
  private List<Region> mSelectedRegions = new ArrayList<Region>();
  
  // frames
  private Composite frameStack;
  private StackLayout stackLayout;
  
  private Composite buttonFrame;
  private Composite canvasFrame;
  
  private SashForm sashForm;
  
  /**
   * Listens for mouse wheel to scroll into canvas.
   */
  //TODO: zoom to cursor
  private MouseWheelListener mMouseWheelListener = new MouseWheelListener() {
    
    public void mouseScrolled(MouseEvent e) {
      // check if ctrl is being held
      if ((e.stateMask & SWT.CTRL) != 0 ) {
        // check whether to zoom in or out
        if (e.count < 0) {
          canvas.zoomOut();
        }
        else {
          canvas.zoomIn();
        }
      }
    }
  };
  
  /**
   * Listen for panning with the middle mouse button.
   */
  private MouseMoveListener mMouseMoveListener = new MouseMoveListener() {
    
    private int mPrevX = -1;
    private int mPrevY = -1;
    
    public void mouseMove(MouseEvent e) {
      // check if the middle mouse button is pressed
      if ((e.stateMask & SWT.BUTTON2) != 0) {
        // make if this is the first valid event
        if (mPrevX != -1) {
          int dx = e.x - mPrevX;
          int dy = e.y - mPrevY;              
          canvas.pan(dx, dy);
        }
        // record the previous point
        mPrevX = e.x;
        mPrevY = e.y;
      }
      else {
        // record we have stopped
        mPrevX = -1;
      }
    }
  };
  
  private PaintListener mPaintRegionsListener = new PaintListener() {
    
    public void paintControl(PaintEvent e) {
      GC gc = e.gc;
      Color unselected = new Color(Display.getCurrent(), 255, 255, 255);
      Color selected = new Color(Display.getCurrent(), 0, 255, 255);
      List<Region> selectedRegions = CONTROLLER.getSelectedRegions();
      for (Region r : CONTROLLER.getRegions()) {
        Rectangle bounds = r.getBounds();
        bounds = canvas.toScreenSpace(bounds);
        // determine if the region is selected
        if (selectedRegions.contains(r)) {
          gc.setForeground(selected);
        }
        else {
          gc.setForeground(unselected);
        }
        switch(r.getShape()) {
          case RECTANGLE:
            gc.drawRectangle(bounds);
            break;
          case OVAL:
            gc.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
            break;
          case POLYGON:
            int[] points = r.getPolygon().toArray();
            points = canvas.toScreenSpace(points);
            gc.drawPolygon(points);
            break;
        }
      }
      // TODO: draw neighbourhood
//      if (mNeighbourhood != null) {
//        ImageData data = mImage.getImageData();
//        Point p = new Point(0, 0);
//        for (Image img : mNeighbourhood.values()) {
//          
//          p.x = i % mImage.getBounds().width;
//          p.y = i / mImage.getBounds().width;
//          int pixel = data.getPixel(p.x , p.y);
//          pixel = ~pixel; // invert colour
//          // extract the rgb colours from the pixel
//          Color color = 
//              new Color(Display.getCurrent(), (pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF);
//          gc.setForeground(color);
//          p = canvas.toScreenSpace(p);
//          gc.drawPoint(p.x, p.y);
//          color.dispose();
//        }
//      }
    }
  };

  /**
   * Launch the application.
   * @param args
   */
  public static void main(String args[]) {
    Display display = Display.getDefault();
    try {
      ProximityDesktop window = new ProximityDesktop();
      window.setBlockOnOpen(true);
      window.open();
      Display.getCurrent().dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the application window.
   */
  public ProximityDesktop() {
    super(null);
    APP = this;
    mKeyLookup = KeyLookupFactory.getDefault();
    createActions();
    addToolBar(SWT.FLAT | SWT.WRAP);
    addMenuBar();
    //addStatusLine();
  }
  
  public static ProximityDesktop getApp() {
    return APP;
  } 

  public static ProximityController getController() {
    return CONTROLLER;
  }
  
  public static ResourceBundle getBundle() {
    return BUNDLE;  }

  /**
   * Create contents of the application window.
   * @param parent
   */
  @Override
  protected Control createContents(Composite parent) {
    Composite container = new Composite(parent, SWT.NONE);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    
    sashForm = new SashForm(container, SWT.NONE);
    sashForm.setSashWidth(5);
    
    createFeaturesPane(sashForm);
    createFramesPane(sashForm);
    
    sashForm.setWeights(new int[] {1, 3});

    return container;
  }
  
  /**
   * Create the right-hand sash to display features.
   * @param container
   */
  private void createFeaturesPane(Composite container) {
    Composite composite = new Composite(container, SWT.NONE);
    
    // create the grid layout for the sash
    GridLayout gl_composite = new GridLayout(1, false);
    gl_composite.marginLeft = 5;
    gl_composite.marginWidth = 0;
    composite.setLayout(gl_composite);
    
    // create the tree to show features
    Tree tree = new Tree(composite, SWT.BORDER | SWT.CHECK | SWT.MULTI);
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    
    // create the add features button
    ActionContributionItem add = new ActionContributionItem(actnAddFeatures);
    add.fill(composite);
    Button btnAdd = (Button) add.getWidget();
    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    btnAdd.setText("Add Features");
  }
  
  /**
   * Create the sash to display the image and select image panes.
   * @param container
   */
  private void createFramesPane(Composite container) {
    // create the stack for the two frames
    frameStack = new Composite(container, SWT.NONE);
    stackLayout = new StackLayout();
    frameStack.setLayout(stackLayout);
    
    createCanvasFrame(frameStack);    
    createButtonFrame(frameStack);    
    
    // put the button frame on top
    stackLayout.topControl = buttonFrame;
    frameStack.layout();
  }
  
  /**
   * Creates the frame with the canvas and property selection.
   * @param container
   */
  private void createCanvasFrame(Composite container) {
    canvasFrame = new Composite(frameStack, SWT.NONE);
    GridLayout gl_canvasFrame = new GridLayout(1, false);
    gl_canvasFrame.marginRight = 5;
    gl_canvasFrame.marginWidth = 0;
    canvasFrame.setLayout(gl_canvasFrame);
    
    // create the toolbar to select the property
    {
      ToolBar propertyBar = new ToolBar(canvasFrame, SWT.FLAT);
      propertyBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
      ToolBarManager propertyBarManager = new ToolBarManager(propertyBar);
//      propertyBarManager.add(actnRegions);
//      propertyBarManager.add(actnNeighbourhoods);
//      propertyBarManager.add(actnIntersection);
//      propertyBarManager.add(actnCompliment);
//      propertyBarManager.add(actnDifference);
      propertyBarManager.update(false);
      
      // select the first action
//      actnRegions.setChecked(true);
    }
    
    // create the image canvas
    {
      canvas = new ImageCanvas(canvasFrame, SWT.BORDER | SWT.DOUBLE_BUFFERED, mImage);
      canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));      

      canvas.addMouseWheelListener(mMouseWheelListener);
      canvas.addMouseMoveListener(mMouseMoveListener);
      
      canvas.addPaintListener(mPaintRegionsListener);
      
      MenuManager menuMgr = new MenuManager();
      menuMgr.add(actnUndo);
      menuMgr.add(actnRedo);
      menuMgr.add(new Separator());
      menuMgr.add(actnCut);
      menuMgr.add(actnCopy);
      menuMgr.add(actnPaste);
      menuMgr.add(new Separator());
      menuMgr.add(actnDuplicate);
      menuMgr.add(actnDelete);
      
      Menu menu = menuMgr.createContextMenu(canvas);
      canvas.setMenu(menu);   
      
      // setup first tool
      actnPointer.run();
    }
  }
  
  /**
   * Create the frame with the button requesting to select the first image.
   * @param container
   */
  private void createButtonFrame(Composite container) {
    buttonFrame = new Composite(frameStack, SWT.NONE);
    buttonFrame.setLayout(new GridLayout(1, false));
    
    // create the label asking to pick an image
    Label lblOpenButton = new Label(buttonFrame, SWT.NONE);
    lblOpenButton.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, true, 1, 1));
    lblOpenButton.setBounds(0, 0, 569, 582);
    lblOpenButton.setText(BUNDLE.getString("MainWindow.OpenLabel.text")); //$NON-NLS-1$
    
    // create the button to open the image
    ActionContributionItem open = new ActionContributionItem(actnOpen);
    open.fill(buttonFrame);
    Button btnNewButton = (Button) open.getWidget();
    btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));
    btnNewButton.setBounds(0, 0, 81, 27);
    btnNewButton.setText("Select Image");
  }
  
  /**
   * Create the actions.
   */
  private void createActions() {
    // file
    actnOpen = new OpenAction();
    actnSnapshot = new SnapshotAction();
    actnExit = new ExitAction();
    actnEmpty = new EmptyAction();
    
    // edit
    actnUndo = new UndoAction();
    actnRedo = new RedoAction();
    actnCut = new CopyAction();
    actnCopy = new CutAction();
    actnPaste = new PasteAction();
    actnDuplicate = new DuplicateAction();
    actnDelete = new DeleteAction();
    actnSelectAll = new SelectAllAction();
    
    // view
    actnZoomIn = new ZoomInAction();
    actnZoomOut = new ZoomOutAction();
    actnZoom1to1 = new ZoomTo1Action();
    actnZoomSelection = new ZoomSelectionAction();
    actnZoomImage = new ZoomImageAction();
    actnFeatures = new ToggleFeaturesAction();
    actnCenter = new CenterAction();
    
    // help
    actnManual = new ManualAction();
    actnAbout = new AboutAction();
    
    // properties
    //  actnRegions = new PropertyAction("MainWindow.actnRegions.text");
    //  actnNeighbourhoods = new PropertyAction("MainWindow.actnNeighbourhoods.text");
    //  actnIntersection = new PropertyAction("MainWindow.actnIntersection.text");
    //  actnCompliment = new PropertyAction("MainWindow.actnCompliment.text");
    //  actnDifference = new PropertyAction("MainWindow.actnDifference.text");
    
    // tools
    actnPointer = new PointerTool.Action();
    actnRectangle = new RectangleTool.Action();
    actnOval = new OvalTool.Action();
    actnPolygon = new PolygonTool.Action();
    actnZoom = new ZoomTool.Action();
    
    // features
    actnAddFeatures = new EditFeaturesAction();
    
    // record all actions that need an image
    mImageDependantActions = new Action[] {
        actnSnapshot,
        actnSelectAll,
        actnZoomIn,
        actnZoomOut,
        actnZoom1to1,
        actnZoomImage,
        actnPointer,
        actnRectangle,
        actnOval,
        actnPolygon,
        actnZoom,
        actnCenter
    };
    
    for (Action a : mImageDependantActions) {
      a.setEnabled(false);
    }
    
    // record all actions that need a selection
    mSelectionDependantActions = new Action[] {
        actnCut,
        actnCopy,
        actnDuplicate,
        actnDelete,
        actnZoomSelection
    };
    
    for (Action a : mSelectionDependantActions) {
      a.setEnabled(false);
    }
    
    // setup undo and redo
    updateHistoryActions();
  }
  
  /**
   * Create the menu manager.
   * @return the menu manager
   */
  @Override
  protected MenuManager createMenuManager() {
    MenuManager menuManager = new MenuManager("menu");
    MenuManager menuFile = new MenuManager(BUNDLE.getString("MainWindow.File.text"));
    menuManager.add(menuFile);
    menuFile.add(actnOpen);    
    menuFile.add(createRecentMenu());    
    menuFile.add(new Separator());
    menuFile.add(actnSnapshot);
    menuFile.add(new Separator());
    menuFile.add(actnExit);
    
    MenuManager menuEdit = new MenuManager(BUNDLE.getString("MainWindow.Edit.text"));
    menuManager.add(menuEdit);
    menuEdit.add(actnUndo);
    menuEdit.add(actnRedo);
    menuEdit.add(new Separator());
    menuEdit.add(actnCut);
    menuEdit.add(actnCopy);
    menuEdit.add(actnPaste);
    menuEdit.add(new Separator());
    menuEdit.add(actnDuplicate);
    menuEdit.add(new Separator());
    menuEdit.add(actnDelete);
    menuEdit.add(new Separator());
    menuEdit.add(actnSelectAll);
    
    MenuManager menuView = new MenuManager(BUNDLE.getString("MainWindow.View.text"));
    menuManager.add(menuView);
    menuView.add(actnCenter);
    menuView.add(new Separator());
    
    MenuManager menuZoom = new MenuManager(BUNDLE.getString("MainWindow.Zoom.text"));
    menuView.add(menuZoom);
    menuZoom.add(actnZoomIn);
    menuZoom.add(actnZoomOut);
    menuZoom.add(new Separator());
    menuZoom.add(actnZoom1to1);
    menuZoom.add(actnZoomSelection);
    menuZoom.add(actnZoomImage);
    menuView.add(new Separator());
    menuView.add(actnFeatures);
    
    MenuManager menuHelp = new MenuManager(BUNDLE.getString("MainWindow.Help.text"));
    menuManager.add(menuHelp);
    menuHelp.add(actnManual);
    menuHelp.add(new Separator());
    menuHelp.add(actnAbout);
    return menuManager;
  }
  
  private MenuManager createRecentMenu() {
    MenuManager menuRecent = 
        new MenuManager(BUNDLE.getString("MainWindow.OpenRecent.text"));
    // add all the recent items to the recent menu
    try {
      for (String key : mRecentPrefs.keys()) {
        String path = mRecentPrefs.get(key, null);
        if (path != null && new File(path).exists()) {
          menuRecent.add(new RecentlyOpenedAction(path));
        }
      }
    } catch (BackingStoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // add the empty action if it is empty
    if (menuRecent.isEmpty()) {
      menuRecent.add(actnEmpty);
    }
    return menuRecent;
  }

  /**
   * Create the toolbar manager.
   * @return the toolbar manager
   */
  @Override
  protected ToolBarManager createToolBarManager(int style) {
    ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP);
    toolBarManager.add(actnPointer);
    actnPointer.setChecked(true);
    toolBarManager.add(actnRectangle);
    toolBarManager.add(actnOval);
    toolBarManager.add(actnPolygon);
    toolBarManager.add(actnZoom);
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
   * Configure the shell.
   * @param newShell
   */
  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(BUNDLE.getString("MainWindow.Shell.text"));
    
    // set icon
    String dir = "/ca/uwinnipeg/proximity/desktop/icons/";
    Display display = Display.getCurrent();
    
    Image[] imgs = new Image[] {
      ResourceManager.getImageDescriptor(ProximityDesktop.class, dir + "launcher_16.png").createImage(display),
      ResourceManager.getImageDescriptor(ProximityDesktop.class, dir + "launcher_24.png").createImage(display),
      ResourceManager.getImageDescriptor(ProximityDesktop.class, dir + "launcher_36.png").createImage(display),
      ResourceManager.getImageDescriptor(ProximityDesktop.class, dir + "launcher_48.png").createImage(display),
      ResourceManager.getImageDescriptor(ProximityDesktop.class, dir + "launcher_64.png").createImage(display),
      ResourceManager.getImageDescriptor(ProximityDesktop.class, dir + "launcher_128.png").createImage(display)
  };

    newShell.setImages(imgs);
  }

  /**
   * Return the initial size of the window.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(580, 190);
  }

  public ImageCanvas getCanvas() {
    return canvas;
  }
  
  public Image getImage() {
    return mImage;
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
    dialog.setFilterExtensions(new String[]{"*.jpg;*.png;*.gif;*.bmp"});
    
    // get a file path
    String path = dialog.open();

    if (path != null) {     
      openFile(path);
    }
  }
  
  public void openFile(String path) {  

    mImageName = path.substring(path.lastIndexOf(File.separatorChar) + 1);

    // swap frames if this is the first image selected
    if (stackLayout.topControl == buttonFrame) {
      stackLayout.topControl = canvasFrame;
      frameStack.layout();
    }

    // load the image
    mImage = new Image(Display.getCurrent(), path);    

    // update canvas
    canvas.setImage(mImage);

    // enable disabled buttons
    enableImageActions();

    // tell the controller about the new image
    CONTROLLER.onImageSelected(mImage.getImageData());

    // TODO: prevent duplicates
    
    // shuffle documents down
    for (int i = RECENT_DOCUMENTS_LIMIT - 1; i > 0; i-- ) {
      String tmp = mRecentPrefs.get(Integer.toString(i - 1), null);
      if (tmp != null) {
        mRecentPrefs.put(Integer.toString(i), tmp);
      }
    }

    // store into recent documents
    mRecentPrefs.put("0", path);
  }

  /**
   * Enable all actions that only make ssense when we have an image.
   */
  private void enableImageActions() {
    for (Action a : mImageDependantActions) {
      a.setEnabled(true);
    }
  }
  
  /**
   * Save a snapshot of the current view.
   */
  public void doSnapshot() {
    // TODO: prepare image
    SnapshotDialog dialog = new SnapshotDialog(getShell(), mImage, mImageName);
    dialog.open();
  }

  /**
   * Exit the program.
   */
  public void doExit() {    
    close();
  }
  
  /**
   * Toggle displaying the features pane.
   * @param show
   */
  public void toggleFeatures(boolean show) {
    if (show) {
      sashForm.setWeights(new int[]{1, 3});
    }
    else {
      sashForm.setWeights(new int[]{0, 1});
    }
  }
  
  @Override
  public boolean close() {
    // tell the controller we are closing
    CONTROLLER.onClose();
    return super.close();
  }

  /**
   * Updates the names and enabled status of the undo and redo actions.
   */
  public void updateHistoryActions() {
    //undo
    actnUndo.setEnabled(CONTROLLER.getUndo());
    actnUndo.setText(CONTROLLER.getUndoString());
    
    //redo
    actnRedo.setEnabled(CONTROLLER.getRedo());
    actnRedo.setText(CONTROLLER.getRedoString());
  }
  
  public void updateSelectionActions() {
    boolean enable = !CONTROLLER.getSelectedRegions().isEmpty();
    for (Action a: mSelectionDependantActions) {
      a.setEnabled(enable);
    }
  }
}