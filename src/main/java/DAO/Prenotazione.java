package DAO;

public class Prenotazione_con_data {
    private String corso;
    private int id_docente;
    private int id_utente;
    private int giorno;     // 0:lunedì, 1:martedì, 2:mercoledì, 3:giovedì, 4:venerdì
    private int orario;     // 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

    public Prenotazione_con_data(String corso, int id_docente, int id_utente, int giorno, int orario) {
        this.corso = corso;
        this.id_docente = id_docente;
        this.id_utente = id_utente;
        this.giorno = giorno;
        this.orario = orario;
    }

    public String getCorso() {
        return corso;
    }

    public int getIdDocente() {
        return id_docente;
    }

    public int getIdUtente() {
        return id_utente;
    }

    public int getGiorno() {
        return giorno;
    }

    public int getOrario() {
        return orario;
    }

    @Override
    public String toString() {
        return "Prenotazione_con_data{" +
                "corso='" + corso + '\'' +
                ", id_docente=" + id_docente +
                ", id_utente=" + id_utente +
                ", giorno=" + giorno +
                ", orario=" + orario +
                '}';
    }
}
