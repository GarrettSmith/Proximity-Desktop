/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.ResourceManager;

import ca.uwinnipeg.proximity.desktop.ImageCanvas;
import ca.uwinnipeg.proximity.desktop.MainController;
import ca.uwinnipeg.proximity.desktop.MainWindow;

/**
 * A tool provides an action to enable the tool and registers listeners to perform actions.
 * @author Garrett Smith
 *
 */
public abstract class Tool {
  private static final ResourceBundle BUNDLE = 
      ResourceBundle.getBundle("ca.uwinnipeg.proximity.desktop.strings.messages");
  
  private static final String ICON_PATH = "/ca/uwinnipeg/proximity/desktop/icons/";
  
  public static final Color DRAG_COLOR = new Color(Display.getCurrent(), 0, 0, 0);

  /**
   * The action used to activate this tool.
   */
  private ToolAction mAction;

  /**
   * The map of event types to even listeners this tool provides.
   */
  private Map<Integer, Listener> mListeners;
  
  private ToolHost mHost;
  
  private boolean mRegistered = false;

  /**
   * An action used to activate a tool.
   * @author Garrett Smith
   *
   */
  public class ToolAction extends Action {

    public ToolAction(String label, String icon) {
      super(BUNDLE.getString(label), Action.AS_RADIO_BUTTON);
      setImageDescriptor(ResourceManager.getImageDescriptor(MainWindow.class, ICON_PATH + icon));
    }

    @Override
    public void run() {
      if (mRegistered != isChecked()) {
        // register and unregister from events
        if (isChecked()) {
          addListeners();
        }
        else {
          removeListeners();
        }
      }
    }
  }
  
  public Tool(ToolHost host, String label, String icon) {
    mHost = host;
    mAction = new ToolAction(label, icon);
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
    mRegistered = true;
    ImageCanvas canvas = getCanvas();
    for (Entry<Integer, Listener> e : mListeners.entrySet()) {
      canvas.addListener(e.getKey(), e.getValue());
    }
  }
  
  /**
   * Unregister from receiving events.
   */
  public void removeListeners() {
    mRegistered = false;
    ImageCanvas canvas = getCanvas();
    for (Entry<Integer, Listener> e : mListeners.entrySet()) {
      canvas.removeListener(e.getKey(), e.getValue());
    }
  }
  
  public ToolAction getAction() {
    return mAction;
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
  
  public interface ToolHost {
    public ImageCanvas getCanvas();    
    public MainController getController();
    public Image getImage();
  }
  
  public abstract class DragToolListener implements Listener {
    
    public void register(Map<Integer, Listener> map) {
      map.put(SWT.MouseMove, this);
      map.put(SWT.MouseDown, this);
      map.put(SWT.MouseUp, this);
      map.put(SWT.KeyDown, this);
      map.put(SWT.Paint, this);
    }

    // route events
    public void handleEvent(Event event) {
      switch(event.type) {
        case SWT.MouseDown:
          mouseDown(event);
          break;
        case SWT.MouseUp:
          mouseUp(event);
          break;
        case SWT.MouseMove:
          mouseMove(event);
          break;
        case SWT.KeyDown:
          keyDown(event);
          break;
        case SWT.Paint:
          if (mImageStartPoint != null) {
            paint(
                event, 
                mImageStartPoint, 
                mImageCurrentPoint, 
                mScreenStartPoint, 
                mScreenCurrentPoint);
          }
          break;
      }
    }
    
    private Point mImageStartPoint;
    private Point mImageCurrentPoint;

    private Point mScreenStartPoint;
    private Point mScreenCurrentPoint;
    
    public void keyDown(Event event) {
      switch(event.keyCode) {
        // stop action on esc pressed
        case SWT.ESC: 
          mScreenStartPoint = mImageStartPoint = null;
          getCanvas().redraw();
          break;
      }
    }
    
    public void mouseDown(Event event) {
      // if we are pressing the first button inside the image
      if (event.button == 1 && getCanvas().contains(mImageCurrentPoint)) {
        mScreenStartPoint = mScreenCurrentPoint;
        mImageStartPoint = mImageCurrentPoint;
      }
    }
    
    public void mouseUp(Event event) {    
      // if the cursor was dragged
      if (mImageStartPoint != null) {      
        
        if (mImageStartPoint.equals(mImageCurrentPoint)) {
          // perform the click action
          onClick(event, mImageCurrentPoint, mScreenCurrentPoint);
        }
        else {
          // perform the drag action
          onDrag(event, mImageStartPoint, mImageCurrentPoint, mImageStartPoint, mScreenCurrentPoint);
        }

        // redraw to clear box
        getCanvas().redraw();
      }
      // clear the start point
      mImageStartPoint = mScreenStartPoint = null;
    }
    
    public void mouseMove(Event event) {
      ImageCanvas canvas = getCanvas();
      Image image = getImage();
      
      mScreenCurrentPoint = new Point(event.x, event.y);
      mImageCurrentPoint = canvas.toImageSpace(mScreenCurrentPoint);

      // check if the mouse has been clicked and we should draw something
      if (mImageStartPoint != null) {
        Rectangle imageBounds = image.getBounds();
        Rectangle imageScreenBounds = canvas.toScreenSpace(imageBounds);
        
        //limit points to be within image bounds
        mImageCurrentPoint.x = Math.max(0, mImageCurrentPoint.x);
        mImageCurrentPoint.x = Math.min(imageBounds.width, mImageCurrentPoint.x);        
        mImageCurrentPoint.y = Math.max(0, mImageCurrentPoint.y);
        mImageCurrentPoint.y = Math.min(imageBounds.height, mImageCurrentPoint.y);
        
        mScreenCurrentPoint.x = Math.max(imageScreenBounds.x, mScreenCurrentPoint.x);
        mScreenCurrentPoint.x = Math.min(imageScreenBounds.x + imageScreenBounds.width, mScreenCurrentPoint.x);        
        mScreenCurrentPoint.y = Math.max(imageScreenBounds.y, mScreenCurrentPoint.y);
        mScreenCurrentPoint.y = Math.min(imageScreenBounds.y + imageScreenBounds.height, mScreenCurrentPoint.y);

        canvas.redraw();
      }
    }
    
    public abstract void paint(
        Event event, 
        Point imageStart, 
        Point imageEnd,
        Point screenStart, 
        Point screenEnd);
    
    public abstract void onClick(
        Event event, 
        Point image, 
        Point screen);
    
    public abstract void onDrag(
        Event event, 
        Point imageStart, 
        Point imageEnd, 
        Point screenStart, 
        Point screenEnd);
    
  };

}
