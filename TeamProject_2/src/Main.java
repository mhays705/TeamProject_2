import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			InfixExpression infix = new InfixExpression("test.txt");
			System.out.println(infix.evaluate());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}