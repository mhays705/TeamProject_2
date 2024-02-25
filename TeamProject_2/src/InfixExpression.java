import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class InfixExpression {

	private Stack<String> infix; // Stores infix expression as a stack of characters
	private String postfix; // Stores infix expression as postfix after conversion

	// Constructor that parses file and creates an infix expression as a stack of
	// characters
	public InfixExpression(String file) throws IOException {
		infix = new Stack<>();
		try {
			FileReader reader = new FileReader(file);
			StringBuilder number = new StringBuilder(); // Store digits of number
			StringBuilder operator = new StringBuilder(); // Store comparison and logical operators
			int currentChar;
			while ((currentChar = reader.read()) != -1) { // Make sure stream isn't empty
				char ch = (char) currentChar;
				if (Character.isDigit(ch)) { // If character is digit append it to builder
					number.append(ch);
				}
				if (ch == '<' || ch == '>' || ch == '&' || ch == '|' || ch == '!' || ch == '=') { // Check if character
																									// is comparison
					// or logical operator
					char nextChar = (char) reader.read();
					if (nextChar == '=') {
						operator.append(ch).append(nextChar);
						infix.push(operator.toString());
						operator.setLength(0); // Reset operator string builder
					} else if ((ch == '&' && nextChar == '&') || (ch == '|' && nextChar == '|')) { // If logical
																									// operator append
																									// it to builder and
																									// push on to stack
						operator.append(ch).append(nextChar);
						infix.push(operator.toString());
						operator.setLength(0); // Reset operator string builder
					} else {
						infix.push(String.valueOf(ch)); // Push single character operator
					}
				} else if (!Character.isWhitespace(ch)) {
					char nextChar = (char) reader.read();
					if (Character.isDigit(nextChar)) { // Check if multidigit number
						number.append(nextChar);
					}
					if (number.length() > 0) {
						infix.push(number.toString()); // Push accumulated digits to stack
						number.setLength(0); // Reset StringBuilder
					}
					// Push the character onto the stack
					else {
						infix.push(String.valueOf(ch));
					}

				}
			}

			// Push any remaining numbers on to the stack
			if (number.length() > 0) {
				infix.push(number.toString());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
		}
		reverseInfixStack(infix);
		System.out.println(infix.toString()); // Remove before submitting. Only for testing
		this.postfix = infixToPostfix(infix);
		System.out.println(this.postfix); // Remove before submitting. For Testing.
	}

	/**
	 * Return value for precedence of operator
	 * 
	 * @param operator: operator to be checked
	 * @return: Value representing level of precedence
	 */
	public int precedence(String operator) {
		if (operator.equals("^")) {
			return 7;
		} else if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {
			return 6;
		} else if (operator.equals("+") || operator.equals("-")) {
			return 5;
		} else if (operator.equals(">") || operator.equals(">=") || operator.equals("<") || operator.equals("<=")) {
			return 4;
		} else if (operator.equals("==") || operator.equals("!=")) {
			return 3;
		} else if (operator.equals("&&")) {
			return 2;
		} else if (operator.equals("||")) {
			return 1;
		}

		return 0;
	}

	/**
	 * Reverses infix stack so beginning of expression is at top of the stack
	 * 
	 * @param infix: infix stack to be reversed.
	 */
	private void reverseInfixStack(Stack<String> infix) {
		Stack<String> newInfix = new Stack<>();

		while (!infix.isEmpty()) {
			newInfix.push(infix.pop());
		}
		this.infix = newInfix;
	}

	/**
	 * Converts an infix expression to postfix expression.
	 * 
	 * @param infixExp: infix expression to convert
	 * @return: result postfix expression
	 */
	public String infixToPostfix(Stack<String> infix) {
		Stack<String> operatorStk = new Stack<>();
		StringBuilder postfix = new StringBuilder();

		while (!infix.isEmpty()) {
			String token = infix.pop();

			if (Character.isDigit(token.charAt(0))) {
				postfix.append(token).append(' ');
			} else if (token.equals("(")) {
				operatorStk.push(token);
			} else if (token.equals(")")) {
				while (!operatorStk.peek().equals("(")) {
					postfix.append(operatorStk.pop()).append(' ');
				}
				operatorStk.pop();
			} else {
				while (!operatorStk.isEmpty() && !operatorStk.peek().equals("(")
						&& precedence(token) <= precedence(operatorStk.peek())) {
					postfix.append(operatorStk.pop()).append(' ');
				}
				operatorStk.push(token);
			}
		}

		while (!operatorStk.isEmpty()) {
			postfix.append(operatorStk.pop()).append(' ');
		}

		return postfix.toString();
	}

	/**
	 * Evaluates a postfix expression using a stack.
	 * 
	 * @param postfixExp: postfix expression to evaluate
	 * @return: evaluation result
	 * @throws ArithmeticException:           divide-by-zero
	 * @throws UnsupportedOperationException: operator not supported
	 */
	public int evaluate() {
		Stack<Integer> stk = new Stack<>();
		Scanner scanner = new Scanner(this.postfix);
		while (scanner.hasNext()) {
			String token = scanner.next();
			if (Character.isDigit(token.charAt(0))) {
				stk.push(Integer.valueOf(token));
			} else {
				int rightOperand = stk.pop(), leftOperand = stk.pop();
				// Supported operators
				switch (token) {
				case "+":
					stk.push(leftOperand + rightOperand);
					break;
				case "-":
					stk.push(leftOperand - rightOperand);
					break;
				case "*":
					stk.push(leftOperand * rightOperand);
					break;
				case "/":
					if (rightOperand == 0) {
						scanner.close();
						throw new ArithmeticException("Dividing by zero");
					}
					stk.push(leftOperand / rightOperand);
					break;
				case "^":
					stk.push((int) Math.pow(leftOperand, rightOperand));
					break;
				case ">":
					stk.push(leftOperand > rightOperand ? 1 : 0);
					break;
				case ">=":
					stk.push(leftOperand >= rightOperand ? 1 : 0);
					break;
				case "<":
					stk.push(leftOperand < rightOperand ? 1 : 0);
					break;
				case "<=":
					stk.push(leftOperand <= rightOperand ? 1 : 0);
					break;
				case "==":
					stk.push(leftOperand == rightOperand ? 1 : 0);
					break;
				case "!=":
					stk.push(leftOperand != rightOperand ? 1 : 0);
					break;
				case "&&":
					stk.push((leftOperand != 0 && rightOperand != 0) ? 1 : 0);
					break;
				case "||":
					stk.push((leftOperand != 0 || rightOperand != 0) ? 1 : 0);
					break;
				default:
					throw new UnsupportedOperationException("Operator not supported: " + token);
				}
			}
		}
		scanner.close();
		return stk.pop();
	}

}
