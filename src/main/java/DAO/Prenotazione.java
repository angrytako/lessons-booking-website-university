package DAO;

public class Prenotazione {
    private String corso;
    private int idDocente;
    private String utente;
    private String stato;
    private int giorno;     // 0:lunedì, 1:martedì, 2:mercoledì, 3:giovedì, 4:venerdì
    private int orario;     // 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

    public Prenotazione(String corso, int idDocente, String utente, String stato, int giorno, int orario) {
        this.corso = corso;
        this.idDocente = idDocente;
        this.utente = utente;
        this.stato = stato;
        this.giorno = giorno;
        this.orario = orario;
    }

    public String getCorso() {
        return corso;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public String getUtente() {
        return utente;
    }

    public String getStato() {
        return stato;
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
                ", idDocente=" + idDocente +
                ", utente='" + utente + '\'' +
                ", stato='" + stato + '\'' +
                ", giorno=" + giorno +
                ", orario=" + orario +
                '}';
    }
}
