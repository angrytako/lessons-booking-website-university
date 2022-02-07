package com.happylearn.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.happylearn.DAO.*;
import com.happylearn.support.Slot;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
 * This servlet takes all available slots. --> doGet
 */

@WebServlet(name = "AvailableSlotsServlet", value = "/AvailableSlotsServlet")
public class AvailableSlotsServlet extends SecuredHttpServlet {

	public void init(ServletConfig config) {
		try {
			super.init(config);
			ServletContext ctx = config.getServletContext();
			String url = ctx.getInitParameter("url");
			String user = ctx.getInitParameter("user");
			String password = ctx.getInitParameter("password");
			DAO.registerDriver(url, user, password);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.setContentType("text/html");
		PrintWriter out = jsonResponseSetup(response);
		ArrayList<Slot> allAvailableSlots = showAllAvailableSlots();

		out.print(slotsToJson(allAvailableSlots));

		response.setStatus(200);
		out.flush();
		return;
	}

	// POST:
	// prendere giorno e ora
	// ritornare tutti gli utenti che non hanno una prenotazione attiva o effettuata
	// solo l'admin può farlo

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.setContentType("text/html");
		PrintWriter out = jsonResponseSetup(response);
		if (!hasSession(request)) {
			json401ErrorResponse(response, out);
			return;
		}
		if (isAdmin(request)) {
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
			String nome = jobj.get("nome").getAsString();
			String cognome = jobj.get("cognome").getAsString();

			if (nome != null && cognome != null) {
				if (DAO.queryAddDocenteDB(nome, cognome)) {
					int id = DAO.queryShowOneDocenteDB(nome, cognome);
					//out.print(slotsToJson(allAvailableSlots));
					response.setStatus(200);
				} else {
					out.print("{\"error\":\"error query\"}");
					response.setStatus(500);
				}
			} else {
				out.print("{\"error\":\"error in nome,cognome\"}");
				response.setStatus(400);
			}

		} else {
			out.print("{\"error\":\"not authorized\"}");
			response.setStatus(401);
		}
		out.flush();
	}

	private String slotsToJson(ArrayList<Slot> slots) {
		Gson gson = new Gson();
		return gson.toJson(slots);
	}

	/**
	 * TODO
	 * DA METTERE NEL DAO!!!!!!
	 * <p>
	 * Sistemare con i join!!!!!!!!!!!!!!!! :(
	 * rs = st.executeQuery("SELECT * FROM (PRENOTAZIONE AS P JOIN UTENTE AS U ON P.UTENTE = U.USERNAME) JOIN DOCENTE AS D ON P.DOCENTE = D.ID " +
	 * "WHERE U.USERNAME = '" + utente + "' AND P.CORSO = '" + corso + "' AND P.GIORNO = " + giorno + " AND P.ORARIO = " + orario);
	 */

	/**
	 *  This method shows all available slots.
	 */
	private ArrayList<Slot> showAllAvailableSlots() {
		// matrix empty
		ArrayList<Integer>[][] bookedTeachers = new ArrayList[5][4];
		ArrayList<Prenotazione> allBookings = DAO.queryShowAllPrenotazioniDB(false);
		// booked teachers
		for (Prenotazione pre : allBookings) {
			if (bookedTeachers[pre.getGiorno()][pre.getOrario()] == null)
				bookedTeachers[pre.getGiorno()][pre.getOrario()] = new ArrayList<Integer>();
			bookedTeachers[pre.getGiorno()][pre.getOrario()].add(pre.getIdDocente());
		}
		ArrayList<Slot>[][] slots = new ArrayList[5][4];
		ArrayList<Insegnamento> allTeachings = DAO.queryShowAllInsegnamentiDB(false);
		ArrayList<Docente> allTeachers = DAO.queryShowAllDocentiDB();
		for (Insegnamento ins : allTeachings) {
			Docente doc = getNameAndSurname(ins.getIdDocente(), allTeachers);
			setSlots(ins.getCorso(), doc, bookedTeachers, slots);
		}
		ArrayList<Slot> allSlotsToReturn = getAllSlotsFromMatrix(slots);
		return allSlotsToReturn;
	}

	private ArrayList<Slot> getAllSlotsFromMatrix(ArrayList<Slot>[][] slots) {
		ArrayList<Slot> allSlots = new ArrayList<>();
		// we already know how long the slots parameter is
		for(int day=0; day<5; day++){
			for (int time=0; time<4; time++) {
				if (slots[day][time] != null)
					allSlots.addAll(slots[day][time]);
			}
		}
		return allSlots;
	}

	private Docente getNameAndSurname (int id, ArrayList<Docente> allTeachers) {
		for (Docente doc : allTeachers) {
			if (doc.getId() == id)
				return doc;
		}
		return null;
	}

	private void setSlots(String course, Docente teacher, ArrayList<Integer>[][] bookedTeachers, ArrayList<Slot>[][] slots) {
		for(int day=0; day<5; day++){
			for (int time=0; time<4; time++){
				// if bookedTeachers[day][time] is null there aren't bookings with this teacher (and any others teachers).
				// if ! bookedTeachers[day][time].contains(teacher.getId()) means that other teachers are booked on this day at this time, BUT not this teacher.
				if (bookedTeachers[day][time] == null || (! bookedTeachers[day][time].contains(teacher.getId()))){
					// here teacher is free on this day at this time
					addSlotOrTeacherToSlot(course, teacher, day, time, slots);
				}
				// else if teacher is booked (contains) --> he is not free on this day at this time
			}
		}
	}

	private void addSlotOrTeacherToSlot(String course, Docente teacher, int day, int time, ArrayList<Slot>[][] slots) {
		// if there weren't any slots on this day at this time
		if (slots[day][time] == null) {
			slots[day][time] = new ArrayList<Slot>();
			List <Docente> allTeachers = new ArrayList<>();
			allTeachers.add(teacher);
			slots[day][time].add(new Slot(course, allTeachers, day, time));
		}
		else{
			for (Slot slot: slots[day][time]){
				if(slot.getCourse().equals(course)) {
					slot.addTeacherToList(teacher);
					return;
				}
			}
			// here there aren't slots with course parameter
			List <Docente> allTeachers = new ArrayList<>();
			allTeachers.add(teacher);
			slots[day][time].add(new Slot(course, allTeachers, day, time));
		}
	}
}
