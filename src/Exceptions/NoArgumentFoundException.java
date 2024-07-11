package Exceptions;

public class NoArgumentFoundException extends Exception {

    private String title;
    
    public NoArgumentFoundException() {
        super();
    }

    public NoArgumentFoundException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

}
