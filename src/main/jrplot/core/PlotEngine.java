package jrplot.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jrplot.core.expression.Expression;
import jrplot.core.expression.ExpressionException;

public class PlotEngine {

	public static final double PLOTTING_X_STEP = 0.0001;

	private String currentFunctionText;
	private Expression currentFunction;
	
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
		this.currentFunction = Expression.parse(expression);
		this.currentFunctionText = expression;
		
		/*
		 * Plots the function.
		 */
		List<Pair> pairs = new ArrayList<Pair>();
		double curMinY = 0.0;
		double curMaxY = 0.0;

		double x = minX;
		while(x <= maxX) {
			double y = currentFunction.evaluate(x);
			if (y < curMinY) curMinY = y;
			if (y > curMaxY) curMaxY = y;
			
			pairs.add(new Pair(x, y));
			x = x + PLOTTING_X_STEP;
		}
		
		this.currentFunctionPairs = pairs;
		this.minX = minX <= 0.0 ? minX : 0.0;
		this.maxX = maxX >= 0.0 ? maxX : 0.0;
		this.minY = curMinY;
		this.maxY = curMaxY;
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
	
	public String getCurrentFunctionText() {
		return currentFunctionText;
	}

	
}
