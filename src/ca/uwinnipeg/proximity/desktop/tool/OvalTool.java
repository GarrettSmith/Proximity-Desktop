/**
 * 
 */
package ca.uwinnipeg.proximity.desktop.tool;

import ca.uwinnipeg.proximity.desktop.Region;

/**
 * @author Garrett Smith
 *
 */
public class OvalTool extends SimpleRegionTool {

  public OvalTool(ToolHost host) {
    super(host, "MainWindow.action.text_1", "oval.png", Region.Shape.OVAL);
  }

}
