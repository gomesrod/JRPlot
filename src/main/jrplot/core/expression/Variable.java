package jrplot.core.expression;

/**
 * Represents a variable, or a unknown value from an expression.
 * @author rodrigo
 *
 */
class Variable implements ExpressionElement {
	final double innerValue;

	public Variable(double innerValue) {
		this.innerValue = innerValue;
	}

	@Override
	public String toString() {
		return String.valueOf(innerValue);
	}
}
