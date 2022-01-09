package com.happylearn.servlet;

import com.google.gson.Gson;
import com.happylearn.DAO.*;
import com.happylearn.support.Slot;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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
		} catch (ServletException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.setContentType("text/html");
		PrintWriter out = jsonResponseSetup(response);
		ArrayList<Slot> allAvailableSlots = showAllAvailableSlots();

		System.out.println(allAvailableSlots);
		System.out.println(allAvailableSlots.size());

		out.print(slotsToJson(allAvailableSlots));
		response.setStatus(200);
		out.flush();
		return;
	}

	private String slotsToJson(ArrayList<Slot> slots) {
		Gson gson = new Gson();
		return gson.toJson(slots);
	}




	/** TODO
	 * DA METTERE NEL DAO!!!!!!
	 *
	 * Sistemare con i join!!!!!!!!!!!!!!!! :(
	 * rs = st.executeQuery("SELECT * FROM (PRENOTAZIONE AS P JOIN UTENTE AS U ON P.UTENTE = U.USERNAME) JOIN DOCENTE AS D ON P.DOCENTE = D.ID " +
	 * 									"WHERE U.USERNAME = '" + utente + "' AND P.CORSO = '" + corso + "' AND P.GIORNO = " + giorno + " AND P.ORARIO = " + orario);
	 */

	/*
	 *  This method shows all available slots.
	 */
	private ArrayList <Slot> showAllAvailableSlots() {
		boolean toAdd;
		ArrayList<Slot> allAvailableSlots = new ArrayList<>();
		ArrayList<Insegnamento> allTeachings = DAO.queryShowAllInsegnamentiDB(false);
		ArrayList<Prenotazione> allBookings = DAO.queryShowAllPrenotazioniDB(false);
		ArrayList<Docente> allTeachers = DAO.queryShowAllDocentiDB();
		ArrayList<Corso> allCourses = DAO.queryShowAllCoursesDB(false);

		for (int day = 0; day < 5; day++) {
			for (int time = 0; time < 4; time++) {
				for (Corso c : allCourses) {
					List<Docente> listTeachers = new ArrayList<>();
					for (Insegnamento ins : allTeachings) {
						if (c.getMateria().equals(ins.getCorso())){
							toAdd = true;
							System.out.println(ins.getCorso());
							for (Prenotazione pre : allBookings) {
								if (day == pre.getGiorno() && time == pre.getOrario() && ins.getCorso().equals(pre.getCorso()) && ins.getIdDocente() == pre.getIdDocente()) {
									toAdd = false;
									break;
								}
							}
							if (toAdd) {
								for (Docente d : allTeachers) {
									if (ins.getIdDocente() == d.getId())
										listTeachers.add(d);
								}
							}

						}
					}
					if(! listTeachers.isEmpty())
						allAvailableSlots.add(new Slot(c.getMateria(), listTeachers, day, time));
				}
			}
		}
		return allAvailableSlots;
	}
}
