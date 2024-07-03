package mg.itu.prom16;

import java.util.Enumeration;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

public class CustomSession {
    private HttpSession session;
    

    public CustomSession(HttpSession httpSession){
        // ServletContext sc = getServletContext();
        this.session = httpSession;
    }



    public Object get(String key) {
        return this.session.getAttribute(key);
    }


    public void add(String key, Object objet) {
        this.session.setAttribute(key, objet);
    }
    
    
    public void delete(String key) {
        this.session.removeAttribute(key);
    }
    
    
    public void update(String key, Object objet) {
        delete(key);
        add(key, objet);
    }
    
    
    public HttpSession export () {
        HashMap<String, Object> data = new HashMap<String, Object>();

        Enumeration<String> attributeNames = session.getAttributeNames();

        


        

        return this.session;
    }
}
