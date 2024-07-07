package Exceptions;

public class NullRequestParamException extends Exception {
    public NullRequestParamException() {
        super();
    }

    public NullRequestParamException(String message) {
        super(message);
    }
}
