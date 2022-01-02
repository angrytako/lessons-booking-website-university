package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DAO {
    private static String url;
    private static String user;
    private static String password;

    private static Connection conn1 = null;

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
     *  This method returns all courses in 'corso' table.
     *  If 'showOnlyDeleted' is false it only shows actived courses.
     *  Otherwise if 'showOnlyDeleted' is true it only shows deleted courses (it is used in 'queryAddCorsoDB' method).
     */
    public static ArrayList<Corso> queryShowAllCoursesDB(boolean showOnlyDeleted) {
        ArrayList<Corso> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement corso = conn1.createStatement();
            ResultSet rs = corso.executeQuery("SELECT * FROM CORSO");
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
            Statement corso = conn1.createStatement();
            // if the course already exists, only the 'rimosso' variable is changed ('rimosso' = false)
            boolean existedCourse = false;
            for (Corso c : corsi) {
                if (c.getMateria().equals(materia)) {
                    corso.executeUpdate("UPDATE CORSO SET rimosso = false WHERE materia = '" + materia + "'");
                    existedCourse = true;
                    break;
                }
            }
            if (!existedCourse)
                corso.executeUpdate("INSERT INTO CORSO (materia) VALUES ('" + materia + "')");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
            queryResult = false;
        } finally {
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
            Statement course = conn1.createStatement();
            course.executeUpdate("UPDATE CORSO SET rimosso = true WHERE materia = '" + materia + "'");
            course.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = true WHERE corso = '" + materia + "'");
            course.executeUpdate("UPDATE PRENOTAZIONE SET stato = 'cancellata' WHERE corso = '" + materia + "'");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
            queryResult = false;
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    /*
     *  This method returns all teachers in 'docente' table with 'rimosso' variable set to false.
     */
    public static ArrayList<Docente> queryShowAllDocentiDB() {
        ArrayList<Docente> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement docente = conn1.createStatement();
            ResultSet rs = docente.executeQuery("SELECT * FROM DOCENTE");
            while (rs.next()) {
                Docente doc = new Docente(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getBoolean("rimosso"));
                if (!doc.isRimosso())
                    out.add(doc);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    /*
     *  This method adds a teacher in 'docente' table.
     */
    public static boolean queryAddDocenteDB(String nome, String cognome) {
        boolean queryResult = true;
        try {
            connectionToDB();
            Statement docente = conn1.createStatement();
            docente.executeUpdate("INSERT INTO DOCENTE (nome, cognome) VALUES ('" + nome + "', '" + cognome + "')");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
            queryResult = false;
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    /*
     *  This method deletes a theacher in 'docente' table.
     *  It sets 'rimosso' variable to true both for the theacher and for all teachings associated with the theacher.
     *  It also sets 'stato' variable to 'cancellata' for all bookings with this course.
     */
    public static boolean queryDeleteDocenteDB(int idDoc) {
        boolean queryResult = true;
        try {
            connectionToDB();
            Statement docente= conn1.createStatement();
            docente.executeUpdate("UPDATE CORSO SET rimosso = true WHERE id = "+ idDoc);
            docente.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = true WHERE docente = " + idDoc);
            docente.executeUpdate("UPDATE PRENOTAZIONE SET stato = 'cancellata' WHERE docente = " + idDoc);
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
            queryResult = false;
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    /*
     *  This method returns all teachings in 'insegnamento' table.
     *  If 'showOnlyDeleted' is false it only shows actived teachings.
     *  If 'showOnlyDeleted' is true it only shows deleted teachings (it is used in 'queryAddInsegnamentoDB' method).
     */
    public static ArrayList<Insegnamento> queryShowAllInsegnamentiDB(boolean showOnlyDeleted) {
        ArrayList<Insegnamento> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement insegnamento = conn1.createStatement();
            ResultSet rs = insegnamento.executeQuery("SELECT * FROM INSEGNAMENTO");
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
            closeDBConnection();
        }
        return out;
    }

    /*
     *  This method adds a teaching in 'insegnamento' table.
     *  If couple (course,idDoc) already exists and its 'rimosso' variable is true, this method update it with false.
     *  It also checks that both the course and the teacher exist and have the 'rimosso' variables set to false.
     *  Otherwise it adds a new tuple.
     */
    public static boolean queryAddInsegnamentoDB(String course, int idDoc) {
        boolean queryResult = false;
        try {
            ArrayList<Insegnamento> insegnamenti = queryShowAllInsegnamentiDB(true);
            connectionToDB();
            Statement ins = conn1.createStatement();
            // if the theaching already exists, only the 'rimosso' variable is changed ('rimosso' = false)
            boolean existedTeaching = false;
            for (Insegnamento i : insegnamenti) {
                if (i.getCorso().equals(course) && i.getIdDocente() == idDoc) {
                    ins.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = false WHERE corso = '" + course + "' AND docente = " + idDoc);
                    existedTeaching = true;
                    queryResult = true;
                    break;
                }
            }
            if (!existedTeaching){
                ResultSet rsC = ins.executeQuery("SELECT rimosso FROM CORSO WHERE corso = '" + course + "'");
                ResultSet rsD = ins.executeQuery("SELECT rimosso FROM DOCENTE WHERE id = " + idDoc);
                boolean courseIsRemoved = true;
                boolean theacherIsRemoved = true;
                if (rsC.next())
                    courseIsRemoved = rsC.getBoolean("rimosso");
                if (rsD.next())
                    theacherIsRemoved = rsD.getBoolean("rimosso");
                if (!courseIsRemoved && !theacherIsRemoved){
                    ins.executeUpdate("INSERT INTO INSEGNAMENTO (corso, docente) VALUES ('" + course + "', " + idDoc + ")");
                    queryResult = true;
                    //ins.executeUpdate("INSERT INTO insegnamento (corso, docente) VALUES (SELECT materia FROM corso WHERE materia = 'Elementi_di_probabilità_e_statistica', SELECT id FROM docente WHERE nome = 'Ciro' and cognome = 'Cattuto')");
                }
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    /*
     *  This method deletes a theaching in 'insegnamento' table.
     *  It sets 'rimosso' variable to true and is sets 'stato' variable to 'cancellata' for all bookings with this course.
     */
    public static boolean queryDeleteInsegnamentoDB(String course, int idDoc) {
        boolean queryResult = true;
        try {
            connectionToDB();
            Statement ins= conn1.createStatement();
            ins.executeUpdate("UPDATE INSEGNAMENTO SET rimosso = true WHERE corso = '" + course + "' and docente = " + idDoc);
            ins.executeUpdate("UPDATE PRENOTAZIONE SET stato = 'cancellata' WHERE corso = '" + course + "' and docente = " + idDoc);
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
            queryResult = false;
        } finally {
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
            Statement insegnamento = conn1.createStatement();
            ResultSet rsI = insegnamento.executeQuery("SELECT * FROM INSEGNAMENTO WHERE corso = '" + course + "'");
            ResultSet rsD = insegnamento.executeQuery("SELECT * FROM DOCENTE");
            while (rsI.next()) {
                int idDocIns = rsI.getInt("docente");
                boolean removed = rsI.getBoolean("rimosso");
                if (!removed){
                    while (rsD.next()){
                        int idDoc = rsD.getInt("id");
                        if (idDocIns == idDoc){
                            Docente doc = new Docente(idDoc, rsD.getString("nome"), rsD.getString("cognome"), rsD.getBoolean("rimosso"));
                            out.add(doc);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    /*
     *  This method shows all bookings.
     */
    public static ArrayList<Prenotazione> queryShowAllPrenotazioniDB() {
        ArrayList<Prenotazione> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement pren = conn1.createStatement();
            ResultSet rs = pren.executeQuery("SELECT * FROM PRENOTAZIONE");
            while (rs.next()) {
                Prenotazione p = new Prenotazione(rs.getString("corso"), rs.getInt("docente"), rs.getString("utente"), rs.getString("stato"), rs.getInt("giorno"), rs.getInt("orario"));
                out.add(p);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    /*
     *  This method adds a booking in 'prenotazione' table.
     *  It checks that the couple (course, idDoc) already exists in 'insegnamento' table and that its 'rimosso' variable is set to false.
     *  It also checks that both the user and the teacher are not already occupied in that day-time slot.
     */
    public static boolean queryAddPrenotazioneDB(String course, int idDoc, String user, int day, int time) {
        boolean queryResult = false;
        try {
            connectionToDB();
            Statement ins = conn1.createStatement();
            ResultSet rs = ins.executeQuery("SELECT rimosso FROM INSEGNAMENTO WHERE corso = '" + course + "' AND docente = " + idDoc);
            boolean deletedTeaching = false;
            if(rs.next()){
                deletedTeaching = rs.getBoolean("rimosso");
            }
            if(!deletedTeaching) {
                // Here the teaching is present on the database and its 'rimossa' variable is set to false.
                boolean toInsert = true;
                ResultSet rsP = ins.executeQuery("SELECT docente,utente FROM PRENOTAZIONE WHERE giorno = " + day + "AND orario = " + time);
                while (toInsert && rsP.next()) {
                    int d = rsP.getInt("docente");
                    String u = rsP.getString("utente");
                    if (d == idDoc || user.equals(u))
                        toInsert = false;
                }
                if (toInsert) {
                    ins.executeUpdate("INSERT INTO prenotazione (corso, docente, utente, giorno, orario) VALUES ('" + course + "', " + idDoc + ", '" + user + "', " + day + ", " + time + ")");
                    queryResult = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    /*
     *  This method marks a booking as deleted.
     */
    public static boolean queryMarkPrenotazioneAsDeletedDB(String course, int idDoc, String user, int day, int time) {
        boolean queryResult = true;
        try {
            connectionToDB();
            Statement pren= conn1.createStatement();
            pren.executeUpdate("UPDATE PRENOTAZIONE SET stato='cancellata' WHERE corso = '" + course + "' and docente = " + idDoc + " and utente = '" + user + "' and giorno = " + day + " and orario = " + time);
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
            queryResult = false;
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    /*
     *  This method marks a booking as done.
     */
    public static boolean queryMarkPrenotazioneAsDoneDB(String course, int idDoc, String user, int day, int time) {
        boolean queryResult = false;
        try {
            connectionToDB();
            Statement pren= conn1.createStatement();
            ResultSet rs = pren.executeQuery("SELECT ruolo FROM UTENTE WHERE username = '" + user + "'");
            if (rs.next() && rs.getString("ruolo").equals("cliente")) {
                pren.executeUpdate("UPDATE PRENOTAZIONE SET stato='effettuata' WHERE corso = '" + course + "' and docente = " + idDoc + " and utente = '" + user + "' and giorno = " + day + " and orario = " + time);
                queryResult = true;
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    /*
     *  This method shows all users.
     */
    public static ArrayList<Utente> queryUtenteDB() {
        ArrayList<Utente> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement utente = conn1.createStatement();
            ResultSet rs = utente.executeQuery("SELECT * FROM UTENTE");
            while (rs.next()) {
                Utente ut = new Utente(rs.getString("username"),rs.getString("password"), rs.getString("ruolo"));
                out.add(ut);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    /*
     *  This method get user rule.
     */
    public static String getUserRule (String username){
        String rule = "";
        try {
            connectionToDB();
            Statement utente = conn1.createStatement();
            ResultSet rs = utente.executeQuery("SELECT ruolo FROM UTENTE WHERE username = '" + username + "'");
            if (rs.next()) {
                rule = rs.getString("ruolo");
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return rule;
    }


    /*
     *  This method returns a user obj, if a user with a given
     * username and password exists, null otherwise
     */
    public static Utente getUser (String username, String hashedPassword){
        Utente user= null;
        try {
            connectionToDB();
            Statement utente = conn1.createStatement();
            ResultSet rs = utente.executeQuery("SELECT username, password, ruolo FROM UTENTE WHERE username = '" + username + "'"
                                            + " AND password ='" + password +"'");
            if(rs.next()){
                user = new Utente(rs.getString("username"),rs.getString("password"),rs.getString("ruolo"));
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return user;
    }
    /*
     *  This method opens database connection.
     */
    private static void connectionToDB(){
        try{
            conn1 = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     *  This method closes database connection.
     */
    private static void closeDBConnection(){
        if (conn1 != null) {
            try {
                conn1.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
