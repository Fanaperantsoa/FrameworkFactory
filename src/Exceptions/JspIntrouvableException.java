package Exceptions;

public class JspIntrouvableException extends Exception {

    private String title;

    public JspIntrouvableException() {
        super();
    }

    public JspIntrouvableException(String message) {
        super(message);
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

}
