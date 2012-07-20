/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.tool.Pointer;
import ca.uwinnipeg.proximity.desktop.tool.Tool.ToolHost;

/**
 * The main window of the application.
 * @author Garrett Smith
 *
 */
// TODO: split up
public class MainWindow extends ApplicationWindow implements ToolHost {
  private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("ca.uwinnipeg.proximity.desktop.strings.messages"); //$NON-NLS-1$
  
//  enum Tool {
//    POINTER,
//    RECTANGLE,
//    OVAL,
//    POLYGON,
//    ZOOM
//  }  
  
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
  
  // tools
  private Pointer toolPointer;
  
  private ImageCanvas canvas;
  
  private Image mImage;
  
  private MainController mController;
  
//  private Tool mTool = Tool.POINTER;
  
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
  
//  private Listener mToolListener = new ToolListener();
//  
//  private class ToolListener implements Listener, MouseListener, MouseMoveListener, PaintListener {
//    
//    private Point mStartImagePoint;
//    private Point mCurrentImagePoint;
//    
//    private Point mStartScreenPoint;
//    private Point mCurrentScreenPoint;
//
//    /**
//     * Route the events to handlers
//     */
//    public void handleEvent(Event event) {
//      switch(event.type) {
//        case SWT.MouseDown:
//          mouseDown(new MouseEvent(event));
//          break;
//        case SWT.MouseUp:
//          mouseUp(new MouseEvent(event));
//          break;
//        case SWT.MouseMove:
//          mouseMove(new MouseEvent(event));
//          break;
//        case SWT.Paint:
//          paintControl(new PaintEvent(event));
//          break;
//      }
//    }
//
//    public void mouseDown(MouseEvent e) {
//      // if the first button was pressed
//      if (e.button == 1) {
//        mStartScreenPoint = mCurrentScreenPoint;
//        mStartImagePoint = mCurrentImagePoint;
//
//        // check if we clicked outside of the image
//        if (!canvas.contains(mStartImagePoint)) {
//          mStartImagePoint = mStartScreenPoint = null;
//        }
//      }
//    }
//
//    public void mouseUp(MouseEvent e) {
//      // make sure we started in a valid position3
//      if (mStartImagePoint != null) {
//        
//        // check if the didn't just click a point
//        if (mCurrentImagePoint != mStartImagePoint) {
//          switch (mTool) {
//            case ZOOM:
//              canvas.zoomTo(mStartImagePoint, mCurrentImagePoint);
//              break;
//            case RECTANGLE:
//              List<Point> points = new ArrayList<Point>();
//              points.add(mStartImagePoint);
//              points.add(mCurrentImagePoint);
//              mController.addRegion(Region.Shape.RECTANGLE, points);
//              break;
//          }
//        }
//        // handle clicks
//        else {
//          if (mTool == Tool.POINTER) {
//            // TODO: check if shift or ctrl is being held
//            mSelectedRegions.clear();
//            // see if we clicked any regions
//            for (Region r : mController.getRegions()) {
//              Rectangle bounds = r.getBounds();
//              if (bounds.contains(mStartImagePoint)) {
//                mSelectedRegions.add(r);
//              }
//            }
//          }
//        }
//
//        mStartImagePoint = mStartScreenPoint = null;
//        canvas.redraw();
//
//      }
//    }
//
//    public void mouseMove(MouseEvent e) {
//      mCurrentScreenPoint = new Point(e.x, e.y);
//      mCurrentImagePoint = canvas.toImageSpace(mCurrentScreenPoint);
//      
//      // check if the mouse has been clicked and we should draw something
//      if (mStartImagePoint != null) {
//        // limit points to be within image bounds
//        mCurrentImagePoint.x = Math.max(0, mCurrentImagePoint.x);
//        mCurrentImagePoint.x = Math.min(mImage.getBounds().width, mCurrentImagePoint.x);        
//
//        mCurrentImagePoint.y = Math.max(0, mCurrentImagePoint.y);
//        mCurrentImagePoint.y = Math.min(mImage.getBounds().height, mCurrentImagePoint.y);
//
//        canvas.redraw();
//      }
//    }
//
//    public void mouseDoubleClick(MouseEvent e) {
//      // Do nothing
//    }
//
//    public void paintControl(PaintEvent e) {      
//      if (mStartImagePoint != null && mCurrentImagePoint != null) {
//        
//        GC gc = e.gc;
//        
//        int width = mCurrentScreenPoint.x - mStartScreenPoint.x;
//        int height = mCurrentScreenPoint.y - mStartScreenPoint.y;
//        
//        // draw color
//        Color color;
//        if (mTool == Tool.RECTANGLE || mTool == Tool.OVAL) {
//          // CYAN
//          color = new Color(Display.getCurrent(), 0, 255, 255);
//        }
//        else {
//          // Black
//          color = new Color(Display.getCurrent(), 0, 0, 0);
//        }
//        gc.setForeground(color);
//        
//        if (mTool == Tool.POINTER) {
//          gc.setLineStyle(SWT.LINE_DOT);
//        }
//        
//        switch (mTool) {
//          case RECTANGLE:
//          case ZOOM:
//          case POINTER:
//            gc.drawRectangle(
//                mStartScreenPoint.x, 
//                mStartScreenPoint.y, 
//                width, 
//                height);
//            break;
//          case OVAL:
//            gc.drawOval(
//              mStartScreenPoint.x, 
//              mStartScreenPoint.y, 
//              width, 
//              height);
//            gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
//            gc.setAlpha(150);
//            gc.drawRectangle(
//              mStartScreenPoint.x, 
//              mStartScreenPoint.y, 
//              width, 
//              height);
//            break;
//        }
//      }
//    }
//    
//  }
  
