package com.noodle.noodle;


import DAO.Docente;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


@WebServlet(name = "ProfessoriServlet", value = "/ProfessoriServlet")
public class ProfessoriServlet extends SecuredHttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out =  jsonResponseSetup(response);
        if(!hasSession(request)) {
            json401ErrorResponse(response,out);
            return;
        }

        if (isAuthorized(request)){
            ArrayList<DAO.Docente> professori = DAO.DAO.queryShowAllDocentiDB();
            out.print(professoreToJson(professori));
            response.setStatus(200);
        }
        else{
            out.print("{\"error\":\"not authorized\"}");
            response.setStatus(401);
        }
        out.flush();
        return;

    }

    private String professoreToJson(ArrayList<Docente> docenti){
        Gson gson = new Gson();
        return gson.toJson(docenti);
    }



}

