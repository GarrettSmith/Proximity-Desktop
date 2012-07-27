/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.ImageCanvas;
import ca.uwinnipeg.proximity.desktop.MainController;

/**
 * A tool provides an action to enable the tool and registers listeners to perform actions.
 * @author Garrett Smith
 *
 */
public abstract class Tool {  
  
  public interface ToolHost {
    public ImageCanvas getCanvas();    
    public MainController getController();
    public Image getImage();
  }
  
  public static final Color DRAG_COLOR = new Color(Display.getCurrent(), 0, 0, 0);

  /**
   * The map of event types to even listeners this tool provides.
   */
  private Map<Integer, Listener> mListeners;
  
  private ToolHost mHost;
  
  private boolean registered = false;

  public boolean isRegistered() {
    return registered;
  }
  
  public Tool(ToolHost host) {
    mHost = host;
    mListeners = createListeners(new HashMap<Integer, Listener>());
  }
  
  /**
   * Creates the listeners used by the tool to perform its actions.
   * @param map
   * @return
   */
  protected abstract HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map);
  
  /**
   * Register all the tool's listeners to the canvas.
   */
  public void addListeners() {
    registered = true;
    ImageCanvas canvas = getCanvas();
    for (Entry<Integer, Listener> e : mListeners.entrySet()) {
      canvas.addListener(e.getKey(), e.getValue());
    }
  }
  
  /**
   * Unregister from receiving events.
   */
  public void removeListeners() {
    registered = false;
    ImageCanvas canvas = getCanvas();
    for (Entry<Integer, Listener> e : mListeners.entrySet()) {
      canvas.removeListener(e.getKey(), e.getValue());
    }
  }
  
  public ImageCanvas getCanvas() {
    return mHost.getCanvas();
  }
  
  public MainController getController() {
    return mHost.getController();
  }
  
  public Image getImage() {
    return mHost.getImage();
  }

}
