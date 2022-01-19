package com.happylearn.servlet;

import com.happylearn.DAO.Utente;
import com.happylearn.DAO.DAO;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "AllUsersServlet", value = "/AllUsersServlet")
public class AllUsersServlet extends SecuredHttpServlet {
	/**
	 * This method returns a json object with all clients and the admin who invoking servlet.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = jsonResponseSetup(response);
		if(!hasSession(request)) {
			json401ErrorResponse(response,out);
			return;
		}
		if(isAdmin(request)) {
			String admin = (String) request.getSession().getAttribute("username");
			ArrayList<Utente> allUsers = DAO.queryShowAllUsersDB();
			ArrayList<Utente> allUsersFiltered = filterAdmin(allUsers, admin);
			out.print(usersToJson(allUsersFiltered));
			response.setStatus(200);
		}
		else{
			out.print("{\"error\":\"not authorized\"}");
			response.setStatus(401);
		}
		out.flush();
	}

	private ArrayList<Utente> filterAdmin(ArrayList<Utente> allUsers, String admin) {
		ArrayList<Utente> result = new ArrayList<>() ;
		result.add(DAO.getUser(admin));
		for(Utente u : allUsers) {
			if(!u.getRuolo().equals("amministratore"))
				result.add(new Utente(u.getUsername(), u.getPassword(), u.getRuolo()));
		}
		return result;
	}

	private String usersToJson(ArrayList<Utente> allUsersFiltered){
		Gson gson = new Gson();
		return gson.toJson(allUsersFiltered);
	}
}