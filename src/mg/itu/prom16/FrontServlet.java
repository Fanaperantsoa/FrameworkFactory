package mg.itu.prom16;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.String;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashMap;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import Exceptions.*;

import annotations.ControllerAnous;
import annotations.Get;

import mg.itu.prom16.Mapping;
import mg.itu.prom16.ModelView;


public class FrontServlet extends HttpServlet{

    //HashMap pour stocker tous les methodes et leur classe
    private final HashMap<String, Mapping> methodeEtController = new HashMap<>();

    // ATTRIBUTS SERVANTS A RECUPERER L'EXCEPION DANS LA METHODE init() POUR POUVOIR L'AFFICHER COTE UI
    private PackageIntrouvableException initPackageIntrouvableException;

    private PackageVideException initPackageVideException;

    private MethodeUrlConflitException methodUrlConflitException;




    @Override
    public void init() throws ServletException{
        //super.init();
        try {
            ServletConfig config = getServletConfig();
            scanner(config);
        } catch (PackageIntrouvableException e) {
            this.initPackageIntrouvableException = e;
            log("Exception during servlet initialization", e);
        } catch (PackageVideException e) {
            this.initPackageVideException = e;
            log("Exception during servlet initialization", e);
        } catch (MethodeUrlConflitException e) {
            this.methodUrlConflitException = e;
            log("Exception during servlet initialization", e);
        }
        
    }

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException
                | SecurityException | ServletException | IOException | NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException{
        
        PrintWriter out = response.getWriter();
        boolean methode_trouvee = false;


        if(initPackageIntrouvableException != null){
            // // ERREUR POUR UN PACKAGE INEXISTANT !!
            response.setContentType("text/html");
            response.setCharacterEncoding( "UTF-8" );
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"utf-8\" />");
            out.println("<title>Erreur !</title>");
            out.println("</head>");
            out.println("<body style=\"text-align:center\">");
            out.println("<h1 style=\"font-family:Courier New \" style=\"font-family:Courier New \">» Erreur!! Package introuvable.</h1>");
            out.println("<h3>Le package sense contenir tous les controleurs est introuvable. Merci de verifier votre fichier de configuration de deploiement web.xml ou contactez votre administrateur.</h3>");
            out.println("<p style=\"font-style:italic\">" + initPackageIntrouvableException.getMessage() + "</p>");
            out.println("</body>");
            out.println("</html>");

        } else if (initPackageVideException != null){
            // ERREUR POUR UN PACKAGE VIDE !!
            response.setContentType("text/html");
            response.setCharacterEncoding( "UTF-8" );
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"utf-8\" />");
            out.println("<title>Erreur !</title>");
            out.println("</head>");
            out.println("<body style=\"text-align:center\">");
            out.println("<h1 style=\"font-family:Courier New \">» Erreur!! Package vide.</h1>");
            out.println("<h3>Le package sense contenir tous les controleurs est vide. Il se peut que les fichiers .class n'aient pas ete deploye dedans lors de la compilation. Merci de verifier votre fichier votre fichier de deploiement .bat ou d'eventuelles erreurs lors de la compilation.</h3>");
            out.println("<p style=\"font-style:italic\">" + initPackageVideException.getMessage() + "</p>");
            out.println("</body>");
            out.println("</html>");

        
        } else if (methodUrlConflitException != null){
            // ERREUR POUR UN PACKAGE VIDE !!
            response.setContentType("text/html");
            response.setCharacterEncoding( "UTF-8" );
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"utf-8\" />");
            out.println("<title>Erreur !</title>");
            out.println("</head>");
            out.println("<body style=\"text-align:center\">");
            out.println("<h1 style=\"font-family:Courier New \">» Erreur!! Conflit entre annotation.</h1>");
            out.println("<h3>Une meme URL est utilisee pour annoter deux methodes differentes. Le system ne sait trancher lequel des deux methodes appeler. Veuillez changer l'annotation de l'une de ces methodes.</h3>");
            out.println("<p style=\"font-style:italic\">" + methodUrlConflitException.getMessage() + "</p>");
            out.println("</body>");
            out.println("</html>");

        } else {
            
            out.println(request.getRequestURL());
            String uri = request.getRequestURI();
            // out.println(request.getRequestURI());
            // out.println(uri);


            for(Entry<String, Mapping> entree: methodeEtController.entrySet() ){
                // out.println("/TestSprint/" + entree.getKey());
                if(uri.equals("/TestSprint/" + entree.getKey())){
                    // out.println("------------ LISTING DES CONTROLEURS ------------");
                    Mapping result = entree.getValue();
                    // out.println(" - La classe est : " + result.getClasse());
                    // out.println(" - La methode est : " + result.getMethode());
                    
        
                    Class<?> classe = Class.forName(result.getClasse());
                    // out.println("1 - " + classe.toString());
                    Object instanceClass = classe.getDeclaredConstructor().newInstance();
                    // out.println("2 - " + instanceClass.toString());
                    
                    Method method = classe.getMethod(result.getMethode());
                    Class<?> type_de_retour = method.getReturnType();
                    
                    Object retour = execMethod(instanceClass, result.getMethode(), null);
                    
                    if(type_de_retour.equals(String.class)/*  && method.getParameters() == null */){
                        String retourFonction = (String)retour;
                        out.println("La fonction " + result.getMethode() + " retourne --> \"" + retourFonction + "\"");
                    } else if (type_de_retour.equals(ModelView.class)){
                        ModelView mv = (ModelView) retour;
                        String url = mv.getUrl();
                        try {
                            thisExist(url);
                            HashMap<String, Object> data = mv.getData();

                            // ON AJOUTE LES PARAMETRES POUR L'ENVOI
                            if(!data.isEmpty()){
                                Set<Entry<String, Object>> entrees = data.entrySet();
                                for (Entry<String, Object> entrie : entrees) {
                                    request.setAttribute(entrie.getKey(), entrie.getValue());
                                }
                            }
                            // REDIRIGER L'UTILISATEUR VERS LA PAGE url
                            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                            dispatcher.forward(request, response);
                        } catch (JspIntrouvableException e) {
                            response.setContentType("text/html");
                            response.setCharacterEncoding( "UTF-8" );
                            
                            out.println("<!DOCTYPE html>");
                            out.println("<html>");
                            out.println("<head>");
                            out.println("<meta charset=\"utf-8\" />");
                            out.println("<title>Erreur ! Vue introuvable</title>");
                            out.println("</head>");
                            out.println("<body style=\"text-align:center\">");
                            out.println("<h1 style=\"font-family:Courier New \">» Erreur!! Vue introuvable</h1>");
                            out.println("<h3>La vue associee a cette requete est introuvable. Veuillez verifier l'attribut url du ModelView</h3>");
                            out.println("<p style=\"font-style:italic\">" + e.getMessage() + "</p>");
                            out.println("</body>");
                            out.println("</html>");
                        }
                        
                    } else {
                        try {
                            throw new TypeRetourException("La methode : \"" + result.getMethode() + "()\" associee a cette URI : '<u>" + uri + "</u>' retourne un type que l'application ne reconnait pas.");
                        } catch (TypeRetourException e) {
                            response.setContentType("text/html");
                            response.setCharacterEncoding( "UTF-8" );
                            
                            out.println("<!DOCTYPE html>");
                            out.println("<html>");
                            out.println("<head>");
                            out.println("<meta charset=\"utf-8\" />");
                            out.println("<title>Erreur ! Type de retour non reconnu.</title>");
                            out.println("</head>");
                            out.println("<body style=\"text-align:center\">");
                            out.println("<h1 style=\"font-family:Courier New \">» Erreur!! Type de retour non reconnu</h1>");
                            out.println("<h3>Le type de retour de la methode associee a cette ressource n'est pas reconnu.</h3>");
                            out.println("<p style=\"font-style:italic\">" + e.getMessage() + "</p>");
                            out.println("</body>");
                            out.println("</html>");
                        }

                        
                        
                    }
                    
                    methode_trouvee = true;

                    break;

                }
            }

            if (methode_trouvee == false){
                // AORIANA KELY AVADIKA ANATY error.jsp - URL n'existe pas (associe a aucune URL)
                
                try {
                    throw new UrlIntrouvableException("L'URI : \"<u>" + uri + "</u>\" est introuvable.");
                } catch (UrlIntrouvableException e) {
                    response.setContentType("text/html");
                    response.setCharacterEncoding( "UTF-8" );
                    
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<meta charset=\"utf-8\" />");
                    out.println("<title>Erreur ! Ressource introuvable</title>");
                    out.println("</head>");
                    out.println("<body style=\"text-align:center\">");
                    out.println("<h1 style=\"font-family:Courier New \">» Erreur!! Ressource introuvable</h1>");
                    out.println("<h3>La methode et la classe est introuvable. Il semble que cette URL n'est associee a aucune ressource.</h3>");
                    out.println("<p style=\"font-style:italic\">" + e.getMessage() + "</p>");
                    out.println("</body>");
                    out.println("</html>");
                }
                
                
                        

            }


        }
        
        
        
        

        

        // --------------- SPRINT 1 ------------------
        // ServletConfig config = getServletConfig();
        // String info_servlet = scanner(config);
        // out.println(info_servlet);
    }

    private void scanner(ServletConfig config) throws PackageIntrouvableException, PackageVideException, MethodeUrlConflitException {
        String controllerPackage = config.getInitParameter("Package_of_controllers");
        String nom_servlet = config.getServletName();
        String text = "Scanning package: " + controllerPackage + " Nom du Servlet : " + nom_servlet;

        // Nous allons scanner toutes les classes dans le package donné dans WEB-INF/classes
        try {
            String path = "WEB-INF/classes/" + controllerPackage.replace('.', '/'); //normalement com.controller mivadika com/controller no ataony eto
            File directory = new File(getServletContext().getRealPath(path));
            if (directory.exists()) {
                text = text + " \n the real directory is : " + directory;
                // log(text);
                // log(String.valueOf(directory.isDirectory()));
                // log(String.valueOf(directory.isFile()));
                // log(String.valueOf(directory.list().length));
                // log(String.valueOf(directory.listFiles().length));
                scanDirectory(directory, controllerPackage);
                // text = text + "\n" + scanDirectory(directory, controllerPackage);
            } else {
                // AVADIKA THROW EXCEPTION - LE PACKAGE DE CONTROLEUR N'EXISTE PAS
                throw new PackageIntrouvableException ("Le repertoire : " + directory.getAbsolutePath() + " n'existe pas");
            }
        
        } catch (PackageIntrouvableException e) {
            throw e;
        } catch (PackageVideException e){
            throw e;
        }
        
        // return text;
    }


    // SCANNE LE REPERTOIRE DONNE ET INSERE DANS LA LISTE DES CONTROLEURS TOUTES LES CLASSES ANNOTEES AVEC @ControllerAnous
    private void scanDirectory(File directory, String packageName) throws PackageVideException, MethodeUrlConflitException {
        //String nom_fichier = "";

        // DANS LE CAS OU LE DOSSIER DE CONTROLEUR N'EST PAS VIDE
        if(directory.listFiles().length != 0){
            for (File file : directory.listFiles()) {
                // UN PEU DE RECURSIVITE
                if (file.isDirectory()) {
                    scanDirectory(file, packageName + "." + file.getName());
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(annotations.ControllerAnous.class)) {
                            // A CE STADE ON SAIT QUE LA CLASSE SELECTIONNEE EST UN CONTROLEUR
                            // listeControllers.add(clazz.getName());
                            Method[] methodes = clazz.getMethods();
                            for (Method m : methodes){
                                if (m.isAnnotationPresent(annotations.Get.class)){
                                    Get methodAnnotation = m.getAnnotation(annotations.Get.class);
                                    String attribut = methodAnnotation.url();
                                    if (methodeEtController.containsKey(methodAnnotation.url())) {
                                        // UNE URL EST UTILISEE POUR ANNOTER DEUX FONCTIONS DIFFERENTES
                                        throw new MethodeUrlConflitException ("Attention ! l'URI : \"/" + attribut + "\" est utilisee comme '@Get(url = \"/" + attribut + "') pour annoter deux methodes differentes.");
                                    }
                                    Mapping entry = new Mapping(clazz.getName(), m.getName());
                                    methodeEtController.put(attribut, entry);
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (MethodeUrlConflitException e) {
                        // e.printStackTrace();
                        throw e;
                    }
                }
            }
        } else {
            throw new PackageVideException ("Le package de controleur est vide !");
        }
    }






    public static Object execMethod(Object o, String methodName, Object[] params){
    
        Object a_retourner = new Object();
        
        if(params != null && params.length > 0){
            
            Class<?>[] parameterTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++){
                parameterTypes[i] = params[i].getClass();
            }
            try {
                Method methode = o.getClass().getDeclaredMethod(methodName, parameterTypes);
                a_retourner = methode.invoke(o, params);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            // retour = retour + "\n --> dia eto isika tafiditra ato anaty else";
            try {
                Method methode = o.getClass().getDeclaredMethod(methodName);
                // retour = retour + "\n \t --> dia eto indray isika mahazo ny methode : " + methodName;
                // retour = retour + "\n \t --> izay ho antsoin'ny objet : " + o.toString();
                // retour = retour + "\n \t --> ahazoantsika objet Methode vaovao : " + methode.toString();
                // System.out.println("4 - " + methode.toString());
                a_retourner = methode.invoke(o);
                // retour = retour + "\n \t --> dia andramantsika invoker-na ilay fonction : " + a_retourner.toString();

                return a_retourner;

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        // System.out.println("5 - " + retour.toString());
        return null;
    }


    public void thisExist(String url) throws JspIntrouvableException{
        File directory = new File(getServletContext().getRealPath(url));
        if (!directory.exists()) {
            throw new JspIntrouvableException("La vue : <u>" + url + "</u> est introuvable.");       
        }
    }

}