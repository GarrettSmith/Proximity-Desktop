/**
 * 
 */
package ca.uwinnipeg.proximity.desktop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Garrett Smith
 *
 */
public abstract class LinearPropertyController extends PropertyController {
  
  ExecutorService executor= Executors.newFixedThreadPool(1);

  /**
   * 
   */
  public LinearPropertyController() {
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see ca.uwinnipeg.proximity.desktop.PropertyController#invalidate()
   */
  @Override
  protected void invalidate() {
    // TODO Auto-generated method stub

  }

}
