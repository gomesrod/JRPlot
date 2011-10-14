package jrplot.core.expression;

/**
 * Represents a literal value from an expression.
 * @author rodrigo
 *
 */
class Value implements ExpressionElement {
	final double innerValue;

	public Value(double innerValue) {
		this.innerValue = innerValue;
	}

	@Override
	public String toString() {
		return String.valueOf(innerValue);
	}
}
