package com.happylearn.support;

import com.happylearn.DAO.Prenotazione;

import java.util.Comparator;

public class PrenotazioneDocenteRuolo extends Prenotazione implements Comparable<PrenotazioneDocenteRuolo> {
    private String ruolo;
    private String nomeDocente;
    private String cognomeDocente;

    public PrenotazioneDocenteRuolo(String corso, int idDocente, String nomeDocente, String cognomeDocente, String utente, String stato, int giorno, int orario, String ruolo) {
        super(corso, idDocente, utente, stato, giorno, orario);
        this.ruolo = ruolo;
        this.nomeDocente = nomeDocente;
        this.cognomeDocente = cognomeDocente;
    }

    public String getRuolo() {
        return ruolo;
    }
    public String getNomeDocente() {
        return nomeDocente;
    }
    public String getCognomeDocente() {
        return cognomeDocente;
    }

    @Override
    public String toString() {
        return "PrenotazioneDocenteRuolo{" +
                "corso='" + getCorso() + '\'' +
                ", idDocente=" + getIdDocente() +
                ", nome docente='" + getNomeDocente() + '\'' +
                ", cognome docente='" + getCognomeDocente() + '\'' +
                ", utente='" + getUtente() + '\'' +
                ", stato='" + getStato() + '\'' +
                ", giorno=" + getGiorno()+
                ", orario=" + getOrario() +
                ", ruolo=" + getRuolo() +
                '}';
    }

    // this function returns a number <0 if this precedes p, returns 0 if this is equals to p, returns a number <0 otherwise
    @Override
    public int compareTo(PrenotazioneDocenteRuolo p) {
        if (this.getStato().equals(p.getStato())) {
            if (this.getGiorno() == p.getGiorno()) {
                if (this.getOrario() == p.getOrario()) {
                    if (this.getCorso().equals(p.getCorso())) {
                        return this.getNomeDocente().compareTo(p.getNomeDocente());
                    } else
                        return this.getCorso().compareTo(p.getCorso());
                } else
                    return this.getOrario() - p.getOrario();
            } else
                return this.getGiorno() - p.getGiorno();
        } else if (this.getStato().equals("effettuata") && p.getStato().equals("cancellata")){
            return -1;
        } else if (this.getStato().equals("cancellata") && p.getStato().equals("effettuata")){
            return 1;
        } else
            return (this.getStato()).compareTo(p.getStato());
    }
}
