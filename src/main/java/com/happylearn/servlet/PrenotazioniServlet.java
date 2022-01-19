package com.happylearn.servlet;
import com.happylearn.support.PrenotazioneDocenteRuolo;
import com.happylearn.DAO.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "PrenotazioniServlet", value = "/PrenotazioniServlet")
public class PrenotazioniServlet extends SecuredHttpServlet {
	// ritorna le prenotazioni nel database.
	// Se c'è lo username ritorna TUTTE le SUE prenotazioni
	// Se NON c'è lo username le ritorna TUTTE tranne quelle degli admin
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = jsonResponseSetup(response);
		if(!hasSession(request)) {
			json401ErrorResponse(response,out);
			return;
		}
		//getting the querystring variables as a map
		//if username is present, the list of prenotazioni will be given for that user, if the user is the same as the one requesting, or it is an admin
		//otherwise it returns all prenotazioni, if the user is an admin
		Map<String, String[]> params = request.getParameterMap();
		if(params.containsKey("username")){
			String requestedUsername = params.get("username")[0];
			System.out.println(requestedUsername);
			if(isAuthorized(request, requestedUsername)) {
				ArrayList<PrenotazioneDocenteRuolo> prenotazioni = DAO.queryShowUserPrenotazioniDB(requestedUsername);
				//if the requested user is an admin, you cannot get the information, unless it's yourself
				if(prenotazioni.get(0).getRuolo().equals("amministratore") && !prenotazioni.get(0).getUtente().equals( request.getSession().getAttribute("username"))){
					out.print("{\"error\":\"not authorized\"}");
					response.setStatus(401);
					out.flush();
					return;
				}
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
				ArrayList<PrenotazioneDocenteRuolo> prenotazioni = DAO.queryShowAllPrenotazioniDB();
				ArrayList<PrenotazioneDocenteRuolo> prenotazioniFiltered = filterAdmin(prenotazioni);
				out.print(prenotazioniToJson(prenotazioniFiltered));
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

	// aggiunge una prenotazione al database
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = jsonResponseSetup(response);
		if(!hasSession(request)) {
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
		String username = jobj.get("utente").getAsString();
		String corso = jobj.get("corso").getAsString();
		Integer idDocente = jobj.get("idDocente").getAsInt();
		Integer giorno = jobj.get("giorno").getAsInt();
		Integer orario = jobj.get("orario").getAsInt();
		if(username == null || corso == null || idDocente == null || giorno == null || orario == null)
		{
			out.print("{\"error\":\"Missing params\"}");
			response.setStatus(400);
			out.flush();
			return;
		}

		//check if booking exists, and if it does, show error
		PrenotazioneDocenteRuolo prenotazione = DAO.queryGetPrenotazioneDB(corso,username,giorno,orario);
		if(prenotazione != null){
			out.print("{\"error\":\"Resource already exists\"}");
			response.setStatus(400);
			out.flush();
			return;
		}

		if(isAuthorized(request, username)) {
			//if the user whose data is being changed is admin, you cannot change it, unless you are that admin
			//TODO: un admin non può prenotare per un altro admin
//			if(isAdmin(request) && !prenotazione.getUtente().equals( request.getSession().getAttribute("username"))){
//				out.print("{\"error\":\"not authorized\"}");
//				response.setStatus(401);
//				out.flush();
//				return;
//			}
			if(DAO.queryAddPrenotazioneDB(corso,idDocente,username,giorno,orario)){
				out.print("{\"message\":\"Success\"}");
				response.setStatus(200);
			}
			else{
				out.print("{\"error\":\"Unexpected error\"}");
				response.setStatus(500);
			}

		}
		else{
			out.print("{\"error\":\"not authorized\"}");
			response.setStatus(401);
		}
		out.flush();
		return;
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = jsonResponseSetup(response);
		if(!hasSession(request)) {
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
		String username = jobj.get("utente").getAsString();
		String corso = jobj.get("corso").getAsString();
		Integer idDocente = jobj.get("idDocente").getAsInt();
		String stato = jobj.get("stato").getAsString();
		Integer giorno = jobj.get("giorno").getAsInt();
		Integer orario = jobj.get("orario").getAsInt();
		if(username == null || corso == null || idDocente == null || stato == null || giorno == null || orario == null)
		{
			out.print("{\"error\":\"Missing params\"}");
			response.setStatus(400);
			out.flush();
			return;
		}
		//check if booking exists, and if not show error
		PrenotazioneDocenteRuolo prenotazione = DAO.queryGetPrenotazioneDB(corso,username,giorno,orario);
		System.out.println(prenotazione);
		if(prenotazione == null)
		{
			out.print("{\"error\":\"Requested field does not exist\"}");
			response.setStatus(400);
			out.flush();
			return;
		}
		if(isAuthorized(request, username)) {
				//if the user whose data is being changed is admin, you cannot change it, unless you are that admin
				if(prenotazione.getRuolo().equals("amministratore") && !prenotazione.getUtente().equals( request.getSession().getAttribute("username"))){
					out.print("{\"error\":\"not authorized\"}");
					response.setStatus(401);
					out.flush();
					return;
				}
				if(!stato.equals("cancellata") && !stato.equals("effettuata")){
					out.print("{\"error\":\"Requested status is not a valid status\"}");
					response.setStatus(400);
					out.flush();
					return;
				}
				if(doRightUpdate(prenotazione,stato)){
					out.print("{\"message\":\"Success\"}");
					response.setStatus(200);
					out.flush();
					return;
				}
				else{
					out.print("{\"error\":\"Unexpected error\"}");
					response.setStatus(500);
					out.flush();
					return;
				}

			}
			else{
				out.print("{\"error\":\"not authorized\"}");
				response.setStatus(401);
				out.flush();
				return;
			}
	}
	private String prenotazioniToJson(ArrayList<PrenotazioneDocenteRuolo> prenotazioni){
		Gson gson = new Gson();
		return gson.toJson(prenotazioni);
	}
	private ArrayList<PrenotazioneDocenteRuolo> filterAdmin(ArrayList<PrenotazioneDocenteRuolo> prenotazioni){
		List<PrenotazioneDocenteRuolo> prenotazioniFiltered =   prenotazioni.stream()
				.filter((prenotazione) -> !prenotazione.getRuolo().equals("amministratore"))
				.collect(Collectors.toList());
		ArrayList<PrenotazioneDocenteRuolo> result = new ArrayList<>() ;
		for(PrenotazioneDocenteRuolo p : prenotazioniFiltered)
			result.add(new PrenotazioneDocenteRuolo(p.getCorso(),p.getIdDocente(),p.getNomeDocente(),p.getCognomeDocente(),p.getUtente(),p.getStato(),p.getGiorno(),p.getOrario(),p.getRuolo()));
		return result;

	}
	private boolean doRightUpdate(PrenotazioneDocenteRuolo p, String newState){
		if(newState.equals("cancellata"))
			return DAO.queryMarkPrenotazioneAsDeletedDB(p.getCorso(),p.getIdDocente(),p.getUtente(),p.getGiorno(),p.getOrario());
		else if(newState.equals("effettuata"))
			return DAO.queryMarkPrenotazioneAsDoneDB(p.getCorso(),p.getIdDocente(),p.getUtente(),p.getGiorno(),p.getOrario());
		else return false;
	}

}
