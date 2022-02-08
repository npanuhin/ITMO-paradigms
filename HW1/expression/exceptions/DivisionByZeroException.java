package expression.exceptions;

public class DivisionByZeroException extends ArithmeticException {
    public DivisionByZeroException(String cause) {
        super("Division by zero in " + cause);
    }
}
