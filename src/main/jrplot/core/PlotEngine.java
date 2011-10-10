package jrplot.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jrplot.core.expression.Expression;
import jrplot.core.expression.ExpressionException;

/**
 * Encapsulates the expression parsing and chart plotting calculations.
 * This should be the only core class acessed by the view components.
 * <br />
 * Thread safety: This class is not threa-safe, if clients are working with different functions.
 * Some values from the last plotted function are stored in each instance.
 * 
 * @author Rodrigo Gomes
 *
 */
public class PlotEngine {

	private String currentFunctionText;
	private Expression currentFunction;
	private PrecisionLevel currentPrecisionX;
	private PrecisionLevel currentPrecisionY;
	
	/**
	 * Keeps a cache of the plotted points, so that the function don't need to be re-evaluated
	 * on each repaint.
	 */
	private List<Pair> currentFunctionPairs;
	
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	
	/**
	 * Retrieve the last plotted function.
	 * @return The (x,y) pairs corresponding to the results of the plotting.
	 */
	public List<Pair> getCurrentFunctionPairs() {
		if (currentFunctionPairs != null) {
			return Collections.unmodifiableList(currentFunctionPairs);
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Plots a new Function.
	 * 
	 * @param expression
	 * @param minX
	 * @param maxX
	 * @throws ExpressionException
	 */
	public void updateFunction(String expression, double minX, double maxX) throws ExpressionException {
		if (expression == null || expression.trim().equals("")) {
			throw new ExpressionException("Expression cannot be empty");
		}
		if (minX >= maxX) {
			throw new ExpressionException("Invalid X interval");
		}
		
		this.currentFunction = Expression.parse(expression);
		this.currentFunctionText = expression;
		
		this.currentFunctionPairs = null; // make the old data eligible for GC

		/*
		 * Plots the function.
		 */
		PrecisionLevel precisionX = new PrecisionLevel(minX, maxX);
		List<Pair> pairs = new ArrayList<Pair>();
		double curMinY = 0.0;
		double curMaxY = 0.0;

		double x = minX;
		while(x <= maxX) {
			double y = currentFunction.evaluate(x);
			if (y < curMinY) curMinY = y;
			if (y > curMaxY) curMaxY = y;
			
			pairs.add(new Pair(x, y));
			x = x + precisionX.xStep;
		}
		
		this.currentFunctionPairs = pairs;
		this.minX = minX <= 0.0 ? minX : 0.0;
		this.maxX = maxX >= 0.0 ? maxX : 0.0;
		this.minY = curMinY;
		this.maxY = curMaxY;
		
		this.currentPrecisionX = new PrecisionLevel(this.minX, this.maxX); // Reassign precision after the adjustement above 
		this.currentPrecisionY = new PrecisionLevel(this.minY, this.maxY);
	}

	public double currentMinX() {
		return this.minX;
	}
	
	public double currentMaxX() {
		return this.maxX;
	}
	
	public double currentMinY() {
		return this.minY;
	}
	
	public double currentMaxY() {
		return this.maxY;
	}
	
	public double scaleIntervalX() {
		return this.currentPrecisionX.scaleInterval;
	}
	
	public double scaleIntervalY() {
		return this.currentPrecisionY.scaleInterval;
	}
	
	public String getCurrentFunctionText() {
		return currentFunctionText;
	}

	/**
	 * Holds the data that correspond to the precision level of the reqested function.
	 * 
	 * The precision level is determined by the plot interval (axis length). Larger intervals
	 * require smaller precision.
	 * 
	 * @author Rodrigo Gomes
	 *
	 */
	private static class PrecisionLevel {
		final double xStep;
		final double scaleInterval;
		
		PrecisionLevel(double min, double max) {
			double range = max - min;
			if (range <= 2) {
				xStep = 0.0001;
				scaleInterval = 0.05;
				
			} else if (range <= 10) {
				xStep = 0.0001;
				scaleInterval = 0.1;
				
			} else if (range <= 100) {
				xStep = 0.0005;
				scaleInterval = 1;
				
			} else if (range <= 1000) {
				xStep = 0.001;
				scaleInterval = 10;
				
			} else if (range <= 10000) {
				xStep = 0.01;
				scaleInterval = 100;
			
			} else {
				xStep = 1;
				scaleInterval = 1000;
			}
		}
	}
}
