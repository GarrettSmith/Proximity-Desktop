/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;

import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.desktop.ProximityController;

/**
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
    if (element instanceof ProbeFunc<?>) {
      ProbeFunc<Integer> func = (ProbeFunc<Integer>) element;
      mController.setProbeFuncEnabled(func, enabled);
    }
    // enable or disable the entire category of probe funcs
    else if (element instanceof Category<?>) {
      Category<Integer> category = (Category<Integer>) element;
      mController.setCategoryEnabled(category, enabled);
    }
    viewer.refresh();
  }

}
