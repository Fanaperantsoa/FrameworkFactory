package Exceptions;

public class UrlIntrouvableException extends Exception {

    private String title;
    
    public UrlIntrouvableException() {
        super();
    }

    public UrlIntrouvableException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }


}
