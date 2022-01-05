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
public class PrenotazioniServlet extends SecuredHttpServlet {
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


    private String prenotazioniToJson(ArrayList<DAO.Prenotazione> prenotazioni){
        Gson gson = new Gson();
        return gson.toJson(prenotazioni);
    }



}
