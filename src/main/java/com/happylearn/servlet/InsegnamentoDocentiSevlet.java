package com.happylearn.servlet;

import com.google.gson.Gson;
import com.happylearn.DAO.Corso;
import com.happylearn.DAO.DAO;
import com.happylearn.DAO.Docente;
import com.happylearn.DAO.InsegnamentoDocente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            System.out.println(professori.get(i).getId());
            ArrayList<Corso> corsi = DAO.showCoursesForTeachersDB(professori.get(i).getId());
            System.out.println(corsi);
            InsegnamentoDocente insegnamentoDocente = new InsegnamentoDocente(professori.get(i).getId(),corsi);
            insegnamentoDocenti.add(insegnamentoDocente);
            System.out.println(insegnamentoDocente.getCorsi());
        }


        Gson gson = new Gson();
        String json = gson.toJson(insegnamentoDocenti);
        out.print(json);
        response.setStatus(200);

        out.flush();
        return;
    }


}