  private PaintListener mPaintRegionsListener = new PaintListener() {
    
    public void paintControl(PaintEvent e) {
      GC gc = e.gc;
      Color unselected = new Color(Display.getCurrent(), 255, 255, 255);
      Color selected = new Color(Display.getCurrent(), 0, 255, 255);
      gc.setAlpha(100);
      for (Region r : mController.getRegions()) {
        Rectangle bounds = r.getBounds();
        bounds = canvas.toScreenSpace(bounds);
        // determine if the region is selected
        if (mSelectedRegions.contains(r)) {
          gc.setBackground(selected);
        }
        else {
          gc.setBackground(unselected);
        }
        gc.fillRectangle(bounds);
      }
    }
  };

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
   * Create the application window.
   */
  public MainWindow() {
    super(null);
    mKeyLookup = KeyLookupFactory.getDefault();
    createTools();
    createActions();
    addToolBar(SWT.FLAT | SWT.WRAP);
    addMenuBar();
    //addStatusLine();
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
      propertyBarManager.add(actnRegions);
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

      canvas.addMouseWheelListener(mMouseWheelListener);
      canvas.addMouseMoveListener(mMouseMoveListener);
      
//      canvas.addListener(SWT.MouseDown, mToolListener);
//      canvas.addListener(SWT.MouseUp, mToolListener);
//      canvas.addListener(SWT.MouseMove, mToolListener);
//      canvas.addListener(SWT.Paint, mToolListener);
      
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
    lblOpenButton.setText(BUNDLE.getString("MainWindow.lblOpenButton.text")); //$NON-NLS-1$
    
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
    createFileActions();
    createEditActions();
    createViewActions();
    createHelpActions();
    createPropertyActions();
    createToolActions();
    
    // features
    {
      actnAddFeatures = new Action("Add Features") {
        @Override
        public void run() {
          doAddFeatures();
        }
      };
    }
    
    // record all actions that need an image
    mImageDependantActions = new Action[] {
        actnSnapshot,
        actnSelectAll,
        actnZoomIn,
        actnZoomOut,
        actnZoom1to1,
        actnZoomImage,
        actnPointer,
//        actnRectangle,
//        actnOval,
//        actnPolygon,
//        actnZoom,
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
  }

  private void createFileActions() {
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
      actnSnapshot.setImageDescriptor(ResourceManager.getImageDescriptor(MainWindow.class, "/ca/uwinnipeg/proximity/desktop/icons/snap.png"));
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
      actnEmpty = new Action(BUNDLE.getString("MainWindow.actionEmpty.text")) { //$NON-NLS-1$
      };
      actnEmpty.setEnabled(false);
    }
  }
  
