package Exceptions;

public class TypeRetourException extends Exception {

    private String title;
    
    public TypeRetourException() {
        super();
    }

    public TypeRetourException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }


}
