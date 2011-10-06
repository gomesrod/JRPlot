package jrplot.core.expression;

/**
 * Identifies a mathematical operation, i.e. a Function or an Operator.
 * @author rodrigo
 *
 */
public interface Evaluable extends ExpressionElement {

	/**
	 * Returns true if this operation is unary. Otherwise returns false, meaning that the
	 * operation is binary.
	 * 
	 * The client code must use this method in order
	 * to correctly determine when to call evaluate(double a) or evaluate(double a, double b)  
	 * @return
	 */
	boolean isUnary();
	
	/**
	 * Evaluates the operation using the given value as input. 
	 * If the implementor represents a binary function, it must throw an exception.
	 * @param a
	 * @return
	 */
	double evaluate(double a) throws ExpressionException;
	
	/**
	 * Evaluates the operation using the given values as input. 
	 * If the implementor represents a unary function, it must throw an exception.
	 * @param a
	 * @param b
	 * @return
	 */
	double evaluate(double a, double b) throws ExpressionException;
}
