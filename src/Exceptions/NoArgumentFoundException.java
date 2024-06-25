package Exceptions;

public class NoArgumentFoundException extends Exception {
    public NoArgumentFoundException() {
        super();
    }

    public NoArgumentFoundException(String message) {
        super(message);
    }
}
