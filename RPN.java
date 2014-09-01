
import java.util.Scanner;


public class RPN {
	
	private Processor RPNProcessor;
	
	public RPN() {
		RPNProcessor = new Processor();
	}
	
	protected void processData() {
		String userInput = " ";
		while (!userInput.equals("quit")) {
			RPNProcessor.printStorage();
			userInput = readUserInput();
			this.RPNProcessor.process(userInput);
		}
	}
	
	protected String readUserInput() {
		Scanner in = new Scanner(System.in);
        System.out.print("Input : ");
        String input = in.nextLine();
        return input;
        //already checked that even when we input 1, it's still considered as a String input. YAY
	}
	
	
	public static void main(String[] args) {
		RPN rpnCalculator = new RPN();
		rpnCalculator.processData();
	}
}
