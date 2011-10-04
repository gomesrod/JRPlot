package jrplot.core;

public class PlotEngine {

	public static final double PLOTTING_X_STEP = 0.0001;
	
	private String currentFunctionText;
	
	public void updateFunction(String functionText) {
		this.currentFunctionText = functionText;
	}
	
	public double currentMinXBound() {
		return -10.00;
	}
	
	public double currentMaxXBound() {
		return 10.00;
	}
	
	public double currentMinYBound() {
		return -10.00;
	}
	
	public double currentMaxYBound() {
		return 10.00;
	}
	
	public String getCurrentFunctionText() {
		return currentFunctionText;
	}

	public double evaluateCurrentFunctionFor(double x) {
		// TODO Just a UI test. Function evaluation is not implemented
		if ("sin(x)".equals(currentFunctionText)) {
			return Math.sin(x);
		} else if ("x^2".equals(currentFunctionText)) {
			return x * x;
		} else {
			return x;
		}
	}
}
