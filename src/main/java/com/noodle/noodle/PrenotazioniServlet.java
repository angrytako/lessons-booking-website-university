package com.noodle.noodle;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "PrenotazioniServlet", value = "/PrenotazioniServlet")
public class PrenotazioniServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = jsonResponseSetup(response);
        if(!hasSession(request)) {
            json401ErrorResponse(response,out);
            return;
        }
        //getting the querystring variables as a map
        //if username is present, the list of prenotazioni will be given for that user,
        //if the user is the same as the one requesting, or it is an admin
        //otherwise it returns all prenotazioni, if the user is an admin
       Map<String, String[]> params = request.getParameterMap();
       if(params.containsKey("username")){
           String requestedUsername = params.get("username")[0];
           System.out.println(requestedUsername);
           if(isAuthorized(request, requestedUsername)) {
               ArrayList<DAO.Prenotazione> prenotazioni = DAO.DAO.queryShowUserPrenotazioniDB(requestedUsername);
               out.print(prenotazioniToJson(prenotazioni));
               response.setStatus(200);
           }
           else{
               out.print("{\"error\":\"not authorized\"}");
               response.setStatus(401);
           }
           out.flush();
           return;
       }else{
           if(isAuthorized(request)) {
               ArrayList<DAO.Prenotazione> prenotazioni = DAO.DAO.queryShowAllPrenotazioniDB();
               out.print(prenotazioniToJson(prenotazioni));
               response.setStatus(200);

           }
           else{
               out.print("{\"error\":\"not authorized\"}");
               response.setStatus(401);
           }
           out.flush();
           return;
       }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = jsonResponseSetup(response);
        if(!hasSession(request)) {
            json401ErrorResponse(response,out);
            return;
        }
    }

    private PrintWriter jsonResponseSetup(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        return out;
    }
    private void json401ErrorResponse(HttpServletResponse response,PrintWriter out) throws IOException {
        out.print("{\"error\":\"not authorized\"}");
        response.setStatus(401);
        out.flush();
        return;
        }
    private String prenotazioniToJson(ArrayList<DAO.Prenotazione> prenotazioni){
        Gson gson = new Gson();
        return gson.toJson(prenotazioni);
    }
    private boolean hasSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session != null && session.getAttribute("username")!=null && session.getAttribute("role")!=null)
            return true;
        else return false;
    }
    private boolean isAuthorized(HttpServletRequest request, String requestedUsername){
        HttpSession session = request.getSession();
        System.out.println(((String)session.getAttribute("username")).equals(requestedUsername));
        if(((String)session.getAttribute("role")).equals("amministratore") || session.getAttribute("username").equals(requestedUsername))
            return true;
        else return false;
    }
    private boolean isAuthorized(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(((String)session.getAttribute("role")).equals("amministratore"))
            return true;
        else return false;
    }
}
