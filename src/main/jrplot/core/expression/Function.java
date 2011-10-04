package jrplot.core.expression;


/**
 * Represents a mathematical function.
 *  
 * @author rodrigo
 *
 */
enum Function implements ExpressionElement {

	ABS(true);
	
	private boolean unary;

	/**
	 * 
	 * @param isUnary
	 */
	private Function(boolean isUnary) {
		this.unary = isUnary;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws ExpressionException
	 */
	static Function findByName(String name) throws ExpressionException {
		for (Function fn : values()) {
			if (name.equalsIgnoreCase(fn.name())) {
				return fn;
			}
		}
		throw new ExpressionException("Invalid function: " + name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	static boolean isValidFunction(String name) {
		for (Function fn : values()) {
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
	public boolean isUnary() {
		return unary;
	}
	
	/**
	 * Must be ovewriten by instances that represent unary operations.
	 * @param a
	 * @return
	 */
	double evaluate(double a) {
		throw new RuntimeException("Function body not implemented");
	}
	
	/**
	 * Must be ovewriten by instances that represent binary operations.
	 * @param a
	 * @param b
	 * @return
	 */
	double evaluate(double a, double b) {
		throw new RuntimeException("Function body not implemented");
	}
}
