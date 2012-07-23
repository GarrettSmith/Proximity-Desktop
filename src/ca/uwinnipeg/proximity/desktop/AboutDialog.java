package ca.uwinnipeg.proximity.desktop;

import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class AboutDialog extends Dialog {
  private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("ca.uwinnipeg.proximity.desktop.messages"); //$NON-NLS-1$
  

  private StackLayout stackLayout;

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
    lblIcon.setImage(SWTResourceManager.getImage(AboutDialog.class, "/ca/uwinnipeg/proximity/desktop/icons/launcher_48.png"));
    lblIcon.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    
    Label lblName = new Label(container, SWT.NONE);
    lblName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
    lblName.setFont(SWTResourceManager.getFont("Sans", 10, SWT.BOLD));
    lblName.setText("Proximity System");
    
    Composite frameStack = new Composite(container, SWT.NONE);
    stackLayout = new StackLayout();
    frameStack.setLayout(stackLayout);
    frameStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
    Composite mainFrame = new Composite(frameStack, SWT.NONE);
    mainFrame.setLayout(new GridLayout(1, false));
    
    Label lblVersion = new Label(mainFrame, SWT.NONE);
    lblVersion.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    lblVersion.setText("1.0");
    
    Label lblDescription = new Label(mainFrame, SWT.NONE);
    lblDescription.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    lblDescription.setText("The Proximity System desktop application.");
    
    Label lblCopyright = new Label(mainFrame, SWT.NONE);
    lblCopyright.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, true, 1, 1));
    lblCopyright.setFont(SWTResourceManager.getFont("Droid Sans", 8, SWT.NORMAL));
    lblCopyright.setText("© 2012 Holder");
    
    Composite licenceFrame = new Composite(frameStack, SWT.NONE);
    licenceFrame.setLayout(new FillLayout(SWT.HORIZONTAL));
    
    StyledText licenceText = new StyledText(licenceFrame, SWT.BORDER);
    licenceText.setEditable(false);
    
    Composite creditsFrame = new Composite(frameStack, SWT.NONE);
    creditsFrame.setLayout(new FillLayout(SWT.HORIZONTAL));
    
    StyledText creditsText = new StyledText(creditsFrame, SWT.BORDER);
    
    stackLayout.topControl = mainFrame;
    frameStack.layout();

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
    
//    Button btnCredits = createButton(
//        parent, 
//        IDialogConstants.CLOSE_ID, 
//        IDialogConstants.CLOSE_LABEL, 
//        false);
//    btnCredits.setText("Credits");
//    
//    Button btnLicence = createButton(
//        parent, 
//        IDialogConstants.CLOSE_ID, 
//        IDialogConstants.CLOSE_LABEL, 
//        false);
//    btnLicence.setText("Licence");
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
