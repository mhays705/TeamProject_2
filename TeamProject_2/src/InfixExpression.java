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
		int currentChar;
		while ((currentChar = reader.read()) != -1) { // Make sure stream isn't empty
			char ch = (char) currentChar;
			if (Character.isDigit(ch)) { // If character is digit append it to builder
				number.append(ch);     
			} else if (!Character.isWhitespace(ch)) {
			    if (number.length() > 0) {
			        infix.push(number.toString()); // Push accumulated digits to stack
			        number.setLength(0); // Reset StringBuilder
			    }
			    // Push the character onto the stack
			    infix.push(String.valueOf(ch));
			}
		}
		
		// Push any remaining numbers on to the stack
        if (number.length() > 0) {
            infix.push(number.toString());
        }
        reader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
		}
		System.out.println(infix.toString());
	}

}
