package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

public class AddFeaturesDialog extends Dialog {
  private Composite container;
  
  private CheckboxTreeViewer treeViewer;

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

    Label lblFolder = new Label(container, SWT.NONE);
    lblFolder.setText("Source folder: ");

    Button btnFolder = new Button(container, SWT.NONE);
    btnFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    btnFolder.setText("Browse");
    
    treeViewer = new CheckboxTreeViewer(container, SWT.BORDER);
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
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 500);
  }

}
