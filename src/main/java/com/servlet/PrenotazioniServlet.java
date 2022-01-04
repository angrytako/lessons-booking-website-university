package com.noodle.noodle;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

/*
 * La servlet fa eseguire le operazioni crud(create-read-update-delete) sulle prenotazioni
 * Post --> create
 * Get --> read
 * Update --> update
 * Delete --> delete
 */

@WebServlet(name = "PrenotazioniServlet", value = "/PrenotazioniServlet")
public class PrenotazioniServlet extends HttpServlet {
	/*
	 * read --> a seconda della queryString restituisce tutte le prenotazioni oppure solo le proprie/ oppure gli admin quelle di qualcuno
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = jsonResponseSetup(response);
		if (!hasSession(request)) {											// se non si ha la sessione inutile proseguire
			json401ErrorResponse(response, out);
			return;
		}
		// getting the querystring variables as a map
		// if username is present, the list of prenotazioni will be given for that user,
		// if the user is the same as the one requesting, or it is an admin
		// otherwise it returns all prenotazioni, if the user is an admin

		/*
		 * Se lo username è presente nella queryString: se è un admin vedi le prenotazioni dello username oppure se
		 *
		 *
		 * Si controlla se si possono vedere le prenotazioni dello username richiesto o quelle di tutti (se non c'è lo username):
		 * - se sei un admin ti manda tutte le prenotazioni (oppure solo quelle dello username se nella queryString viene specificato uno username)
		 * - se sei un cliente ti manda le tue prenotazioni se la queryString combacia col tuo nome utente (preso dalla tua sessione)
		 * - in ogni altro caso ti manda errore --> manda un json con l'errore a chi te l'ha richiesto.
		 */
		Map<String, String[]> params = request.getParameterMap();
		if (params.containsKey("username")) {
			String requestedUsername = params.get("username")[0];
			System.out.println(requestedUsername);
			if (isAuthorized(request, requestedUsername)) {
				ArrayList<DAO.Prenotazione> prenotazioni = DAO.DAO.queryShowUserPrenotazioniDB(requestedUsername);
				out.print(prenotazioniToJson(prenotazioni));
				response.setStatus(200);
			} else {
				out.print("{\"error\":\"not authorized\"}");
				response.setStatus(401);
			}
			out.flush();
			return;
		} else {
			if (isAuthorized(request)) {
				ArrayList<DAO.Prenotazione> prenotazioni = DAO.DAO.queryShowAllPrenotazioniDB(true);
				out.print(prenotazioniToJson(prenotazioni));
				response.setStatus(200);

			} else {
				out.print("{\"error\":\"not authorized\"}");
				response.setStatus(401);
			}
			out.flush();
			return;
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = jsonResponseSetup(response);
		if (!hasSession(request)) {
			json401ErrorResponse(response, out);
			return;
		}
	}

	/*
	 * serve per dire che sto mandando json nella risposta --> chiave content-type, valore application/json
	 */
	private PrintWriter jsonResponseSetup(HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		return out;
	}

	/*
	 * stampa un json con la chiave errore e la stringa "not authorized"
	 */
	private void json401ErrorResponse(HttpServletResponse response, PrintWriter out) throws IOException {
		out.print("{\"error\":\"not authorized\"}");
		response.setStatus(401);
		out.flush();
		return;
	}

	private String prenotazioniToJson(ArrayList<DAO.Prenotazione> prenotazioni) {
		Gson gson = new Gson();
		return gson.toJson(prenotazioni);
	}

	/*
	 * controlla se esiste una sessione, cioè se l'utente si è già autenticato --> controlla solo username e role perché questo è quello che abbiamo inserito
	 * nella sessione nella servlet LoginServlet.
	 */
	private boolean hasSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null && session.getAttribute("username") != null && session.getAttribute("role") != null)
			return true;
		else return false;
	}

	private boolean isAuthorized(HttpServletRequest request, String requestedUsername) {
		HttpSession session = request.getSession();
		System.out.println(((String) session.getAttribute("username")).equals(requestedUsername));
		if (((String) session.getAttribute("role")).equals("amministratore") || session.getAttribute("username").equals(requestedUsername))
			return true;
		else return false;
	}

	private boolean isAuthorized(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (((String) session.getAttribute("role")).equals("amministratore"))
			return true;
		else return false;
	}
}
