/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.Map;

import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ca.uwinnipeg.proximity.ProbeFunc;

/**
 * @author Garrett Smith
 *
 */
public class FeaturesContentProvider implements ITreeContentProvider, ICheckStateProvider  {
  
  private Map<String, Map<ProbeFunc<Integer>, Boolean>> mFuncsMap;

  public void dispose() {
    // do nothing
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    mFuncsMap = (Map<String, Map<ProbeFunc<Integer>, Boolean>>) newInput;
  }

  public Object[] getElements(Object inputElement) {
    return mFuncsMap.keySet().toArray();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
   */
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof String) {
      return mFuncsMap.get((String) parentElement).keySet().toArray();
    }
    else {
      return null;
    }
  }

  public Object getParent(Object element) {
    return null;
  }

  public boolean hasChildren(Object element) {
    if (element instanceof String) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean isChecked(Object element) {
    // check individual feature
    if (element instanceof ProbeFunc<?>) {
      for (Map<ProbeFunc<Integer>, Boolean> funcs : mFuncsMap.values()) {
        if (funcs.containsKey(element)) {
          return funcs.get(element);
        }
      }
    }
    // check categories
    else if (element instanceof String) {
      boolean rtn = true;
      Map<ProbeFunc<Integer>, Boolean> map = mFuncsMap.get(element);
      if (map != null) {
        for (Boolean enabled : map.values()) {
          rtn &= enabled;
        }
        return rtn;
      }
    }
    // otherwise false
    return false;
  }

  public boolean isGrayed(Object element) {
    return false;
  }

}
