/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import ca.uwinnipeg.proximity.image.Image;


/**
 * The main controller of the application.
 * @author Garrett Smith
 *
 */
public class MainController {
  
  private Image mImage = new Image();
  
  private List<Region> mRegions = new ArrayList<Region>();
  
  /**
   * Sets up the image data into the image.
   * @param data
   */
  public void onImageSelected(ImageData data) {
    int[] pixels = new int[data.width * data.height];
    data.getPixels(0, 0, data.width, pixels, 0);
    mImage.set(pixels, data.width, data.height);
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
  public void addRegion(Region.Shape shape, List<Point> points) {
    Region reg = new Region(mImage);
    reg.setShape(shape);
    
    //
    if (shape == Region.Shape.POLYGON) {
      for (Point p : points) {
        reg.addPoint(p.x, p.y);
      }
    }
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
    mRegions.add(reg);
  }
  
  /**
   * Removes a region from the image.
   * @param region
   */
  public void removeRegion(Region region) {
    mRegions.remove(region);
  }
  
  /**
   * Perform closing operations.
   */
  public void onClose() {
    
  }
  
}
