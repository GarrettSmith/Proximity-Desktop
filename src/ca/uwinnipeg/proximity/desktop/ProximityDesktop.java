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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.action.EditFeaturesAction;
import ca.uwinnipeg.proximity.desktop.action.PropertyAction;
import ca.uwinnipeg.proximity.desktop.action.edit.CopyAction;
import ca.uwinnipeg.proximity.desktop.action.edit.CutAction;
import ca.uwinnipeg.proximity.desktop.action.edit.DeleteAction;
import ca.uwinnipeg.proximity.desktop.action.edit.DuplicateAction;
import ca.uwinnipeg.proximity.desktop.action.edit.PasteAction;
import ca.uwinnipeg.proximity.desktop.action.edit.RedoAction;
import ca.uwinnipeg.proximity.desktop.action.edit.SelectAllAction;
import ca.uwinnipeg.proximity.desktop.action.edit.UndoAction;
import ca.uwinnipeg.proximity.desktop.action.file.ExitAction;
import ca.uwinnipeg.proximity.desktop.action.file.OpenAction;
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
import ca.uwinnipeg.proximity.desktop.features.FeaturesCheckStateProvider;
import ca.uwinnipeg.proximity.desktop.features.FeaturesContentProvider;
import ca.uwinnipeg.proximity.desktop.features.FeaturesLabelProvider;
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
//TODO: handle deleting all regions
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
  private Action actnCenter;
  
  private Action[] mImageDependantActions;
  private Action[] mSelectionDependantActions;
  
  private ImageCanvas canvas;
  
  private String mImageName;
  
  private Image mImage;
  
  private Preferences mPrefs = Preferences.userRoot().node("proximity-system");  
  private Preferences mRecentPrefs = mPrefs.node("recent");
  
  private static final int RECENT_DOCUMENTS_LIMIT = 10;
  
  // frames
  private Composite frameStack;
  private StackLayout stackLayout;
  
  private Composite buttonFrame;
  private Composite canvasFrame;
  
  private SashForm sashForm;
  
  private EpsilonSelectionListener mEpsilonListener = new EpsilonSelectionListener();
  private Spinner mEpsilonSpinner;
  
  private NeighbourhoodSelectionListener mNeighbourhoodsListener = new NeighbourhoodSelectionListener();
  private Button mNeighbourhoodButton;

  /**
   * Launch the application.
   * @param args
   */
  public static void main(String args[]) {
    try {
      ProximityDesktop window = new ProximityDesktop();
      window.setBlockOnOpen(true);
      window.open();
      Display.getCurrent().dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
   * Create the application window.
   */
  public ProximityDesktop() {
    super(null);
    APP = this;
    createActions();
    addToolBar(SWT.FLAT | SWT.WRAP);
    addMenuBar();
    //addStatusLine();
  }
  
  public ImageCanvas getCanvas() {
    return canvas;
  }

  public Image getImage() {
    return mImage;
  }
  
  public String getImageName() {
    return mImageName;
  }

  public void openFile(String path) {  

    // if there is an image loaded ask for confirmation
    if (mImage == null || 
        MessageDialog.openQuestion(
            getShell(), 
            BUNDLE.getString("ProximityDesktop.OpenConfirm.title"), 
            BUNDLE.getString("ProximityDesktop.OpenConfirm.message"))) {

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

      try {
        List<String> files = new ArrayList<String>();
        // add previous files
        for (String key: mRecentPrefs.keys()) {
          String file = mRecentPrefs.get(key, null);
          if (file != null) {
            files.add(file);
          }
        }
        // prevent duplicates
        files.remove(path);
        // add to front
        files.add(0, path);
        // trim
        files = files.subList(0, Math.min(RECENT_DOCUMENTS_LIMIT - 1, files.size()));
        // store
        for (int i = 0; i < files.size(); i++) {
          mRecentPrefs.put(Integer.toString(i), files.get(i));
        }
      } 
      catch (BackingStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
  }
  
  public void setProperty(Class<? extends PropertyController> key) {
    canvas.setProperty(key);
    
    // epsilon
    mEpsilonListener.setProperty(key);
    float epsilon = mEpsilonListener.getEpsilon();
    
    // set the spinner to be the key's saved epsilon
    int digits = mEpsilonSpinner.getDigits();
    mEpsilonSpinner.setSelection((int) (epsilon * Math.pow(10, digits)));
    
    if (key != null) {            
      // tell the controller the epsilon
      CONTROLLER.setEpsilon(key, epsilon);
      
      // set current value of neighbourhood check box
      mNeighbourhoodButton.setSelection(CONTROLLER.getPropertyController(key).getUseNeighbourhoods());
    } 
    else {
      mNeighbourhoodButton.setSelection(false);
    }

    // disable the spinner when the key is null enable otherwise
    mEpsilonSpinner.setEnabled(key != null);
    
    // neighbourhoods
    mNeighbourhoodButton.setEnabled(key != null && key != NeighbourhoodController.class);
    mNeighbourhoodsListener.setProperty(key);
  }

  public void updateEpsilonMaximum() {
    int digits = mEpsilonSpinner.getDigits();
    mEpsilonSpinner.setMaximum((int) (CONTROLLER.getEpsilonMaximum() * Math.pow(10, digits)));
  }

  /**
   * Configure the shell.
   * @param newShell
   */
  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(BUNDLE.getString("MainWindow.Shell.text"));
    
    // set icons
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
    
    // hide the weird extra seperator
    getSeperator1().setVisible(false);
    
  }

  /**
   * Return the initial size of the window.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(580, 190);
  }

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
    GridLayout gl_composite = new GridLayout(1, false);
    gl_composite.marginLeft = 5;
    gl_composite.marginWidth = 0;
    composite.setLayout(gl_composite);
    
    CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(composite, SWT.BORDER | SWT.CHECK | SWT.MULTI);
    FeaturesContentProvider provider = new FeaturesContentProvider();
    checkboxTreeViewer.setAutoExpandLevel(2);
    checkboxTreeViewer.setContentProvider(provider);
    checkboxTreeViewer.setLabelProvider(new FeaturesLabelProvider());
    checkboxTreeViewer.setInput(CONTROLLER.getCategories());
    checkboxTreeViewer.setCheckStateProvider(new FeaturesCheckStateProvider(CONTROLLER.getCategories()));
    checkboxTreeViewer.addCheckStateListener(CONTROLLER.getCheckStateListener());
    checkboxTreeViewer.setSorter(new ViewerSorter());
    Tree tree = checkboxTreeViewer.getTree();
    tree.setHeaderVisible(true);
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    
    TreeColumn trclmnNewColumn = new TreeColumn(tree, 0);
    trclmnNewColumn.setWidth(120);
    trclmnNewColumn.setResizable(false);
    trclmnNewColumn.setText(BUNDLE.getString("ProximityDesktop.trclmnNewColumn.text")); //$NON-NLS-1$
    
    // create the add features button
    ActionContributionItem add = new ActionContributionItem(actnAddFeatures);
    add.fill(composite);
    Button btnAdd = (Button) add.getWidget();
    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    btnAdd.setText(BUNDLE.getString("Actions.EditFeatures.text"));
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
   * Swaps the currently displayed frame to the given composite.
   * @param frame
   */
  public void swapFrame(Composite frame) {
    stackLayout.topControl = frame;
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
      ActionContributionItem item = new ActionContributionItem(actnRegions);
      item.setMode(ActionContributionItem.MODE_FORCE_TEXT);
      propertyBarManager.add(item);
      propertyBarManager.add(actnNeighbourhoods);
      propertyBarManager.add(actnIntersection);
      propertyBarManager.add(actnCompliment);
      propertyBarManager.add(actnDifference);
      propertyBarManager.update(false);
      
      // select the first action
      actnRegions.setChecked(true);
    }
    
    // create the image canvas
    {
      canvas = new ImageCanvas(canvasFrame, SWT.BORDER | SWT.DOUBLE_BUFFERED, mImage);
      canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));      

      canvas.addMouseWheelListener(new ZoomListener(canvas));
      canvas.addMouseMoveListener(new PanListener(canvas));
      
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
      
      Composite composite = new Composite(canvasFrame, SWT.NONE);
      GridLayout gl_composite = new GridLayout(4, false);
      gl_composite.marginHeight = 1;
      composite.setLayout(gl_composite);
      composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      
      Label lblNewLabel = new Label(composite, SWT.NONE);
      lblNewLabel.setText(BUNDLE.getString("ProximityDesktop.lblNewLabel.text_1")); //$NON-NLS-1$
      
      mEpsilonSpinner = new Spinner(composite, SWT.BORDER);
      mEpsilonSpinner.setDigits(getEpsilonDigits());
      mEpsilonSpinner.setMinimum(0);
      // set max
      updateEpsilonMaximum();
      mEpsilonSpinner.addSelectionListener(mEpsilonListener);
      // disable spinner by default
      mEpsilonSpinner.setEnabled(false);
      
      mNeighbourhoodButton = new Button(composite, SWT.CHECK);
      mNeighbourhoodButton.setText(BUNDLE.getString("ProximityDesktop.btnCheckButton.text"));
      mNeighbourhoodButton.addSelectionListener(mNeighbourhoodsListener);
      mNeighbourhoodButton.setEnabled(false);
      
      ProgressBar progressBar = new ProgressBar(composite, SWT.NONE);
      progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      
      // setup first tool
      actnPointer.run();
    }
  }
  
  protected int getEpsilonDigits() {
    return 3;
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
      actnRegions = new PropertyAction("Properties.Regions.text", null);
      actnNeighbourhoods = 
          new PropertyAction("Properties.Neighbourhoods.text", NeighbourhoodController.class);
      actnIntersection = 
          new PropertyAction("Properties.Intersection.text", IntersectionController.class);
      actnCompliment = 
          new PropertyAction("Properties.Compliment.text", ComplimentController.class);
      actnDifference = 
          new PropertyAction("Properties.Difference.text", DifferenceController.class);
    
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

  /**
   * Create the menu manager.
   * @return the menu manager
   */
  @Override
  protected MenuManager createMenuManager() {
    MenuManager menuManager = new MenuManager();
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
    menuRecent.setRemoveAllWhenShown(true);
    menuRecent.addMenuListener(new RecentMenuListener(mRecentPrefs));
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
   * Enable all actions that only make ssense when we have an image.
   */
  private void enableImageActions() {
    for (Action a : mImageDependantActions) {
      a.setEnabled(true);
    }
  }
  
  @Override
  public boolean close() {
    // tell the controller we are closing
    CONTROLLER.onClose();
    return super.close();
  }
}
