package mg.itu.prom16;

import java.lang.String;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

public class ModelView {

    private String url;
    private HashMap<String, Object> data;
    
    // Constructeur
    public ModelView(String newUrl){
        this.url = newUrl;
        this.data = new HashMap<>();
    }
    // Getters & Setters
    public String getUrl(){
        return this.url;
    }
    public void setUrl(String newUrl) {
        this.url = newUrl;
    }

    public HashMap<String, Object> getData() {
        return data;
    }
    public void setData(HashMap<String, Object> newData) {
        this.data = newData;
    }


    // ADDING OR RETRIEVING DATA TO SENT
    public Object getObject(String nom_variable){
        return this.data.get(nom_variable);
    }

    public void addObject(String nom_variable, Object o){
        this.data.put(nom_variable, o);
    }


}
