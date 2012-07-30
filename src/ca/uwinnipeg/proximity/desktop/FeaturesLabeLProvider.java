/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import org.eclipse.jface.viewers.LabelProvider;

import ca.uwinnipeg.proximity.ProbeFunc;

/**
 * @author Garrett Smith
 *
 */
public class FeaturesLabeLProvider extends LabelProvider {
  
  @Override
  public String getText(Object element) {
    if (element instanceof String) {
      return (String) element;
    }
    else return ((ProbeFunc<Integer>) element).toString();
  }

}
