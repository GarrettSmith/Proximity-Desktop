/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import java.util.List;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.desktop.ProximityDesktop;
import ca.uwinnipeg.proximity.desktop.features.Category;

/**
 * Removes the selected {@link ProbeFunc}s and {@link Category}s.
 * @author Garrett Smith
 *
 */
public class RemoveFeatureAction extends Action {
  
  private List<?> mSelection;

  /**
   * Creates a new action to remove the given funcs and cats.
   */
  public RemoveFeatureAction(List<?> selection) {
    super(ProximityDesktop.getBundle().getString("Actions.RemoveFeature.text"));
    mSelection = selection;
  }
  
  @Override
  public void run() {
    for (Object o: mSelection) {
      if (o instanceof Category<?>) {
        ProximityDesktop.getController().removeCategory((Category<Integer>) o);
      }
      else if (o instanceof ProbeFunc<?>) {
        ProximityDesktop.getController().removeProbeFuncs((ProbeFunc<Integer>) o);
      }
    }
  }
}
