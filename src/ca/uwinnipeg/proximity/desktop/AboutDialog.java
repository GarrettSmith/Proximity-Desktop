package ca.uwinnipeg.proximity.desktop;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

// TODO: add credits and license
// TODO: use external strings
/**
 * The about dialog that displays credits and a description.
 * @author Garrett Smith
 *
 */
public class AboutDialog extends Dialog {  

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
    
    // the application icon
    Label lblIcon = new Label(container, SWT.NONE);
    lblIcon.setImage(SWTResourceManager.getImage(AboutDialog.class, "/ca/uwinnipeg/proximity/desktop/icons/launcher_48.png"));
    lblIcon.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    
    // the application name
    Label lblName = new Label(container, SWT.NONE);
    lblName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
    lblName.setFont(SWTResourceManager.getFont("Sans", 10, SWT.BOLD));
    lblName.setText("Proximity System");
    
    // stacks to display information
    Composite frameStack = new Composite(container, SWT.NONE);
    stackLayout = new StackLayout();
    frameStack.setLayout(stackLayout);
    frameStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
    // the main frame for the about info
    Composite mainFrame = new Composite(frameStack, SWT.NONE);
    mainFrame.setLayout(new GridLayout(1, false));
    
    // the version
    Label lblVersion = new Label(mainFrame, SWT.NONE);
    lblVersion.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    lblVersion.setText("1.0");
    
    // the description sentence for the program
    Label lblDescription = new Label(mainFrame, SWT.NONE);
    lblDescription.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
    lblDescription.setText("The Proximity System desktop application.");
    
    // the copyright string
    Label lblCopyright = new Label(mainFrame, SWT.NONE);
    lblCopyright.setAlignment(SWT.CENTER);
    lblCopyright.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, true, 1, 1));
    lblCopyright.setText("Programmed by Garrett Smith \n" +
    		"Research by Christopher Henry \n" +
    		"University of Winnipeg");
    
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
}
