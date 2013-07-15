/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ca.uwinnipeg.proximity.image.ImageFunc;

/**
 * Provides the probe funcs when adding probe funcs.
 * @author Garrett Smith
 *
 */
public class AddFeaturesContentProvider implements ITreeContentProvider {
  
  private List<ImageFunc> mFuncs;

  public void dispose() {
    // do nothing
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    mFuncs = (List<ImageFunc>) newInput;
  }

  public Object[] getElements(Object inputElement) {
    return mFuncs.toArray();
  }

  public Object[] getChildren(Object parentElement) {
    return null;
  }

  public Object getParent(Object element) {
    return null;
  }

  public boolean hasChildren(Object element) {
    return false;
  }

}
