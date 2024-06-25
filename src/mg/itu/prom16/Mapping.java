package mg.itu.prom16;

import java.lang.String;
import java.lang.reflect.Method;

public class Mapping {
    String classe;
    // String methode;
    Method methode;
    
    public Mapping(String classy, Method methody){
        this.classe = classy;
        this.methode = methody;
    }

    public String getClasse(){
        return this.classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Method getMethode(){
        return this.methode;
    }
    public void setMethode(Method methode) {
        this.methode = methode;
    }

}
