package jrplot.core.expression;

/**
 * Represents a variable, or a unknown value from an expression.
 * @author rodrigo
 *
 */
class Variable implements ExpressionElement {
	final String name;

	public Variable(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
