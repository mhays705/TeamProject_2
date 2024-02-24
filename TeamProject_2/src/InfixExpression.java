import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class InfixExpression {

	private Stack<String> infix; // Stores infix expression as a stack of characters

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
				if (ch == '<' || ch == '>' || ch == '&' || ch == '|' || ch == '!' || ch == '=') { // Check if character is comparison
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
		System.out.println(infix.toString()); // Remove before submitting. Only for testing
	}

	/**
	 * Return value for precedence of operator
	 * @param operator: operator to be checked
	 * @return: Value representing level of precedence
	 */
	public int precedence(String operator) {
		if (operator.equals("^")) {
			return 7;
		}
		else if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {
			return 6;
		}
		else if (operator.equals("+") || operator.equals("-")) {
			return 5;
		}
		else if (operator.equals(">") || operator.equals(">=") || operator.equals("<") || operator.equals("<=")) {
			return 4;
		}
		else if (operator.equals("==") || operator.equals("!=")) {
			return 3;
		}
		else if (operator.equals("&&")) {
			return 2;
		}
		else if (operator.equals("||")) {
			return 1;
		}
			
		return 0;
	}
	
	
	
	
	
	
	
	
}
