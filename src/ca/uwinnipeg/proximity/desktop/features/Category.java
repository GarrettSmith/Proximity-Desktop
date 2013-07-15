/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.image.ImageFunc;

/**
 * A category of {@link ProbeFunc}s with a name.
 * @author Garrett Smith
 *
 */
public class Category {
  
  private String mName;
  
  private Map<ImageFunc, Boolean> mFuncs = new HashMap<ImageFunc, Boolean>();
 
  /**
   * Creates a new category with the given name.
   */
  public Category(String name) {
    mName = name;
  }

  /**
   * Returns the name of category.
   * @return
   */
  public String getName() {
    return mName;
  }

  /**
   * Sets the name of the category.
   * @param name
   */
  public void setName(String name) {
    mName = name;
  }
  
  /**
   * Sets whether the given {@link ProbeFunc} is enabled and adds it it it was not been added yet.
   * @param func
   * @param enabled
   */
  public void set(ImageFunc func, boolean enabled) {
    mFuncs.put(func, enabled);
  }
  
  /**
   * Removes the given {@link ProbeFunc} from the category.
   * @param func
   */
  public void remove(ImageFunc func) {
    mFuncs.remove(func);
  }
  
  /**
   * Returns whether the current {@link ProbeFunc} is in the category.
   * @param func
   * @return
   */
  public boolean contains(ImageFunc func) {
    return mFuncs.containsKey(func);
  }
  
  /**
   * Returns a set of all the {@link ProbeFunc}s in the category.
   * @return
   */
  public Set<ImageFunc> getProbeFuncs() {
    return mFuncs.keySet();
  }
  
  /**
   * Returns a set of all {@link ProbeFunc} that are enabled.
   * @return
   */
  public Set<ImageFunc> getEnabledProbeFuncs() {
    Set<ImageFunc> funcs = mFuncs.keySet();
    Set<ImageFunc> enabledFuncs = new HashSet<ImageFunc>();
    for (ImageFunc func: funcs) {
      if (isEnabled(func)) {
        enabledFuncs.add(func);
      }
    }
    return enabledFuncs;
  }
  
  /**
   * Returns if the category is empty.
   * @return
   */
  public boolean isEmpty() {
    return mFuncs.isEmpty();
  }
  
  /**
   * Returns if the given {@link ProbeFunc} is enabled.
   * @param func
   * @return
   */
  public boolean isEnabled(ImageFunc func) {
    return mFuncs.get(func);
  }
  
  /**
   * Enable or disable all {@link ProbeFunc}.
   * @param enabled
   */
  public void setEnabled(boolean enabled) {
    for (ImageFunc func: mFuncs.keySet()) {
      mFuncs.put(func, enabled);
    }
  }
  
  /**
   * Returns true if every {@link ProbeFunc} is enabled.
   * @return
   */
  public boolean isEnabled() {
    return !mFuncs.containsValue(false);
  }
  
  /**
   * Returns true if no probe funcs are enabled.
   * @return
   */
  public boolean isDisabled() {
    return !mFuncs.containsValue(true);
  }
  
  /**
   * Returns true the category is not fully enabled or disabled.
   * @return
   */
  public boolean isGrayed() {
    boolean rtn = (isDisabled() == false && isEnabled() == false);
    return rtn;
  }
  
  /**
   * Returns the number {@link ProbeFunc} in the category.
   * @return
   */
  public int size() {
    return mFuncs.size();
  }
  
  /**
   * Returns the number of enabled {@link ProbeFunc}s.
   * @return
   */
  public int enabledSize() {
    int sum = 0;
    for (Boolean enabled: mFuncs.values()) {
      if (enabled) sum++;
    }
    return sum;
  }
}
