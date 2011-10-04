package jrplot.core.expression;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Expression {

	/**
	 * Represents the function tokens, in Reverse Polish Notation.
	 */
	private Queue<ExpressionElement> expressionElements;
	
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
		Stack<String> auxStack = new Stack<String>();
		
		String[] tokens = splitInTokens(expressionText);
		
		/*
		 * Reference: http://en.wikipedia.org/wiki/Shunting-yard_algorithm
		 * 
		 * While there are tokens to be read:
		 */
		for (String tok : tokens) {
			/*
			 * If the token is a number, then add it to the output queue.
			 */
			if (isNumber(tok)) {
				double val = Double.parseDouble(tok);
				outputQueue.offer(new Value(val));

			/*
			 * If the token is a function token, then push it onto the stack.
			 */
			} else if (Function.isValidFunction(tok)) {
				auxStack.push(tok);
				
			/*
			 * If the token is a function argument separator (e.g., a comma):
			 * - Until the token at the top of the stack is a left parenthesis, 
			 * pop operators off the stack onto the output queue. If no left 
			 * parentheses are encountered, either the separator was misplaced 
			 * or parentheses were mismatched.
			 */
			} else if (",".equals(tok)) {
				try {
					while (!"(".equals(auxStack.peek())) {
						outputQueue.offer(Operator.findBySymbol(auxStack.pop()));
					}
				} catch (EmptyStackException e) {
					throw new ExpressionException(
						"Malformed expression. A function separator (comma) was used, but there is no starting parenthesis.");
				}
			
			/*
			 * If the token is an operator, o1, then:
			 * - while there is an operator token, o2, at the top of the stack, and
			 *   either o1 is left-associative and its precedence is less than or equal to that of o2,
			 *   or o1 is right-associative and its precedence is less than that of o2,
			 * --- pop o2 off the stack, onto the output queue;
			 * - push o1 onto the stack.
			 */
			} else if (Operator.isValid(tok)) {
				Operator o1 = Operator.findBySymbol(tok);
				
				while (!auxStack.isEmpty() && Operator.isValid(auxStack.peek())) {
					Operator o2 = Operator.findBySymbol(auxStack.peek());
					if ((!o1.isRightAssociative() && o1.getPrecedence() <= o2.getPrecedence())
							|| (o1.isRightAssociative() && o1.getPrecedence() < o2.getPrecedence())) {
						auxStack.pop();
						outputQueue.offer(o2);
					} else {
						break;
					}
				}
				
				auxStack.push(tok);
				
				
			/*
			 * If the token is a left parenthesis, then push it onto the stack.
			 */
			} else if (tok.equals("(")) {
				auxStack.push("(");
				
			/*
			 * If the token is a right parenthesis:
			 * - Until the token at the top of the stack is a left parenthesis, 
			 *   pop operators off the stack onto the output queue.
			 * - Pop the left parenthesis from the stack, but not onto the output queue.
			 * - If the token at the top of the stack is a function token, pop it onto the output queue.
			 * - If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
			 * 
			 * /TODO: Handle the difference between a "function token" and a "valid function token"
			 */
			} else if (tok.equals(")")) {
				try {
					while (!"(".equals(auxStack.peek())) {
						outputQueue.offer(Operator.findBySymbol(auxStack.pop()));
					}
				} catch (EmptyStackException e) {
					throw new ExpressionException(
						"Malformed expression. Mismatched parenthesis.");
				}
				
				auxStack.pop();
				if (Function.isValidFunction(auxStack.peek())) {
					outputQueue.add(Function.findByName(auxStack.pop()));
				}
			}
		}
		
		/*
		 * While there are still operator tokens in the stack:
		 * If the operator token on the top of the stack is a parenthesis, then there are mismatched parentheses.
		 * Pop the operator onto the output queue.
		 */
		while (!auxStack.isEmpty()) {
			outputQueue.offer(Operator.findBySymbol(auxStack.pop()));
		}
		
		Expression exp = new Expression();
		exp.expressionElements = outputQueue;
		return exp;
	}

	private static boolean isNumber(String tok) throws ExpressionException {
		char firstChar = tok.charAt(0);
		if (!Character.isDigit(firstChar) && firstChar != '-') {
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
