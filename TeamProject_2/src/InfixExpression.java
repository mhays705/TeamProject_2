import java.util.Scanner;
import java.util.Stack;

public class InfixExpression {

	private Stack<String> infix; // Stores infix expression as a stack of tokens
	private String postfix; // Stores infix expression as postfix after conversion

	// Constructor that parses file and creates an infix expression as a stack of
	// characters and processes calculations
	public InfixExpression(String line) {
		infix = new Stack<>();
		Scanner sc = new Scanner(line);
		StringBuilder number = new StringBuilder(); // Store digits of number
		StringBuilder operator = new StringBuilder(); // Store comparison and logical operators

		while (sc.hasNext()) { // Check if there is another token in the input
			String token = sc.next(); // Read the next token
			char ch = token.charAt(0); // Get the first character of the token

			if (Character.isDigit(ch)) { // If character is digit append it to builder
				number.append(token);
				infix.push(number.toString());
				number.setLength(0); // Reset number builder
			} else if (ch == '<' || ch == '>' || ch == '&' || ch == '|' || ch == '!' || ch == '=' || ch == '+' // Check for operator
					|| ch == '-' || ch == '^' || ch == '*' || ch == '/') {
				
				if (token.length() > 1) { // If token has more than one character, it's a multi-character operator
					infix.push(token); // Push the operator onto the stack
				} else {
					infix.push(token); // Push single character operator
				}
			}
		}

		sc.close(); // Close the scanner

		System.out.println("Expression read from input file:  " + infix.toString());
		reverseInfixStack(infix);
		System.out.println("Reversed infix " + infix.toString()); // Remove before submitting. Only for testing
		this.postfix = infixToPostfix(infix);
		System.out.println("Postfix expression " + this.postfix); // Remove before submitting. For Testing.
		System.out.println("Result of expression: " +this.evaluate());
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
