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



import annotations.ControllerAnous;
import annotations.Get;

import mg.itu.prom16.Mapping;
import mg.itu.prom16.ModelView;


public class FrontServlet extends HttpServlet{

    //HashMap pour stocker tous les methodes et leur classe
    private final HashMap<String, Mapping> methodeEtController = new HashMap<>();



    @Override
    public void init() throws ServletException{
        //super.init();
        ServletConfig config = getServletConfig();
        
        // Cette 
        scanner(config);
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

        out.println(request.getRequestURL());
        String uri = request.getRequestURI();
        // out.println(request.getRequestURI());
        // out.println(uri);

        boolean checker = false;

        for(Entry<String, Mapping> entree: methodeEtController.entrySet() ){
            // out.println("/TestSprint/" + entree.getKey());
            if(uri.equals("/TestSprint/" + entree.getKey())){
                out.println("------------ LISTING DES CONTROLEURS ------------");
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
                    HashMap<String, Object> data = mv.getData();

                    if(data.isEmpty()){
                        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                        dispatcher.forward(request, response);
                    } else {
                        Set<Entry<String, Object>> entrees = data.entrySet();
                        for (Entry<String, Object> entrie : entrees) {
                            request.setAttribute(entrie.getKey(), entrie.getValue());
                        }
                        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                        dispatcher.forward(request, response);
                    }
                }
                               
                
                // out.println("<p> La fonction " + result.getMethode() + " retourne : " + retour);

                checker = true;

                break;
            } else {
                out.println("Methode introuvable !!");
            }
        }
        if (checker == false){
            out.println("Erreur!! La methode et la classe est introuvable !");
        }
    


        

       




        // --------------- SPRINT 1 ------------------
        // ServletConfig config = getServletConfig();
        // String info_servlet = scanner(config);
        // out.println(info_servlet);
    }

    private void scanner(ServletConfig config) {
        String controllerPackage = config.getInitParameter("Package_of_controllers");
        // String nom_servlet = config.getServletName();
        // String text = "Scanning package: " + controllerPackage + " Nom du Servlet : " + nom_servlet;

        // Nous allons scanner toutes les classes dans le package donné dans WEB-INF/classes
        try {
            String path = "WEB-INF/classes/" + controllerPackage.replace('.', '/'); //normalement com.controller mivadika com/controller no ataony eto
            File directory = new File(getServletContext().getRealPath(path));
            if (directory.exists()) {
                // text = text + " \n the real directory is : " + directory;
                scanDirectory(directory, controllerPackage);
                // text = text + "\n" + scanDirectory(directory, controllerPackage);
            } else {
                System.out.println("Directory does not exist: " + directory.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return text;
    }


    // SCANNE LE REPERTOIRE DONNE ET INSERE DANS LA LISTE DES CONTROLEURS TOUTES LES CLASSES ANNOTEES AVEC @ControllerAnous
    private void scanDirectory(File directory, String packageName) {
        //String nom_fichier = "";
        for (File file : directory.listFiles()) {

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
                                Mapping entry = new Mapping(clazz.getName(), m.getName());
                                methodeEtController.put(attribut, entry);
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object/* String */ execMethod(Object o, String methodName, Object[] params){
        // Object retour = new Object();
        Object a_retourner = new Object();
        // String retour;
        // retour = "Tafiditra ato anaty execMethod";
        if(params != null && params.length > 0){
            // retour = retour + "\n --> dia eto isika tafiditra ato anaty if";
            Class<?>[] parameterTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++){
                parameterTypes[i] = params[i].getClass();
            }
            try {
                Method methode = o.getClass().getDeclaredMethod(methodName, parameterTypes);
        //         System.out.println("3 - " + methode.toString());
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



}