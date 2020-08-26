
public class InvalidAmountException extends Exception{
	
	
	public InvalidAmountException(double amount) {
		super("Error: Amount can not be negative");
	}
}
