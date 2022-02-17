package com.happylearn.support;

import com.happylearn.DAO.Docente;

import java.util.ArrayList;

public class InsegnamentoCorso {
    private String corso;
    private ArrayList<Docente> docenti;

    public InsegnamentoCorso(String corso, ArrayList<Docente> docenti) {
        this.corso = corso;
        this.docenti = docenti;
    }

    public ArrayList<Docente> getCorsi() {
        return docenti;
    }
}
