package Exceptions;

public class PackageVideException extends Exception {

    private String title;

    public PackageVideException() {
        super();
    }

    public PackageVideException(String message) {
        super(message);
    }


    
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }


}
