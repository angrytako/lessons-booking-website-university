package DAO;

public class PrenotazioneConRuolo extends Prenotazione {


    private String ruolo;

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public PrenotazioneConRuolo(String corso, int idDocente, String utente, String stato, int giorno, int orario, String ruolo) {
        super(corso, idDocente, utente, stato, giorno, orario);
        this.ruolo = ruolo;
    }
    @Override
    public String toString() {
        return "PrenotazioneConRuolo{" +
                "corso='" + getCorso() + '\'' +
                ", idDocente=" + getIdDocente() +
                ", utente='" + getUtente() + '\'' +
                ", stato='" + getStato() + '\'' +
                ", giorno=" + getGiorno()+
                ", orario=" + getOrario() +
                ", ruolo=" + getRuolo() +
                '}';
    }
}
