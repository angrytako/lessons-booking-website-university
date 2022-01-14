package com.happylearn.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.happylearn.DAO.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;


@WebServlet(name = "InsegnamentoDocentiSevlet", value = "/InsegnamentoDocentiSevlet")
public class InsegnamentoDocentiSevlet extends SecuredHttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = jsonResponseSetup(response);
        if (!hasSession(request) || !isAdmin(request)) {
            json401ErrorResponse(response, out);
            return;
        }
        ArrayList<InsegnamentoDocente> insegnamentoDocenti = new ArrayList<InsegnamentoDocente>();
        ArrayList<Docente> professori = DAO.queryShowAllDocentiDB();
        for (int i=0; i<professori.size();i++){
            ArrayList<Corso> corsi = DAO.showCoursesForTeachersDB(professori.get(i).getId());
            InsegnamentoDocente insegnamentoDocente = new InsegnamentoDocente(professori.get(i).getId(),corsi);
            insegnamentoDocenti.add(insegnamentoDocente);
        }


        Gson gson = new Gson();
        String json = gson.toJson(insegnamentoDocenti);
        out.print(json);
        response.setStatus(200);

        out.flush();
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("sono in insegnamento docenti");
        PrintWriter out = jsonResponseSetup(response);
        if(!hasSession(request) || !isAdmin(request)) {
            json401ErrorResponse(response,out);
            return;
        }
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }
        String data = buffer.toString();
        JsonObject jobj = new Gson().fromJson(data, JsonObject.class);

        // 1. JSON file to Java object
        String materia = jobj.get("corso").getAsString();
        Integer idDocente = jobj.get("docente").getAsInt();

        if(materia == null || idDocente == null){
            out.print("{\"error\":\"Missing params\"}");
            response.setStatus(400);
            out.flush();
            return;
        }
        //check to see if both teacher and course exist
        Docente docente = DAO.queryGetDocente(idDocente);
        Corso corso = DAO.queryGetCorso(materia);

        if(docente == null || corso == null){
            out.print("{\"error\":\"Specified parameters do not exist\"}");
            response.setStatus(400);
            out.flush();
            return;
        }
        //TODO se insegnamento esiste, bisogna fare l'update!
        if(DAO.queryAddInsegnamentoDB(materia,idDocente)){
            out.print("{\"message\":\"Success\"}");
            response.setStatus(200);
        }
        else{
            out.print("{\"error\":\"Unexpected error\"}");
            response.setStatus(500);
        }
        out.flush();
        return;
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = jsonResponseSetup(response);
        if(!hasSession(request) || !isAdmin(request)) {
            json401ErrorResponse(response,out);
            return;
        }
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }
        String data = buffer.toString();
        JsonObject jobj = new Gson().fromJson(data, JsonObject.class);

        // 1. JSON file to Java object
        String materia = jobj.get("corso").getAsString();
        Integer idDocente = jobj.get("docente").getAsInt();

        if(materia == null || idDocente == null){
            out.print("{\"error\":\"Missing params\"}");
            response.setStatus(400);
            out.flush();
            return;
        }
        //check if teaching exists
        Insegnamento ins = DAO.queryGetInsegnamento(idDocente,materia);
        if(ins == null){
            out.print("{\"error\":\"Specified resource does not exist\"}");
            response.setStatus(400);
            out.flush();
            return;
        }
        if(DAO.queryDeleteInsegnamentoDB(materia,idDocente)){
            out.print("{\"message\":\"Success\"}");
            response.setStatus(200);
        }
        else{
            out.print("{\"error\":\"Unexpected error\"}");
            response.setStatus(500);
        }
        out.flush();
        return;

    }


}
