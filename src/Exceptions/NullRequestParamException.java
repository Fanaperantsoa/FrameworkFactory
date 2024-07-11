package Exceptions;

public class NullRequestParamException extends Exception {

    private String title;
    
    public NullRequestParamException() {
        super();
    }

    public NullRequestParamException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

}
