/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;

import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.desktop.features.Category;
import ca.uwinnipeg.proximity.desktop.features.FeaturesCheckedListener;
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;
import ca.uwinnipeg.proximity.image.AlphaFunc;
import ca.uwinnipeg.proximity.image.BlueFunc;
import ca.uwinnipeg.proximity.image.DifferentialExcitationFunc;
import ca.uwinnipeg.proximity.image.GreenFunc;
import ca.uwinnipeg.proximity.image.HomogeneityFunc;
import ca.uwinnipeg.proximity.image.Image;
import ca.uwinnipeg.proximity.image.ImageFunc;
import ca.uwinnipeg.proximity.image.PerceptualGrayScaleFunc;
import ca.uwinnipeg.proximity.image.RedFunc;

/**
 * The main controller of the application, maintains all the {@link PropertyController}s and the 
 * {@link Image} perceptual system.
 * @author Garrett Smith
 *
 */
public class ProximityController {
  
  private Image mImage = new Image();
  
  public Image getImage() {
    return mImage;
  }
  
  private List<Region> mRegions = new ArrayList<Region>();
  private List<Region> mSelectedRegions = new ArrayList<Region>();
  
  private Deque<HistoryAction> mUndoStack = new ArrayDeque<HistoryAction>();
  private Deque<HistoryAction> mRedoStack = new ArrayDeque<HistoryAction>();
  
  private Map<Class<? extends PropertyController<?>>, PropertyController<?>> mPropertyControllers = 
      new HashMap<Class<? extends PropertyController<?>>, PropertyController<?>>();
  
  @SuppressWarnings("unchecked")
  private static final Class<PropertyController<?>>[] PROPERTY_CONTROLLER_CLASSES = 
      (Class<PropertyController<?>>[]) new Class<?>[] {
          NeighbourhoodController.class,
          IntersectionController.class,
          ComplimentController.class,
          DifferenceController.class,
          UpperApprox.class
      };
  
  private Preferences mFuncPrefs = Preferences.userRoot().node("proximity-system").node("probe-funcs");
  private Preferences mCatPrefs = mFuncPrefs.node("categories");
  private Preferences mClassPrefs = mFuncPrefs.node("class-files");
  
  @SuppressWarnings("unchecked")
  private static final Class<ImageFunc>[] COLOR_FUNCS = 
      (Class<ImageFunc>[]) new Class<?>[] {
        AlphaFunc.class,
        RedFunc.class,
        GreenFunc.class,
        BlueFunc.class,
        PerceptualGrayScaleFunc.class,
      };
  
  @SuppressWarnings("unchecked")
  private static final Class<ImageFunc>[] EDGE_FUNCS = 
      (Class<ImageFunc>[]) new Class<?>[] {
        DifferentialExcitationFunc.class
       };  
  
  @SuppressWarnings("unchecked")
  private static final Class<ImageFunc>[] TEXTURE_FUNCS = 
      (Class<ImageFunc>[]) new Class<?>[] {
        HomogeneityFunc.class
       };
  
  private static final Class<?>[][] FUNCS = {
    COLOR_FUNCS,
    EDGE_FUNCS,
    TEXTURE_FUNCS
  };
  
  private static final String COLOR_CATEGORY = "Colours";
  private static final String EDGE_CATEGORY = "Edge";
  private static final String TEXTURE_CATEGORY = "Texture";
  
  private static final String[] CATEGORIES = {
    COLOR_CATEGORY, 
    EDGE_CATEGORY, 
    TEXTURE_CATEGORY
  };
  
  private Map<String, Category> mCategories = new HashMap<String, Category>();
  
  private ICheckStateListener mCheckStateListener = new FeaturesCheckedListener(this);
  
  public ICheckStateListener getCheckStateListener() {
    return mCheckStateListener;
  }
  
  public ProximityController() {
    loadFuncs();
    createPropertyControllers();
  }
  
