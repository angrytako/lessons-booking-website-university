package DAO;

public class Prenotazione {
    private String corso;
    private int id_docente;
    private String utente;
    private int giorno;     // 0:lunedì, 1:martedì, 2:mercoledì, 3:giovedì, 4:venerdì
    private int orario;     // 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

    public Prenotazione(String corso, int id_docente, String utente, int giorno, int orario) {
        this.corso = corso;
        this.id_docente = id_docente;
        this.utente = utente;
        this.giorno = giorno;
        this.orario = orario;
    }

    public String getCorso() {
        return corso;
    }

    public int getIdDocente() {
        return id_docente;
    }

    public String getUtente() {
        return utente;
    }

    public int getGiorno() {
        return giorno;
    }

    public int getOrario() {
        return orario;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "corso='" + corso + '\'' +
                ", id_docente=" + id_docente +
                ", utente='" + utente + '\'' +
                ", giorno=" + giorno +
                ", orario=" + orario +
                '}';
    }
}
