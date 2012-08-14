package ca.uwinnipeg.proximity.desktop;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.desktop.features.AddFeaturesContentProvider;
import ca.uwinnipeg.proximity.desktop.features.FeaturesLabelProvider;
import org.eclipse.swt.widgets.Text;

//TODO: preserve path?
/**
 * The dialog used to add external {@link ProbeFunc}s for use calculating properties.
 * @author Garrett Smith
 *
 */
public class AddFeaturesDialog extends Dialog {
  
  private Composite container;
  
  private CheckboxTreeViewer treeViewer;
  
  private Button btnFolder;

  private String mPath;
  
  // filters out files that end in ".class"
  protected FilenameFilter mClassFileNameFilter = new FilenameFilter() {
    
    public boolean accept(File dir, String name) {
      return name.endsWith(".class");
    }
  };
  
  private Text text;
  
  boolean mTextValid = false;
  
  // checks if the category text is non empty
  private ModifyListener mTextListener = new ModifyListener() {
    
    public void modifyText(ModifyEvent e) {
      Text text = (Text) e.getSource();
      String str = text.getText();
      mTextValid = !str.trim().isEmpty();
      updateOkEnabled();
    }
  };
  
  boolean mFuncsValid = false;
  
  // checks if any funcs are pressed
  private ICheckStateListener mFuncsListeners = new ICheckStateListener() {
    
    public void checkStateChanged(CheckStateChangedEvent event) {
      CheckboxTreeViewer viewer = (CheckboxTreeViewer) event.getSource();
      mFuncsValid = viewer.getCheckedElements().length != 0;
      updateOkEnabled();
    }
  };

  /**
   * Create the dialog.
   * @param parentShell
   */
  public AddFeaturesDialog(Shell parentShell) {
    super(parentShell);
  }

  /**
   * Create contents of the dialog.
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {

    container = (Composite) super.createDialogArea(parent);  
    GridLayout gl_container = new GridLayout(2, false);
    gl_container.marginWidth = 11;
    container.setLayout(gl_container);
    
    Label lblNewLabel = new Label(container, SWT.NONE);
    lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblNewLabel.setText("Category: ");
    
    text = new Text(container, SWT.BORDER);
    text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    text.addModifyListener(mTextListener);

    Label lblFolder = new Label(container, SWT.NONE);
    lblFolder.setText("Source folder: ");

    btnFolder = new Button(container, SWT.NONE);
    btnFolder.addSelectionListener(new SelectionListener() {
      
      public void widgetSelected(SelectionEvent e) {
        doBrowse();
      }
      
      public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
      }
    });
    btnFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    btnFolder.setText("Browse");
    
    treeViewer = new CheckboxTreeViewer(container, SWT.BORDER);
    treeViewer.setContentProvider(new AddFeaturesContentProvider());
    treeViewer.setLabelProvider(new FeaturesLabelProvider());
    treeViewer.addCheckStateListener(mFuncsListeners);
    Tree tree = treeViewer.getTree();
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));    

    addSelectionButtons(container);

    return container;
  }
  
  /**
   * Add the selection and deselection buttons to the dialog.
   * @param composite org.eclipse.swt.widgets.Composite
   */
  private void addSelectionButtons(Composite composite) {    
    
    Composite buttonComposite = new Composite(composite, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 0;
    layout.marginWidth = 0;
    layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
    buttonComposite.setLayout(layout);
    buttonComposite.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false, 2, 1));

    Button selectButton = createButton(buttonComposite,
        IDialogConstants.SELECT_ALL_ID, "Select All", false);

    SelectionListener listener = new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        treeViewer.setAllChecked(true);
      }
    };
    selectButton.addSelectionListener(listener);

    Button deselectButton = createButton(buttonComposite,
        IDialogConstants.DESELECT_ALL_ID, "Deselect All", false);

    listener = new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        treeViewer.setAllChecked(false);
      }
    };
    deselectButton.addSelectionListener(listener);
  }

  /**
   * Create contents of the button bar.
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
        true);
    createButton(parent, IDialogConstants.CANCEL_ID,
        IDialogConstants.CANCEL_LABEL, false);

    updateOkEnabled();
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 500);
  }

  /**
   * Open a directory dialog for the user to select the directory to load class files from.
   */
  public void doBrowse() {
    DirectoryDialog dialog = new DirectoryDialog(getShell());
    dialog.setFilterPath(mPath); // start from current directory
    String path = dialog.open();
    
    if (path != null) {
      btnFolder.setText(path.substring(path.lastIndexOf('/')+1));
      mPath = path;
      onPathSelected(path);
    }
  }
  
  /**
   * Load probe func classes from the given path. 
   * @param path
   */
  //TODO: support classes in packages other than the empty default package.
  protected void onPathSelected(String path) {
    File dir = new File(path);
    
    // clear the current funcs
    List<ProbeFunc<Integer>> funcs = new ArrayList<ProbeFunc<Integer>>();
    
    // if we can read the directory
    if (dir.canRead()) {
      try {
        // create the class loader for the selected directory
        URL[] urls = {dir.toURI().toURL()};
        URLClassLoader loader = new URLClassLoader(urls);
        
        // try to load a class from each file that ends with .class
        for (File f : dir.listFiles(mClassFileNameFilter)) {
          if (f.canRead()) {
            try {
              String className = f.getName().substring(0, f.getName().lastIndexOf(".class"));            
              Class<?> clazz = Class.forName(className, true, loader);

              // if the class has a default constructor
              if (clazz.getConstructor() != null) {
                Object o = clazz.newInstance();

                // if the object is a probe func
                if (o instanceof ProbeFunc<?>) {
                  System.out.println("Loaded succesfully: " + className);
                  funcs.add((ProbeFunc<Integer>) o);
                }
                else {
                  System.out.println("Does not extend ProbeFunc: " + className);
                }
              }
              else {
                System.out.println("No default constructor found in: " + className);
              }
            } catch (ClassNotFoundException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (InstantiationException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (IllegalAccessException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (IllegalArgumentException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (SecurityException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }
      } catch (MalformedURLException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

    }
    
    // set the tree's content
    treeViewer.setInput(funcs);
  }
  
  @Override
  protected void okPressed() {
    @SuppressWarnings("unchecked")
    // add all the checked funcs to the controller in the given category
    Object[] funcsArray = treeViewer.getCheckedElements();
    List<ProbeFunc<Integer>> funcs = new ArrayList<ProbeFunc<Integer>>();
    for (int i = 0; i < funcsArray.length; i++) {
      funcs.add((ProbeFunc<Integer>) funcsArray[i]);
    }
    ProximityDesktop.getController().addProbeFuncs(text.getText().trim(), funcs, mPath);
    super.okPressed();
  };
  
  /**
   * Sets if the ok button is enabled.
   */
  protected void updateOkEnabled() {
    getOKButton().setEnabled(mFuncsValid && mTextValid);
  }

}
