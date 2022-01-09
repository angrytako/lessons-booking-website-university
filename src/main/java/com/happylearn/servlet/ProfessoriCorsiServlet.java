package com.happylearn.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.happylearn.DAO.Corso;
import com.happylearn.DAO.DAO;
import com.happylearn.DAO.Docente;
import com.happylearn.DAO.Insegnamento;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "ProfessoriCorsiServlet", value = "/ProfessoriCorsiServlet")
public class ProfessoriCorsiServlet extends SecuredHttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = jsonResponseSetup(response);
        if(!hasSession(request) || !isAdmin(request)) {
            json401ErrorResponse(response,out);
            return;
        }
        /*querystring must have corso or docente, otherwise is a bad request*/
        Map<String, String[]> params = request.getParameterMap();
        if(params.containsKey("corso")){
            String corso = params.get("corso")[0];
            ArrayList<Docente> docenti = DAO.showDocentiForCourseDB(corso);
            Gson gson = new Gson();
            String json =  gson.toJson(docenti);
            out.print(json);
            response.setStatus(200);
        }
        else if(params.containsKey("docente")){
            int docenteId = Integer.parseInt(params.get("docente")[0]);
            ArrayList<Corso> corsi = DAO.showCoursesForTeachersDB(docenteId);
            Gson gson = new Gson();
            String json =  gson.toJson(corsi);
            out.print(json);
            response.setStatus(200);
        }
        else{
            out.print("{\"error\":\"Missing querystring params\"}");
            response.setStatus(400);
            }
        out.flush();
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String materia = jobj.get("materia").getAsString();
        Integer idDocente = jobj.get("idDocente").getAsInt();

        if(materia == null || idDocente == null){
            out.print("{\"error\":\"Missing params\"}");
            response.setStatus(400);
            out.flush();
            return;
        }
        //check to see if both teacher and course exist
        Docente docente = DAO.queryGetDocente(idDocente);
        Corso corso = DAO.queryGetCorso(materia);
        if(docente != null || corso == null){
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
        String materia = jobj.get("materia").getAsString();
        Integer idDocente = jobj.get("idDocente").getAsInt();

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
