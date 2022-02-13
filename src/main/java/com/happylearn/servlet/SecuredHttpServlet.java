package com.happylearn.servlet;



import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class SecuredHttpServlet extends HttpServlet {
    /*
     * serve per dire che sto mandando json nella risposta --> chiave content-type, valore application/json
     */
    protected PrintWriter jsonResponseSetup(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        return out;
    }
    protected boolean hasSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session != null && session.getAttribute("username")!=null && session.getAttribute("role")!=null)
            return true;
        else return false;
    }
    protected void json401ErrorResponse(HttpServletResponse response,PrintWriter out) throws IOException {
        out.print("{\"error\":\"not authorized\"}");
        response.setStatus(401);
        out.flush();
        return;
    }

    protected boolean isAuthorized(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(((String)session.getAttribute("role")).equals("amministratore"))
            return true;
        else return false;
    }
    protected boolean isAuthorized(HttpServletRequest request, String requestedUsername){
        HttpSession session = request.getSession();
//        System.out.println(((String)session.getAttribute("username")).equals(requestedUsername));
        if(((String)session.getAttribute("role")).equals("amministratore") || session.getAttribute("username").equals(requestedUsername))
            return true;
        else return false;
    }
    protected boolean isAdmin(HttpServletRequest request){
        return request.getSession().getAttribute("role").equals("amministratore");
    }



}
