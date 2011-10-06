package jrplot.core.geometry;

import static org.junit.Assert.assertEquals;
import jrplot.core.Pair;
import jrplot.core.geometry.CoordinatesConverter;

import org.junit.Test;


public class CoordinateConverterTests {

	private static final double DBL_COMPARE_DELTA = 0.0001;

	/**
	 * Converts the logical point (0,0) to a screen coordinate.
	 * It should be located at the middle of the screen.
	 */
	@Test
	public void origin0x0_symmetric_axis_nopadding() {
		
		Pair p = new Pair(0.0, 0.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(-100.0, 100.0, -100.0, 100.0)
			.screenSize(50.0, 30.0, 0.0)
			.toScreenCoordinate(p);
		
		assertEquals(25.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(15.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts the logical point (0,0) to a screen coordinate.
	 * It should be located at the middle of the screen (the padding
	 * does not affect this, as everything is symmmetric).
	 */
	@Test
	public void origin0x0_symmetric_axis_withpadding() {
		
		Pair p = new Pair(0.0, 0.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(-100.0, 100.0, -100.0, 100.0)
			.screenSize(50.0, 30.0, 5.0)
			.toScreenCoordinate(p);
		
		assertEquals(25.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(15.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad1 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void quad1_to_physical_symmetric_axis_withpadding() {
		
		Pair p = new Pair(5.0, 5.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(-10.0, 10.0, -10.0, 10.0)
			.screenSize(20.0, 20.0, 2.0)
			.toScreenCoordinate(p);
		
		assertEquals(14.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(6.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad2 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void quad2_to_physical_symmetric_axis_withpadding() {
		
		Pair p = new Pair(-4.0, 5.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(-10.0, 10.0, -10.0, 10.0)
			.screenSize(20.0, 20.0, 2.0)
			.toScreenCoordinate(p);
		
		assertEquals(6.8, p2.x, DBL_COMPARE_DELTA);
		assertEquals(6.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad3 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void quad3_to_physical_symmetric_axis_withpadding() {
		
		Pair p = new Pair(-1.0, -1.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(-10.0, 10.0, -10.0, 10.0)
			.screenSize(20.0, 20.0, 3.0)
			.toScreenCoordinate(p);
		
		assertEquals(9.3, p2.x, DBL_COMPARE_DELTA);
		assertEquals(10.7, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad4 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void quad4_to_physical_symmetric_axis_withpadding() {
		
		Pair p = new Pair(3.0, -2.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(-10.0, 10.0, -10.0, 10.0)
			.screenSize(20.0, 20.0, 3.0)
			.toScreenCoordinate(p);
		
		assertEquals(12.1, p2.x, DBL_COMPARE_DELTA);
		assertEquals(11.4, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts the logical point (0,0) to a screen coordinate, with asymmetric
	 * axis (negative and positive bounds have different absolute values)
	 */
	@Test
	public void origin0x0_asymmetric_axis_withpadding() {
		
		Pair p = new Pair(0.0, 0.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(-5, 50, -20, 40)
			.screenSize(50.0, 30.0, 4.0)
			.toScreenCoordinate(p);
		
		assertEquals(7.8181818181818181818181818181818, p2.x, DBL_COMPARE_DELTA);
		assertEquals(18.666666666666666666666666666667, p2.y, DBL_COMPARE_DELTA);
	}
}
