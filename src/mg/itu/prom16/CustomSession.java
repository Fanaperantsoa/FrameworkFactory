package mg.itu.prom16;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


public class CustomSession {

    private HttpSession session;
    

    public CustomSession(HttpSession httpSession){
        // ServletContext sc = getServletContext();
        this.session = httpSession;
    }

    public CustomSession(HttpServletRequest req){
        // ServletContext sc = getServletContext();
        this.session = req.getSession();
    }


    // GETTERS & SETTERS
    public HttpSession getSession() {
        return this.session;
    }

    public void setSession(HttpSession newHttpSession) {
        this.session = newHttpSession;
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

    public void kill() throws IllegalStateException{
        // System.out.println(this.session.getId());
        try {
            this.session.invalidate();

        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw e;
        }
        // System.out.println("vita ilay fanafoanana ny HttpSession");
    }
    
    
    // public HttpSession export () {
    //     HashMap<String, Object> data = new HashMap<String, Object>();

    //     Enumeration<String> attributeNames = session.getAttributeNames();

        


        

    //     return this.session;
    // }


}
