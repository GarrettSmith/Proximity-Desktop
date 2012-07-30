/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.Map;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;

import ca.uwinnipeg.proximity.ProbeFunc;

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
    Object element = event.getElement();
    if (element instanceof ProbeFunc<?>) {
      mController.setProbeFuncEnabled((ProbeFunc<Integer>) element, event.getChecked());
    }
    else if (element instanceof String) {
      Map<ProbeFunc<Integer>, Boolean> maps = mController.getProbeFuncs().get(element);
      if (maps != null) {
        for (ProbeFunc<Integer> f : maps.keySet()) {
          mController.setProbeFuncEnabled(f, event.getChecked());
        }
      }
    }
  }

}
