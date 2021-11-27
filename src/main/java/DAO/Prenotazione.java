package DAO;

public class Prenotazione {
    private int id_corso;
    private int id_docente;
    private int id_utente;

    public Prenotazione(int id_corso, int id_docente, int id_utente) {
        this.id_corso = id_corso;
        this.id_docente = id_docente;
        this.id_utente = id_utente;
    }

    public int getIdCorso() {
        return id_corso;
    }

    public int getIdDocente() {
        return id_docente;
    }

    public int getIdUtente() {
        return id_utente;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id_corso=" + id_corso +
                ", id_docente=" + id_docente +
                ", id_utente=" + id_utente +
                '}';
    }
}
