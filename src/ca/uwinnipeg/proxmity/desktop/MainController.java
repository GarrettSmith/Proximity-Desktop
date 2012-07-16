/**
 * 
 */
package ca.uwinnipeg.proxmity.desktop;

import org.eclipse.swt.graphics.ImageData;

import ca.uwinnipeg.proximity.image.Image;


/**
 * The main controller of the application.
 * @author Garrett Smith
 *
 */
public class MainController {
  
  private Image mImage = new Image();
  
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
   * Perform closing operations.
   */
  public void onClose() {
    
  }
  
}
