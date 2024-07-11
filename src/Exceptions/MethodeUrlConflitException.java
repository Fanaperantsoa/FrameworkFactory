package Exceptions;

public class MethodeUrlConflitException extends Exception {

    private String title;

    public MethodeUrlConflitException() {
        super();
    }

    public MethodeUrlConflitException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    
}
