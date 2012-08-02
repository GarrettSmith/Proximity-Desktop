/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sun.awt.motif.MPopupMenuPeer;

import ca.uwinnipeg.proximity.ProbeFunc;

/**
 * A category of {@link ProbeFunc}.
 * @author Garrett Smith
 *
 */
public class Category<T> {
  
  private String mName;
  
  private Map<ProbeFunc<T>, Boolean> mFuncs = new HashMap<ProbeFunc<T>, Boolean>();
 
  /**
   * 
   */
  public Category(String name) {
    mName = name;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }
  
  public void set(ProbeFunc<T> func, boolean enabled) {
    mFuncs.put(func, enabled);
  }
  
  public void remove(ProbeFunc<T> func) {
    mFuncs.remove(func);
  }
  
  public boolean contains(ProbeFunc<T> func) {
    return mFuncs.containsKey(func);
  }
  
  public Set<ProbeFunc<T>> getProbeFuncs() {
    return mFuncs.keySet();
  }
  
  public Set<ProbeFunc<T>> getEnabledProbeFuncs() {
    Set<ProbeFunc<T>> funcs = mFuncs.keySet();
    Set<ProbeFunc<T>> enabledFuncs = new HashSet<ProbeFunc<T>>();
    for (ProbeFunc<T> func: funcs) {
      if (isEnabled(func)) {
        enabledFuncs.add(func);
      }
    }
    return enabledFuncs;
  }
  
  public boolean isEmpty() {
    return mFuncs.isEmpty();
  }
  
  public boolean isEnabled(ProbeFunc<T> func) {
    return mFuncs.get(func);
  }
  
  /**
   * Enable or disable all {@link ProbeFunc}.
   * @param enabled
   */
  public void setEnabled(boolean enabled) {
    for (ProbeFunc<T> func: mFuncs.keySet()) {
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
  
  public int size() {
    return mFuncs.size();
  }
  
  public int enabledSize() {
    int sum = 0;
    for (Boolean enabled: mFuncs.values()) {
      if (enabled) sum++;
    }
    return sum;
  }
}
