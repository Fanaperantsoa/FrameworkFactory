package mg.itu.prom16;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.MalformedParametersException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.String;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
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

import annotations.*;
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
        } catch (Exception e) {
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

            StringBuffer requestURL = request.getRequestURL();
            String[] urlSplitted = requestURL.toString().split("/");
            String entri = urlSplitted[urlSplitted.length-1];
            if (urlSplitted.length <= 4) {
                entri =  "/";
            }

            

            try {
                for(Entry<String, Mapping> entree: methodeEtController.entrySet() ){
                    if(entri.equals(entree.getKey())){
                        // out.println("------------ LISTING DES CONTROLEURS ------------");
                        Mapping result = entree.getValue();
                        
            
                        Class<?> classe = Class.forName(result.getClasse());
                        log ("1 - La classe est : " + classe.toString());
                        
                        Object retour;
                        try {
                            retour = execMethod(classe, result, request);

                            Class<?> kilasy = retour.getClass();
    
                            if(kilasy.equals(String.class)/*  && method.getParameters() == null */){
                                String retourFonction = (String)retour;
                                out.println("La fonction " + result.getMethode() + " retourne --> \"" + retourFonction + "\"");
                            } else if (kilasy.equals(ModelView.class)){
                                log ("2- Le type de retour est : " + kilasy.toString());
                                ModelView mv = (ModelView) retour;
                                String url = mv.getUrl();
                                log ("3- L'URL de destination est : " + url);
                                try {
                                    // NECESSAIRE POUR LANCER UNE EXCEPTION SPECIFIQUE AU CAS OU LE .jsp N'EXISTE PAS
                                    thisExist(url);
                                    HashMap<String, Object> data = mv.getData();
                                    log ("4- Le fichier JSP existe");
                                    
                                    log ("5- Le contenu de data est : " + data.isEmpty());
                                    
                                    log ("6- Le contenu de data est : " + data.values().toString());


                                    // ON AJOUTE LES PARAMETRES POUR L'ENVOI
                                    if(!data.isEmpty()){
                                        log ("7- data n'est pas vide");
                                        Set<Entry<String, Object>> entrees = data.entrySet();
                                        for (Entry<String, Object> entrie : entrees) {
                                            log ("8_1 - La cle est : " + entrie.getKey());
                                            log ("8_2 - La valeur est : " + entrie.getValue());
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
                                    throw new TypeRetourException("La methode : \"" + result.getMethode() + "\" associee a cette URI : '<u>" + uri + "</u>' retourne un type que l'application ne reconnait pas.");
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
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                                | NoSuchMethodException | SecurityException e) {
                            // TODO Auto-generated catch block
                            log("Tandremo fa misy Exception mitranga eto : " + e.getMessage());
                            e.printStackTrace();

                        } catch (NoAnnotationParamException e) {
                            log("Une autre erreur est survenue : " + e.getMessage(), e);
            
                            response.setContentType("text/html");
                            response.setCharacterEncoding( "UTF-8" );
                                
                            out.println("<!DOCTYPE html>");
                            out.println("<html>");
                            out.println("<head>");
                            out.println("<meta charset=\"utf-8\" />");
                            out.println("<title>Erreur ! Parametre de methode non annote.</title>");
                            out.println("</head>");
                            out.println("<body style=\"text-align:center\">");
                            out.println("<h1 style=\"font-family:Courier New \">» Erreur!! Parametre de methode non annote</h1>");
                            out.println("<h3>Le(s) parametre(s) de la methode n'est(ne sont) pas annote(s).</h3>");
                            out.println("<p style=\"font-style:italic\">" + e.getMessage() + "</p>");
                            out.println("</body>");
                            out.println("</html>");

                        } catch (NoArgumentFoundException e) {
                            log("Une autre erreur est survenue : " + e.getMessage(), e);
            
                            response.setContentType("text/html");
                            response.setCharacterEncoding( "UTF-8" );
                                
                            out.println("<!DOCTYPE html>");
                            out.println("<html>");
                            out.println("<head>");
                            out.println("<meta charset=\"utf-8\" />");
                            out.println("<title>Erreur ! Parametre de la requete non prise en charge.</title>");
                            out.println("</head>");
                            out.println("<body style=\"text-align:center\">");
                            out.println("<h1 style=\"font-family:Courier New \">» Erreur!! Parametre de la requete non prise en charge.</h1>");
                            out.println("<h3>Le(s) parametre(s) de la requete ne correspondent a aucun nom de variable.</h3>");
                            out.println("<p style=\"font-style:italic\">" + e.getMessage() + "</p>");
                            out.println("</body>");
                            out.println("</html>");
                        }
                        
                        
                        methode_trouvee = true;
                        log ("9 - L'instance est : " + String.valueOf(methode_trouvee));
                        log ("--------------------------- VOILA ! ----------------------------");
                        log (" ");
                        // out.println("9 - " + String.valueOf(methode_trouvee));

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
            } catch (IndexOutOfBoundsException e) {
                log("Exception capturee dans la servlet : " + e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                log("La classe specifiee est introuvable : " + e.getMessage(), e);
            } catch (SecurityException e) {
                log("Acces a la methode refuse : " + e.getMessage(), e);
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
                
            } else {
                // LE PACKAGE DE CONTROLEUR N'EXISTE PAS
                throw new PackageIntrouvableException ("Le repertoire : " + directory.getAbsolutePath() + " n'existe pas");
            }
        
        } catch (PackageIntrouvableException e) {
            throw e;
        } catch (PackageVideException e){
            throw e;
        }
        
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
                                    // Mapping entry = new Mapping(clazz.getName(), m.getName());
                                    Mapping entry = new Mapping(clazz.getName(), m.getName());
                                    methodeEtController.put(attribut, entry);
                                } else if (m.isAnnotationPresent(annotations.Post.class)){
                                    Post methodAnnotation = m.getAnnotation(annotations.Post.class);
                                    String attribut = methodAnnotation.url();
                                    if (methodeEtController.containsKey(methodAnnotation.url())) {
                                        // UNE URL EST UTILISEE POUR ANNOTER DEUX FONCTIONS DIFFERENTES
                                        throw new MethodeUrlConflitException ("Attention ! l'URI : \"/" + attribut + "\" est utilisee comme '@Post(url = \"/" + attribut + "') pour annoter deux methodes differentes.");
                                    }
                                    // Mapping entry = new Mapping(clazz.getName(), m.getName());
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





    private Object execMethod(Class<?> classe, Mapping mapping, HttpServletRequest request) throws NoAnnotationParamException, NoArgumentFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    
        Object a_retourner = new Object();

        // log (" ------------- ON EST ENTREE DANS execMethod() ------------------");

        try {
            
            Object instanceClass = classe.getDeclaredConstructor().newInstance();
            log ("       a) l'instance de la classe est : " + instanceClass.toString());

            Boolean paramExist = false;
            
            Method[] methodes = classe.getDeclaredMethods();
            log ("       b) les methodes declaree dans la classe sont : " + methodes.toString());

            Method m = null;
            for (Method methodi : methodes) {
                if (methodi.getName().equals(mapping.getMethode())) {
                    paramExist = methodi.getParameterCount() > 0;
                    m = methodi;

                    log ("       c) la methode trouvee est : " + m.toString());
                    break;
                }
            }

            

            // log(String.valueOf(method.getParameterCount()));
            if(paramExist){
                log ("       d) la methode exige un ou des parametre(s)");
                // TABLEAU DES PARAMETRE DE LA METHODE
                Parameter[] parametres = m.getParameters();
                log ("       e) ces parametres sont : " + parametres.toString());
                
                // VARIABLE QUI CONTIENDRA LES VALEURS DE PARAMETRE A DONNER POUR LA METHODE
                // Object[] params = new String[parametres.length];
                Object[] params = new Object[parametres.length];
                log ("       f) la taille de params est : " + String.valueOf(params.length));


                for(int i=0; i < m.getParameterCount(); i++) {
                    params[i] = null;
                    Enumeration<String> nomParametresEnvoyees = request.getParameterNames();
                    
                        
                      
                    String paramName = parametres[i].getName();
                    log ("       g) le nom du parametre selectionnee est : " + paramName);
                    if ((parametres[i].isAnnotationPresent(Param.class) && nomParametresEnvoyees != null) || (!(parametres[i].isAnnotationPresent(Param.class) && nomParametresEnvoyees == null))) {    
                        paramName = parametres[i].getAnnotation(Param.class).name(); 
                        log ("       g) le nom du parametre selectionnee DEVRAIT ETRE : " + parametres[i].getAnnotation(Param.class).name());
                        log ("        -- le nom du parametre selectionnee est : " + paramName);
                    } else {    
                        throw new NoAnnotationParamException("ETU 0057 : Les parametres ne sont pas annotees ou alors la requete ne contient aucun parametre !!");    
                    }
    
                    if (parametres[i].getType().isAnnotationPresent(Objet.class)) {  
                        log ("       h) le type de cette parametre est : " + parametres[i].getType().getName());  
                        Class<?> classiko = Class.forName(parametres[i].getParameterizedType().getTypeName());
                        log ("       i) le type de cette parametre est : " + parametres[i].getParameterizedType().getTypeName());
                        log ("       j) le type de cette parametre est : " + classiko.getName());
                        
                        Object obj = classiko.getDeclaredConstructor().newInstance();
                        log ("       k) la nouvelle instance de cette classe est : " + obj.toString());  

                        Field[] fields = obj.getClass().getDeclaredFields();
                        log ("       l) le nombre d'attribut de cet objet est : " + fields.length);

                        Object[] valueObjects = new String[fields.length];
                        
                        boolean paramCorrespondAnnotation = false;
                        boolean paramCorrespondNomVariable = false;
                        while (nomParametresEnvoyees.hasMoreElements()) {
                            String currentParamName = nomParametresEnvoyees.nextElement();
                            
                            log ("       m) le nom du parametre envoye est : " + currentParamName);
                            log ("       m_2) le nom du parametre envoye est : " + paramName);
                            for (int k = 0; k < fields.length; k++) {
                                log("           le nom de l'attribut est : " + fields[k].getName());
                                // TANDREMO ITO MANARAKA ITO FA METY MITERAKA => java.lang.NullPointerException: Cannot invoke "annotations.AttribObjet.value()" because the return value of "java.lang.reflect.Field.getAnnotation(java.lang.Class)" is null
                                // REHEFA MISY FIELD NEFA TSY ANNOTEE @AttribObject
                                // log("           la valeur de son annotation est : " + fields[k].getAnnotation(AttribObjet.class).value());

                                log("           =================> : " + currentParamName.startsWith(paramName + "."));
                                if (currentParamName.startsWith(paramName + ".")) {
                                    int indiceNext = (paramName + ".").length();
                                    log ("       n) l'index de depart de la lecture de l'attribut est : " + indiceNext);
                                    String attribName = currentParamName.substring(indiceNext);
                                    log ("       ------ le nom du parametre envoye est : " + currentParamName);
                                    log ("       o) le nom de l'attribut est donc : " + attribName);
                                    if (fields[k].isAnnotationPresent(AttribObjet.class)) {
                                        log ("       --- la valeur de l'annotation est quand a elle : " + fields[k].getAnnotation(AttribObjet.class).value());
                                        log ("       =================> : " + attribName.equals(fields[k].getAnnotation(AttribObjet.class).value()));
                                        if (attribName.equals(fields[k].getAnnotation(AttribObjet.class).value())) {
                                            valueObjects[k] = request.getParameter(currentParamName);
                                            paramCorrespondAnnotation = true;
                                            log ("       p) la valeur du parametre est : " + valueObjects[k]);
                                            break;
                                        } else {
                                            // Ceci voudra dire que dans cette tour de boucle aucune correspondance n'a ete trouvee --> si au bout de la boucle, cette valeur reste false alors une Exception va etre throwee un peu plus bas
                                            paramCorrespondAnnotation = false;
                                        }
                                    } else {
                                        if (attribName.equals(fields[k].getName())) {
                                            valueObjects[k] = request.getParameter(currentParamName);
                                            paramCorrespondNomVariable = true;
                                            log ("       p') la valeur du parametre est : " + valueObjects[k]);
                                        } else {
                                            paramCorrespondNomVariable = false;
                                        }
                                    }
                                }
                            }
                            if (!paramCorrespondAnnotation && !paramCorrespondNomVariable) {
                                // Il y a eu au moins un parametre dans la requete qui n'a correspondu a aucun annotation ou nom d'attribut
                                throw new NoArgumentFoundException("Il y a au moins UN nom de parametre envoye dans la requete qui ne correspond ni a la valeur de l'annotation de l'attribut : '" + currentParamName + "' de l'instance : '" + obj.toString() + "' de la classe '" + classiko.getName() + "'  donne en argument a la methode '" + m.toString() + "' liee a cet URL, ni ne correspond au nom du parametre de cette methode.");
                            }
                        }
                        obj = process(obj, valueObjects);
                        params[i] = obj;

                    } else { 
                        // log ("       h') le type de cette parametre est : " + parametres[i].getType().getName());  
                        while(nomParametresEnvoyees.hasMoreElements()) {
                            String currentParamName = nomParametresEnvoyees.nextElement();
                            if(currentParamName.equalsIgnoreCase(paramName)){    
                                params[i] = request.getParameter(paramName);    
                                break;    
                            } else if (currentParamName.equalsIgnoreCase(parametres[i].getName())) {
                                params[i] = request.getParameter(currentParamName);  
                                break;  
                            } else {
                                throw new NoArgumentFoundException("Le nom de parametre envoye dans la requete ne correspond pas a la valeur de l'annotation de la methode '" + m.toString() + "' liee a cet URL ; ni aux noms des arguments de la methode non plus.");
                            }

                        }
                    }
                }
                
                Class<?>[] parameterType = new Class<?>[m.getParameterCount()];

                for (int i = 0; i < m.getParameterCount(); i++) {
                    parameterType[i] = params[i].getClass();
                }

                Method methodPourObjet = classe.getDeclaredMethod(mapping.getMethode(), parameterType);


                a_retourner = methodPourObjet.invoke(instanceClass, params);
                
            } else {
                a_retourner = m.invoke(instanceClass);
            }

        } catch (NoAnnotationParamException e) {
            log("Exception capturee dans la servlet : " + e.getMessage(), e);
            e.printStackTrace();
            throw e;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return a_retourner;
    }




    

    public void thisExist(String url) throws JspIntrouvableException{
        File directory = new File(getServletContext().getRealPath(url));
        if (!directory.exists()) {
            throw new JspIntrouvableException("La vue : <u>" + url + "</u> est introuvable.");       
        }
    }

    public static <T> T process (T obj, Object[] valuesAttribut) throws IllegalArgumentException, IllegalAccessException {
        Class<?> classe = obj.getClass();
        Field[] fields = classe.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Object valeur = valuesAttribut[i];
            if (valeur != null) {
                field.set(obj, valeur);
            } else {
                field.set(obj, null);
            }
        }
        return obj;
    }



}