/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import ca.uwinnipeg.proximity.desktop.Region;

/**
 * @author Garrett Smith
 *
 */
public class RectangleTool extends SimpleRegionTool {

  public RectangleTool(ToolHost host) {
    super(host, "MainWindow.actnRectangle.text", "rect.png", Region.Shape.RECTANGLE);
  }

}
