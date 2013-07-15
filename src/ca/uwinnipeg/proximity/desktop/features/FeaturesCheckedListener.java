/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;

import ca.uwinnipeg.proximity.desktop.ProximityController;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;
import ca.uwinnipeg.proximity.image.ImageFunc;

/**
 * Listens to when a a feature is enabled and adds or removes it from the system.
 * @author Garrett Smith
 *
 */
public class FeaturesCheckedListener implements ICheckStateListener {
  
  private ProximityController mController;

  public FeaturesCheckedListener(ProximityController controller) {
    mController = controller;
  }

  public void checkStateChanged(CheckStateChangedEvent event) {
    CheckboxTreeViewer viewer = (CheckboxTreeViewer) event.getSource();
    Object element = event.getElement();
    boolean enabled = event.getChecked();
    
    // enable or disable the given probe function
    if (element instanceof ImageFunc) {
      ImageFunc func = (ImageFunc) element;
      mController.setProbeFuncEnabled(func, enabled);
    }
    // enable or disable the entire category of probe funcs
    else if (element instanceof Category) {
      Category category = (Category) element;
      mController.setCategoryEnabled(category, enabled);
    }
    viewer.refresh();
    
    // tell window to up maximum epsilon
    ProximityDesktop.getApp().updateEpsilonMaximum();
  }

}
