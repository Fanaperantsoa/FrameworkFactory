package Exceptions;

public class NoAnnotationParamException extends Exception {
    public NoAnnotationParamException() {
        super();
    }

    public NoAnnotationParamException(String message) {
        super(message);
    }
}
