/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ca.uwinnipeg.proximity.image.ImageFunc;

/**
 * Provides the features and categories for the features pane.
 * @author Garrett Smith
 *
 */
public class FeaturesContentProvider implements ITreeContentProvider  {
  
  private Collection<Category> mCategories;

  public void dispose() {
    // do nothing
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    mCategories = (Collection<Category>) newInput;
  }

  public Object[] getElements(Object inputElement) {
    return mCategories.toArray();
  }

  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof Category) {
      return ((Category) parentElement).getProbeFuncs().toArray();
    }
    else {
      return null;
    }
  }

  public Object getParent(Object element) {
    if (element instanceof ImageFunc) {
      for (Category cat : mCategories) {
        if (cat.contains((ImageFunc) element)) {
          return cat;
        }
      }
    }
    // fall through
    return null;
  }

  public boolean hasChildren(Object element) {
    if (element instanceof Category) {
      return !((Category) element).isEmpty();
    }
    else {
      return false;
    }
  }

}
