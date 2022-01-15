package com.happylearn.DAO;

import com.happylearn.support.PrenotazioneDocenteRuolo;
import com.happylearn.support.Slot;

import java.sql.*;
import java.util.*;

/** TODO
 * Sistemare con i join!!!!!!!!!!!!!!!! :(
 * rs = st.executeQuery("SELECT * FROM (PRENOTAZIONE AS P JOIN UTENTE AS U ON P.UTENTE = U.USERNAME) JOIN DOCENTE AS D ON P.DOCENTE = D.ID " +
 * 									"WHERE U.USERNAME = '" + utente + "' AND P.CORSO = '" + corso + "' AND P.GIORNO = " + giorno + " AND P.ORARIO = " + orario);
 */
public class DAO {
	private static String url;
	private static String user;
	private static String password;

	private static Connection conn1 = null;
	private static Statement st = null;

	public static void registerDriver(String url1, String user1, String password1) {
		try {
			url = url1;
			user = user1;
			password = password1;

			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			System.out.println("Driver correttamente registrato");
		} catch (SQLException e) {
			System.out.println("Errore: " + e.getMessage());
		}
	}

	/*
	 *  This method returns a user obj if a user with a given
	 *  username and password exists, null otherwise
	 */
	public static Utente getUser(String username, String password) {
		Utente user = null;
		try {
			connectionToDB();
			Statement utente = conn1.createStatement();
			ResultSet rs = utente.executeQuery("SELECT username, password, ruolo FROM UTENTE WHERE username = '" + username + "' AND password ='" + password + "'");
			if (rs.next()) {
				user = new Utente(rs.getString("username"), rs.getString("password"), rs.getString("ruolo"));
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeDBConnection();
		}
		return user;
	}
	/*
	 *  This method returns a corso obj, if it exists
	 * else it returns null
	 */
	public static Corso queryGetCorso(String materia) {
		Corso corso = null;
		try {
			connectionToDB();
			Statement utente = conn1.createStatement();
			ResultSet rs = utente.executeQuery("SELECT * FROM CORSO WHERE materia = '" + materia + "' AND RIMOSSO = FALSE");
			if (rs.next()) {
				corso = new Corso(rs.getString("materia"), rs.getBoolean("rimosso"));
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeDBConnection();
		}
		return corso;
	}

	/*
	 *  This method returns a user obj if a user with a given
	 *  username and password exists, null otherwise
	 */
	public static Docente queryGetDocente(int docenteId) {
		Docente docente = null;
		try {
			connectionToDB();
			Statement utente = conn1.createStatement();
			ResultSet rs = utente.executeQuery("SELECT * FROM DOCENTE WHERE ID = " + docenteId + " AND RIMOSSO = FALSE");
			if (rs.next()) {
				docente = new Docente(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getBoolean("rimosso"));
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeDBConnection();
		}
		return docente;
	}
	/*
	 *  This method returns a user obj if a user with a given
	 *  username and password exists, null otherwise
	 */
	public static Insegnamento queryGetInsegnamento(int docenteId, String materia) {
		Insegnamento ins = null;
		try {
			connectionToDB();
			Statement utente = conn1.createStatement();
			ResultSet rs = utente.executeQuery("SELECT * FROM INSEGNAMENTO WHERE DOCENTE = " + docenteId + " AND CORSO = '" + materia + "'");
			if (rs.next()) {
				ins = new Insegnamento(rs.getString("corso"), rs.getInt("DOCENTE"), rs.getBoolean("rimosso"));
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeDBConnection();
		}
		return ins;
	}
	/*
	 *  This method returns all bookings.
	 *  If 'showAlsoDeleted' is true it shows all bookings.
	 *  Otherwise if 'showAlsoDeleted' is false it only shows active or effectuated bookings.
	 * anna
	 */
	public static ArrayList<Prenotazione> queryShowAllPrenotazioniDB(boolean showAlsoDeleted) {
		ArrayList<Prenotazione> out = new ArrayList<>();
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT * FROM PRENOTAZIONE");
			while (rs.next()) {
				Prenotazione p = new Prenotazione(rs.getString("corso"), rs.getInt("docente"), rs.getString("utente"), rs.getString("stato"), rs.getInt("giorno"), rs.getInt("orario"));
				if (showAlsoDeleted)
					out.add(p);
				else if (!p.getStato().equals("cancellata"))        // Here showAlsoDeleted = false
					out.add(p);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return out;
	}

	/*
     *  This method shows all bookings with the role, since is important.
	 * lorenzo
     */

	/*this method is used to get one specific prenotazione*/
    public static PrenotazioneDocenteRuolo queryGetPrenotazioneDB(String corso, String utente, int giorno, int orario) {
        ResultSet rs = null;
        try {
            connectionToDB();
            st = conn1.createStatement();
            rs = st.executeQuery("SELECT * FROM (PRENOTAZIONE AS P JOIN UTENTE AS U ON P.UTENTE = U.USERNAME) JOIN DOCENTE AS D ON P.DOCENTE = D.ID " +
									"WHERE U.USERNAME = '" + utente + "' AND P.CORSO = '" + corso + "' AND P.GIORNO = " + giorno + " AND P.ORARIO = " + orario);
            if (rs.next()) {
                return new PrenotazioneDocenteRuolo(
						rs.getString("corso"), rs.getInt("docente"), rs.getString("nome"),
						rs.getString("cognome"),rs.getString("utente"), rs.getString("stato"),
						rs.getInt("giorno"), rs.getInt("orario"), rs.getString("ruolo"));
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement();
            closeDBConnection();
        }
        return null;
    }

	public static ArrayList<PrenotazioneDocenteRuolo> queryShowAllPrenotazioniDB() {
		ArrayList<PrenotazioneDocenteRuolo> out = new ArrayList<>();
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT * FROM (PRENOTAZIONE AS P JOIN UTENTE AS U ON P.UTENTE = U.USERNAME) JOIN DOCENTE AS D ON P.DOCENTE = D.ID");
			while (rs.next()) {
				PrenotazioneDocenteRuolo p = new PrenotazioneDocenteRuolo(
						rs.getString("corso"), rs.getInt("docente"), rs.getString("nome"),
						rs.getString("cognome"),rs.getString("utente"), rs.getString("stato"),
						rs.getInt("giorno"), rs.getInt("orario"), rs.getString("ruolo"));
				out.add(p);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return out;
	}

	/*
	 *  This method shows all user's bookings.
	 */
	public static ArrayList<PrenotazioneDocenteRuolo> queryShowUserPrenotazioniDB(String username) {
		ArrayList<PrenotazioneDocenteRuolo> out = new ArrayList<>();
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT * FROM (PRENOTAZIONE AS P JOIN UTENTE AS U ON P.UTENTE = U.USERNAME) JOIN DOCENTE AS D ON P.DOCENTE = D.ID WHERE utente = '" + username + "'");
			while (rs.next()) {
				PrenotazioneDocenteRuolo p = new PrenotazioneDocenteRuolo(
						rs.getString("corso"), rs.getInt("docente"), rs.getString("nome"),
						rs.getString("cognome"),rs.getString("utente"), rs.getString("stato"),
						rs.getInt("giorno"), rs.getInt("orario"), rs.getString("ruolo"));
				out.add(p);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return out;
	}

	/*
	 *  This method marks a booking as deleted.
	 */
	public static boolean queryMarkPrenotazioneAsDeletedDB(String course, int idDoc, String username, int day, int time) {
		boolean queryResult = true;
		try {
			connectionToDB();
			st = conn1.createStatement();
			st.executeUpdate("UPDATE PRENOTAZIONE SET stato='cancellata' WHERE corso = '" + course + "' and docente = " + idDoc + " and utente = '" + username + "' and giorno = " + day + " and orario = " + time);
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
			queryResult = false;
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/*
	 *  This method marks a booking as done.
	 */
	public static boolean queryMarkPrenotazioneAsDoneDB(String course, int idDoc, String username, int day, int time) {
		boolean queryResult = false;
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT ruolo FROM UTENTE WHERE username = '" + username + "'");
			if (rs.next() && rs.getString("ruolo").equals("cliente")) {
				st.executeUpdate("UPDATE PRENOTAZIONE SET stato='effettuata' WHERE corso = '" + course + "' and docente = " + idDoc + " and utente = '" + username + "' and giorno = " + day + " and orario = " + time);
				queryResult = true;
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}


	/*
	 *  This method adds a booking in 'prenotazione' table.
	 *  It checks that, if the couple (course, idDoc) already exists in 'insegnamento' table, its 'rimosso' variable is set to false.
	 *  It checks that both the user and the teacher are not already occupied in that day-time slot.
	 */
	public static boolean queryAddPrenotazioneDB(String course, int idDoc, String username, int day, int time) {
		boolean queryResult = false;
		try {
			List<Prenotazione> bookings = queryShowAllPrenotazioniDB(true);
			connectionToDB();
			st = conn1.createStatement();

			ResultSet rsI = null;
			boolean deletedTeaching = true;
			try {
				rsI = st.executeQuery("SELECT rimosso FROM INSEGNAMENTO WHERE corso = '" + course + "' AND docente = " + idDoc);
				if (rsI.next()) {
					deletedTeaching = rsI.getBoolean("rimosso");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResultSet(rsI);
			}

			if (!deletedTeaching) {
				// Here, if the teaching is present on the database, its 'rimosso' variable is set to false.
				ResultSet rsP = null;
				boolean toInsert = true;
				try {
					rsP = st.executeQuery("SELECT docente,utente,stato FROM PRENOTAZIONE WHERE giorno = " + day + " AND orario = " + time);
					while (rsP.next()) {
						int d = rsP.getInt("docente");
						String u = rsP.getString("utente");
						String s = rsP.getString("stato");
						if ((d == idDoc || username.equals(u)) && (s.equals("effettuata") || s.equals("attiva"))) {
							toInsert = false;
							break;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					closeResultSet(rsP);
				}
				if (toInsert) {
					boolean existedBooking = false;
					for (Prenotazione p : bookings) {
						if (p.getCorso().equals(course) && p.getIdDocente() == idDoc && p.getUtente().equals(username) && p.getGiorno() == day && p.getOrario() == time) {
							st.executeUpdate("UPDATE PRENOTAZIONE SET stato = 'attiva' WHERE corso = '" + course + "' AND docente = " + idDoc + " AND utente = '" + username + "' AND stato = '" + p.getStato() + "' AND giorno = " + day + " AND orario = " + time);
							existedBooking = true;
							queryResult = true;
							break;
						}
					}
					if(!existedBooking) {
						st.executeUpdate("INSERT INTO PRENOTAZIONE (corso, docente, utente, giorno, orario) VALUES ('" + course + "', " + idDoc + ", '" + username + "', " + day + ", " + time + ")");
						queryResult = true;
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/*
	 *  This method shows all available slots.
	 */
//	private ArrayList <Slot> showAllAvailableSlots() {
//		ArrayList<Corso> allAvailableSlots = new ArrayList<>();
//		ResultSet rs = null;
//		try{
//			connectionToDB();
//			st = conn1.createStatement();
//			rs = st.executeQuery("SELECT * " +
//					"FROM (PRENOTAZIONE AS P RIGHT JOIN DOCENTE AS D ON P.DOCENTE = D.ID " +
//					"RIGHT JOIN INSEGNAMENTO AS I ON (I.CORSO = P.CORSO AND I.DOCENTE = P.DOCENTE) " +
//					"WHERE stato = 'effettuata' OR stato = 'attiva'");
//			while(rs.next()){
//				rs.getString("I.corso");
//				rs.getBoolean("I.rimosso");
//
//				rs.getInt("D.id");
//				rs.getString("D.nome");
//				rs.getString("D.cognome");
//				rs.getBoolean("D.rimosso");
//
//				rs.getString("P.corso");
//				rs.getInt("P.docente");
//				rs.getInt("P.giorno");
//				rs.getInt("P.orario");
//			}
//
//			for (int day = 0; day < 5; day++) {
//				for (int time = 0; time < 4; time++) {
//					List<Docente> listTeachers = new ArrayList<>();
//
//
//
//					for (Insegnamento ins : allTeachings) {
//						List<Docente> listTeachers = new ArrayList<>();
//						toAdd = true;
//						for (Prenotazione pre : allBookings) {
//							if (day == pre.getGiorno() && time == pre.getOrario() && ins.getCorso().equals(pre.getCorso()) && ins.getIdDocente() == pre.getIdDocente()) {
//								toAdd = false;
//								break;
//							}
//						}
//						if (toAdd) {
//							for (Docente d: allTeachers) {
//								if (ins.getIdDocente() == d.getId())
//									listTeachers.add(d);
//							}
//						}
//						allAvailableSlots.add(new Slot(ins.getCorso(), listTeachers, day, time));
//					}
//
//				}
//			}
//
//		}catch (SQLException e) {
//			System.out.println("errore di connessione al db: " + e.getMessage());
//		} finally {
//			closeResultSet(rs);
//			closeStatement();
//			closeDBConnection();
//		}
//		return allAvailableSlots;
//

//		try {
//			connectionToDB();
//			st = conn1.createStatement();
//			rs = st.executeQuery("SELECT * FROM (PRENOTAZIONE AS P JOIN UTENTE AS U ON P.UTENTE = U.USERNAME) JOIN DOCENTE AS D ON P.DOCENTE = D.ID " +
//					"WHERE U.USERNAME = '" + utente + "' AND P.CORSO = '" + corso + "' AND P.GIORNO = " + giorno + " AND P.ORARIO = " + orario);
//			if (rs.next()) {
//				return new PrenotazioneDocenteRuolo(
//						rs.getString("corso"), rs.getInt("docente"), rs.getString("nome"),
//						rs.getString("cognome"), rs.getString("utente"), rs.getString("stato"),
//						rs.getInt("giorno"), rs.getInt("orario"), rs.getString("ruolo"));
//			}
//		}
//	}

		/*
	 *  This method returns all courses in 'corso' table.
	 *  If 'showOnlyDeleted' is false it only shows actived courses.
	 *  Otherwise if 'showOnlyDeleted' is true it only shows deleted courses (it is used in 'queryAddCorsoDB' method).
	 */
	public static ArrayList<Corso> queryShowAllCoursesDB(boolean showOnlyDeleted) {
		ArrayList<Corso> out = new ArrayList<>();
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT * FROM CORSO");
			while (rs.next()) {
				Corso c = new Corso(rs.getString("materia"), rs.getBoolean("rimosso"));
				if (showOnlyDeleted && c.isRimosso())
					out.add(c);
				else if (!showOnlyDeleted && !c.isRimosso())
					out.add(c);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return out;
	}

	/*
	 *  This method adds a course in 'corso' table.
	 *  If 'materia' already exists and its 'rimosso' variable is true, this method update it with false.
	 *  Otherwise this method adds a new tuple.
	 */
	public static boolean queryAddCorsoDB(String materia) {
		boolean queryResult = true;
		try {
			ArrayList<Corso> corsi = queryShowAllCoursesDB(true);
			connectionToDB();
			st = conn1.createStatement();
			// If the course already exists, only the 'rimosso' variable is changed ('rimosso' = false)
			boolean existedCourse = false;
			for (Corso c : corsi) {
				if (c.getMateria().equals(materia)) {
					st.executeUpdate("UPDATE CORSO SET rimosso = false WHERE materia = '" + materia + "'");
					existedCourse = true;
					break;
				}
			}
			if (!existedCourse)
				st.executeUpdate("INSERT INTO CORSO (materia) VALUES ('" + materia + "')");
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
			queryResult = false;
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/*
	 *  This method deletes a course in 'corso' table.
	 *  It sets 'rimosso' variable to true both for the course and for all teachings associated with the course.
	 *  It also sets 'stato' variable to 'cancellata' for all bookings with this course.
	 */
	public static boolean queryDeleteCorsoDB(String materia) {
		boolean queryResult = true;
		try {
			connectionToDB();
			st = conn1.createStatement();
			st.executeUpdate("UPDATE CORSO SET rimosso = true WHERE materia = '" + materia + "'");
			st.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = true WHERE corso = '" + materia + "'");
			st.executeUpdate("UPDATE PRENOTAZIONE SET stato = 'cancellata' WHERE corso = '" + materia + "' AND stato = 'attiva'");
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
			queryResult = false;
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/*
	 *  This method returns all teachers in 'docente' table with 'rimosso' variable set to false.
	 */
	public static ArrayList<Docente> queryShowAllDocentiDB() {
		ArrayList<Docente> out = new ArrayList<>();
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT * FROM DOCENTE WHERE rimosso = false");
			while (rs.next()) {
				Docente doc = new Docente(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getBoolean("rimosso"));
				out.add(doc);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return out;
	}

	/*
	 *  This method returns one specific teacher in 'docente' table with 'rimosso' variable set to false.
	 */
	public static int queryShowOneDocenteDB(String nome, String cognome) {
		int id = -1;
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT id FROM DOCENTE WHERE rimosso = false and nome='"+
										nome+"' and cognome ='"+cognome+"' order by id desc limit 1");
			while (rs.next()) {
			id=rs.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return id;
	}



	/*
	 *  This method adds a teacher in 'docente' table.
	 */
	public static boolean queryAddDocenteDB(String nome, String cognome) {
		boolean queryResult = true;
		try {
			connectionToDB();
			st = conn1.createStatement();
			st.executeUpdate("INSERT INTO DOCENTE (nome, cognome) VALUES ('" + nome + "', '" + cognome + "')");
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
			queryResult = false;
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/*
	 *  This method deletes a teacher in 'docente' table.
	 *  It sets 'rimosso' variable to true both for the teacher and for all teachings associated with the teacher.
	 *  It also sets 'stato' variable to 'cancellata' for all bookings with this course.
	 */
	public static boolean queryDeleteDocenteDB(int idDoc) {
		boolean queryResult = true;
		try {
			connectionToDB();
			st = conn1.createStatement();
			st.executeUpdate("UPDATE DOCENTE SET rimosso = true WHERE id = " + idDoc);
			st.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = true WHERE docente = " + idDoc);
			st.executeUpdate("UPDATE PRENOTAZIONE SET stato = 'cancellata' WHERE docente = " + idDoc + " AND stato = 'attiva'");
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
			queryResult = false;
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/**
	 *  This method returns all teachings in 'insegnamento' table.
	 *  If 'showOnlyDeleted' is false it only shows actived teachings.
	 *  If 'showOnlyDeleted' is true it only shows deleted teachings.
	 */
	public static ArrayList<Insegnamento> queryShowAllInsegnamentiDB(boolean showOnlyDeleted) {
		ArrayList<Insegnamento> out = new ArrayList<>();
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT * FROM INSEGNAMENTO");
			while (rs.next()) {
				Insegnamento ins = new Insegnamento(rs.getString("corso"), rs.getInt("docente"), rs.getBoolean("rimosso"));
				if (showOnlyDeleted && ins.isRimosso())
					out.add(ins);
				else if (!showOnlyDeleted && !ins.isRimosso())
					out.add(ins);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return out;
	}

	/*
	 *  This method adds a teaching in 'insegnamento' table.
	 *  It checks that both the course and the teacher exist and have the 'rimosso' variables set to false.
	 *  If couple (course,idDoc) already exists and its 'rimosso' variable is true, this method update it with false.
	 *  Otherwise it adds a new tuple.
	 */
	public static boolean queryAddInsegnamentoDB(String course, int idDoc) {
		boolean queryResult = false;
		try {
			ArrayList<Insegnamento> insegnamenti = queryShowAllInsegnamentiDB(false);
			connectionToDB();
			st = conn1.createStatement();
			// It checks if course already exists and if it's not removed. Same thing for teacher.
			boolean courseIsRemoved = true;
			boolean teacherIsRemoved = true;
			ResultSet rsC = null;
			try {
				rsC = st.executeQuery("SELECT rimosso FROM CORSO WHERE materia = '" + course + "'");
				if (rsC.next())
					courseIsRemoved = rsC.getBoolean("rimosso");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResultSet(rsC);
			}

			if (!courseIsRemoved) {
				ResultSet rsD = null;
				try {
					rsD = st.executeQuery("SELECT rimosso FROM DOCENTE WHERE id = " + idDoc);
					if (rsD.next())
						teacherIsRemoved = rsD.getBoolean("rimosso");
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					closeResultSet(rsD);
				}

				if (!teacherIsRemoved) {
					boolean existedTeaching = false;
					for (Insegnamento i : insegnamenti) {
						if (i.getCorso().equals(course) && i.getIdDocente() == idDoc) {
							st.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = false WHERE corso = '" + course + "' AND docente = " + idDoc);
							existedTeaching = true;
							queryResult = true;
							break;
						}
					}
					if(!existedTeaching) {
						st.executeUpdate("INSERT INTO INSEGNAMENTO (corso, docente) VALUES ('" + course + "', " + idDoc + ")");
						queryResult = true;
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/*
	 *  This method deletes a teaching in 'insegnamento' table.
	 *  It sets 'rimosso' variable to true and is sets 'stato' variable to 'cancellata' for all bookings with this course.
	 */
	public static boolean queryDeleteInsegnamentoDB(String course, int idDoc) {
		boolean queryResult = true;
		try {
			connectionToDB();
			st = conn1.createStatement();
			st.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = true WHERE corso = '" + course + "' AND docente = " + idDoc);
			st.executeUpdate("UPDATE PRENOTAZIONE SET stato = 'cancellata' WHERE corso = '"+course+"' AND docente = "+idDoc+" AND stato = 'attiva'");
		} catch (SQLException e) {
			System.out.println("errore db:  " + e.getMessage());
			queryResult = false;
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return queryResult;
	}

	/*
	 *  This method shows all teachers for a given course.
	 */
	public static ArrayList<Docente> showDocentiForCourseDB(String course) {
		ArrayList<Docente> out = new ArrayList<>();
		try {
			connectionToDB();
			st = conn1.createStatement();

			ResultSet rsI = null;
			List<Integer> idDocIns = new ArrayList<>();
			try {
				rsI = st.executeQuery("SELECT docente FROM INSEGNAMENTO WHERE corso = '" + course + "' AND rimosso = false");
				while (rsI.next())
					idDocIns.add(rsI.getInt("docente"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResultSet(rsI);
			}

			ResultSet rsD = null;
			try {
				rsD = st.executeQuery("SELECT * FROM DOCENTE WHERE rimosso = false");
				while (rsD.next()) {
					int idDoc = rsD.getInt("id");
					for (Integer idDocIn : idDocIns) {
						if (idDocIn == idDoc) {
							Docente doc = new Docente(idDoc, rsD.getString("nome"), rsD.getString("cognome"), rsD.getBoolean("rimosso"));
							out.add(doc);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResultSet(rsD);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeStatement();
			closeDBConnection();
		}
		return out;
	}


	/*
	 *  This method shows all courses for a given teacher.
	 */
	public static ArrayList<Corso> showCoursesForTeachersDB(int docenteId) {
		ArrayList<Corso> courses = new ArrayList<>();
		ResultSet rsI = null;
		try {
			connectionToDB();
			st = conn1.createStatement();

			rsI = st.executeQuery("SELECT CORSO  FROM INSEGNAMENTO WHERE DOCENTE = " + docenteId + " AND rimosso = false");
			while (rsI.next())
				courses.add(new Corso(rsI.getString("CORSO"), false));
		}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResultSet(rsI);
				closeStatement();
				closeDBConnection();
			}
		return courses;

	}




	/*
	 *  This method shows all users.
	 */
	public static ArrayList<Utente> queryShowAllUsersDB() {
		ArrayList<Utente> out = new ArrayList<>();
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT * FROM UTENTE");
			while (rs.next()) {
				Utente ut = new Utente(rs.getString("username"), rs.getString("password"), rs.getString("ruolo"));
				out.add(ut);
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return out;
	}

	/*
	 *  This method get user rule.
	 */
	public static String getUserRule(String username) {
		String rule = "";
		ResultSet rs = null;
		try {
			connectionToDB();
			st = conn1.createStatement();
			rs = st.executeQuery("SELECT ruolo FROM UTENTE WHERE username = '" + username + "'");
			if (rs.next()) {
				rule = rs.getString("ruolo");
			}
		} catch (SQLException e) {
			System.out.println("errore di connessione al db: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closeStatement();
			closeDBConnection();
		}
		return rule;
	}


	/*
	 *  This method opens database connection.
	 */
	private static void connectionToDB() {
		try {
			conn1 = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 *  This method closes a statement.
	 */
	private static void closeStatement() {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/*
	 *  This method closes a resultset.
	 */
	private static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/*
	 *  This method closes database connection.
	 */
	private static void closeDBConnection() {
		if (conn1 != null) {
			try {
				conn1.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
