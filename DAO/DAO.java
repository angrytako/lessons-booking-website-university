package com.example.demo.DAO;


import java.sql.*;
import java.util.ArrayList;

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

    public static ArrayList<Corso> queryCorsoDB() {
        ArrayList<Corso> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement corso = conn1.createStatement();
            ResultSet rs = corso.executeQuery("SELECT * FROM CORSO");
            while (rs.next()) {
                Corso c = new Corso(rs.getInt("id"), rs.getString("materia"));
                out.add(c);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    public static void queryAddCorsoDB(String corso) {
        try {
            connectionToDB();
            Statement c = conn1.createStatement();
            c.executeUpdate("INSERT INTO corso (materia) VALUES ('" + corso + "')");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static void queryDeleteCorsoDB(String corso) {
        try {
            connectionToDB();
            Statement c = conn1.createStatement();
            c.executeUpdate("DELETE FROM corso WHERE materia = '" + corso + "'");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    // mmm da rivedere
    public static ArrayList<Integer> showIdDocentiForIdCourse(int id_course) {
        ArrayList<Integer> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement insegnamento = conn1.createStatement();
            ResultSet rs = insegnamento.executeQuery("SELECT docente FROM insegnamento WHERE corso = '" + id_course + "'");
            while (rs.next()) {
                out.add(rs.getInt("docente"));
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    public static ArrayList<Docente> queryDocenteDB() {
        ArrayList<Docente> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement docente = conn1.createStatement();
            ResultSet rs = docente.executeQuery("SELECT * FROM DOCENTE");
            while (rs.next()) {
                Docente doc = new Docente(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"));
                out.add(doc);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    public static void queryAddDocenteDB(String nome, String cognome) {
        try {
            connectionToDB();
            Statement docente = conn1.createStatement();
            docente.executeUpdate("INSERT INTO docente (nome, cognome) VALUES ('" + nome + "', '" + cognome + "')");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static void queryDeleteDocenteDB(String nome, String cognome) {
        try {
            connectionToDB();
            Statement docente= conn1.createStatement();
            docente.executeUpdate("DELETE FROM docente WHERE nome = '"+ nome + "' and cognome = '" + cognome + "'");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static ArrayList<Insegnamento> queryInsegnamentoDB() {
        ArrayList<Insegnamento> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement insegnamento = conn1.createStatement();
            ResultSet rs = insegnamento.executeQuery("SELECT * FROM INSEGNAMENTO");
            while (rs.next()) {
                Insegnamento ins = new Insegnamento(rs.getInt("corso"), rs.getInt("docente"));
                out.add(ins);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    public static void queryAddInsegnamentoDB(int id_course, int id_doc) {
        try {
            connectionToDB();
            Statement ins = conn1.createStatement();
            ins.executeUpdate("INSERT INTO insegnamento (corso, docente) VALUES (" + id_course + ", " + id_doc + ")");
            //ins.executeUpdate("INSERT INTO insegnamento (corso, docente) VALUES (SELECT id FROM corso WHERE materia = 'Elementi_di_probabilità_e_statistica', SELECT id FROM docente WHERE nome = 'Ciro' and cognome = 'Cattuto')");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static void queryDeleteInsegnamentoDB(int id_course, int id_doc) {
        try {
            connectionToDB();
            Statement ins= conn1.createStatement();
            ins.executeUpdate("DELETE FROM insegnamento WHERE corso = " + id_course + " and docente = " + id_doc);
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static ArrayList<Prenotazione> queryPrenotazioneDB() {
        ArrayList<Prenotazione> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement pren = conn1.createStatement();
            ResultSet rs = pren.executeQuery("SELECT * FROM PRENOTAZIONE");
            while (rs.next()) {
                Prenotazione ins = new Prenotazione(rs.getInt("corso"), rs.getInt("docente"), rs.getInt("utente"));
                out.add(ins);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    public static void queryAddPrenotazioneDB(int id_course, int id_doc, int id_user) {
        try {
            // non si deve poter aggiungere una prenotazione di corso e docente che NON sia presenti in insegnamento
            ArrayList<Insegnamento> insegnamenti = queryInsegnamentoDB();
            boolean ins = false;
            for (Insegnamento i : insegnamenti) {
                if(i.getIdCorso() == id_course && i.getIdDocente() == id_doc) {
                    connectionToDB();
                    Statement pren = conn1.createStatement();
                    pren.executeUpdate("INSERT INTO prenotazione (corso, docente, utente) VALUES (" + id_course + ", " + id_doc + ", " + id_user + ")");
                    ins = true;
                    break;
                }
            }
            if (! ins)
                System.out.println("Impossibile inserire la prenotazione richiesta poiché il docente non insegna quel corso!");
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static void queryDeletePrenotazioneDB(int id_course, int id_doc, int id_user) {
        try {
            connectionToDB();
            Statement pren= conn1.createStatement();
            pren.executeUpdate("DELETE FROM prenotazione WHERE corso = " + id_course + " and docente = " + id_doc + " and utente = " + id_user);
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static ArrayList<Prenotazione_con_data> queryPrenotazioneWithDataDB() {
        ArrayList<Prenotazione_con_data> out = new ArrayList<>();
        try {
            connectionToDB();
            Statement pren = conn1.createStatement();
            ResultSet rs = pren.executeQuery("SELECT * FROM PRENOTAZIONE_CON_DATA");
            while (rs.next()) {
                Prenotazione_con_data ins = new Prenotazione_con_data(rs.getInt("corso"), rs.getInt("docente"), rs.getInt("utente"), rs.getInt("giorno"), rs.getInt("orario"));
                out.add(ins);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    public static void queryAddPrenotazioneWithDataDB(int id_course, int id_doc, int id_user, int day, int time) {
        try {
            // non si deve poter aggiungere una prenotazione fuori da 15-18 del lunedì-venerdì
            if(day < 0 || day > 4 || time < 0 || time > 4){
                System.out.println("Impossibile inserire la prenotazione richiesta poiché la combinazione giorno-orario non è nello slot disponibile");
            }
            else{
                // controllo che il docente insegni quel corso
                ArrayList<Insegnamento> insegnamenti = queryInsegnamentoDB();
                boolean ins = false;
                for (Insegnamento i : insegnamenti) {
                    if (i.getIdCorso() == id_course && i.getIdDocente() == id_doc) {
                        ins = true;
                        break;
                    }
                }
                if(ins){    // ok: il docente insegna quel corso!
                    // controllo che non sia già occupato in quel giorno-orario con un'ALTRA lezione (diversa!!)
                    ArrayList<Prenotazione_con_data> prenotazioni_con_data = queryPrenotazioneWithDataDB();
                    boolean doc_occ = false;
                    boolean user_occ = false;
                    for (Prenotazione_con_data p : prenotazioni_con_data) {
                        if (p.getGiorno() == day && p.getOrario() == time && p.getIdCorso() != id_course) {
                            if (p.getIdDocente() == id_doc)
                                doc_occ = true;
                            else if (p.getIdUtente() == id_user)
                                user_occ = true;
                            break;
                        }
                    }
                    if (doc_occ)
                        System.out.println("Impossibile inserire la prenotazione richiesta poiché il docente é occupato in quello slot!");
                    else if (user_occ)
                        System.out.println("Impossibile inserire la prenotazione richiesta poiché l'utente é già occupato in quello slot!");
                    else{
                        connectionToDB();
                        Statement pren = conn1.createStatement();
                        pren.executeUpdate("INSERT INTO prenotazione_con_data (corso, docente, utente, giorno, orario) VALUES (" + id_course + ", " + id_doc + ", " + id_user + ", " + day + ", " + time + ")");
                    }
                } else
                    System.out.println("Impossibile inserire la prenotazione richiesta poiché il docente non insegna quel corso!");
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static void queryDeletePrenotazioneWithDataDB(int id_course, int id_doc, int id_user, int day, int time) {
        try {
            connectionToDB();
            Statement pren= conn1.createStatement();
            pren.executeUpdate("DELETE FROM prenotazione_con_data WHERE corso = " + id_course + " and docente = " + id_doc + " and utente = " + id_user + " and giorno = " + day + " and orario = " + time);

        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
    }

    public static ArrayList<Utente> queryUtenteDB() {
        ArrayList<Utente> out = new ArrayList<>();
        try {
            connectionToDB();
            /*
            * Per recuperare i dati dalla tabella: nella SELECT * FROM nometabella
            * il nometabella deve essere corretta.
            * Oppure FROMp da errore nel SQL syntax.
            * */
            Statement utente = conn1.createStatement();
            ResultSet rs = utente.executeQuery("SELECT * FROM UTENTE");
            while (rs.next()) {
                Utente ut = new Utente(rs.getInt("id"), rs.getString("account"),rs.getString("password"), rs.getInt("ruolo"));
                out.add(ut);
            }
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return out;
    }

    // da sistemare: il metodo deve prendere l'ID dell'utente
    public static String getUserRule (String account, String password){
        String rule = "";
        int n_rule = -1;
        try {
            connectionToDB();
            Statement utente = conn1.createStatement();
            ResultSet rs = utente.executeQuery("SELECT ruolo FROM UTENTE WHERE account = '" + account + "' and password = '" + password + "'");
            if (rs.next()) {
                n_rule = rs.getInt("ruolo");
            }
            //1: amministratore, 2: cliente
            if (n_rule == 1)
                rule ="admin";
            else if (n_rule == 2)
                rule = "client";
        } catch (SQLException e) {
            System.out.println("errore di connessione al db: " + e.getMessage());
        } finally {
            closeDBConnection();
        }
        return rule;
    }


    private static void connectionToDB() throws SQLException {
        conn1 = DriverManager.getConnection(url, user, password);
        /*
        * if (conn1 != null) {
        *     System.out.println("Connected to the database test");
        * }
        * */
    }

    private static void closeDBConnection(){
        if (conn1 != null) {
            try {
                conn1.close();
            } catch (SQLException e2) {
                System.out.println(e2.getMessage());
            }
        }
    }
}
