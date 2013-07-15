/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import java.util.Collection;

import org.eclipse.jface.viewers.ICheckStateProvider;

import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.image.ImageFunc;

/**
 * Sets the checked state in the features pane for {@link ProbeFunc} and {@link Category}.
 * @author Garrett Smith
 *
 */
public class FeaturesCheckStateProvider implements ICheckStateProvider {
  
  private Collection<Category> mCategories;

  /**
   * 
   */
  public FeaturesCheckStateProvider(Collection<Category> collection) {
    mCategories = collection;
  }

  public boolean isChecked(Object element) {
    // return if the category is enabled
    if (element instanceof Category) {
      boolean enabled = !((Category) element).isDisabled();
      return enabled;
    }
    // return if the function is enabled
    else {
      ImageFunc func = (ImageFunc) element;
      for (Category cat: mCategories) {
        if (cat.contains(func)) {
          boolean enabled = cat.isEnabled(func);
          return enabled;
        }
      }
      return false;
    }
  }

  public boolean isGrayed(Object element) {
    if (element instanceof Category) {
      return ((Category) element).isGrayed();
    }
    else {
      return false;
    }
  }

}
