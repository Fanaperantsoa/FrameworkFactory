package mg.itu.prom16;

import java.lang.String;
import java.util.HashSet;
import java.util.Set;

public class Mapping {
    //le controleur
    String classe;
    // VerbMethod verbMethod;
    Set<VerbeMethode> collectionVerbeMethode; 
    
    // public Mapping(String classy, VerbeMethode newVerbeMethode){
    //     this.classe = classy;
    //     this.verbeMethode = newVerbeMethode;
    // }

    public Mapping(){
        this.collectionVerbeMethode = new HashSet<VerbeMethode>();
    }

    public Mapping(String classy, Set<VerbeMethode> newCollectionVerbeMethode){
        this.classe = classy;
        this.collectionVerbeMethode = newCollectionVerbeMethode;
    }

    public String getClasse(){
        return this.classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }

    // public VerbeMethode getVerbeMethode(){
    //     return this.verbeMethode;
    // }
    // public void setVerbeMethode(VerbeMethode newVerbeMethode) {
    //     this.verbeMethode = newVerbeMethode;
    // }


    public Set<VerbeMethode> getCollectionVerbeMethode(){
        return this.collectionVerbeMethode;
    }
    public void setCollectionVerbeMethode(Set<VerbeMethode> newCollectionVerbeMethode) {
        this.collectionVerbeMethode = newCollectionVerbeMethode;
    }

    public VerbeMethode getVerbeMethode (String nomVerbe) {
        for (VerbeMethode vbM : this.getCollectionVerbeMethode()) {
            if (vbM.getVerbe() == nomVerbe) {
                return vbM;
            }
        }
        return null;
    }

}
