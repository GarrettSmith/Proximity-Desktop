/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.action;

import java.util.List;

import org.eclipse.jface.action.Action;

import ca.uwinnipeg.proximity.desktop.ProximityDesktop;

/**
 * @author Garrett Smith
 *
 */
public class RemoveFeatureAction extends Action {
  
  private List<?> mSelection;

  /**
   * 
   */
  public RemoveFeatureAction(List<?> selection) {
    super(ProximityDesktop.getBundle().getString("Actions.RemoveFeature.text"));
    mSelection = selection;
  }
  
  @Override
  public void run() {
    for (Object o: mSelection) {
      //TODO: remove funcs and categories
    }
  }
}
