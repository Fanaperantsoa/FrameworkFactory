package Exceptions;

public class NoAnnotationParamException extends Exception {

    private String title;

    public NoAnnotationParamException() {
        super();
    }

    public NoAnnotationParamException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    
}
