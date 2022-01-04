package com.noodle.noodle;

import DAO.Prenotazione;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;


@WebServlet(name = "ProfessoriServlet", value = "/ProfessoriServlet")
public class ProfessoriServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = jsonResponseSetup(response);
        if(!hasSession(request)) {
            json401ErrorResponse(response,out);
            return;
        }

        if (isAuthorized(request)){
            ArrayList<DAO.Docente> professori = DAO.DAO.queryShowAllDocentiDB();
            System.out.println(professori);
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

    private PrintWriter jsonResponseSetup(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        return out;
    }
    private boolean hasSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session != null && session.getAttribute("username")!=null && session.getAttribute("role")!=null)
            return true;
        else return false;
    }
    private void json401ErrorResponse(HttpServletResponse response,PrintWriter out) throws IOException {
        out.print("{\"error\":\"not authorized\"}");
        response.setStatus(401);
        out.flush();
        return;
    }

    private boolean isAuthorized(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(((String)session.getAttribute("role")).equals("amministratore"))
            return true;
        else return false;
    }

    private String professoreToJson(ArrayList<DAO.Docente> docenti){
        Gson gson = new Gson();
        return gson.toJson(docenti);
    }

}

