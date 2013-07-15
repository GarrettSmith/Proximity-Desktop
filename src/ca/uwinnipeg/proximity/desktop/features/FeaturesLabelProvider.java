/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import org.eclipse.jface.viewers.LabelProvider;

import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.image.ImageFunc;

/**
 * Returns the name of the {@link Category} or the toString method of the {@link ProbeFunc}.
 * @author Garrett Smith
 *
 */
public class FeaturesLabelProvider extends LabelProvider {
  
  @Override
  public String getText(Object element) {
    if (element instanceof Category) {
      return ((Category) element).getName();
    }
    else return ((ImageFunc) element).toString();
  }

}
