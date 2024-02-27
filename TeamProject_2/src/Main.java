import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
    	InfixExpression infix;
        try (BufferedReader reader = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	line = line.replaceAll("\\s", ""); //remove whitespace
            	
            	//add correct spacing
            	line = line.replaceAll("([+\\-*/^%><])", " $1 ");
            	line = line.replaceAll("([()])", " $1 ");
            	line = line.replaceAll("&&",  " && ");
            	line = line.replaceAll("\\|\\|",  " \\|\\| ");
            	line = line.replaceAll(">=",  " >= ");
            	line = line.replaceAll("<=",  " <= ");
            	line = line.replaceAll("==",  " == ");
            	line = line.replaceAll("!=",  " != ");
            	
            	infix = new InfixExpression(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }
}
