package jrplot.core.expression;


/**
 * Represents an operator.
 *  
 * @author rodrigo
 *
 */
enum Operator implements Evaluable {

	ADDITION("+", false, 3, false) {
		@Override
		public double evaluate(double a, double b) throws ExpressionException {
			return a + b;
		}
	},
	SUBTRACTION("-", false, 3, false) {
		@Override
		public double evaluate(double a, double b) throws ExpressionException {
			return a - b;
		}
	},
	MULTIPLICATION("*", false, 2, false) {
		@Override
		public double evaluate(double a, double b) throws ExpressionException {
			return a * b;
		}
	},
	DIVISION("/", false, 2, false) {
		@Override
		public double evaluate(double a, double b) throws ExpressionException {
			return a / b;
		}
	},
	EXPONENTIATION("^", false, 1, false) {
		@Override
		public double evaluate(double a, double b) throws ExpressionException {
			return Math.pow(a, b);
		}
	}
	;
	
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
	
	/*
	 * (non-Javadoc)
	 * @see jrplot.core.expression.Evaluable#isUnary()
	 */
	public boolean isUnary() {
		return unary;
	}
	
	/*
	 * (non-Javadoc)
	 * Must be ovewriten by instances that represent unary operations.
	 * 
	 * @see jrplot.core.expression.Evaluable#evaluate(double)
	 */
	public double evaluate(double a) throws ExpressionException {
		throw new ExpressionException("Function body not implemented");
	}
	
	/*
	 * (non-Javadoc)
	 * Must be ovewriten by instances that represent binary operations.
	 *  
	 * @see jrplot.core.expression.Evaluable#evaluate(double, double)
	 */
	public double evaluate(double a, double b) throws ExpressionException {
		throw new ExpressionException("Function body not implemented");
	}
	
	@Override
	public String toString() {
		return symbol;
	}
}