  private void createEditActions() {
    {
      actnUndo = new Action(BUNDLE.getString("MainWindow.actnUndo.text")) { //$NON-NLS-1$
      };
    }
    {
      actnRedo = new Action(BUNDLE.getString("MainWindow.actnRedo.text")) { //$NON-NLS-1$
      };
    }
    {
      actnCut = new Action(BUNDLE.getString("MainWindow.actnCut.text")) { //$NON-NLS-1$
      };
    }
    {
      actnCopy = new Action(BUNDLE.getString("MainWindow.actnCopy.text")) { //$NON-NLS-1$
      };
    }
    {
      actnPaste = new Action(BUNDLE.getString("MainWindow.actnPaste.text")) { //$NON-NLS-1$
      };
    }
    {
      actnDuplicate = new Action(BUNDLE.getString("MainWindow.actnDuplicate.text")) { //$NON-NLS-1$
      };
    }
    {
      actnDelete = new Action(BUNDLE.getString("MainWindow.actnDelete.text")) { //$NON-NLS-1$
      };
    }
    {
      actnSelectAll = new Action(BUNDLE.getString("MainWindow.actnSelectAll.text")) { //$NON-NLS-1$
      };
    }
  }
  
  private void createViewActions() {
    {
      actnZoomIn = new Action(BUNDLE.getString("MainWindow.actnZoomIn.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          canvas.zoomIn();
        }
      };
      actnZoomIn.setAccelerator(0 | '=');
      actnZoomIn.setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_ADD_NAME));
    }
    {
      actnZoomOut = new Action(BUNDLE.getString("MainWindow.actnZoomOut.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          canvas.zoomOut();
        }
      };
      actnZoomOut.setAccelerator(0 | '-');
      actnZoomOut.setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_SUBTRACT_NAME));
    }
    {
      actnZoom1to1 = new Action(BUNDLE.getString("MainWindow.actnZoom1to1.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          canvas.zoomTo1();
        }
      };
      actnZoom1to1.setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_1_NAME));
    }
    {
      actnZoomSelection = new Action(BUNDLE.getString("MainWindow.actnZoomSelection.text")) { //$NON-NLS-1$
      };
      actnZoomSelection.setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_2_NAME));
    }
    {
      actnZoomImage = new Action(BUNDLE.getString("MainWindow.actnZoomImage.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          canvas.fitToImage();
        }
      };
      actnZoomImage.setAccelerator(0 | mKeyLookup.formalKeyLookup(IKeyLookup.NUMPAD_3_NAME));
    }
    {
      actnFeatures = new Action(BUNDLE.getString("MainWindow.actnFeatures.text"), Action.AS_CHECK_BOX) { //$NON-NLS-1$
        @Override
        public void runWithEvent(Event event) {
          MenuItem item = (MenuItem) event.widget;
          doToggleFeatures(item.getSelection());
        }
      };
      actnFeatures.setChecked(true);
    }
    {
      actnCenter = new Action(BUNDLE.getString("MainWindow.actnCenter.text")) { //$NON-NLS-1$
        @Override
        public void run() {
          canvas.center();
        }
      };
    }
  }
  
  private void createHelpActions() {
    {
      actnManual = new Action(BUNDLE.getString("MainWindow.actnManual.text")) { //$NON-NLS-1$
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
  }
  
  private void createPropertyActions() {
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
  
//  private class ToolAction extends Action {
//    
//    private Tool mThisTool;
//    
//    public ToolAction(String label, String icon, Tool tool) {
//      super(BUNDLE.getString(label), Action.AS_RADIO_BUTTON);
//      setImageDescriptor(ResourceManager.getImageDescriptor(MainWindow.class, "/ca/uwinnipeg/proximity/desktop/icons/" + icon));
//      mThisTool = tool;
//    }
//    
//    @Override
//    public void run() {
//      //System.out.println(mThisTool);
//      mTool = mThisTool;
//    }
//  }
  
  private void createTools() {
    toolPointer = new Pointer(this);
  }
  
  private void createToolActions() {
    actnPointer = toolPointer.getAction();
//    actnPointer = new ToolAction("MainWindow.action.text", "pointer.png", Tool.POINTER);
//    actnRectangle = new ToolAction("MainWindow.actnRectangle.text", "rect.png", Tool.RECTANGLE);
//    actnOval = new ToolAction("MainWindow.action.text_1", "oval.png", Tool.OVAL);
//    actnPolygon = new ToolAction("MainWindow.actnPolygon.text", "poly.png", Tool.POLYGON);
//    actnZoom = new ToolAction("MainWindow.actnZoom.text", "zoom.png", Tool.ZOOM);
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
    MenuManager menuRecent = new MenuManager(BUNDLE.getString("MainWindow.menuRecent.text"));
    menuFile.add(menuRecent);
    menuRecent.add(actnEmpty);
    menuFile.add(new Separator());
    menuFile.add(actnSnapshot);
    menuFile.add(new Separator());
    menuFile.add(actnExit);
    
    MenuManager menuEdit = new MenuManager(BUNDLE.getString("MainWindow.menuManager_1.text")); //$NON-NLS-1$
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
    
    MenuManager menuView = new MenuManager(BUNDLE.getString("MainWindow.menuView.text")); //$NON-NLS-1$
    menuManager.add(menuView);
    menuView.add(actnCenter);
    menuView.add(new Separator());
    
    MenuManager menuZoom = new MenuManager(BUNDLE.getString("MainWindow.menuZoom.text")); //$NON-NLS-1$
    menuView.add(menuZoom);
    menuZoom.add(actnZoomIn);
    menuZoom.add(actnZoomOut);
    menuZoom.add(new Separator());
    menuZoom.add(actnZoom1to1);
    menuZoom.add(actnZoomSelection);
    menuZoom.add(actnZoomImage);
    menuView.add(new Separator());
    menuView.add(actnFeatures);
    
    MenuManager menuHelp = new MenuManager("&Help");
    menuManager.add(menuHelp);
    menuHelp.add(actnManual);
    menuHelp.add(new Separator());
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
    toolBarManager.add(actnPointer);
    actnPointer.setChecked(true);
//    toolBarManager.add(actnRectangle);
//    toolBarManager.add(actnOval);
//    toolBarManager.add(actnPolygon);
//    toolBarManager.add(actnZoom);
//    toolBarManager.add(actnSnapshot);
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
    newShell.setText(BUNDLE.getString("MainWindow.newShell.text")); //$NON-NLS-1$
    
    // set icon
    String dir = "/ca/uwinnipeg/proximity/desktop/icons/";
    Display display = Display.getCurrent();
    
    Image[] imgs = new Image[] {
      ResourceManager.getImageDescriptor(MainWindow.class, dir + "launcher_16.png").createImage(display),
      ResourceManager.getImageDescriptor(MainWindow.class, dir + "launcher_24.png").createImage(display),
      ResourceManager.getImageDescriptor(MainWindow.class, dir + "launcher_36.png").createImage(display),
      ResourceManager.getImageDescriptor(MainWindow.class, dir + "launcher_48.png").createImage(display),
      ResourceManager.getImageDescriptor(MainWindow.class, dir + "launcher_64.png").createImage(display),
      ResourceManager.getImageDescriptor(MainWindow.class, dir + "launcher_128.png").createImage(display)
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
  
  /**
   * Sets the controller to send and receive data from.
   * @param controller
   */
  public void setController(MainController controller) {
    mController = controller;
  }

  public MainController getController() {
    return mController;
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

      // update canvas
      canvas.setImage(mImage);

      // enable disabled buttons
      enableImageActions();
      
      // tell the controller about the new image
      mController.onImageSelected(data);
    }
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
    // TODO: prepare image
    SnapshotDialog dialog = new SnapshotDialog(getShell(), mImage);
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
  public void doToggleFeatures(boolean show) {
    if (show) {
      sashForm.setWeights(new int[]{1, 3});
    }
    else {
      sashForm.setWeights(new int[]{0, 1});
    }
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
