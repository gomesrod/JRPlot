package jrplot.core.expression;

/**
 * Represent a parenthesis in an expression.
 * A parenthesis is not used when evaluating a RPN expression, but it's necessary
 * during the parsing process.
 * 
 * @author Rodrigo Gomes
 *
 */
public enum Parenthesis implements ExpressionElement {
	LEFT,RIGHT
}
