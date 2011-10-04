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
	public void scaleConverter_logic0x0_to_physical() {
		
		Pair p = new Pair(0.0, 0.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(100.0, 100.0)
			.screenSize(50.0, 30.0)
			.toScreenCoordinate(p);
		
		assertEquals(25.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(15.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad1 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void scaleConverter_logicQuad1_to_physical() {
		
		Pair p = new Pair(5.0, 5.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(10.0, 10.0)
			.screenSize(20.0, 20.0)
			.toScreenCoordinate(p);
		
		assertEquals(15.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(5.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad2 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void scaleConverter_logicQuad2_to_physical() {
		
		Pair p = new Pair(-4.0, 5.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(10.0, 10.0)
			.screenSize(20.0, 20.0)
			.toScreenCoordinate(p);
		
		assertEquals(6.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(5.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad3 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void scaleConverter_logicQuad3_to_physical() {
		
		Pair p = new Pair(-1.0, -1.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(10.0, 10.0)
			.screenSize(20.0, 20.0)
			.toScreenCoordinate(p);
		
		assertEquals(9.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(11.0, p2.y, DBL_COMPARE_DELTA);
	}
	
	/**
	 * Converts a logical point from quad4 to a screen coordinate.
	 * It should be located at the proportional position of the screen.
	 */
	@Test
	public void scaleConverter_logicQuad4_to_physical() {
		
		Pair p = new Pair(3.0, -2.0);
		
		CoordinatesConverter conv = new CoordinatesConverter();
		Pair p2 = conv.logicalBounds(10.0, 10.0)
			.screenSize(20.0, 20.0)
			.toScreenCoordinate(p);
		
		assertEquals(13.0, p2.x, DBL_COMPARE_DELTA);
		assertEquals(12.0, p2.y, DBL_COMPARE_DELTA);
	}
}
