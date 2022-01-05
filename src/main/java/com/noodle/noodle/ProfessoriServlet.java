package com.noodle.noodle;


import DAO.Docente;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = jsonResponseSetup(resp);
        if(!hasSession(req)) {
            json401ErrorResponse(resp,out);
            return;
        }
        if (isAuthorized(req)) {
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.lineSeparator());
            }
            String data = buffer.toString();
            JsonObject jobj = new Gson().fromJson(data, JsonObject.class);

            // 1. JSON file to Java object
            String nome =  jobj.get("nome").getAsString();
            String cognome =  jobj.get("cognome").getAsString();

            if (nome!=null&&cognome!=null)
            {
                DAO.DAO.queryAddDocenteDB(nome,cognome);
                resp.setStatus(200);
            }
            else {
                out.print("{\"error\":\"not had corso\"}");resp.setStatus(401);
            }



        }else{
            out.print("{\"error\":\"not authorized\"}");resp.setStatus(401);
        }

        out.flush();

    }




    private String professoreToJson(ArrayList<Docente> docenti){
        Gson gson = new Gson();
        return gson.toJson(docenti);
    }



}

