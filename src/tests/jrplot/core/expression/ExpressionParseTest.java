package jrplot.core.expression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;



public class ExpressionParseTest {

	private static final double DBL_COMPARE_DELTA = 0.000001;
	
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
		assertEquals(3.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
	}
	
	@Test
	public void simple_chained_operations_with_variable() throws ExpressionException {
		Expression exp = Expression.parse("1+2 + 3 +4 +5+ x");
		assertEquals("1.0 2.0 + 3.0 + 4.0 + 5.0 + x + ", exp.toString());
		assertEquals(25.0, exp.evaluate(10.0), DBL_COMPARE_DELTA);
	}
	
	@Test
	public void subtraction_and_negative_numbers() throws ExpressionException {
		Expression exp = Expression.parse("1-3");
		assertEquals("1.0 3.0 - ", exp.toString());
		assertEquals(-2.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
		
		exp = Expression.parse("-1+3");
		assertEquals("-1.0 3.0 + ", exp.toString());
		assertEquals(2.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
	}
	
	@Test
	public void simple_unary_function() throws ExpressionException {
		Expression exp = Expression.parse("abs(-3)");
		assertEquals("-3.0 ABS ", exp.toString());
		assertEquals(3.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
	}
	
	@Test
	public void simple_binary_function() throws ExpressionException {
		Expression exp = Expression.parse("min(30, x)");
		assertEquals("30.0 x MIN ", exp.toString());
		assertEquals(19.0, exp.evaluate(19.0), DBL_COMPARE_DELTA);
	}

	@Test
	public void operator_precedence_and_grouping() throws ExpressionException {
		Expression exp = Expression.parse("2+10*100");
		assertEquals("2.0 10.0 100.0 * + ", exp.toString());
		assertEquals(1002.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);

		exp = Expression.parse("2*10+100");
		assertEquals("2.0 10.0 * 100.0 + ", exp.toString());
		assertEquals(120.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
		
		exp = Expression.parse("(2+10)*100");
		assertEquals("2.0 10.0 + 100.0 * ", exp.toString());
		assertEquals(1200.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
	}

	@Test
	public void operator_precedence_and_grouping_2() throws ExpressionException {
		Expression exp = Expression.parse("2^3+10*100");
		assertEquals("2.0 3.0 ^ 10.0 100.0 * + ", exp.toString());
		assertEquals(1008.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);

		exp = Expression.parse("2*10^3+100");
		assertEquals("2.0 10.0 3.0 ^ * 100.0 + ", exp.toString());
		assertEquals(2100.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
		
		exp = Expression.parse("(2+1)^3*100^2");
		assertEquals("2.0 1.0 + 3.0 ^ 100.0 2.0 ^ * ", exp.toString());
		assertEquals(270000.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
	}
	
	@Test
	public void operators_inside_function() throws ExpressionException {
		Expression exp = Expression.parse("min(10-2, 2*10)");
		assertEquals("10.0 2.0 - 2.0 10.0 * MIN ", exp.toString());
		assertEquals(8.0, exp.evaluate(0.0), DBL_COMPARE_DELTA);
	}
	
	@Test
	public void operation_with_constant() throws ExpressionException {
		Expression exp = Expression.parse("sin(x*pi)");
		assertEquals("x PI * SIN ", exp.toString());
		assertEquals(1.0, exp.evaluate(0.5), DBL_COMPARE_DELTA); // sin (0.5 * pi) = 1.0
	}
	
	@Test
	public void complex_expressions() throws ExpressionException {
		Expression exp = Expression.parse("min(x^2, x^3)*(7+x/2)");
		assertEquals("x 2.0 ^ x 3.0 ^ MIN 7.0 x 2.0 / + * ", exp.toString());
		assertEquals(144.0, exp.evaluate(4), DBL_COMPARE_DELTA);
		
		exp = Expression.parse("exp( cos(x^2 + 1) )");
		assertEquals("x 2.0 ^ 1.0 + COS EXP ", exp.toString());
		assertEquals(0.43211154023488678847355411770506, exp.evaluate(3), DBL_COMPARE_DELTA);
	}
}
