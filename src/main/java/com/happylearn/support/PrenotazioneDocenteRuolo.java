package com.happylearn.support;

import com.happylearn.DAO.Prenotazione;

public class PrenotazioneDocenteRuolo extends Prenotazione {


    private String ruolo, nomeDocente, cognomeDocente;
    public String getRuolo() {
        return ruolo;
    }
    public String getNomeDocente() {
        return nomeDocente;
    }
    public String getCognomeDocente() {
        return cognomeDocente;
    }

    public PrenotazioneDocenteRuolo(String corso, int idDocente, String nomeDocente, String cognomeDocente, String utente, String stato, int giorno, int orario, String ruolo) {
        super(corso, idDocente, utente, stato, giorno, orario);
        this.ruolo = ruolo;
        this.nomeDocente = nomeDocente;
        this.cognomeDocente = cognomeDocente;
    }
    @Override
    public String toString() {
        return "PrenotazioneDocenteRuolo{" +
                "corso='" + getCorso() + '\'' +
                ", idDocente=" + getIdDocente() +
                ", nome docente" + getNomeDocente()+
                ", cognome docente" + getCognomeDocente()+
                ", utente='" + getUtente() + '\'' +
                ", stato='" + getStato() + '\'' +
                ", giorno=" + getGiorno()+
                ", orario=" + getOrario() +
                ", ruolo=" + getRuolo() +
                '}';
    }
}