  /**
   * Loads the initial probe functions and their enabled status.
   */
  @SuppressWarnings("unchecked")
  private void loadFuncs() {
    // load the default probe functions and enable
    for (int i = 0; i < FUNCS.length; i++) {
      for (Class<?> clazz : FUNCS[i]) {
        Class<ImageFunc> funcClazz = (Class<ImageFunc>) clazz;
        String className = funcClazz.getName();
        mFuncPrefs.putBoolean(className, mFuncPrefs.getBoolean(className, true));
        mCatPrefs.put(className, CATEGORIES[i]);
      }
    }

    // load the previously loaded probe funcs
    try {
      
      Set<URL> classURLs = new HashSet<URL>();
      for (String clazz: mClassPrefs.keys()) {
        String path = mClassPrefs.get(clazz, null);
        if (path != null) {
          File f = new File(path);
          URL url = null;
          try {
            url = f.toURI().toURL();
          } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          if (url != null) classURLs.add(url);
        }
      }

      // get the correct class loader
      ClassLoader loader = 
          new URLClassLoader(
              classURLs.toArray(new URL[classURLs.size()]), 
              ClassLoader.getSystemClassLoader());
      
      for (String className : mFuncPrefs.keys()) {
        try {

          // load the probe func
          Class<ImageFunc> funcClazz;
          funcClazz = (Class<ImageFunc>) Class.forName(className, true, loader);

          String categoryStr = mCatPrefs.get(className, "Uncategorized");
          Category category = null;

          // find the matching category
          category = mCategories.get(categoryStr);

          // create the category if we haven't yet
          if (category == null) {
            category = new Category(categoryStr);
            mCategories.put(categoryStr, category);
          }

          // add the func to the category
          category.set(funcClazz.newInstance(), mFuncPrefs.getBoolean(className, false));
        } catch (InstantiationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (ClassNotFoundException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    } catch (BackingStoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // add enabled probe funcs
    for (Category cat: mCategories.values()) {
      for (ImageFunc func: cat.getEnabledProbeFuncs()) {
        mImage.addProbeFunc(func);
      }
    }
  }

  /**
   * Creates the initial property controllers.
   */
  @SuppressWarnings("unchecked")
private void createPropertyControllers() {
    for (Class<PropertyController<?>> clazz : PROPERTY_CONTROLLER_CLASSES) {
      try {
        PropertyController<?> pc = clazz.newInstance();
        pc.setup(mImage);
        mPropertyControllers.put((Class<? extends PropertyController<?>>) pc.getClass(), pc);
        
      } catch (InstantiationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Returns the instantiated {@link PropertyController} of the given class if it exists.
   * @param key
   * @return
   */
  public PropertyController<?> getPropertyController(Class<? extends PropertyController<?>> key) {
    return mPropertyControllers.get(key);
  }
  
  /**
   * Returns all the categories of features loaded.
   * @return
   */
  public Collection<Category> getCategories() {
    return mCategories.values();
  }
  
  /**
   * Adds a list of {@link ProbeFunc}s under the given category.
   * @param categoryName the name of the category to add to
   * @param funcs the list of funcs
   * @param path the file system path to the class files so the funcs can be reloaded
   */
  // TODO: prevent duplicates
  public void addProbeFuncs(String categoryName, List<ImageFunc> funcs, String path) {
    // find the given category
    Category cat = mCategories.get(categoryName);
    // create the category if it is new
    if (cat == null) {
      cat = new Category(categoryName);
      mCategories.put(categoryName, cat);
    }
    // add the funcs to the category
    for (ImageFunc f: funcs) {
      cat.set(f, false);
      // save the funcs to the prefs
      String className = f.getClass().getName();
      mFuncPrefs.putBoolean(className, false);
      mCatPrefs.put(className, categoryName);
      mClassPrefs.put(className, path);
    }
    // update the tree in the window
    ProximityDesktop.getApp().refreshFeaturesTree();
  }
  
  /**
   * Removes probe funcs from the system.
   * @param func
   */
  // TODO: prevent removing default funcs
  public void removeProbeFuncs(ImageFunc func) {
    for (Category cat: mCategories.values()) {
      cat.remove(func);
      // remove category if it is empty
      if (cat.isEmpty()) {
        mCategories.remove(cat.getName());
      }
      // update preferences
      String className = func.getClass().getName();
      mFuncPrefs.remove(className);
      mCatPrefs.remove(className);
      mClassPrefs.remove(className);
    }
    // remove from the system
    mImage.removeProbeFunc(func);
    // update the tree in the window
    ProximityDesktop.getApp().refreshFeaturesTree();
  }
  
  /**
   * Removes a category of {@link ProbeFunc}s.
   * @param category
   */
  // TODO: prevent removing default categories
  public void removeCategory(Category category) {
    // remove from categories
    mCategories.remove(category.getName());
    // remove all the child funcs
    for (ImageFunc f: category.getProbeFuncs()) {
      String className = f.getClass().getName();
      mFuncPrefs.remove(className);
      mCatPrefs.remove(className);
      mClassPrefs.remove(className);
    }
    // update the tree in the window
    ProximityDesktop.getApp().refreshFeaturesTree();
  }

  /**
   * Returns the number of {@link ProbeFunc}s loaded.
   * @return
   */
  public int probeFuncsSize() {
    int sum = 0;
    for (Category cat : mCategories.values()) {
      sum += cat.size();
    }
    return sum;
  }

  /**
   * Returns the number of enabled {@link ProbeFunc}s.
   * @return
   */
  public int enabledProbeFuncsSize() {
    int sum = 0;
    for (Category cat : mCategories.values()) {
      sum += cat.enabledSize();
    }
    return sum;
  }
  
  /**
   * Enables or disables the given {@link ProbeFunc}.
   * @param func
   * @param enabled
   */
  public void setProbeFuncEnabled(ImageFunc func, boolean enabled) {
    // set the probe func's state
    for (Category cat: mCategories.values()) {
      if (cat.contains(func)) {
        cat.set(func, enabled);
      }
    }
    // save the status
    mFuncPrefs.putBoolean(func.getClass().getName(), enabled);
    if (enabled) {
      mImage.addProbeFunc(func);
    }
    else {
      mImage.removeProbeFunc(func);
    }
    for (PropertyController<?> pc : mPropertyControllers.values()) {
      pc.onProbeFuncsChanged();
    }
  }

  /**
   * Reutrns the maximum possible epsilon value. This is determined by the number of 
   * {@link ProbeFunc}s that have been enabled.
   * @return
   */
  public float getEpsilonMaximum() {
    return (float) mImage.getNorm();
  }
  
  /**
   * Enables or disables an entire category of {@link ProbeFunc}s.
   * @param category
   * @param enabled
   */
  public void setCategoryEnabled(Category category, boolean enabled) {
    for (ImageFunc func: category.getProbeFuncs()) {
      setProbeFuncEnabled(func, enabled);
    }
  }
  
  /**
   * Sets up the image data into the image.
   * @param data
   */
  public void onImageSelected(ImageData data) {
    int[] pixels = new int[data.width * data.height];
    data.getPixels(0, 0, pixels.length, pixels, 0);
    mImage.set(pixels, data.width, data.height);
    // clear the region
    reset();
  }
  
  /**
   * Resets the history stacks and removes all regions. This is called when a new image is opened.
   */
  public void reset() {
    mUndoStack.clear();
    mRedoStack.clear();
    // clear the regions and tell the property controllers.
    mRegions.clear();
    for (PropertyController<?> pc : mPropertyControllers.values()) {
      pc.clearRegions();
    }
  }
  
  /**
   * Returns all the regions added to the image.
   * @return
   */
  public List<Region> getRegions() {
    return new ArrayList<Region>(mRegions);
  }
  
  /**
   * Adds a region to the system. 
   * <p>
   * This should only be called directly from a history action in order
   * for the action to be added to the history stack allowing the user to undo and redo the action.
   * @param region
   */
  public void addRegion(Region region) {
    mRegions.add(region);
    // update property controllers
    for (PropertyController<?> pc : mPropertyControllers.values()) {
      pc.addRegion(region);
    }
  }
  
  /**
   * Removes a region from the system.
   * <p>
   * This should only be called directly from a history action in order
   * for the action to be added to the history stack allowing the user to undo and redo the action.
   * @param region
   */
  public void removeRegion(Region region) {
    mRegions.remove(region);
    // notify property controllers
    for (PropertyController<?> pc : mPropertyControllers.values()) {
      pc.removeRegion(region);
    }
  }
  
  /**
   * Notifies the {@link PropertyController}s that the given regions have been modified and that
   * new results will need to be calculated.
   * @param regions
   */
  public void regionsModified(List<Region> regions) {
    for (PropertyController<?> pc: mPropertyControllers.values()) {
      pc.regionsModified(regions);
    }
  }
  
  /**
   * Sets the neighbourhood of the given region for all the property controllers.
   * @param region
   * @param neighbourhood
   */
  public void setNeighbourhood(Region region, List<Integer> neighbourhood) {
    // notify property controllers
    for (PropertyController<?> pc : mPropertyControllers.values()) {
      pc.setNeighbourhood(region, neighbourhood);
    }
  }
  
  /**
   * Sets the given region to be the currently selected region.
   * @param r
   */
  public void setSelected(Region r) {
    mSelectedRegions.clear();
    if (r != null) {
      mSelectedRegions.add(r);
    }
    else {
      mSelectedRegions.clear();
    }
    ProximityDesktop.getApp().updateSelectionActions();
    ProximityDesktop.getApp().getCanvas().redraw();
  }
  
  /**
   * Sets the list of regions to be the currently selected regions.
   * @param regs
   */
  public void setSelected(List<Region> regs) {
    mSelectedRegions.clear();
    if (regs != null) {
      mSelectedRegions.addAll(regs);
    }
    else {
      mSelectedRegions.clear();
    }
    ProximityDesktop.getApp().updateSelectionActions();
    ProximityDesktop.getApp().getCanvas().redraw();
  }
  
  /**
   * Returns a list of the currently selected regions.
   * @return
   */
  public List<Region> getSelectedRegions() {
    return new ArrayList<Region>(mSelectedRegions);
  }
  
  /**
   * Returns the bounds of the current selection of regions. This is a rectangle that contains all
   * the selected regions.
   * @return
   */
  public Rectangle getSelectionBounds() {
    Rectangle bounds = null;
    for (Region r: mSelectedRegions) {
      if (bounds == null) {
        bounds = r.getBounds();
      }
      else {
        bounds = bounds.union(r.getBounds());
      }
    }
    return bounds;
  }
  
  /**
   * Sets the epsilon of the {@link PropertyController} of the given class to the given value.
   * @param key
   * @param epsilon
   */
  public void setEpsilon(Class<? extends PropertyController<?>> key, float epsilon) {
    mPropertyControllers.get(key).setEpsilon(epsilon);
  }
  
  /**
   * Sets whether the {@link PropertyController} of the given class should use neighbourhoods to 
   * calculate results.
   * @param key
   * @param enabled
   */
  public void setUseNeighbourhoods(Class<? extends PropertyController<?>> key, boolean enabled) {
    mPropertyControllers.get(key).setUseNeighbourhoods(enabled);
  }

  /**
   * Applies a {@link HistoryAction} adds it to the undo stack and clears the redo stack.
   * @param action
   */
  public void performAction(HistoryAction action) {
    action.apply();
    mUndoStack.push(action);
    mRedoStack.clear();
    
    updateSelectedRegions();
    
    ProximityDesktop app = ProximityDesktop.getApp();
    app.updateHistoryActions();
    app.updateSelectionActions();
    app.getCanvas().redraw();
  }
  
  /**
   * Culls out all regions from the selection that are no longer part of the system. That is they 
   * have been deleted in some way.
   */
  private void updateSelectedRegions() {
    // get rid of regions that have been removed
    List<Region> newSelected = new ArrayList<Region>();
    for (Region r: mSelectedRegions) {
      if (mRegions.contains(r)) {
        newSelected.add(r);
      }
    }
    mSelectedRegions = newSelected;
  }
  
  /**
   * Check if there are actions that can be undone.
   * @return
   */
  public boolean getUndo() {
    return !mUndoStack.isEmpty();
  }
  
  /**
   * Check if there are actions that can be redone.
   * @return
   */
  public boolean getRedo() {
    return !mRedoStack.isEmpty();
  }
  
  /**
   * Get the name of the next action to be undone.
   * @return
   */
  public String getUndoString() {
    StringBuffer str = new StringBuffer();
    str.append("Undo ");
    HistoryAction action = mUndoStack.peek();
    if (action != null) {
      str.append(action.getName());
    }
    return str.toString();
  }
  
  /**
   * Get the name of the next action to be redone.
   * @return
   */
  public String getRedoString() {
    StringBuffer str = new StringBuffer();
    str.append("Redo ");
    HistoryAction action = mRedoStack.peek();
    if (action != null) {
      str.append(action.getName());
    }
    return str.toString();
  }
  
  /**
   * Unapply the last {@link HistoryAction}, remove it from the undo stack and adds the action to 
   * the redo stack.
   */
  public void undo() {
    HistoryAction action = mUndoStack.pop();
    action.unapply();
    mRedoStack.push(action);
  }
  
  /**
   * Reapply the next next {@link HistoryAction}, remove it from the redo stack and add it to the 
   * undo stack.
   */
  public void redo() {
    HistoryAction action = mRedoStack.pop();
    action.apply();
    mUndoStack.push(action);
  }
  
  /**
   * Perform closing operations.
   */
  public void onClose() {
    // Nothing here at the moment, maybe save current state one day.
  }
  
}
