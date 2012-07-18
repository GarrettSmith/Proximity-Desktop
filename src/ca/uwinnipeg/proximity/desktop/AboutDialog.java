package ca.uwinnipeg.proximity.desktop;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends Dialog {
  //private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  /**
   * Create the dialog.
   * @param parentShell
   */
  public AboutDialog(Shell parentShell) {
    super(parentShell);
  }

  /**
   * Create contents of the dialog.
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new GridLayout(1, false));
    
    Label lblIcon = new Label(container, SWT.NONE);
    lblIcon.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    
    Label lblName = new Label(container, SWT.NONE);
    lblName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    lblName.setText("Proximity System");
    
    Label lblVersion = new Label(container, SWT.NONE);
    lblVersion.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    lblVersion.setText("Version");
    
    Label lblDescription = new Label(container, SWT.NONE);
    lblDescription.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    lblDescription.setText("The Proxmity System desktop application.");
    
    Label lblCopyright = new Label(container, SWT.NONE);
    lblCopyright.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, true, 1, 1));
    lblCopyright.setText("© 2012 Holder");

    return container;
  }

  /**
   * Create contents of the button bar.
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    // TODO: setup buttons properly
    createButton(
        parent, 
        IDialogConstants.CANCEL_ID, 
        IDialogConstants.CLOSE_LABEL, 
        true);
    
    Button btnCredits = createButton(
        parent, 
        IDialogConstants.CLOSE_ID, 
        IDialogConstants.CLOSE_LABEL, 
        false);
    btnCredits.setEnabled(false);
    btnCredits.setText("Credits");
    
    Button btnLicence = createButton(
        parent, 
        IDialogConstants.CLOSE_ID, 
        IDialogConstants.CLOSE_LABEL, 
        false);
    btnLicence.setEnabled(false);
    btnLicence.setText("Licence");
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }
  
  @Override
  protected void buttonPressed(int buttonId) {
    switch (buttonId) {
      default:
        super.buttonPressed(buttonId);
    }
  }

}
