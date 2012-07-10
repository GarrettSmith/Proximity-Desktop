package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class MainWindow extends ApplicationWindow {
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Action toolbar_open;
  private Action menu_file;
  private Action toolbar_snapshot;

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
    container.setLayout(new GridLayout(2, false));
    
    Tree tree_feature = formToolkit.createTree(container, SWT.BORDER);
    tree_feature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
    formToolkit.paintBordersFor(tree_feature);
    {
      ToolBar tabBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
      {
        ToolItem tab_regions = new ToolItem(tabBar, SWT.NONE);
        tab_regions.setWidth(-1);
        tab_regions.setText("Regions");
      }
      {
        ToolItem tab_neighbourhoods = new ToolItem(tabBar, SWT.NONE);
        tab_neighbourhoods.setWidth(-1);
        tab_neighbourhoods.setText("Neighbourhoods");
      }
      {
        ToolItem tab_intersection = new ToolItem(tabBar, SWT.NONE);
        tab_intersection.setWidth(-1);
        tab_intersection.setText("Intersection");
      }
      
      ToolItem tab_compliment = new ToolItem(tabBar, SWT.NONE);
      tab_compliment.setWidth(-1);
      tab_compliment.setText("Compliment");
      
      ToolItem tab_difference = new ToolItem(tabBar, SWT.NONE);
      tab_difference.setWidth(-1);
      tab_difference.setText("Difference");
    }
    {
      Canvas canvas = new Canvas(container, SWT.BORDER);
      canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
    }
    
    Button button_add_feature = new Button(container, SWT.NONE);
    button_add_feature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    button_add_feature.setText("Add Feature");

    return container;
  }

  /**
   * Create the actions.
   */
  private void createActions() {
    // Create the actions
    {
      toolbar_open = new Action("Open") {
      };
    }
    {
      menu_file = new Action("File") {
      };
    }
    {
      toolbar_snapshot = new Action("Snapshot") {
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
    menuManager.add(menu_file);
    return menuManager;
  }

  /**
   * Create the toolbar manager.
   * @return the toolbar manager
   */
  @Override
  protected ToolBarManager createToolBarManager(int style) {
    ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP);
    toolBarManager.add(toolbar_open);
    toolBarManager.add(toolbar_snapshot);
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
}
