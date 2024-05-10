package mg.itu.prom16;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;


public class FrontServlet extends HttpServlet{
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    public void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        PrintWriter out = response.getWriter();

        out.println(request.getRequestURL());
    }
}