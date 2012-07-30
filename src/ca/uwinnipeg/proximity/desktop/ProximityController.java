/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import ca.uwinnipeg.proximity.PerceptualSystem.PerceptualSystemSubscriber;
import ca.uwinnipeg.proximity.ProbeFunc;
import ca.uwinnipeg.proximity.desktop.history.AddRegionAction;
import ca.uwinnipeg.proximity.desktop.history.HistoryAction;
import ca.uwinnipeg.proximity.desktop.history.RemoveRegionAction;
import ca.uwinnipeg.proximity.image.AlphaFunc;
import ca.uwinnipeg.proximity.image.BlueFunc;
import ca.uwinnipeg.proximity.image.GreenFunc;
import ca.uwinnipeg.proximity.image.Image;
import ca.uwinnipeg.proximity.image.RedFunc;


/**
 * The main controller of the application.
 * @author Garrett Smith
 *
 */
// TODO: update undo and redo to window
public class ProximityController {
  
  private Image mImage = new Image();
  
  private List<Region> mRegions = new ArrayList<Region>();
  private List<Region> selectedRegions = new ArrayList<Region>();
  
  private Deque<HistoryAction> mUndoStack = new ArrayDeque<HistoryAction>();
  private Deque<HistoryAction> mRedoStack = new ArrayDeque<HistoryAction>();
  
  private List<PropertyController> mPropertyControllers = new ArrayList<PropertyController>();
  
  @SuppressWarnings("unchecked")
  private static final Class<PropertyController>[] PROPERTY_CONTROLLER_CLASSES = 
      (Class<PropertyController>[]) new Class<?>[] {
          NeighbourhoodController.class,
          IntersectionController.class,
          ComplimentController.class
      };
  
  private Preferences mFuncPrefs = Preferences.userRoot().node("proximity-system").node("probe-funcs");
  
  @SuppressWarnings("unchecked")
  private static final Class<ProbeFunc<Integer>>[] DEFAULT_FUNCS = 
      (Class<ProbeFunc<Integer>>[]) new Class<?>[] {
        AlphaFunc.class,
        RedFunc.class,
        GreenFunc.class,
        BlueFunc.class
      };
  
  private Map<ProbeFunc<Integer>, Boolean> mProbeFuncs = new HashMap<ProbeFunc<Integer>, Boolean>();
  
  public ProximityController() {
    loadFuncs();
    createPropertyControllers();
  }
  
  private void loadFuncs() {
    try {
      // load the default probe functions and enable
      for (Class<ProbeFunc<Integer>> clazz : DEFAULT_FUNCS) {
//        mProbeFuncs.put(clazz.newInstance(), true);
        String className = clazz.getName();
        mFuncPrefs.putBoolean(className, mFuncPrefs.getBoolean(className, true));
      }
      // load the previously loaded probe funcs
      for (String classStr : mFuncPrefs.keys()) {
        Class clazz = Class.forName(classStr);
        Class<ProbeFunc<Integer>> funcClazz = (Class<ProbeFunc<Integer>>) clazz;
        mProbeFuncs.put(funcClazz.newInstance(), mFuncPrefs.getBoolean(classStr, false));
      }
    } 
    catch (BackingStoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    // add enabled probe funcs
    for (Entry<ProbeFunc<Integer>, Boolean> entry : mProbeFuncs.entrySet()) {
      if (entry.getValue()) {
        mImage.addProbeFunc(entry.getKey());
      }
    }
  }
  
  private void createPropertyControllers() {
    for (Class<PropertyController> clazz : PROPERTY_CONTROLLER_CLASSES) {
      try {
        PropertyController pc = clazz.newInstance();
        pc.setup(mImage);
        mPropertyControllers.add(pc);
        
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
  
  public void reset() {
    mUndoStack.clear();
    mRedoStack.clear();
    mRegions.clear();
    for (PropertyController pc : mPropertyControllers) {
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
   * Adds a region to the image.
   * @param shape
   * @param points
   */
  public void addRegionAction(Region.Shape shape, List<Point> points) {
    Region reg = new Region(mImage);
    reg.setShape(shape);
    
    if (shape == Region.Shape.POLYGON) {
      for (Point p : points) {
        reg.addPoint(p.x, p.y);
      }
    }
    // add non-polygon shape
    else {
      Point p1 = points.get(0);
      Point p2 = points.get(1);
      reg.setBounds(
          new Rectangle(
          Math.min(p1.x, p2.x),
          Math.min(p1.y, p2.y),
          Math.abs(p1.x - p2.x),
          Math.abs(p1.y - p2.y)));
    }
    
    // select the newly added region
    setSelected(reg);
    
    performAction(new AddRegionAction(reg, this));
  }

  /**
   * Adds a region to the image.
   * @param shape
   * @param points
   */
  public void addRegionsAction(List<Region> regs) {    
    // select the newly added regions
    setSelected(regs);
    
    performAction(new AddRegionAction(regs, this));
  }
  
  public void addRegion(Region region) {
    mRegions.add(region);
    // update property controllers
    for (PropertyController pc : mPropertyControllers) {
      pc.addRegion(region);
    }
  }
  
  /**
   * Removes a region from the image.
   * @param region
   */
  public void removeRegionAction(Region region) {
    performAction(new RemoveRegionAction(region, this));
  }
  
  public void removeRegionsAction(List<Region> regions) {
    performAction(new RemoveRegionAction(regions, this));
  }
  
  public void removeRegion(Region region) {
    mRegions.remove(region);
    // notify property controllers
    for (PropertyController pc : mPropertyControllers) {
      pc.removeRegion(region);
    }
  }
  
  public void setSelected(Region r) {
    selectedRegions.clear();
    if (r != null) {
      selectedRegions.add(r);
    }
    ProximityDesktop.getApp().updateSelectionActions();
    ProximityDesktop.getApp().getCanvas().redraw();
  }
  
  public void setSelected(List<Region> regs) {
    selectedRegions.clear();
    if (regs != null) {
      selectedRegions.addAll(regs);
    }
    ProximityDesktop.getApp().updateSelectionActions();
    ProximityDesktop.getApp().getCanvas().redraw();
  }
  
  public List<Region> getSelectedRegions() {
    return new ArrayList<Region>(selectedRegions);
  }

  /**
   * Applies a {@link HistoryAction} adds it to the undo stack and clears the redo stack.
   * @param action
   */
  protected void performAction(HistoryAction action) {
    action.apply();
    mUndoStack.push(action);
    mRedoStack.clear();
    
    updateSelectedRegions();
    
    ProximityDesktop app = ProximityDesktop.getApp();
    app.updateHistoryActions();
    app.updateSelectionActions();
    app.getCanvas().redraw();
  }
  
  private void updateSelectedRegions() {
    // get rid of regions that have been removed
    List<Region> newSelected = new ArrayList<Region>();
    for (Region r: selectedRegions) {
      if (mRegions.contains(r)) {
        newSelected.add(r);
      }
    }
    selectedRegions = newSelected;
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
  
  public List<Integer> getNeighbourhood(Region reg) {
    PerceptualSystemSubscriber sub = new PerceptualSystemSubscriber() {

      public void onProgressSet(float progress) {
        // TODO Auto-generated method stub
        
      }

      public boolean isCancelled() {
        // TODO Auto-generated method stub
        return false;
      }
    };
    return mImage.hybridNeighbourhood(reg.getCenterIndex(), reg.getIndicesList(), 0.3f, sub);
  }
  
  /**
   * Perform closing operations.
   */
  public void onClose() {
    
  }
  
}
