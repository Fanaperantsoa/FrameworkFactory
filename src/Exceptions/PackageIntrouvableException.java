package Exceptions;

public class PackageIntrouvableException extends Exception {

    private String title;

    public PackageIntrouvableException() {
        super();
    }

    public PackageIntrouvableException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }


}
