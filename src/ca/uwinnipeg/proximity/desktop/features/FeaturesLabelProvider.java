/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import org.eclipse.jface.viewers.LabelProvider;

import ca.uwinnipeg.proximity.ProbeFunc;

/**
 * @author Garrett Smith
 *
 */
public class FeaturesLabelProvider extends LabelProvider {
  
  @Override
  public String getText(Object element) {
    if (element instanceof Category) {
      return ((Category) element).getName();
    }
    else return ((ProbeFunc<Integer>) element).toString();
  }

}
