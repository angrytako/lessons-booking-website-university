package com.servlet;

import DAO.*;

import com.google.gson.Gson;
import support.Slot;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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




	/*
	 *  This method shows all available slots.
	 */
	// meglio statico o non statico......??? Mi sembra che non cambi un tubo.
	private ArrayList <Slot> showAllAvailableSlots() {
		boolean toAdd;
		ArrayList<Slot> allAvailableSlots = new ArrayList<>();
		ArrayList<Insegnamento> allTeachings = DAO.queryShowAllInsegnamentiDB(false);
		ArrayList<Prenotazione> allBookings = DAO.queryShowAllPrenotazioniDB(false);
		ArrayList<Docente> allTeachers = DAO.queryShowAllDocentiDB();

		for (int day = 0; day < 5; day++) {
			for (int time = 0; time < 4; time++) {
				for (Insegnamento ins : allTeachings) {
					toAdd = true;
					for (Prenotazione pre : allBookings) {
						if (day == pre.getGiorno() && time == pre.getOrario() && ins.getCorso().equals(pre.getCorso()) && ins.getIdDocente() == pre.getIdDocente()) {
							toAdd = false;
							break;
						}
					}
					if (toAdd) {
						for (Docente d: allTeachers) {
							if (ins.getIdDocente() == d.getId())
								allAvailableSlots.add(new Slot(ins.getCorso(), d.getNome(), d.getCognome(), day, time));
						}
					}
				}
			}
		}
		return allAvailableSlots;
	}
}
