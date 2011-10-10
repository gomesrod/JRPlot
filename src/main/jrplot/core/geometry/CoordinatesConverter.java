package jrplot.core.geometry;

import jrplot.core.Pair;



/**
 * Performs conversions between Cartesian coordinate system and the "screen coordinate system".
 *
 * Example: 
 * This class should be able to correctly position the points (A), (B) and (C)
 * so that they keep their positions when moving from one coordinate system to another.
 * 
 * Cartesian coordinate system:
 *      ___________________________
 *      |                         |
 *      |           ^ y+          |
 *      |     (A)   |             |
 *      |           |   (B)       |
 *      |  x-       |        x+   |
 *      |  <------------------>   |
 *      |           |(0,0)        |
 *      |           |             |
 *      |           |             |
 *      |           v y-     (C)  |
 *      |                         |
 *      --------------------------   
 * 
 * "Screen coordinate system"
 *      ______________________________
 *      |  (0,0)                     |
 *      | |----------------->        |
 *      | |    (A)      x+           |
 *      | |               (B)        |
 *      | |                          |
 *      | |                          |
 *      | | y+                       |
 *      | |                    (C)   |
 *      | v                          |
 *      ------------------------------   
 * 
 * 
 * 
 * 
 * @author rodrigo
 *
 */
public class CoordinatesConverter {

	private double logicalXBoundMin;
	private double logicalYBoundMin;
	private double logicalXBoundMax;
	private double logicalYBoundMax;
	private double screenWidth;
	private double screenHeigth;
	private double screenPadding;
	
	/**
	 * Sets the cartesian coordinate bounds. This object will be able to use the space:
	 * 
	 *                ^ maxY
	 *                |
	 *      minX  <-------> maxX
	 *                |
	 *                v maxY
	 * 
	 * A call to this method is required before any calculation, as long as the screen space
	 * is proportional to the total cartesian space.
	 * 
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 * @return
	 */
	public CoordinatesConverter logicalBounds(double minX, double maxX, double minY, double maxY) {
		logicalXBoundMin = minX;
		logicalYBoundMin = minY;
		logicalXBoundMax = maxX;
		logicalYBoundMax = maxY;
		
		return this;
	}

	/**
	 * 
	 * @param width
	 * @param heigth
	 * @param padding
	 * @return
	 */
	public CoordinatesConverter screenSize(double width, double heigth, double padding) {
		screenWidth = width;
		screenHeigth = heigth;
		screenPadding = padding;
		return this;
	}

	public Pair toScreenCoordinate(Pair p) {
	
		/* 
		 * distances are calculated from the "beginning of the axis" to the specified coordinate
		 * 
		 *            distance
		 *       <------------------------->
		 *      |---------------- 0 --------|-------| 
		 *   -xBound                        x       +xBound
		 */
		double xDist = p.x - logicalXBoundMin;
		double yDist = logicalYBoundMax - p.y;
		
		double cartAxisWidth = logicalXBoundMax - logicalXBoundMin;
		double cartAxisHeigth = logicalYBoundMax - logicalYBoundMin;
		
		// Gets the proportional screen distances
		double drawableWidth = screenWidth - (2 * screenPadding);
		double drawableHeigth = screenHeigth - (2 * screenPadding);
		double xScreenDist = ((xDist * drawableWidth) / cartAxisWidth) + screenPadding;
		double yScreenDist = ((yDist * drawableHeigth) / cartAxisHeigth) + screenPadding;
		
		return new Pair(xScreenDist, yScreenDist);
	}

	
	
}
