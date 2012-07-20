package ca.uwinnipeg.proximity.desktop.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ca.uwinnipeg.proximity.desktop.ImageCanvas;

public class PolygonTool extends Tool {
  
  public static final Color POINT_COLOR = new Color(Display.getCurrent(), 0, 255, 255);

  public PolygonTool(ToolHost host) {
    super(host, "MainWindow.actnPolygon.text", "poly.png");
  }

  @Override
  protected HashMap<Integer, Listener> createListeners(HashMap<Integer, Listener> map) {
    PolygonListener listener = new PolygonListener();
    map.put(SWT.MouseMove, listener);
    map.put(SWT.MouseDown, listener);
    map.put(SWT.MouseUp, listener);
    map.put(SWT.Paint, listener);
    return map;
  }
  
  class PolygonListener implements Listener {
    
    private List<Point> mPoints = new ArrayList<Point>();
    
    private Point currentPoint;

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
        case SWT.Paint:
          paint(event);
          break;
      }
    }
    
    public void mouseDown(Event event) {
      // make sure the first button was pressed
      if (event.button == 1) {
        Point p = new Point(event.x, event.y);      
        p = getCanvas().toImageSpace(p);
        mPoints.add(p);

        // draw the new point
        getCanvas().redraw();
      }
    }
    
    public void mouseUp(Event event) {
      
    }
    
    public void mouseMove(Event event) {
      currentPoint = new Point(event.x, event.y);
      if (!mPoints.isEmpty()) {
        getCanvas().redraw();
      }
    }
    
    public void paint(Event event) {
      GC gc = event.gc;
      ImageCanvas canvas = getCanvas();
      
      gc.setBackground(POINT_COLOR);
      gc.setForeground(POINT_COLOR);
      gc.setAlpha(255);
      
      // draw the current points
      for (Point p : mPoints) {
        p = canvas.toScreenSpace(p);
        gc.fillRectangle(p.x-4, p.y-4, 8, 8);
      }
      
      if (!mPoints.isEmpty()) {
        // create the outline path
        Path path = new Path(Display.getCurrent());
        Point p = mPoints.get(0);
        p = canvas.toScreenSpace(p);
        path.moveTo(p.x, p.y);
        for (int i = 1; i < mPoints.size(); i++) {
          p = mPoints.get(i);
          p = canvas.toScreenSpace(p);
          path.lineTo(p.x, p.y);
        }
        gc.drawPath(path);        
        
        gc.drawLine(p.x, p.y, currentPoint.x, currentPoint.y);
      }
    }
    
  }

}
