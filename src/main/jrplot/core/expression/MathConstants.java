package jrplot.core.expression;


/**
 * Contains the known constants used in expressions.
 *  
 * @author rodrigo
 *
 */
enum MathConstants implements ExpressionElement {

	PI(Math.PI),
	E(Math.E);
	
	final double value;

	private MathConstants(double value) {
		this.value = value;
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws ExpressionException
	 */
	static MathConstants findByName(String name) throws ExpressionException {
		for (MathConstants fn : values()) {
			if (name.equalsIgnoreCase(fn.name())) {
				return fn;
			}
		}
		throw new ExpressionException("Invalid constant: " + name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	static boolean isValid(String name) {
		for (MathConstants fn : values()) {
			if (name.equalsIgnoreCase(fn.name())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getValue() {
		return value;
	}
}
