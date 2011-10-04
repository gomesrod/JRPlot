package jrplot.core.expression;

import static org.junit.Assert.assertEquals;
import jrplot.core.expression.Expression;
import jrplot.core.expression.ExpressionException;

import org.junit.Test;



public class ExpressionParseTest {

	/**
	 * A small test for the expression splitting method.
	 */
	@Test
	public void testSplitInTokens() {
		String exp = "(x^2)+25 -(1.5abs(-9))";
		String[] tokens = Expression.splitInTokens(exp);
		assertEquals("(", tokens[0]);
		assertEquals("x", tokens[1]);
		assertEquals("^", tokens[2]);
		assertEquals("2", tokens[3]);
		assertEquals(")", tokens[4]);
		assertEquals("+", tokens[5]);
		assertEquals("25", tokens[6]);
		assertEquals("-", tokens[7]);
		assertEquals("(", tokens[8]);
		assertEquals("1.5", tokens[9]);
		assertEquals("abs", tokens[10]);
		assertEquals("(", tokens[11]);
		assertEquals("-9", tokens[12]);
		assertEquals(")", tokens[13]);
		assertEquals(")", tokens[14]);
	}
	
	@Test
	public void simple_binary_operator() throws ExpressionException {
		Expression exp = Expression.parse("1+2");
		assertEquals("1.0 2.0 + ", exp.toString());
	}
	
	@Test
	public void simple_unary_function() throws ExpressionException {
		Expression exp = Expression.parse("abs(-3)");
		assertEquals("-3.0 ABS ", exp.toString());
	}
}
