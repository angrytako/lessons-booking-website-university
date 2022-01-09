package com.happylearn.servlet;

import com.google.gson.Gson;
import com.happylearn.DAO.Corso;
import com.happylearn.DAO.DAO;
import com.happylearn.DAO.Docente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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


    }
}
