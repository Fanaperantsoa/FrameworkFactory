package mg.itu.prom16;

import java.lang.String;
import java.util.Objects;

public class VerbeMethode {
    String verbe; // c'est le type de Methode HTTP utilisee : "GET" ou "POST"
    String methode; // la fonction dans la classe annotee @Controller et qui est elle-meme annotee soit par @GET soit par @POST
    
    public VerbeMethode(){
        
    }
    public VerbeMethode(String newVerbe){
        this.verbe = newVerbe;
    }
    public VerbeMethode(String newVerbe, String newMethody){
        this.verbe = newVerbe;
        this.methode = newMethody;
    }

    public String getVerbe(){
        return this.verbe;
    }
    public void setVerbe(String newVerbe) {
        this.verbe = newVerbe.toUpperCase();
    }

    public String getMethode(){
        return this.methode;
    }
    public void setMethode(String methode) {
        this.methode = methode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        VerbeMethode that = (VerbeMethode) o;
        
        return Objects.equals(verbe, that.verbe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verbe);
    }

    
}
