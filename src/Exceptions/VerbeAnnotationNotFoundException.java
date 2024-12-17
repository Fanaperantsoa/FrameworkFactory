package Exceptions;

public class VerbeAnnotationNotFoundException extends Exception {

    private String title;

    public VerbeAnnotationNotFoundException() {
        super();
    }

    public VerbeAnnotationNotFoundException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    
}
