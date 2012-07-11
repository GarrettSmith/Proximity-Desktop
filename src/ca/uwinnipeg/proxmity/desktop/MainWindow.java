/**
 * 
 */
package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The main window of the application.
 * @author Garrett Smith
 *
 */
public class MainWindow extends ApplicationWindow {
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Action actnOpen;
  private Action actnSnapshot;
  private Action actnExit;
  private Action actnAbout;
  
  private Canvas canvas;
  
  private Image mImage;
  
  private MainController mController;

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
  @Override
  protected Control createContents(Composite parent) {
    Composite container = new Composite(parent, SWT.NONE);
    container.setLayout(new FillLayout(SWT.HORIZONTAL));
    
    SashForm sashForm = new SashForm(container, SWT.NONE);
    
    Composite composite = new Composite(sashForm, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));
    
    Tree tree_feature = formToolkit.createTree(composite, SWT.BORDER | SWT.CHECK | SWT.MULTI);
    tree_feature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    
    Button btnAdd = new Button(composite, SWT.NONE);
    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    btnAdd.setText("Add Feature");
    
    Button btnRemove = new Button(composite, SWT.NONE);
    btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    btnRemove.setText("Remove");
    
    Composite composite_1 = new Composite(sashForm, SWT.NONE);
    composite_1.setLayout(new GridLayout(1, false));
    {
      ToolBar propertyBar = new ToolBar(composite_1, SWT.FLAT | SWT.RIGHT);
      propertyBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
      {
        // TODO: Switch between properties
        ToolItem btnRegions = new ToolItem(propertyBar, SWT.RADIO);
        btnRegions.addSelectionListener(new SelectionAdapter() {
          @Override
          public void widgetSelected(SelectionEvent e) {
            System.out.println("Regions Selected");
          }
        });
        btnRegions.setSelection(true);
        btnRegions.setWidth(-1);
        btnRegions.setText("Regions");
      }
      {
        ToolItem btnNeighbourhoods = new ToolItem(propertyBar, SWT.RADIO);
        btnNeighbourhoods.setWidth(-1);
        btnNeighbourhoods.setText("Neighbourhoods");
      }
      {
        ToolItem btnIntersection = new ToolItem(propertyBar, SWT.RADIO);
        btnIntersection.setWidth(-1);
        btnIntersection.setText("Intersection");
      }
      
      ToolItem btnCompliment = new ToolItem(propertyBar, SWT.RADIO);
      btnCompliment.setWidth(-1);
      btnCompliment.setText("Compliment");
      
      ToolItem btnDifference = new ToolItem(propertyBar, SWT.RADIO);
      btnDifference.setWidth(-1);
      btnDifference.setText("Difference");
    }
    {
      canvas = new Canvas(composite_1, SWT.BORDER);
      canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
      canvas.addPaintListener(new PaintListener() {
        
        public void paintControl(PaintEvent e) {
          if (mImage != null) {
            e.gc.drawImage(mImage, 0, 0);
          }
        }
      });
    }
    
    sashForm.setWeights(new int[] {160, 637});

    return container;
  }

  /**
   * Create the actions.
   */
  private void createActions() {
    // Create the actions
    {
      actnOpen = new Action("&Open@Ctrl+O") {
        @Override
        public void run() {
          doOpen();
        }
      };
    }
    {
      actnSnapshot = new Action("Snapshot") {
      };
    }
    {
      actnExit = new Action("Exit@Shift+Ctrl+Q") {
        @Override
        public void run() {
          doExit();
        }
      };
    }
    {
      actnAbout = new Action("About") {
        @Override
        public void run() {
          doAbout();
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
    newShell.setText("Proximity Desktop");
  }

  /**
   * Return the initial size of the window.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }
  
  public void setController(MainController controller) {
    mController = controller;
  }
  
  // Actions

  /**
   * Respond to the open item being selected.
   */
  public void doOpen() {
    // create the dialog to select an image file
    FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
    dialog.setText("Select an image file");
    dialog.setFilterExtensions(new String[]{"*.jpg;*.;*.gif"});
    
    // get a file path
    String path = dialog.open();
    
    // load the image
    ImageData data = new ImageData(path);
    mImage = new Image(Display.getCurrent(), data);
    
    // request a redraw
    canvas.redraw();
  }

  /**
   * Show the about dialog.
   */
  public void doAbout() {
    AboutDialog dialog = new AboutDialog(getShell());
    dialog.open();
  }

  /**
   * Exit the program.
   */
  public void doExit() {    
    close();
  }
  
  @Override
  public boolean close() {
    // tell the controller we are closing
    mController.onClose();
    return super.close();
  }
}
