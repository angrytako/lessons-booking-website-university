package com.happylearn.servlet;

import com.happylearn.DAO.DAO;
import com.happylearn.DAO.Insegnamento;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "TeachingsServlet", value = "/TeachingsServlet")
public class TeachingsServlet extends SecuredHttpServlet {
	/**
	 * This method shows all teaching (which are not deleted).
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("tutto bene");

		PrintWriter out =  jsonResponseSetup(response);
		if(!hasSession(request)) {
			json401ErrorResponse(response,out);
			return;
		}

		if (isAuthorized(request)){
			ArrayList<Insegnamento> teaching = DAO.queryShowAllInsegnamentiDB(false);
			out.print(teachingToJson(teaching));
			response.setStatus(200);
		}
		else{
			out.print("{\"error\":\"not authorized\"}");
			response.setStatus(401);
		}
		out.flush();
		return;
	}

	/**
	 * This method adds a new teaching.
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("tutto bene anche qui");

		PrintWriter out = jsonResponseSetup(response);
		if(!hasSession(request)) {
			json401ErrorResponse(response,out);
			return;
		}
		if (isAuthorized(request)) {
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
			String nome =  jobj.get("nome").getAsString();
			String cognome =  jobj.get("cognome").getAsString();

			if (nome!=null&&cognome!=null)
			{
				if(DAO.queryAddDocenteDB(nome,cognome)){
					int id = DAO.queryShowOneDocenteDB(nome, cognome);
					if (id!=-1){
						jobj.addProperty("id",id);
						out.print(jobj.toString());
						response.setStatus(200);
					}else{
						out.print("{\"error\":\"error search id\"}");response.setStatus(401);
					}
				}else{
					out.print("{\"error\":\"error query\"}");response.setStatus(401);
				}
			}
			else {
				out.print("{\"error\":\"error in nome,cognome\"}");response.setStatus(401);
			}

		}else{
			out.print("{\"error\":\"not authorized\"}");response.setStatus(401);
		}

		out.flush();
	}

	/**
	 * This method deletes a teaching.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("andiamo alla grandeee");
	}


	private String teachingToJson(ArrayList<Insegnamento> teaching) {
		Gson gson = new Gson();
		return gson.toJson(teaching);
	}
}
