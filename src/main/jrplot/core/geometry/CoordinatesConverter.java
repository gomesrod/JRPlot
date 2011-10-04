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
	
	/**
	 * Sets the cartesian coordinate bounds. This object will be able to use the space:
	 * 
	 *                ^ yBound
	 *                |
	 *   -xBound  <-------> xBound
	 *                |
	 *                v -yBound
	 * 
	 * A call to this method is required before any calculation, as long as the screen space
	 * are proportional to the total cartesian space.
	 * 
	 * @param xBound
	 * @param yBound
	 * @return
	 */
	public CoordinatesConverter logicalBounds(double xBound, double yBound) {
		logicalXBoundMin = - xBound;
		logicalYBoundMin = - yBound;
		logicalXBoundMax = xBound;
		logicalYBoundMax = yBound;
		
		return this;
	}

	public CoordinatesConverter screenSize(double width, double heigth) {
		screenWidth = width;
		screenHeigth = heigth;
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
		
		double cartAxisWidth = Math.abs(logicalXBoundMin) + Math.abs(logicalXBoundMax);
		double cartAxisHeigth = Math.abs(logicalYBoundMin) + Math.abs(logicalYBoundMax);
		
		// Gets the proportional screen distances
		double xScreenDist = (xDist * screenWidth) / cartAxisWidth;
		double yScreenDist = (yDist * screenHeigth) / cartAxisHeigth;
		
		return new Pair(xScreenDist, yScreenDist);
	}

	
	
}
