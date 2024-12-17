package Exceptions;

public class VerbeMethodeAlreadyExistException extends Exception {

    private String title;

    public VerbeMethodeAlreadyExistException() {
        super();
    }

    public VerbeMethodeAlreadyExistException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    
}
