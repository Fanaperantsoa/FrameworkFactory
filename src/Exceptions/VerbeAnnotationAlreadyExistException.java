package Exceptions;

public class VerbeAnnotationAlreadyExistException extends Exception {

    private String title;

    public VerbeAnnotationAlreadyExistException() {
        super();
    }

    public VerbeAnnotationAlreadyExistException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    
}
