package Exceptions;

public class MethodeAnnotationIncoherenteException extends Exception {

    private String title;

    public MethodeAnnotationIncoherenteException() {
        super();
    }

    public MethodeAnnotationIncoherenteException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    
}
