package mg.itu.prom16;

import java.lang.String;

public class Mapping {
    String classe;
    String methode;
    // Method methode;
    
    public Mapping(String classy, String methody){
        this.classe = classy;
        this.methode = methody;
    }

    public String getClasse(){
        return this.classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getMethode(){
        return this.methode;
    }
    public void setMethode(String methode) {
        this.methode = methode;
    }

}
