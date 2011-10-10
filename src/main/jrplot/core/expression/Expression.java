package jrplot.core.expression;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Expression {

	/**
	 * Represents the function tokens, in Reverse Polish Notation.
	 */
	private Queue<ExpressionElement> expressionElements;
	private Set<String> neededVariables;
	
	/**
	 * Forbid direct instantiation. The parse method must be used.
	 */
	private Expression() {
		
	}
	
	/**
	 * Parses an expression by implementing the Shunting-yard algorithm.
	 * @param expressionText
	 * @return
	 * @throws ExpressionException 
	 */
	public static Expression parse(String expressionText) throws ExpressionException {
		Queue<ExpressionElement> outputQueue = new LinkedList<ExpressionElement>();
		Stack<ExpressionElement> auxStack = new Stack<ExpressionElement>();
		Set<String> vars = new HashSet<String>();
		
		String[] tokens = splitInTokens(expressionText);
		ExpressionElement previousToken = null;
		
		for (String tok : tokens) {
			if (shouldAddImplicitMultiplication(previousToken, tok)) {
				handleToken(outputQueue, auxStack, "*");
			}
			
			ExpressionElement evaluatedTok = handleToken(outputQueue, auxStack, tok);
			
			if (evaluatedTok instanceof Variable) {
				vars.add(((Variable)evaluatedTok).name);
			}
			
			previousToken = evaluatedTok;
		}
		
		/*
		 * ## While there are still operator tokens in the stack:
		 * If the operator token on the top of the stack is a parenthesis, then there are mismatched parentheses.
		 * Pop the operator onto the output queue.
		 */
		while (!auxStack.isEmpty()) {
			ExpressionElement aux = auxStack.pop();
			if (!(aux instanceof Operator)) {
				throw new ExpressionException("Malformed expression");
			}
			outputQueue.offer(aux);
		}
		
		Expression exp = new Expression();
		exp.expressionElements = outputQueue;
		exp.neededVariables = vars;
		return exp;
	}

	/**
	 * 
	 * @param previousToken
	 * @param tok
	 * @return
	 */
	private static boolean shouldAddImplicitMultiplication(
			ExpressionElement previousToken, String tok) {
		boolean isValue = previousToken instanceof Value 
						|| previousToken instanceof Variable 
						|| previousToken instanceof MathConstants;
		
		char first = tok.charAt(0);
		
		// Case 1: value followed by alpha token. Ex: 5x, 10sin(...)
		if (isValue && Character.isLetter(first)) {
			return true;
		}
		
		// Case 2: value followed by parenthesis. Ex: 3(x^2), x(7+x)
		if (isValue && first == '(') {
			return true;
		}
		
		return false;
	}

	/**
	 * Evaluates the current expression token according to the Shunting-yard algorithm.
	 * 
	 * Reference: http://en.wikipedia.org/wiki/Shunting-yard_algorithm
	 * 
	 * @param outputQueue
	 * @param auxStack
	 * @param tok
	 * 
	 * @return The parsed token, represented by an ExpressionElement instance.
	 * For "throw-away" tokens, such as comma (","), this method returns null.
	 * 
	 * @throws ExpressionException
	 */
	private static ExpressionElement handleToken(Queue<ExpressionElement> outputQueue,
			Stack<ExpressionElement> auxStack, String tok) throws ExpressionException {
		/*
		 * ## If the token is a number, then add it to the output queue.
		 */
		if (isNumber(tok)) {
			double val = Double.parseDouble(tok);
			ExpressionElement evaluatedTok = new Value(val);
			outputQueue.offer(evaluatedTok);
			return evaluatedTok;
		}

		// Note: Variables and constants are also numbers
		if (tok.length() == 1 && Character.isLetter(tok.charAt(0))) {
			Variable evaluatedTok = new Variable(tok);
			outputQueue.offer(evaluatedTok);
			return evaluatedTok;
		}
		
		if (MathConstants.isValid(tok)) {
			MathConstants evaluatedTok = MathConstants.findByName(tok);
			outputQueue.offer(evaluatedTok);
			return evaluatedTok;
		}
		
		/*
		 * ## If the token is a function token, then push it onto the stack.
		 */
		if (Function.isValidFunction(tok)) {
			Function evaluatedTok = Function.findByName(tok);
			auxStack.push(evaluatedTok);
			return evaluatedTok;
		}
		
		/*
		 * ## If the token is a function argument separator (e.g., a comma):
		 * - Until the token at the top of the stack is a left parenthesis, 
		 * pop operators off the stack onto the output queue. If no left 
		 * parentheses are encountered, either the separator was misplaced 
		 * or parentheses were mismatched.
		 */
		if (",".equals(tok)) {
			try {
				while (auxStack.peek() != Parenthesis.LEFT) {
					ExpressionElement aux = auxStack.pop();
					if (!(aux instanceof Operator)) {
						throw new ExpressionException("Unexpected at this point: " + aux);
					}
					outputQueue.offer(aux);
				}
			} catch (EmptyStackException e) {
				throw new ExpressionException(
					"Malformed expression. A function separator (comma) was used, but there is no starting parenthesis.");
			}

			return null;
		}
		
		/*
		 * ## If the token is an operator, o1, then:
		 * - while there is an operator token, o2, at the top of the stack, and
		 *   either o1 is left-associative and its precedence is less than or equal to that of o2,
		 *   or o1 is right-associative and its precedence is less than that of o2,
		 * --- pop o2 off the stack, onto the output queue;
		 * - push o1 onto the stack.
		 * 
		 * note: In our code, smaller precedence values mean HIGHER precedence, or higher priority.
		 */
		if (Operator.isValid(tok)) {
			Operator o1 = Operator.findBySymbol(tok);
			
			while (!auxStack.isEmpty() && (auxStack.peek() instanceof Operator)) {
				Operator o2 = (Operator) auxStack.peek();
				if ((!o1.isRightAssociative() && o1.getPrecedence() >= o2.getPrecedence())
						|| (o1.isRightAssociative() && o1.getPrecedence() > o2.getPrecedence())) {
					auxStack.pop();
					outputQueue.offer(o2);
				} else {
					break;
				}
			}
			
			auxStack.push(o1);
			return o1;
		}
			
		/*
		 * ## If the token is a left parenthesis, then push it onto the stack.
		 */
		if (tok.equals("(")) {
			auxStack.push(Parenthesis.LEFT);
			return Parenthesis.LEFT;
		}
		
		/*
		 * ## If the token is a right parenthesis:
		 * - Until the token at the top of the stack is a left parenthesis, 
		 *   pop operators off the stack onto the output queue.
		 * - Pop the left parenthesis from the stack, but not onto the output queue.
		 * - If the token at the top of the stack is a function token, pop it onto the output queue.
		 * - If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
		 * 
		 */
		if (tok.equals(")")) {
			try {
				while (auxStack.peek() != Parenthesis.LEFT) {
					ExpressionElement aux = auxStack.pop();
					if (!(aux instanceof Operator)) {
						throw new ExpressionException("Unexpected at this point: " + aux);
					}
					outputQueue.offer(aux);
				}
			} catch (EmptyStackException e) {
				throw new ExpressionException(
					"Malformed expression. Unbalanced parenthesis.");
			}
			
			auxStack.pop();

			if (!auxStack.isEmpty() && (auxStack.peek() instanceof Function)) {
				outputQueue.add(auxStack.pop());
			}
			
			return Parenthesis.RIGHT;
		}

		// Did not fit in any rule above...
		throw new ExpressionException("Unrecognized token: " + tok);
	}

	/**
	 * Evaluates the current expression, replacing the variable "x" (if present) with
	 * the supplied value.
	 * 
	 * @param x Value of the "x" variable. If this variable is not part of the expression,
	 * the value is ignored.
	 *  
	 * @return
	 * @throws ExpressionException 
	 */
	public double evaluate(double x) throws ExpressionException {
		Stack<Double> valStack = new Stack<Double>();
		
		Queue<ExpressionElement> workingQueue = new LinkedList<ExpressionElement>(this.expressionElements);
		while(!workingQueue.isEmpty()) {
			ExpressionElement el = workingQueue.poll();
			
			if (el instanceof Value) {
				valStack.add(((Value)el).innerValue);
				
			} else if (el instanceof Variable) {
				Variable var = (Variable) el;				
				if ("x".equalsIgnoreCase(var.name)) {
					valStack.add(x);
				} else {
					throw new ExpressionException("Unrecognized variable: " + var.name);
				}
				
			} else if (el instanceof MathConstants) {
				valStack.add(((MathConstants)el).value);
				
			} else if (el instanceof Evaluable) {
				Evaluable eval = (Evaluable) el;
				double res;
				
				try {
					if (eval.isUnary()) {
						res = eval.evaluate(valStack.pop());
					} else {
						double b = valStack.pop();
						double a = valStack.pop();
						res = eval.evaluate(a, b);
					}
				} catch (EmptyStackException e) {
					throw new ExpressionException("Malformed expression");
				}
				
				valStack.push(res);
			}
		}
		
		double finalResult = valStack.pop();
		
		if (!valStack.isEmpty()) {
			throw new ExpressionException("Malformed expression");
		}
		
		return finalResult;
		
	}
	
	/**
	 * Evaluates the current expression, without providing any variable.
	 * If a variable is needed, the method will throw an exception.
	 * 
	 * @return
	 * @throws ExpressionException
	 */
	public double evaluate() throws ExpressionException {
		if (!this.neededVariables.isEmpty()) {
			throw new ExpressionException("The expression contains variables");
		}
		return evaluate(0.0); // The expression does not contain variables, so this value will be ignored. 
	}
	
	private static boolean isNumber(String tok) throws ExpressionException {
		char firstChar = tok.charAt(0);
		if (!Character.isDigit(firstChar) && (firstChar != '-' || tok.length() == 1)) {
			return false;
		}
		try {
			Double.parseDouble(tok); // Just to ensure the consistency
			return true;
		} catch (NumberFormatException e) {
			throw new ExpressionException("Invalid number: " + tok);
		}
	}

	/**
	 * Transforms the string expression to an array of tokes.
	 * ex: (x^2)+25 - abs(9)   =>  [ (, x, ^, 2, ), +, 25, -, abs, (, 9, ) ]
	 * @param expressionText
	 * @return
	 */
	static String[] splitInTokens(String expressionText) {
		List<String> tokens = new ArrayList<String>();
		
		String curToken = "";
		char lastChar = '\0';
		for(int i = 0, len = expressionText.toCharArray().length; i < len; i++) {
			char c = expressionText.toCharArray()[i];
			
			if (Character.isWhitespace(c)) {
				continue;
			}
			
			// convention for "char types": 1=letter, 2=digit, 3=other(symbols)
			// This are valid only in the scope of the function, no need to create constants.
			int curCharType = Character.isLetter(c) ? 1 : 
								(Character.isDigit(c) || c == '.') ? 2 
								: 3; 
			int lastCharType = Character.isLetter(lastChar) ? 1 : 
								(Character.isDigit(lastChar) || lastChar == '.') ? 2 
								: 3; 

			lastChar = c;
			
			if (curCharType == 2
					&& ("-".equals(curToken))
					&& (i == 1 || expressionText.toCharArray()[i-2] == '(')) {
				// A number following a minus sign is a negative, not an operation.
				// Only when beginning a expression, or after a left parenthesis.
				curToken = curToken + c;
				
			} else if (curCharType == lastCharType && curCharType != 3) {
				// If the char is of the same type as the previous one (letter or digit),
				// then they are part of the same token. Ex:  sin [function], 1234, pi [constant]
				curToken = curToken + c; 
				
			} else {
				if (!"".equals(curToken)) {
					tokens.add(curToken);
				}
				curToken = String.valueOf(c);
			}
			
		}
		
		if (!"".equals(curToken)) {
			tokens.add(curToken);
		}
		
		return tokens.toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Represents this expression in RPN.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Queue<ExpressionElement> copy = new LinkedList<ExpressionElement>(expressionElements);
		StringBuilder sb = new StringBuilder();
		while (!copy.isEmpty()) {
			sb.append(copy.poll()).append(" ");
		}
		return sb.toString();
	}
}
