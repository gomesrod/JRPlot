package jrplot.core.expression;


/**
 * Represents a function, or an operator.
 *  
 * @author rodrigo
 *
 */
enum Operator implements ExpressionElement {

	ADDITION("+", false, 2, false),
	SUBTRACTION("-", false, 2, false),
	MULTIPLICATION("*", false, 1, false),
	DIVISION("/", false, 1, false);
	
	private String symbol;
	private boolean unary;
	private int precedence;
	private boolean rightAssociative;

	public int getPrecedence() {
		return precedence;
	}

	public boolean isRightAssociative() {
		return rightAssociative;
	}

	/**
	 * 
	 * @param symbol
	 * @param isUnary
	 * @param precedence 
	 * @param rightAssociative 
	 */
	private Operator(String symbol, boolean isUnary, int precedence, boolean rightAssociative) {
		this.symbol = symbol;
		this.unary = isUnary;
		this.precedence = precedence;
		this.rightAssociative = rightAssociative;
	}
	
	/**
	 * 
	 * @param symbol
	 * @return
	 * @throws ExpressionException
	 */
	static Operator findBySymbol(String symbol) throws ExpressionException {
		for (Operator fn : values()) {
			if (symbol.equals(fn.symbol)) {
				return fn;
			}
		}
		throw new ExpressionException("Invalid operator: " + symbol);
	}
	
	/**
	 * 
	 * @param symbol
	 * @return
	 */
	static boolean isValid(String symbol) {
		if (symbol == null) return false;
		for (Operator fn : values()) {
			if (symbol.equals(fn.symbol)) {
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
	
	@Override
	public String toString() {
		return symbol;
	}
}
