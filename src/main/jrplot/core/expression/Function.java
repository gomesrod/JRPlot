package jrplot.core.expression;


/**
 * Contains the basic math functions.
 *  
 * @author rodrigo
 *
 */
enum Function implements Evaluable {

	ABS(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.abs(a);
		}
	},
	ACOS(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.acos(a);
		}
	},
	ASIN(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.asin(a);
		}
	},
	ATAN(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.atan(a);
		}
	},
	COS(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.cos(a);
		}
	},
	EXP(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.exp(a);
		}
	},
	LOG(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.log(a);
		}
	},
	LOGTEN(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.log10(a);
		}
	},
	MIN(false) {
		@Override
		public double evaluate(double a, double b) throws ExpressionException {
			return Math.min(a, b);
		}
	},
	MAX(false) {
		@Override
		public double evaluate(double a, double b) throws ExpressionException {
			return Math.max(a, b);
		}
	},
	SIN(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.sin(a);
		}
	},
	SQRT(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.sqrt(a);
		}
	},
	TAN(true) {
		@Override
		public double evaluate(double a) throws ExpressionException {
			return Math.tan(a);
		}
	}
	;
	
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
}
